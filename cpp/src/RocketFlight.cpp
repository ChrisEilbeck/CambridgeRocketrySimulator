/*
%## Copyright (C) 2008 S.Box
%## RocketFlight.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

//RocketFlight.cpp

#include"RocketFlight.h"


//Defing Constructors******************************************************************

Rocket_Flight::Rocket_Flight() 
{
	/*
		\brief initialises rocket flight conditions

		initialises rocket flight conditions, such as launch location

		\return void
	*/
	
	X0.e1=0.0; X0.e2=0.0; X0.e3=0.0;//default initial position
	RL=2.0;                         //default rail length
	Az=0.0;                         //default rail azimuth
	De=0.0;							//default rail declination
	Tspan=2000;						//default maximum simulation time
	ballisticfailure=false;			//default setting flight terminates at apogee
	ShortData=false;			    //default output data format is long
};

Rocket_Flight::Rocket_Flight(INTAB IT1)
{
	/*
		\brief initialises rocket flight conditions

		initialises rocket flight conditions, such as launch location

		\param IT1 intab1, see specifics intabread.h

		\return void
	*/
	
	IntabTR=IT1;
	X0.e1=0.0; X0.e2=0.0; X0.e3=0.0;//default initial position
	RL=2.0;                         //default rail length
	Az=0.0;                         //default rail azimuth
	De=0.0;							//default rail declination
	Tspan=2000;						//default maximum simulation time
	ballisticfailure=false;			//default setting flight terminates at apogee
	ShortData=false;			    //default output data format is long
};

Rocket_Flight::Rocket_Flight(INTAB IT1, INTAB IT2, INTAB IT3, double d1, double d2)
{
	/*
		\brief initialises rocket flight conditions

		initialises rocket flight conditions, such as launch location

		\param IT1 intab1, see specifics intabread.h
		\param IT2 intab2, see specifics intabread.h
		\param IT3 intab3, see specifics intabread.h
		\param d1 separation time
		\param d2 ignition delay (on top of separation time)

		\return void
	*/
	
	IntabTR=IT1;
	IntabBS=IT2;
	IntabUS=IT3;
	Tsep=d1;
	IgDelay=d2;
	X0.e1=0.0; X0.e2=0.0; X0.e3=0.0;// default initial position
	RL=2.0;                         // default rail length
	Az=0.0;                         // default rail azimuth
	De=0.0;													// default rail declination
	Tspan=2000;											// default maximum simulation time
	ballisticfailure=false;					// default setting flight terminates at apogee
	ShortData=false;			    			// default output data format is long
};

//****************************************************************************************

//Functions to calculate the initial conditions********************************************

vector<double> Rocket_Flight::getInitialTime(void)
{
	//	returns the initial time vector
	vector<double> tt;

	tt.push_back(0);
	tt.push_back(Tspan);

	return tt;
};

vector<double> Rocket_Flight::getInitialState(double sigmaDeclinationAngle) 
{
	//	returns the initial state

	vector<double> z0;

	z0.push_back(X0.e1);
	z0.push_back(X0.e2);
	z0.push_back(X0.e3);

	// hardcoded limits here
	double De_temp = SampleTruncated (De, sigmaDeclinationAngle, 1.0);
	double De_rad = De_temp * PI / 180.0;
	bearing AzOrth((Az-90.0),1.0);
	vector2 AzVec=AzOrth.to_vector();
	AzVec=AzVec.norm();

	// Calculate the initial quaternion

	quaternion Q0((cos(De_rad/2.0)),(sin(De_rad/2.0)*AzVec.e1),(sin(De_rad/2.0)*AzVec.e2),0.0);
	Q0=Q0.norm();

	z0.push_back(Q0.e1);
	z0.push_back(Q0.e2);
	z0.push_back(Q0.e3);
	z0.push_back(Q0.e4);

	// xyz 4x quaternion velocity
	for (int i = 0; i<6; i++)
	{
		z0.push_back(0.0);
	}

	return z0;

};

