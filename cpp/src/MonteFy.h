//MonteFy.h
#ifndef MonteFy_H
#define MonteFy_H

#include<cmath>
#include<string>
#include "vmaths.h"
#include "intabread.h"
#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/xml_parser.hpp>
#include <boost/foreach.hpp>
#include <boost/numeric/ublas/matrix.hpp>
#include <boost/numeric/ublas/vector.hpp>
#include <boost/numeric/ublas/io.hpp>
#include <boost/algorithm/string.hpp>
#include <boost/lexical_cast.hpp>
#include <boost/random.hpp>
#include <ctime>
#include "interpolation.h"
#include "intabread.h"
#include "vectorops.h"

class MonteFy{
	// class for the monte carlo simulation -- adds uncertainty
	public:
			INTAB SinTab;
			double CDm, // drag coefficient, std
				CoPm, // centre of pressure, std [m]
				CNm, // normal coefficient, std
				CDdm, // drag coefficient for the secondary+ parachute, std
				CDpm, // drag coefficient for the primary parachute, std
				sigmaLaunchDeclination, // stochastic variable for launch angle
				sigmaThrust; // stochastic variable for thrust
			// these parameters are used for stochastic wind conditions
			// specifics on use are in the user_guide
			boost::numeric::ublas::vector<double> Mu; // average
			boost::numeric::ublas::vector<double> HScale; // altitutes to eval [m]
			boost::numeric::ublas::matrix<double> Sigma; // covariance matrix
			boost::numeric::ublas::matrix<double> EigenValue; // cov matrix eig val
			boost::numeric::ublas::matrix<double> EigenVector; // cov matrix eig vec
			boost::numeric::ublas::matrix<double> PHI; // basis functions
			boost::property_tree::ptree PropTree; // XML property tree

			MonteFy(){};
			MonteFy(INTAB,string);
			boost::numeric::ublas::vector<double> gsamp(void);
			double SampleNormal(double mean, double sigma);
			double SampleNormalTruncated(double mean, double sigma, double truncateSigma);
			void ReadInVector(boost::numeric::ublas::vector<double>* ,string,int);
			void ReadInMatrix(boost::numeric::ublas::matrix<double>*,string,int,int);
			INTAB Wiggle();
			INTAB WiggleProtect(INTAB thisIntab);
			std::vector<double> ToStdVec (boost::numeric::ublas::vector<double>);
};

#endif
