/*
%## Copyright (C) 2008 S.Box


%## ascentcalc.h

%## Author: S.Box
%## Created: 2008-05-27
*/

//ascentcalc.h
#ifndef ascentcalc_H
#define ascentcalc_H

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

class EqMotionData{
	/*
	\brief class that holds state parameters

	class that holds state parameters
	*/
	public:
		double
			alpha, // angle of attack
			Thrust, // thrust produced [N]
			Mass, // current mass [kg]
			CofM, // moment coefficient [Nm]
			aDensity, // atmospheric density
			aTemp; // atmospheric temperature
		vector3
			Raxis, // rotating axis
			X, // position vector [m]
			Xdot, // velocity vector [m/s]
			Thetadot, // spin [rad/s]
			Xddot, // acceleration [m/s2]
			Thetaddot, // spin acceleration [rad/s2]
			Force, // resulting force vector [N]
			Torque, // torque vector [Nm]
			Wind; // wind velocity vector [m/s]
		matrix3x3
			Inertia; // rocket inertia x,y,z
		quaternion
			Qdot; // quaternion angles
		EqMotionData(){}; // constructor
};



class KillSwitch{
	/*
	class for KillSwitch
	*/
	public:
		int index; // this index relates to the state found in z (see ascentcalc.cpp)
		double Kval; // value of this state
		KillSwitch(){}; // empty constructor
		KillSwitch(int a, double b){
			/*
			constructor for KillSwitch with arguments, simulation runs till this state/value

			arg:
			a -- state index
			b -- value of state
			*/
			index=a;
			Kval=b;
		};
};

class ascent{
	/*
	while this class is called ascent, it actually holds the rocket dynamics, and descent is for the parachute dynamics.
	*/
	public:
		vector<double>
			tt, // time
			z0; // initial state
		INTAB1
			intab1; // see intabread.h for description
		INTAB2
			intab2; // see intabread.h for description
		INTAB3
			intab3; // see intabread.h for description
		INTAB4
			intab4; // see intabread.h for description
		double
			RBL, //
			Ar, //
			RL; //
		vector3
			X0; // position vector
		KillSwitch
			Kill; // killswitch (when to stop the simulation)

		bool DatPop; // indicates whether simulation is run
		RKF_data RKd1; // see RKF45.h for description

		ascent(){
			// empty constructor
			DatPop=false;
		};
		ascent(vector<double>, vector<double>, INTAB,double);
		RKF_data fly(void);
		EqMotionData SolveEqMotion(double, vector<double>);
		FlightDataShort getShortData(void);
		FlightDataLong getLongData(void);
};

class blastoff:public integrator, public RKF, public ascent{
	/*
	class that starts the simulation
	*/
	public:
		blastoff(){};
		blastoff(INTAB1,INTAB2,INTAB3,INTAB4,double,double,double,vector3,KillSwitch);
		vector<double> step(double, vector<double>);
		bool stop_flag(double,vector<double>);
};


#endif