//Flight Functions**********************************************************************

OutputData Rocket_Flight::OneStageFlight(void)
{
	/*
		\brief runs all phases for a one stage flight

		\return OutputData
	*/

	// Create the initial conditions
	vector<double> tt, z0;

	tt = getInitialTime();
	z0 = getInitialState(0);

	// Initialize rocket ascent simulation
	ascent as1(tt, z0, IntabTR, RL);

	/*
		Note: default killswitch in "as1" is set to apogee by negative vertical speed
		index = 9 (vertical speed)
		value = -0.001 (negative)
	*/

	// fly! launch to apogee
	RKF_data rocketLaunchToApogee = as1.fly();

	// Simulate descent stages [ROCKET]

	// take final state of previous stage (rocket) to next stage (rocket)
	vector<double> tt2, z02;

	TimeTransfer(&tt2, rocketLaunchToApogee);

	StateTransferRocket(&z02, rocketLaunchToApogee, IntabTR, IntabTR);

	// initialize rocket descent simulation
	ascent ds1(tt2, z02, IntabTR, 0.0);

	// change kill switch to enable at set altitude
	// index 2 = state altitude, value = deployment
	ds1.Kill = KillSwitch(2, getDeploymentAltitude(IntabTR));

	// fly! descent to deployment of parachute
	RKF_data rocketApogeeToDeployment = ds1.fly();

	// Simulate descent [PARACHUTE]

	// take final state of previous stage (rocket) to next stage (parachute)
	vector<double> tt3, z03;
	TimeTransfer(&tt3, rocketApogeeToDeployment);
	StateTransferParachute(&z03, rocketApogeeToDeployment);

	// initialize parachute descent simulation
	descent ds2(tt3, z03, IntabTR);

	// fly! deployment of parachute to ground
	RKF_data parachuteDeploymentToGround = ds2.fall();

	// write all information to output

	OutputData OD;
	OD.InitializePropertyTree("OneStageFlight");

	if(ShortData==true)
	{
		FlightDataShort FDS = (as1.getShortData() + ds1.getShortData() + ds2.getShortData());
		OD.FillPropertyTree(&FDS, 1);
	}
	else
	{
		FlightDataLong FDL = (as1.getLongData() + ds1.getLongData() + ds2.getLongData());
		OD.FillPropertyTree(&FDL, 1);
	}

    return(OD);
};

