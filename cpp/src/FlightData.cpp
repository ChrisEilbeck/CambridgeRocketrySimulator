/*
	%## Copyright (C) 2008 S.Box
	%## FlightData.cpp

	%## Author: S.Box
	%## Created: 2008-05-27
*/

#include"FlightData.h"

FlightDataShort FlightDataShort::operator + (FlightDataShort added)
{
	FlightDataShort Temp = *this;

	Temp.time.insert(Temp.time.end(), added.time.begin(), added.time.end());
	Temp.events.insert(Temp.events.end(), added.events.begin(), added.events.end());
	Temp.X.insert(Temp.X.end(), added.X.begin(), added.X.end());

	return(Temp);
}

FlightDataLong FlightDataLong::operator + (FlightDataLong added)
{
	FlightDataLong Temp = *this;

	Temp.time.insert(Temp.time.end(), added.time.begin(), added.time.end());
	Temp.X.insert(Temp.X.end(), added.X.begin(), added.X.end());
	Temp.alpha.insert(Temp.alpha.end(), added.alpha.begin(), added.alpha.end());
	Temp.Thrust.insert(Temp.Thrust.end(), added.Thrust.begin(), added.Thrust.end());
	Temp.Mass.insert(Temp.Mass.end(), added.Mass.begin(), added.Mass.end());
	Temp.CofM.insert(Temp.CofM.end(), added.CofM.begin(), added.CofM.end());
	Temp.aDensity.insert(Temp.aDensity.end(), added.aDensity.begin(), added.aDensity.end());
	Temp.aTemp.insert(Temp.aTemp.end(), added.aTemp.begin(), added.aTemp.end());
	Temp.Raxis.insert(Temp.Raxis.end(), added.Raxis.begin(), added.Raxis.end());
	Temp.Xdot.insert(Temp.Xdot.end(), added.Xdot.begin(), added.Xdot.end());
	Temp.Thetadot.insert(Temp.Thetadot.end(), added.Thetadot.begin(), added.Thetadot.end());
	Temp.Xddot.insert(Temp.Xddot.end(), added.Xddot.begin(), added.Xddot.end());
	Temp.Thetaddot.insert(Temp.Thetaddot.end(), added.Thetaddot.begin(), added.Thetaddot.end());
	Temp.Force.insert(Temp.Force.end(), added.Force.begin(), added.Force.end());
	Temp.Torque.insert(Temp.Torque.end(), added.Torque.begin(), added.Torque.end());
	Temp.Wind.insert(Temp.Wind.end(), added.Wind.begin(), added.Wind.end());
	Temp.Inertia.insert(Temp.Inertia.end(), added.Inertia.begin(), added.Inertia.end());

	return(Temp);
}

boost::property_tree::ptree FlightDataShort::BuildPropertyTree(int RN)
{
	boost::property_tree::ptree TempTree;
	TempTree.put<int>("Number",RN);

	vector<double> Apogee,Landing;
	double ApoTime;
	ApogeeLanding(&Apogee,&Landing,&ApoTime);

	TreeIfy(&TempTree,Apogee,"FlightStats.Apogee");
	TreeIfy(&TempTree,Landing,"FlightStats.Landing");
	TreeIfy(&TempTree,events,"FlightStats.Events"); // store events

	TempTree.put<double>("FlightStats.AscentTime", ApoTime);

	TreeIfy(&TempTree,time,"FlightData.Time");
	TreeIfy(&TempTree,X,"FlightData.Position");

	return(TempTree);
}

boost::property_tree::ptree FlightDataLong::BuildPropertyTree(int RN)
{
	boost::property_tree::ptree TempTree;
	TempTree.put<int>("Number",RN);

	vector<double> Apogee,Landing;
	double ApoTime, MaxSpeed, MaxG, FlightTime;
	ApogeeLanding(&Apogee,&Landing,&ApoTime);
	SpeedAndG(&MaxSpeed,&MaxG);
	FlightTime = time.back();

	TreeIfy(&TempTree,Apogee,"FlightStats.Apogee");
	TreeIfy(&TempTree,Landing,"FlightStats.Landing");
	TreeIfy(&TempTree,events,"FlightStats.Events"); // store events

	double DList[] = {MaxSpeed,MaxG,ApoTime,FlightTime};
	string NameDList[] = {"MaxSpeed","Maxg","AscentTime","TotalFlightTime"};

	int N_it = 0;
	BOOST_FOREACH(double L,DList)
	{
		TempTree.put<double>(("FlightStats." + NameDList[N_it]),L);
		N_it++;
	}

	vector<double> DoubleData[] = {time,alpha,Thrust,Mass,CofM,aDensity,aTemp};
	string DataName[] = {"Time","AngleOfAttack","Thrust","Mass", "CentreOfMass", "AtmosphericDensity", "AtmosphericTemperature"};

	N_it = 0;
	BOOST_FOREACH(vector<double> DData, DoubleData)
	{
		TreeIfy(&TempTree,DData,("FlightData." + DataName[N_it]));
		N_it++;
	}

	vector<vector3> VecData[] = {X,Xdot,Xddot,Raxis,Thetadot,Thetaddot,Force,Torque,Wind};
	string VecName[] = {"Position","Velocity","Accelleration","Pose","AngularVelocity","AngularAccelleration","Force","Torque","Wind"};

	N_it = 0;
	BOOST_FOREACH(vector<vector3> VData, VecData)
	{
		TreeIfy(&TempTree,VData,("FlightData." + VecName[N_it]));
		N_it++;
	}
	
	return(TempTree);
}

