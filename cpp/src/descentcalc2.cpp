/*
%## Copyright (C) 2008 S.Box

%## descentcalc2.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

#include "descentcalc.h"

//functions for extracting Rocket flight data

FlightDataShort descent::getShortData(void) {
	/*
	\brief function to obtain the flight data

	converts the states obtained with RKF_data to FlightData*

	\return FlightDataShort
	*/
	FlightDataShort tempdat;
	try{
		if(DatPop != true){throw 20;}

		tempdat.time=RKd1.t;

		// store last time only as event
		double end_time = RKd1.t.back();
		//cout << end_time << endl;
		tempdat.events.push_back( end_time );

		vector<vector<double> >::iterator z_it;
		for (z_it=RKd1.z.begin(); z_it!=RKd1.z.end(); z_it++)
		{
			vector<double> B = *z_it;
			vector3 A(B[0],B[1],B[2]);
			tempdat.X.push_back(A);
		}
	}
	catch (int ErrorCode){
		cout<<"Error "<<ErrorCode<<": Attempt to populate parachute data before the simulation has been run"<<endl;
	}

	return(tempdat);
};

FlightDataLong descent::getLongData(void) {
	/*
	\brief function to obtain the flight data

	converts the states obtained with RKF_data to FlightData*

	\return FlightDataLong
	*/
	
	FlightDataLong tempdat;

		try{
			if(DatPop != true){throw 20;}

			tempdat.time=RKd1.t;

			// store last time only as event
			double end_time = RKd1.t.back();
			//cout << end_time << endl;
			tempdat.events.push_back( end_time );

			vector<vector<double> >::iterator z_it;
			vector<double>::iterator t_it;
			t_it=tempdat.time.begin();
			for (z_it=RKd1.z.begin(); z_it!=RKd1.z.end(); z_it++)
			{
				double tt = *t_it;
				vector<double> z = *z_it;

				EqMotionData2 EMD = EqMotionSolve(tt,z);

				vector3 vec0(0.0,0.0,0.0);
				matrix3x3 mat0(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);

				//Outputs********************************************************
				tempdat.alpha.push_back(0);
			    tempdat.Thrust.push_back(0);
				tempdat.Mass.push_back(EMD.Mass);
			    tempdat.CofM.push_back(0);
				tempdat.aDensity.push_back(EMD.aDensity);
				tempdat.aTemp.push_back(EMD.aTemp);
				tempdat.X.push_back(EMD.X);
			    tempdat.Raxis.push_back(vec0);
				tempdat.Xdot.push_back(EMD.Xdot);
			    tempdat.Thetadot.push_back(vec0);
				tempdat.Xddot.push_back(EMD.Xddot);
			    tempdat.Thetaddot.push_back(vec0);
				tempdat.Force.push_back(EMD.Force);
			    tempdat.Torque.push_back(vec0);
				tempdat.Wind.push_back(EMD.Wind);
			    tempdat.Inertia.push_back(mat0);
				//***********************************************

			}
			}
		catch (int ErrorCode){
				cout<<"Error "<<ErrorCode<<": Attempt to populate parachute data before the simulation has been run"<<endl;
		}

	return(tempdat);
};