OutputData Rocket_Flight::TwoStageFlight()
{
	/*
		\brief runs all phases for a two stage flight

		\return OutputData
	*/

	//Create the initial conditions
	vector<double> tt, z0;

	tt = getInitialTime();
	z0 = getInitialState(0);

	// Initialize the simulation from launch to separation
	tt[1] = Tsep;

	ascent as1(tt, z0, IntabTR, RL);

	// fly! launch to separation (rocket, complete)
	RKF_data rocketLaunchToSeparation = as1.fly();

	// copy states (rocket, complete) to (rocket, upper stage)
	vector<double> tt1, z01;
	TimeTransfer(&tt1, rocketLaunchToSeparation);
	StateTransferRocket(&z01, rocketLaunchToSeparation, IntabTR, IntabUS);

	// Add ignition delay to (rocket, upper stage)
	INTAB IntabUS_t = IntabUS;
	IntabUS_t.intab1.addDelay(Tsep+IgDelay);

	// initialize, IntabUS_t = second stage +delay, 0,0 = rail length
	ascent as2(tt1, z01, IntabUS_t, 0.0);

	// fly! separation to apogee (rocket, upper stage)
	RKF_data rocketSeparationToApogee = as2.fly();

	// copy states (rocket, upper stage) to (rocket, upper stage)
	vector<double> tt2, z02;
	TimeTransfer(&tt2, rocketSeparationToApogee);
	StateTransferRocket(&z02, rocketSeparationToApogee, IntabUS_t, IntabUS_t);

	// initialize
	ascent ds1(tt2, z02, IntabUS_t, 0.0);

	// change kill switch to enable at set altitude
	ds1.Kill = KillSwitch(2, getDeploymentAltitude(IntabUS_t));

	// fly! descent to deployment of parachute (rocket, upper stage)
	RKF_data rocketApogeeToParachute = ds1.fly();

	// copy states (rocket) to (parachute)
	vector<double> tt3, z03;
	TimeTransfer(&tt3, rocketApogeeToParachute);
	StateTransferParachute(&z03, rocketApogeeToParachute);

	// initialize parachute descent simulation
	descent ds2(tt3, z03, IntabUS_t);

	// fly! deployment of parachute to ground (rocket, upper stage)
	RKF_data rocketParachuteToGround = ds2.fall();

	// *** booster stage

	// copy states (rocket, complete) to (rocket, booster)
	vector<double> tt4, z04;
	TimeTransfer(&tt4, rocketLaunchToSeparation);
	StateTransferRocket(&z04, rocketLaunchToSeparation, IntabTR, IntabBS);

	// initialize
	ascent as4(tt4, z04, IntabBS, 0.0);

	// fly! separation to apogee (booster)
	RKF_data boosterSeparationToApogee = as4.fly();

	// copy states (rocket, booster) to (rocket, booster)
	vector<double> tt5, z05;
	TimeTransfer(&tt5, boosterSeparationToApogee);
	StateTransferRocket(&z05, boosterSeparationToApogee, IntabBS, IntabBS);

	// initialize
	ascent ds3(tt5, z05, IntabBS, 0.0);

	// change kill switch to enable at set altitude
	ds3.Kill = KillSwitch(2, getDeploymentAltitude(IntabBS));

	// fly! descent to deployment of parachute (booster)
	RKF_data boosterApogeeToParachute = ds3.fly();

	// copy states (rocket) to (parachute)
	vector<double> tt6, z06;
	TimeTransfer(&tt6, boosterApogeeToParachute);
	StateTransferParachute(&z06, boosterApogeeToParachute);

	// initialize
	descent ds4(tt6, z06, IntabBS);

	RKF_data boosterParachuteToGround = ds4.fall();

	/// Package and output
	OutputData OD;
	OD.InitializePropertyTree("TwoStageFlight");
	vector<FlightData *> FDp;

	if (ShortData == true)
	{
		// booster (bottom part)
		FlightDataShort FDSL = (as1.getShortData() + as4.getShortData() + ds3.getShortData() + ds4.getShortData());
		// upper (top part)
		FlightDataShort FDSU = (as1.getShortData() + as2.getShortData() + ds1.getShortData() + ds2.getShortData());
		FDp.push_back(&FDSL);
		FDp.push_back(&FDSU);
		OD.FillPropertyTree(FDp, 1);
	}
	else
	{
		// booster (bottom part)
		FlightDataLong FDLL = (as1.getLongData() + as4.getLongData() + ds3.getLongData() + ds4.getLongData());
		// upper (top part)
		FlightDataLong FDLU = (as1.getLongData() + as2.getLongData() + ds1.getLongData() + ds2.getLongData());
		FDp.push_back(&FDLL);
		FDp.push_back(&FDLU);
		OD.FillPropertyTree(FDp, 1);
	}

	return(OD);

};