void OutputData::WriteToXML(string FileName)
{
	boost::property_tree::xml_parser::write_xml(FileName,PropTree);
}

void OutputData::InitializePropertyTree(std::string Function)
{
	PropTree.put("SimulationOutput.Version","Cambridge Rocketry Simulator v0.1");
	time_t rawtime;
	time(&rawtime);
	//PropTree.put("SimulationOutput.Date",__DATE__);
	PropTree.put("SimulationOutput.Time",ctime(&rawtime));
	PropTree.put("SimulationOutput.Function",Function);
}

void OutputData::FillPropertyTree(vector<FlightData *> TRun,int RN)
{
	boost::property_tree::ptree TempTree1 = TRun[0]->BuildPropertyTree(RN);
	boost::property_tree::ptree TempTree2 = TRun[1]->BuildPropertyTree(RN);
	boost::property_tree::ptree Temp;
	Temp.put<int>("Number",RN);
	Temp.add_child("LowerStage",TempTree1);
	Temp.add_child("UpperStage",TempTree2);
	PropTree.add_child("SimulationOutput.Runs.Run",Temp);
}

void OutputData::FillPropertyTree(FlightData * Run,int RN)
{
		boost::property_tree::ptree TempTree = Run->BuildPropertyTree(RN);
		PropTree.add_child("SimulationOutput.Runs.Run",TempTree);
		RN++;
}

void FlightData::ApogeeLanding(std::vector<double> * ApoP, std::vector<double> * LandP, double * AtP)
{
	double apogee[3] = {0.0,0.0,0.0};
	double Apotime = 0.0;
	
	vector<double>::iterator t_it;
	t_it = time.begin();
	for (vector<vector3>::iterator Z_it = X.begin(); Z_it != X.end(); Z_it++)
	{
		vector3 temp =*Z_it;
		if (temp.e3>apogee[2])
		{
			apogee[0]=temp.e1;
			apogee[1]=temp.e2;
			apogee[2]=temp.e3;
			Apotime = *t_it;
		}
		
		t_it++;
	}
	
	ApoP->push_back(apogee[0]);
	ApoP->push_back(apogee[1]);
	ApoP->push_back(apogee[2]);
	LandP->push_back(X.back().e1);
	LandP->push_back(X.back().e2);
	LandP->push_back(X.back().e3);
	*AtP = Apotime;
}

void FlightData::TreeIfy(boost::property_tree::ptree * pPtree, std::vector<double> data, string Address)
{
	stringstream Line;
	for(vector<double>::iterator D_it = data.begin(); D_it != (data.end()-1); D_it++)
	{
		Line<<*D_it<<", ";
	}
	
	Line<<data.back()<<";";
	pPtree->put(Address,Line.str());
}

void FlightData::TreeIfy(boost::property_tree::ptree * pPtree, std::vector<vector3> data, std::string Address)
{
	stringstream Line1;
	stringstream Line2;
	stringstream Line3;
	for(vector<vector3>::iterator V_it = data.begin(); V_it != (data.end()-1); V_it++)
	{
		vector3 Temp = *V_it;
		Line1<<Temp.e1<<", ";
		Line2<<Temp.e2<<", ";
		Line3<<Temp.e3<<", ";
	}
	
	Line1<<data.back().e1<<";";
	Line2<<data.back().e2<<";";
	Line3<<data.back().e3<<";";
	pPtree->put(Address,(Line1.str()+Line2.str()+Line3.str()));
}

void FlightDataLong::SpeedAndG(double* Sp,double* Gp)
{
	/*
		\brief function to obtain maximum speed and g-forces
		
		\return void
	*/
	double MaxSpeed = 0;
	for (vector<vector3>::iterator Z_it = Xdot.begin(); Z_it != Xdot.end(); Z_it++)
	{
		vector3 temp =*Z_it;
		double MSP = temp.mag();
		if (MSP > MaxSpeed)
		{
			MaxSpeed = MSP;
		}
	}
	
	double Maxg = 0;
	for (vector<vector3>::iterator Z_it = Xddot.begin(); Z_it != Xddot.end(); Z_it++)
	{
		vector3 temp =*Z_it;
		double MG = temp.mag();
		if (MG > Maxg)
		{
			Maxg = MG;
		}
	}
	
	Maxg = Maxg/9.81;
	
	*Sp = MaxSpeed;
	*Gp = Maxg;
}
