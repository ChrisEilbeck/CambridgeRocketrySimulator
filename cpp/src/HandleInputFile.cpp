/*
	%## Copyright (C) 2008 S.Box
	%## HandleInputFile.cpp

	%## Author: S.Box
	%## Created: 2008-05-27
*/

#include "HandleInputFile.h"

HandleInputFile::HandleInputFile(std::string FileName)
{
	/*
		\brief constructor of HandleInputFile

		this initialises the HandleInputFile

		\param FileName this is the file which is to be handled

		\return void
	*/
	
	try{
		//Read data from file into property tree
		boost::property_tree::xml_parser::read_xml(FileName,PropTree);
	}
	catch(exception e)
	{
		cout<<"Error: Unable to open the simulation input file."<<endl;
	}
	
	const string separators="\\/";
	int lastSepIndex = FileName.find_last_of(separators);
	FilePath = FileName.substr(0,lastSepIndex+1);
	
	//determine which function to use
	string FunctionName = PropTree.get<string>("SimulationInput.Function");

	string Functions [] = {"OneStageFlight","TwoStageFlight","OneStageMonte","TwoStageMonte"};
	int Fcase=-1;
	
	do
	{
		Fcase++;
	}
	while(Functions[Fcase].compare(FunctionName)!=0);
	
	switch(Fcase)
	{
		case(0):	// cout<<"OneStageFlight"<<endl;
					DealWithOSF();
					break;
					
		case(1):	// cout<<"TwoStageFlight"<<endl;
					DealWithTSF();
					break;
			
		case(2):	// cout<<"OneStageMonte"<<endl;
					DealWithOSM();
					break;
					
		case(3):	// cout<<"TwoStageMonte"<<endl;
					DealWithTSM();
					break;
			
		default:	cout<<"Error: No simulation function found"<<endl;
					break;
	}
	
	string OutputFileName = FilePath.append("SimulationOutput.xml");
	WriteData.WriteToXML(OutputFileName);
	cout << "Simulation Complete." << endl;
};

void HandleInputFile::DealWithOSF()
{
	// deals with nominal flight, single stage rocket
	Rocket_Flight RF1 = OneStageSetUp();
	WriteData = RF1.OneStageFlight();
};

void HandleInputFile::DealWithTSF()
{
	// deals with nominal flight, two stage rocket
	Rocket_Flight RF1 = TwoStageSetUp();
	WriteData = RF1.TwoStageFlight();
};

void HandleInputFile::DealWithOSM()
{
	// deals with monte carlo runs, one stage rocket
	Rocket_Flight RF1 = OneStageSetUp();
	
	int NoI = PropTree.get<int>("SimulationInput.SimulationSettings.NumberOfIterations");
	WriteData = RF1.OneStageMonte(NoI);
};

void HandleInputFile::DealWithTSM()
{
	// deals with monte carlo runs, two stage rocket
	Rocket_Flight RF1 = TwoStageSetUp();
	
	int NoI = PropTree.get<int>("SimulationInput.SimulationSettings.NumberOfIterations");
	WriteData = RF1.TwoStageMonte(NoI);
};

Rocket_Flight HandleInputFile::OneStageSetUp()
{
	// reads data for a one stage rocket
	boost::property_tree::ptree IntabTree;
	IntabTree = PropTree.get_child("SimulationInput.INTAB_TR");
	INTAB IT(IntabTree);
	
	Rocket_Flight RF1(IT);
	
	RF1 = TestMiscData(RF1);
	RF1.setFilePath(FilePath);
	
	return(RF1);
};

Rocket_Flight HandleInputFile::TwoStageSetUp()
{
	// reads data for a two stage rocket
	boost::property_tree::ptree IntabTree;
	IntabTree=PropTree.get_child("SimulationInput.INTAB_TR");
	INTAB IT_TR(IntabTree);
	
	IntabTree=PropTree.get_child("SimulationInput.INTAB_BS");
	INTAB IT_BS(IntabTree);
	
	IntabTree=PropTree.get_child("SimulationInput.INTAB_US");
	INTAB IT_US(IntabTree);

	double Tsep=PropTree.get<double>("SimulationInput.StageTransition.SeparationTime");
	double IgDelay=PropTree.get<double>("SimulationInput.StageTransition.IgnitionDelay");
	
	Rocket_Flight RF1(IT_TR,IT_BS,IT_US,Tsep,IgDelay);
	
	RF1=TestMiscData(RF1);
	RF1.setFilePath(FilePath);
	
	return(RF1);
}

Rocket_Flight HandleInputFile::TestMiscData(Rocket_Flight RF)
{
	/*
		\brief handles the misc data of the file, (LaunchSettings, SimulationSettings)
		
		\param RF Rocket_Flight containing XML tree
		
		\return Rocket_Flight filled with information from XML tree
	*/
	
	boost::property_tree::ptree TempTree;
	
	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.Eastings");
	if(TempTree.data()!="")		{	RF.X0.e1=TempTree.get<double>("");			}

	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.Northings");
	if(TempTree.data()!="")		{	RF.X0.e2=TempTree.get<double>("");			}

	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.Altitude");
	if(TempTree.data()!="")		{	RF.X0.e3=TempTree.get<double>("");			}

	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.Eastings");
	if(TempTree.data()!="")		{	RF.X0.e1=TempTree.get<double>("");			}

	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.LaunchRailLength");
	if(TempTree.data()!="")		{	RF.RL=TempTree.get<double>("");				}

	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.LaunchAzimuth");
	if(TempTree.data()!="")		{	RF.Az=TempTree.get<double>("");				}

	TempTree=PropTree.get_child("SimulationInput.LaunchSettings.LaunchDeclination");
	if(TempTree.data()!="")		{	RF.De=TempTree.get<double>("");				}

	TempTree=PropTree.get_child("SimulationInput.SimulationSettings.BallisticFailure");
	if(TempTree.data()!="")		{	RF.ballisticfailure=TempTree.get<bool>("");	}

	TempTree=PropTree.get_child("SimulationInput.SimulationSettings.ShortData");
	if(TempTree.data()!="")		{	RF.ShortData=TempTree.get<bool>("");		}

	TempTree=PropTree.get_child("SimulationInput.SimulationSettings.MaxTimeSpan");
	if(TempTree.data()!="")		{	RF.Tspan=TempTree.get<double>("");			}

	return(RF);
}