OutputData Rocket_Flight::OneStageMonte(int noi){
	/*
		\brief runs all phases for a one stage flight, with monte carlo

		runs all phases for a one stage flight, using monte carlo to estimate the effect of the uncertainty. Note, first run is nominal

		\param noi number of monte carlo runs

		\return OutputData
	*/

	//Create the initial conditions
	vector<double> tt, z0;

	tt = getInitialTime();
	z0 = getInitialState(0);

    string Upath = FilePath;
    Upath.append("Uncertainty.xml");

	MonteFy StochTab(IntabTR,Upath);

	OutputData OD;
	OD.InitializePropertyTree("OneStageMonte");
	int Frun = 0;

	for(int i=0; i<noi;i++)
	{
		//Initialize rocket ascent simulation
		try
		{
			INTAB Ftab;
			
			if(i == 0)
			{
				Ftab = IntabTR;
			}
			else
			{
				Ftab = StochTab.Wiggle();
				//Ftab = IntabTR;
				// vary initial declination angle
				z0 = getInitialState(StochTab.sigmaLaunchDeclination);
			}

			// Initialize rocket ascent simulation
			ascent as1(tt, z0, Ftab, RL);

			/*
				Note: default killswitch in "as1" is set to apogee by negative vertical speed
				index = 9 (vertical speed)
				value = -0.001 (negative)
			*/

			// fly! launch to apogee
			RKF_data rocketLaunchToApogee = as1.fly();

			// Simulate descent stages [ROCKET]

			// take final state of previous stage (rocket) to next stage (rocket)
			vector<double> tt2, z02;
			TimeTransfer(&tt2, rocketLaunchToApogee);
			StateTransferRocket(&z02, rocketLaunchToApogee, Ftab, Ftab);

			// initialize rocket descent simulation
			ascent ds1(tt2, z02, Ftab, 0.0);

			// change kill switch to enable at set altitude
			ds1.Kill = KillSwitch(2, getDeploymentAltitude(Ftab));

			// fly! descent to deployment of parachute
			RKF_data rocketApogeeToDeployment = ds1.fly();

			// Simulate descent [PARACHUTE]

			// take final state of previous stage (rocket) to next stage (parachute)
			vector<double> tt3, z03;
			TimeTransfer(&tt3, rocketApogeeToDeployment);
			StateTransferParachute(&z03, rocketApogeeToDeployment);

			// initialize parachute descent simulation
			descent ds2(tt3, z03, Ftab);

			// fly! deployment of parachute to ground
			RKF_data parachuteDeploymentToGround = ds2.fall();

			/*
			ascent as1(tt,z0,Ftab,RL);
			BallisticSwitch(&as1);

			//Fly the rocket!
			RKF_data updata=as1.fly();

			//Take the final state of the rocket to define the
			//initial conditions for the parachute
			vector<double> tt2, z02;
			ParachuteTransfer(&tt2,&z02,updata);

			//Initialize parachute descent simulation
			descent ds1(tt2,z02,IntabTR);
			RKF_data downdata=ds1.fall();

			*/

			if (ShortData == true)
			{
				FlightDataShort FDS = (as1.getShortData() + ds1.getShortData() + ds2.getShortData());
				OD.FillPropertyTree(&FDS,(i-Frun+1));
			}
			else
			{
				FlightDataLong FDL = (as1.getLongData() + ds1.getLongData() + ds2.getLongData());
				OD.FillPropertyTree(&FDL,(i-Frun+1));
			}

		}
		catch(exception e)
		{
			Frun++;
			cout << "Run failed, number of failed runs is: " << Frun << endl;
		}

		// finished a single run
		cout << "(" << (i+1) << "," << noi << ")" << endl;

	}
	
	return(OD);
};

