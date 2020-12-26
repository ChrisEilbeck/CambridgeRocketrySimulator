/*
	%## Copyright (C) 2008 S.Box

	%## descentcalc.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

#ifndef descentcalc_H
	#define descentcalc_H

	//Headers*********************************************
	#include<iostream>
	#include<cmath>
	#include<vector>
	#include"vectorops.h"
	#include"interpolation.h"
	#include"RKF45.h"
	#include"vmaths.h"
	#include"intabread.h"
	#include"FlightData.h"
	
	using namespace std;
	//****************************************************

	class EqMotionData2
	{
		/*
			holds the states of the parachute dynamics
		*/
		
		public:
			double Mass;
			double aDensity;
			double aTemp;
			
			vector3 X;
			vector3 Xdot;
			vector3 Xddot;
			vector3 Force;
			vector3 Wind;
			
			EqMotionData2()
			{
				// empty constructor
			};
	};


	class descent
	{
		/*
			this class simulate the dynamics of a parachute descent
		*/
		
		public:
			vector<double> tt;
			vector<double> z0;
			
			INTAB4 intab4;
			INTAB1 intab1;
			
			ParaTab paratab;

			bool DatPop;
			RKF_data RKd1;

			descent()
			{
				DatPop=false;
			};
			
			descent(vector<double>,vector<double>,INTAB);
			RKF_data fall(void);
			EqMotionData2 EqMotionSolve(double, vector<double>);
			FlightDataShort getShortData(void);
			FlightDataLong getLongData(void);
	};

	class floatdown:public integrator, public RKF, public descent
	{
		/*
			this class gets the parachute to descent, integrate
		*/
		
		public:
			floatdown()
			{
				// empty constructor
			};
			
			floatdown(INTAB4,INTAB1,ParaTab);
			
			vector<double> step(double, vector<double>);
			bool stop_flag(double,vector<double>);
	};

#endif
