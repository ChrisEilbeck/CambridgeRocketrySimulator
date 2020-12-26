/*
	%## Copyright (C) 2008 S.Box
	%## RocketFlight.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

#ifndef RocketFlight_H
	#define RocketFlight_H

	#include"ascentcalc.h"
	#include"descentcalc.h"
	#include<string>
	#include<cmath>
	#include"MonteFy.h"
	#include <boost/random.hpp>
	
	#ifndef PI
		#define PI 3.14159265
	#endif

	class Rocket_Flight
	{
		public:
			INTAB IntabTR;	// first stage
			INTAB IntabBS;	// booster stage
			INTAB IntabUS;	// second stage
			
			vector3	X0;		// initial start vector
			
			double RL;		// rail length
			double Az;		// azimuth
			double De;		// Declination Angle
			double Tspan;
			double Tsep;
			double IgDelay; // ignition delay

			bool ballisticfailure;
			bool ShortData;
			
			string FilePath;
			
			Rocket_Flight();
			Rocket_Flight(INTAB);
			Rocket_Flight(INTAB, INTAB, INTAB, double, double);
			
			// void InitialConditionsCalc(void);
			vector<double> getInitialTime(void);
			vector<double> getInitialState(double sigmaDeclinationAngle);
			OutputData OneStageFlight(void);
			OutputData TwoStageFlight(void);
			OutputData OneStageMonte(int);
			OutputData TwoStageMonte(int);
			//void StageTransfer(vector<double>*,vector<double>*,vector<double>*,RKF_data);
			//void ParachuteTransfer(vector<double>*,vector<double>*,RKF_data);
			void TimeTransfer(vector<double>* ttp, RKF_data Stage);
			void StateTransferRocket(vector<double>* zp, RKF_data Stage, INTAB IntabPrevious, INTAB IntabNext);
			void StateTransferParachute(vector<double>* zp, RKF_data Stage);
			void BallisticSwitch(ascent *);
			void setFilePath(string path);
			double getDeploymentAltitude(INTAB thisINTAB);
			double SampleTruncated (double mean, double sigma, double truncateSigma);
	};
	
#endif