OutputData Rocket_Flight::TwoStageMonte(int noi)
{
	/*
	\brief runs all phases for a two stage flight, with monte carlo

	runs all phases for a two stage flight, using monte carlo to estimate the effect of the uncertainty. Note, first run is nominal

	\param noi number of monte carlo runs to execute

	\return OutputData
	*/

	//Create the initial conditions
	vector<double> tt, z0;

	tt = getInitialTime();
	z0 = getInitialState(0);

	//Initialize the simulation from launch to separation
	tt[1]=Tsep;
	string Upath = FilePath;
	Upath.append("Uncertainty.xml");
	MonteFy StochTabTR(IntabTR,Upath);
	MonteFy StochTabBS(IntabBS,Upath);
	MonteFy StochTabUS(IntabUS,Upath);
	OutputData OD;
	OD.InitializePropertyTree("TwoStageMonte");
	int Frun=0;

	for (int i =0; i<noi; i++)
	{
		try
		{
			INTAB FtabTR, FtabBS, FtabUS;
			
			if(i == 0)
			{
				FtabTR = IntabTR; // rocket, complete
				FtabBS = IntabBS; // rocket, booster
				FtabUS = IntabUS; // rocket, upper
			}
			else
			{
				FtabTR = StochTabTR.Wiggle();
				FtabBS = StochTabBS.Wiggle();
				FtabUS = StochTabUS.Wiggle();
				// vary initial declination angle
				z0 = getInitialState(StochTabTR.sigmaLaunchDeclination);
			}

			// Initialize the simulation from launch to separation
			tt[1] = Tsep;

			ascent as1(tt, z0, FtabTR, RL);

			// fly! launch to separation (rocket, complete)
			RKF_data rocketLaunchToSeparation = as1.fly();

			// copy states (rocket, complete) to (rocket, upper stage)
			vector<double> tt1, z01;
			TimeTransfer(&tt1, rocketLaunchToSeparation);
			StateTransferRocket(&z01, rocketLaunchToSeparation, FtabTR, FtabUS);

			// Add ignition delay to (rocket, upper stage)
			INTAB IntabUS_t = FtabUS;
			IntabUS_t.intab1.addDelay(Tsep + IgDelay);

			// initialize, IntabUS_t = second stage +delay, 0,0 = rail length
			ascent as2(tt1, z01, IntabUS_t, 0.0);

			// fly! separation to apogee (rocket, upper stage)
			RKF_data rocketSeparationToApogee = as2.fly();

			// copy states (rocket, upper stage) to (rocket, upper stage)
			vector<double> tt2, z02;

			TimeTransfer(&tt2, rocketSeparationToApogee);

			StateTransferRocket(&z02, rocketSeparationToApogee, IntabUS_t, IntabUS_t);

			// initialize
			ascent ds1(tt2, z02, IntabUS_t, 0.0);

			// change kill switch to enable at set altitude
			ds1.Kill = KillSwitch(2, getDeploymentAltitude(IntabUS_t));

			// fly! descent to deployment of parachute (rocket, upper stage)
			RKF_data rocketApogeeToParachute = ds1.fly();

			// copy states (rocket) to (parachute)
			vector<double> tt3, z03;
			TimeTransfer(&tt3, rocketApogeeToParachute);
			StateTransferParachute(&z03, rocketApogeeToParachute);

			// initialize parachute descent simulation
			descent ds2(tt3, z03, IntabUS_t);

			// fly! deployment of parachute to ground (rocket, upper stage)
			RKF_data rocketParachuteToGround = ds2.fall();

			// *** booster stage

			// copy states (rocket, complete) to (rocket, booster)
			vector<double> tt4, z04;
			TimeTransfer(&tt4, rocketLaunchToSeparation);
			StateTransferRocket(&z04, rocketLaunchToSeparation, FtabTR, FtabBS);

			// initialize
			ascent as4(tt4, z04, FtabBS, 0.0);

			// fly! separation to apogee (booster)
			RKF_data boosterSeparationToApogee = as4.fly();

			// copy states (rocket, booster) to (rocket, booster)
			vector<double> tt5, z05;
			TimeTransfer(&tt5, boosterSeparationToApogee);
			StateTransferRocket(&z05, boosterSeparationToApogee, FtabBS, FtabBS);

			// initialize
			ascent ds3(tt5, z05, FtabBS, 0.0);

			// change kill switch to enable at set altitude
			ds3.Kill = KillSwitch(2, getDeploymentAltitude(FtabBS));

			// fly! descent to deployment of parachute (booster)
			RKF_data boosterApogeeToParachute = ds3.fly();

			// copy states (rocket) to (parachute)
			vector<double> tt6, z06;
			TimeTransfer(&tt6, boosterApogeeToParachute);
			StateTransferParachute(&z06, boosterApogeeToParachute);

			// initialize
			descent ds4(tt6, z06, FtabBS);

			RKF_data boosterParachuteToGround = ds4.fall();

			///Package and output
			vector<FlightData *> FDp;

			if (ShortData == true){
				// booster (bottom part)
				FlightDataShort FDSL = (as1.getShortData() + as4.getShortData() + ds3.getShortData() + ds4.getShortData());
				// upper (top part)
				FlightDataShort FDSU = (as1.getShortData() + as2.getShortData() + ds1.getShortData() + ds2.getShortData());
				FDp.push_back(&FDSL);
				FDp.push_back(&FDSU);
				OD.FillPropertyTree(FDp,(i-Frun+1));
			}
			else{
				// booster (bottom part)
				FlightDataLong FDLL = (as1.getLongData() + as4.getLongData() + ds3.getLongData() + ds4.getLongData());
				// upper (top part)
				FlightDataLong FDLU = (as1.getLongData() + as2.getLongData() + ds1.getLongData() + ds2.getLongData());
				FDp.push_back(&FDLL);
				FDp.push_back(&FDLU);
				OD.FillPropertyTree(FDp,(i-Frun+1));
			}
		}
		catch(exception e)
		{
			Frun++;
			cout<<"Run failed, number of failed runs is: "<<Frun<<endl;
		}

		// finished a single run
		cout << "(" << (i+1) << "," << noi << ")" << endl;

	}


	return(OD);
};

