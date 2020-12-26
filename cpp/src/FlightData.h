/*
%## Copyright (C) 2008 S.Box
%## FlightData.h.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/
//FlightData.h

#ifndef FlightData_H
	#define FlightData_H
	
	//Headers*********************************************
	#include<vector>
	#include<string>
	#include<sstream>
	#include<time.h>
	#include"vectorops.h"
	#include"vmaths.h"
	#include <boost/property_tree/ptree.hpp>
	#include <boost/property_tree/xml_parser.hpp>
	#include <boost/foreach.hpp>
	//****************************************************
	
	using namespace std;
	
	class FlightData
	{
		// class to store all data gathered in the simulation
		
		public:
			//Class members
			vector<double> time;
			vector<double> events;
			vector<vector3> X;
			
			//Constructor
			FlightData()
			{
				// empty constructor
			};
			
			//Class Functions
			virtual boost::property_tree::ptree BuildPropertyTree(int)=0;
			void ApogeeLanding(vector<double> *,vector<double> *,double *);
			void TreeIfy(boost::property_tree::ptree *,vector<double>,string);
			void TreeIfy(boost::property_tree::ptree *,vector<vector3>,string);
	};

	class FlightDataShort:public FlightData
	{
		// gathers all flightdata (short)
		
		public:
			FlightDataShort()
			{
				// empty constructor
			};
			
			FlightDataShort operator + (FlightDataShort);
			boost::property_tree::ptree BuildPropertyTree(int);
	};

	class FlightDataLong:public FlightData
	{
		// gathers all flightdata (long)
		
		public:
			vector<double> alpha;
			vector<double> Thrust;
			vector<double> Mass;
			vector<double> CofM;
			vector<double> aDensity;
			vector<double> aTemp;
			
			vector<vector3> Raxis;
			vector<vector3> Xdot;
			vector<vector3> Thetadot;
			vector<vector3> Xddot;
			vector<vector3> Thetaddot;
			vector<vector3> Force;
			vector<vector3> Torque;
			vector<vector3> Wind;
			
			vector<matrix3x3> Inertia;
			
			FlightDataLong()
			{
				// empty constructor
			};
			
			FlightDataLong operator + (FlightDataLong);
			boost::property_tree::ptree BuildPropertyTree(int);
			void SpeedAndG(double *,double *);
	};
	
	class OutputData
	{
		// filling tree and writing to XML
		
		public:
			boost::property_tree::ptree PropTree;
			
			//Constructors
			OutputData()
			{
				// empty constructor
			};
			
			//Functions
			void InitializePropertyTree(string);
			void FillPropertyTree(FlightData *,int);
			void FillPropertyTree(vector<FlightData *>,int);
			void WriteToXML(string);
	};
	
#endif
