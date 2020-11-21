/*
%## Copyright (C) 2008 S.Box
%## intabread.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

#include"intabread.h"

//INTAB1 function************************************************************
void INTAB1::FileFill(const char* filename){
	/*
	\brief build INTAB1 from a file

	\param filename points to file

	\return void
	*/
	string number;
	string line;
	double fnum;

	ifstream myfile (filename);
	if (myfile.is_open())
	  {
		while (! myfile.eof() )
		{
			getline (myfile,line,'\n');
			istringstream myline (line);
			for(int i=0;i<11;i++)
			{

				getline (myline,number,'\t');
				stringstream(number) >> fnum;

				switch(i){
					case 0:
						time.push_back(fnum);
						break;
					case 1:
						Thrust.push_back(fnum);
						break;
					case 2:
						Mass.push_back(fnum);
						break;
					case 3:
						Ixx.push_back(fnum);
						break;
					case 4:
						Iyy.push_back(fnum);
						break;
					case 5:
						Izz.push_back(fnum);
						break;
					case 6:
						Ixy.push_back(fnum);
						break;
					case 7:
						Ixz.push_back(fnum);
						break;
					case 8:
						Iyz.push_back(fnum);
						break;
					case 9:
						Xcm.push_back(fnum);
						break;
					case 10:
						Cda1.push_back(fnum);
						break;
					default:
						cout<<"Error in case number for intab1\n";
				}

			}
		}
    myfile.close();
	time.pop_back();
	Thrust.pop_back();
	Mass.pop_back();
	Ixx.pop_back();
	Iyy.pop_back();
	Izz.pop_back();
	Ixy.pop_back();
	Ixz.pop_back();
	Iyz.pop_back();
	Xcm.pop_back();
	Cda1.pop_back();

  }

	else cout << "Unable to open file";

}

void INTAB1::addDelay(double secs){
	/*
	\brief adds a delay to the file

	\param secs number of seconds to delay

	\return void
	*/
	vecop VoP;
	time = VoP.scaladd(time,secs);
}
//***********************************************************************

//INTAB2 Function********************************************************
void INTAB2::FileFill(const char* filename){
	/*
	\brief build INTAB2 from a file

	\param filename points to file

	\return void
	*/
	string number;
	string line;
	double fnum;
	int i=0;
	vector<double>
		cdtemp;

	ifstream myfile (filename);

	if (myfile.is_open()) {

		while (! myfile.eof() ) {
			cdtemp.clear();
			int on=0;
			getline (myfile,line,'\n');
			istringstream myline (line);
			while (! myline.eof()){
				getline(myline,number,'\t');
				stringstream(number)>>fnum;
				if(i==0 && on==0){
					//Re.push_back(fnum);
					//alpha.push_back(fnum);
				}

				else if (i==0)
					Re.push_back(fnum);
				else if (on==0)
					alpha.push_back(fnum);
				else
					cdtemp.push_back(fnum);

				on=1;
			}
			if (!cdtemp.empty())CD.push_back(cdtemp);

			i++;
		}
		myfile.close();
		alpha.pop_back();
	}
	else cout << "Unable to open file";

};

//***********************************************************************

//INTAB3 Function********************************************************
void INTAB3::FileFill(const char* filename){
	/*
	\brief build INTAB3 from a file

	\param filename points to file

	\return void
	*/
	string number;
	int i=0;
	double fnum;
	ifstream myfile (filename);
	if (myfile.is_open()) {
		while (! myfile.eof() ) {
			getline (myfile,number,'\t');
			stringstream(number) >> fnum;

			if (i==0)CNa=fnum;
			else if(i==1)Xcp=fnum;
			else if (i>2)cout<<"Error too much data in INTAB3 file\n";



			i++;
		}
    myfile.close();
  }
	else cout << "Unable to open file";
}
//***********************************************************************

//INTAB4 function************************************************************
void INTAB4::FileFill(const char* filename){
	/*
	\brief build INTAB4 from a file

	\param filename points to file

	\return void
	*/
	string number;
	string line;
	double fnum;

	ifstream myfile (filename);
	if (myfile.is_open()) {
		while (! myfile.eof() ) {
			getline (myfile,line,'\n');
			istringstream myline (line);
			for(int i=0;i<6;i++) {
				getline (myline,number,'\t');
				stringstream(number) >> fnum;

				switch(i){
					case 0:
						Alt.push_back(fnum);
						break;
					case 1:
						Wx.push_back(fnum);
						break;
					case 2:
						Wy.push_back(fnum);
						break;
					case 3:
						Wz.push_back(fnum);
						break;
					case 4:
						rho.push_back(fnum);
						break;
					case 5:
						temp.push_back(fnum);
						break;
					default:
						cout<<"Error in case number for intab1\n";
				}
			}
		}
    myfile.close();
		Alt.pop_back();
		Wx.pop_back();
		Wy.pop_back();
		Wz.pop_back();
		rho.pop_back();
		temp.pop_back();
  }
	else cout << "Unable to open file";
}
//***********************************************************************