void Rocket_Flight::StateTransferParachute(vector<double>* zp, RKF_data Stage)
{
	/*
		for the parachute transfer, all the other parameters are not required.
	*/
	zp->push_back(Stage.z.back()[0]); // x
	zp->push_back(Stage.z.back()[1]); // y
	zp->push_back(Stage.z.back()[2]); // z
	// 3, 4, 5, 6 - 4x quaternions
	zp->push_back(Stage.z.back()[7]); // x dot (velocity)
	zp->push_back(Stage.z.back()[8]); // y dot
	zp->push_back(Stage.z.back()[9]); // z dot
	// 10, 11, 12 - 3x angular velocity
};



void Rocket_Flight::TimeTransfer(vector<double> * ttp, RKF_data Stage)
{
	/*
		copy the timings from the end of one stage to the next
		ttp[0] start
		ttp[1] end
	*/
	double t1 = Stage.t.back();
	double t2 = t1 + Tspan;

	ttp->push_back(t1);
	ttp->push_back(t2);
};

void Rocket_Flight::StateTransferRocket(vector<double> *zp, RKF_data StagePrevious, INTAB IntabPrevious, INTAB IntabNext)
{
	/*
		transfer the state as a start position of the next stage [ROCKET]
		zp: state vector to fill (this will have the start position)
		StagePrevious: the data as returned from the previous simulation
		IntabPrevious: the rocket data from the previous stage
		IntabNext: the rocket data for the next stage
	*/

	vector3 TransitionMomentum(StagePrevious.z.back()[7],StagePrevious.z.back()[8],StagePrevious.z.back()[9]);
	vector3 TransitionVelocity = TransitionMomentum/IntabPrevious.intab1.Mass.back();

	vector3 Momentum_next = TransitionVelocity*IntabNext.intab1.Mass.front();

	vector3 TransitionAngMom(StagePrevious.z.back()[10],StagePrevious.z.back()[11],StagePrevious.z.back()[12]);
	quaternion TransitionQ(StagePrevious.z.back()[3],StagePrevious.z.back()[4],StagePrevious.z.back()[5],StagePrevious.z.back()[6]);
	matrix3x3 TransitionR = TransitionQ.to_matrix();

	matrix3x3 TransitionTensor(IntabPrevious.intab1.Ixx.back(),IntabPrevious.intab1.Ixy.back(),IntabPrevious.intab1.Ixz.back(),IntabPrevious.intab1.Ixy.back(),IntabPrevious.intab1.Iyy.back(),IntabPrevious.intab1.Iyz.back(),IntabPrevious.intab1.Ixz.back(),IntabPrevious.intab1.Iyz.back(),IntabPrevious.intab1.Izz.back());
	matrix3x3 TransitionInverse = TransitionR*TransitionTensor.inv()*TransitionR.transpose();
	vector3 TransitionOmega = TransitionInverse*TransitionAngMom;

	matrix3x3 Tensor_next(IntabNext.intab1.Ixx.front(),IntabNext.intab1.Ixy.front(),IntabNext.intab1.Ixz.front(),IntabNext.intab1.Ixy.front(),IntabNext.intab1.Iyy.front(),IntabNext.intab1.Iyz.front(),IntabNext.intab1.Ixz.front(),IntabNext.intab1.Iyz.front(),IntabNext.intab1.Izz.front());

	matrix3x3 Inverse_next = TransitionR*Tensor_next.inv()*TransitionR.transpose();

	vector3 AngMom_next = Inverse_next.inv()*TransitionOmega;

	for (int i=0; i<7; i++){
		zp->push_back(StagePrevious.z.back()[i]);
	}

	zp->push_back(Momentum_next.e1);
	zp->push_back(Momentum_next.e2);
	zp->push_back(Momentum_next.e3);

	zp->push_back(AngMom_next.e1);
	zp->push_back(AngMom_next.e2);
	zp->push_back(AngMom_next.e3);
}

