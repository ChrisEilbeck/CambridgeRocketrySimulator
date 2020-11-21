/*
%## Copyright (C) 2008 S.Box
%## MonteFy.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

//MonteFy.h

#include "MonteFy.h"

MonteFy::MonteFy(INTAB I1, string FileName) {
	SinTab = I1;
	boost::property_tree::xml_parser::read_xml(FileName,PropTree);

	CDm = PropTree.get<double>("Uncertainty.StochasticCoefficients.CD");
	CoPm = PropTree.get<double>("Uncertainty.StochasticCoefficients.CoP");
	CNm = PropTree.get<double>("Uncertainty.StochasticCoefficients.CN");
	CDdm = PropTree.get<double>("Uncertainty.StochasticCoefficients.CDd");
	CDpm = PropTree.get<double>("Uncertainty.StochasticCoefficients.CDp");
	sigmaLaunchDeclination = PropTree.get<double>("Uncertainty.StochasticCoefficients.LaunchDeclination");
	sigmaThrust = PropTree.get<double>("Uncertainty.StochasticCoefficients.Thrust");

	ReadInVector(&Mu,"Mu",15);
	ReadInVector(&HScale,"HScale",121);

	ReadInMatrix(&Sigma,"Sigma",15,15);
	ReadInMatrix(&EigenValue,"Eigenvalues",15,15);
	ReadInMatrix(&EigenVector,"Eigenvectors",15,15);
	ReadInMatrix(&PHI,"PHI",121,15);
}

void MonteFy::ReadInVector(boost::numeric::ublas::vector<double> * V1,string Name, int d1){

	vector<string> MuS;

	string XMLString = "Uncertainty.StochasticCoefficients.";
	XMLString += Name;
	string MuLS = PropTree.get<string>(XMLString);

	boost::algorithm::split(MuS,MuLS,boost::algorithm::is_any_of(",;"));
	MuS.pop_back();

	boost::numeric::ublas::vector<double> D1 (d1);

	unsigned i = 0;
	BOOST_FOREACH(string s, MuS){

		D1(i) = boost::lexical_cast<double>(s);
		i++;
	}
	*V1=D1;

}

void MonteFy::ReadInMatrix(boost::numeric::ublas::matrix<double> * M1,string Name,int d1, int d2){
	vector<string> Line;
	vector<string> Element;

	string XMLstring = "Uncertainty.StochasticCoefficients.";
	XMLstring += Name;

	string SigmaLS = PropTree.get<string>(XMLstring);
	boost::algorithm::split(Line,SigmaLS,boost::algorithm::is_any_of(";"));
	Line.pop_back();

	boost::numeric::ublas::matrix<double> S1 (d1,d2);
	unsigned i = 0;
	unsigned j = 0;
	BOOST_FOREACH(string s,Line){
		boost::algorithm::split(Element,s,boost::algorithm::is_any_of(","));
		j=0;
		BOOST_FOREACH(string t,Element){
			S1(i,j)=boost::lexical_cast<double>(t);
			j++;
		}
		i++;
	}

	*M1 = S1;


}

INTAB MonteFy::Wiggle(void){
	{
		using namespace boost::numeric::ublas;

		boost::numeric::ublas::vector<double> Wxp,Wyp;

		Wxp = prod(gsamp(),trans(PHI));
		Wyp = prod(gsamp(),trans(PHI));

		double CDc = SampleNormal(1, CDm);
		double CoPc = SampleNormal(1, CoPm);
		double CNc = SampleNormal(1, CNm);
		// parachute
		double CDdc = SampleNormal(1, CDdm);
		double CDpc = SampleNormal(1, CDpm);
		// thrust (truncated)
		double Thrustc = SampleNormalTruncated(1, sigmaThrust, 1.0);

		std::vector<double> Wx,Wy,hscale,X,Y;
		Wx = ToStdVec(Wxp);
		Wy = ToStdVec(Wyp);
		hscale = ToStdVec(HScale);

		INTAB TempTab = SinTab;

		interp interper;
		int CD_it;

		// vary wind conditions
		BOOST_FOREACH(double Height,TempTab.intab4.Alt){
			X.push_back(interper.one(hscale, Wx, Height));
			Y.push_back(interper.one(hscale, Wy, Height));
		}

		vecop Vop;
		TempTab.intab4.Wx = Vop.vecadd(TempTab.intab4.Wx, X);
		TempTab.intab4.Wy = Vop.vecadd(TempTab.intab4.Wy, Y);


		// vary drag coefficient
		CD_it = 0;
		BOOST_FOREACH(std::vector<double> row,TempTab.intab2.CD){
			TempTab.intab2.CD[CD_it] = Vop.scalmult(row,CDc);
			CD_it++;
		}

		// vary normal coefficient and centre of pressure
		TempTab.intab3.CNa = TempTab.intab3.CNa*CNc;
		TempTab.intab3.Xcp = TempTab.intab3.Xcp*CoPc;

		// vary parachute data
		CD_it = 0;
		BOOST_FOREACH(double pcd,TempTab.paratab.CdA){

			if(CD_it == 0){
				TempTab.paratab.CdA[CD_it]=pcd*CDdc;
			}
			else{
				TempTab.paratab.CdA[CD_it]=pcd*CDpc;
			}

			CD_it++;
		}

		// vary thrust data
		int Thrust_it = 0;
		BOOST_FOREACH(double thrust1, TempTab.intab1.Thrust) {
			double newThrust = thrust1 * Thrustc;
			// overwrite
			TempTab.intab1.Thrust[Thrust_it] = newThrust;
			Thrust_it++; // next point
		}

		// protect values
		TempTab = WiggleProtect(TempTab);

		// returns data
		return(TempTab);
	}


}

INTAB MonteFy::WiggleProtect(INTAB thisIntab){
	// do a check on parameters to make sure they are not non-negative

	int intLoop = 0;

	// INTAB1

	intLoop = 0;
	int endLoop = thisIntab.intab1.Thrust.size();

	BOOST_FOREACH(double thrust1, thisIntab.intab1.Thrust) {
		// check bounds
		if (intLoop == 0) {
			thrust1 = 0;
		}
		else if (intLoop == (endLoop)) {
			thrust1 = 0;
		}
		else if (thrust1 <= 0) {
			thrust1 = 0.0001;
		}

		// overwrite
		thisIntab.intab1.Thrust[intLoop] = thrust1;
		intLoop++; // next point
	}

	// INTAB2

	intLoop = 0;

	BOOST_FOREACH(vector<double> cd2, thisIntab.intab2.CD) {

		int intLoop2 = 0;

		BOOST_FOREACH(double cd1, cd2) {

			// check bounds
			if (cd1 < 0) {
				cd1 = 0;
			}
			// overwrite
			cd2[intLoop2] = cd1;
			intLoop2++; // next point
		}

		// overwrite
		thisIntab.intab2.CD[intLoop] = cd2;
		intLoop++; // next vector

	}

	// INTAB3

	if (thisIntab.intab3.CNa < 0) {
		thisIntab.intab3.CNa = 0;
	}

	if (thisIntab.intab3.Xcp < 0) {
		thisIntab.intab3.Xcp = 0;
	}

	// INTAB4

	// ParaTab

	// LandA

	return thisIntab;
}

boost::numeric::ublas::vector<double> MonteFy::gsamp(void){
	{
		using namespace boost::numeric::ublas;
		const int Dim = Sigma.size1();
		boost::numeric::ublas::vector<double> randn (Dim);
		for (unsigned i = 0; i < randn.size();i++){
			randn(i) = SampleNormal(0,1);
		}
		boost::numeric::ublas::matrix<double> SqEigenValue (Dim,Dim);
		for (unsigned i = 0; i < SqEigenValue.size1();i++){
			for (unsigned j =0; j < SqEigenValue.size2();j++){
				SqEigenValue(i,j) = sqrt(EigenValue(i,j));
			}
		}
		boost::numeric::ublas::vector<double> coeffs (Dim);
		coeffs = prod(randn,SqEigenValue);

		boost::numeric::ublas::vector<double> Out (Dim);

		Out = Mu + prod(coeffs,trans(EigenVector));

		return(Out);
	}
}


double MonteFy::SampleNormal (double mean, double sigma)
{
	{
		using namespace boost;
		// Create a Mersenne twister random number generator
		// that is seeded once with #seconds since 1970
		static mt19937 rng(static_cast<unsigned> (std::time(0)));

		// select Gaussian probability distribution
		normal_distribution<double> norm_dist(mean, sigma);

		// bind random number generator to distribution, forming a function
		variate_generator<mt19937&, normal_distribution<double> >  normal_sampler(rng, norm_dist);

		double Out = normal_sampler();
		// sample from the distribution
		return(Out);
	}
}

double MonteFy::SampleNormalTruncated (double mean, double sigma, double truncateSigma)
{
	{
		using namespace boost;
		// Create a Mersenne twister random number generator
		// that is seeded once with #seconds since 1970
		static mt19937 rng(static_cast<unsigned> (std::time(0)));

		// select Gaussian probability distribution
		normal_distribution<double> norm_dist(mean, sigma);

		// bind random number generator to distribution, forming a function
		variate_generator<mt19937&, normal_distribution<double> >  normal_sampler(rng, norm_dist);

		double Out = normal_sampler();
		// sample from the distribution

		// truncate
		double lowerBound = mean - truncateSigma*sigma - 0.0001;
		double upperBound = mean + truncateSigma*sigma + 0.0001; // add a little margin

		while ((Out <= lowerBound) || (Out >= upperBound)) {
			// resample
			Out = normal_sampler();
		}

		return(Out);
	}
}

std::vector<double> MonteFy::ToStdVec(boost::numeric::ublas::vector<double> Vin){
	std::vector<double> Temp;

	for(unsigned i=0; i<Vin.size();i++){
		Temp.push_back(Vin(i));
	}
	return(Temp);
}