//INTAB Function*********************************************************
void INTAB::FileFill(const char* f1,const char* f2,const char* f3,const char* f4){
	/*
	\brief fill the INTAB 1,2,3,4 from the file

	\param f1 file for intab1
	\param f2 file for intab2
	\param f3 file for intab3
	\param f4 file for intab4

	\return void
	*/
	intab1.FileFill(f1);
	intab2.FileFill(f2);
	intab3.FileFill(f3);
	intab4.FileFill(f4);
}

//***********************************************************************


INTAB::INTAB(boost::property_tree::ptree IntabTree){
	/*
	\brief constructor for class that handles XML tree

	\param IntabTree

	\return void
	*/

	//Read in intab1 data
	vector<double> * it1List [] = {&intab1.time, &intab1.Thrust, &intab1.Mass, &intab1.Ixx, &intab1.Iyy, &intab1.Izz, &intab1.Ixy, &intab1.Ixz, &intab1.Iyz, &intab1.Xcm, &intab1.Cda1};
	string it1Names [] = {"time","Thrust","Mass","Ixx","Iyy","Izz","Ixy","Ixz","Iyz","CentreOfMass","ThrustDampingCoefficient"};
	for (int i = 0; i<11; i++){
		*it1List[i] = TreeToVector(IntabTree,("intab1." + it1Names[i]));
	}

	//Read in intab 2 data
	intab2.alpha = TreeToVector(IntabTree,"intab2.AngleOfAttack");
	intab2.Re = TreeToVector(IntabTree,"intab2.ReynoldsNumber");
	intab2.CD = TreeToMatrix(IntabTree,"intab2.CD");

	//Read in intab 3 data
	intab3.CNa = IntabTree.get<double>("intab3.CN");
	intab3.Xcp = IntabTree.get<double>("intab3.CentreOfPressure");

	//Read in intab 4 data
	vector<double> * it4List [] = {&intab4.Alt, &intab4.Wx, &intab4.Wy, &intab4.Wz, &intab4.rho, &intab4.temp};
	string it4Names [] = {"Altitude","XWind","YWind","ZWind","AtmosphericDensity","AtmosphericTemperature"};
	for (int i = 0; i<6; i++){
		*it4List[i] = TreeToVector(IntabTree,("intab4." + it4Names[i]));
	}

	//Read in LandA
	landa.RBL = IntabTree.get<double>("LengthAndArea.RocketLength");
	landa.Ar = IntabTree.get<double>("LengthAndArea.RocketXsectionArea");

	//Read in Paratab
	paratab.AltPd = TreeToVector(IntabTree,"ParachuteData.ParachuteSwitchAltitude");
	paratab.CdA = TreeToVector(IntabTree,"ParachuteData.ParachuteCDxA");
}

vector<double> INTAB::TreeToVector(boost::property_tree::ptree Tree, std::string Key){
	// converts an XML tree to a vector
	vector<string> DatString;
	vector<double> Temp;

	string vecstring = Tree.get<string>(Key);
	boost::algorithm::split(DatString,vecstring,boost::algorithm::is_any_of(",;"));
	DatString.pop_back();
	for(vector<string>::iterator DS_it = DatString.begin(); DS_it != DatString.end(); DS_it++){
		double Element;
		stringstream(*DS_it)>>Element;
		Temp.push_back(Element);
	}
	return(Temp);
}

vector<vector<double> > INTAB::TreeToMatrix(boost::property_tree::ptree Tree, std::string Key){
	// converts an XML tree to a matrix
	vector<string> LineString;
	vector<vector<double> > Temp;

	string vecstring = Tree.get<string>(Key);
	boost::algorithm::split(LineString,vecstring,boost::algorithm::is_any_of(";"));
	LineString.pop_back();

	for(vector<string>::iterator LS_it = LineString.begin(); LS_it != LineString.end(); LS_it++){
		vector<string> DatString;
		vector<double> Tsmall;
		boost::algorithm::split(DatString,*LS_it,boost::algorithm::is_any_of(","));

		for(vector<string>::iterator DS_it = DatString.begin(); DS_it != DatString.end(); DS_it++){
			double Element;
			stringstream(*DS_it)>>Element;
			Tsmall.push_back(Element);
		}
		Temp.push_back(Tsmall);
	}

	return(Temp);
}