void Rocket_Flight::BallisticSwitch(ascent * a1)
{
	if (ballisticfailure==true){
		a1->Kill.index = 2;      //Change the stop flag settings so that the
		a1->Kill.Kval = -0.01;   //the ground
	}
}

void Rocket_Flight::setFilePath(string path)
{
    FilePath = path;
}

double Rocket_Flight::getDeploymentAltitude(INTAB thisINTAB) 
{
	/*
		1. apogee (one of the parachutes has large altitude > 1000 000)
		2. altitude (one of the parachutes has a sensible altitude)
		3. never (all parachutes have negative altitude)
	*/

	double deployAtAltitude = 0.001; // default: don't deploy

	ParaTab thisParaTab = thisINTAB.paratab;

	vector<double>::iterator altitudeIterator;

	for (altitudeIterator = thisParaTab.AltPd.begin(); altitudeIterator != thisParaTab.AltPd.end(); altitudeIterator++)
	{
		// find highest altitude (moment for killswitch)
		if (*altitudeIterator > deployAtAltitude) 
		{
			deployAtAltitude = *altitudeIterator;
		}
	}

	return deployAtAltitude;
}

double Rocket_Flight::SampleTruncated (double mean, double sigma, double truncateSigma) 
{
	/*
	\brief sample from a truncated Gaussian

	provides a truncated Gaussian sampling method

	\param mean average
	\param sigma standard deviation
	\param truncateSigma at which sigma to truncate

	\return double with a sample
	*/
	using namespace boost;
	// Create a Mersenne twister random number generator
	// that is seeded once with #seconds since 1970
	static mt19937 rng(static_cast<unsigned> (std::time(0)));

	// select Gaussian probability distribution
	normal_distribution<double> norm_dist(mean, sigma);

	// bind random number generator to distribution, forming a function
	variate_generator<mt19937&, normal_distribution<double> >  normal_sampler(rng, norm_dist);

 	// sample from the distribution
	double Out = normal_sampler();

	// truncate at +- 1 sigma
	double lowerBound = mean - truncateSigma*sigma - 0.0001;
	double upperBound = mean + truncateSigma*sigma + 0.0001; // added a little margin

	while ((Out <= lowerBound) || (Out >= upperBound)) 
	{
		// re- sample
		Out = normal_sampler();
	}

	// return value
	return(Out);
}
