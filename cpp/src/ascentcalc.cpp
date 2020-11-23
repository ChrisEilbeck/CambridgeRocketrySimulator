/*
	%## Copyright (C) 2008 S.Box

	%## ascentcalc.cpp

	%## Author: S.Box
	%## Created: 2008-05-27
*/

#include "ascentcalc.h"

//ASCENTCALC Builder**************************
ascent::ascent(vector<double> v1, vector<double> v2, INTAB IT1, double d1)
{
	/*
		\brief constructor of ascent class
		
		this simulates the rocket dynamics
		
		\param v1 a vector for the time
		\param v2 a vector for the start position
		\param IT1 INTAB values for the rocket properties, see intabread.cpp
		\param d1 parameter for rail-length, only non-zero at launch
		
		\return void
	*/
	
	tt=v1,
	z0=v2;
	intab1=IT1.intab1;
	intab2=IT1.intab2;
	intab3=IT1.intab3;
	intab4=IT1.intab4;
	RBL=IT1.landa.RBL;
	Ar=IT1.landa.Ar;
	RL=d1;
	X0.e1=z0[0];
	X0.e2=z0[1];
	X0.e3=z0[2];
	Kill.index=9;
	Kill.Kval=-0.001;
	DatPop=false;
};

//BLASTOFF Builder****************************

blastoff::blastoff(INTAB1 IT1,INTAB2 IT2,INTAB3 IT3,INTAB4 IT4,double d1,double d2,double d3,vector3 v1,KillSwitch k1)
{
	/*
		\brief initialise the start
		
		this simulates the rocket dynamics
		
		\param IT1 holds rocket properties, see intabread.cpp
		\param IT2 holds rocket properties, see intabread.cpp
		\param IT3 holds rocket properties, see intabread.cpp
		\param IT4 holds rocket properties, see intabread.cpp
		\param d1 parameter related to Reynolds number [-]
		\param d2 frontal area surface [m2]
		\param d3 rail-length at launch (no rotation)
		\param v1 start position x,y,z
		\param K1 KillSwitch to determine when to stop the simulation
		
		\return void
	*/
	
	intab1=IT1;
	intab2=IT2;
	intab3=IT3;
	intab4=IT4;
	RBL=d1;
	Ar=d2;
	RL=d3;
	X0=v1;
	Kill=k1;
};

//Ascent::fly function*******************************************
RKF_data ascent::fly(void)
{
	/*
		\brief method for runga-kutta integration

		this simulates the rocket dynamics

		\return RKF_data
	*/
	
	blastoff flight1(intab1,intab2,intab3,intab4,RBL,Ar,RL,X0,Kill);
	
	integrator* pflight1=&flight1;
	
	RKF rocketint;
	rocketint.max_it=2000;
	rocketint.max_step=1.0;
	
	RKd1 = rocketint.RKF45(tt,z0,pflight1);
	DatPop=true;
	
	return(RKd1);
};
//***************************************************************

//ascent::SolveEqMotion function******************************************
EqMotionData ascent::SolveEqMotion(double tt,vector<double>z)
{
	/*
		\brief solves the equations of motion
		
		this simulates the rocket dynamics
		
		\param tt indicates the current time in the simulation
		\param z holds the current state
		
		\return EqMotionData
	*/
	
	//Constants****************************************************************
	const double gamma=1.4;		//Ratio if specific heats for air
	const int    R=287;			//gas constant (cp-cv) for air
	const double cll=0.4;		//Lower limit of compressible flow (Mach #)
	const double pgl=0.8;		//Lower limit of prandtl glauert singularity (Mach #)
	const double pgu=1.2;		//Upper limit of prandtl glauert singularity (Mach #)
	const double mu=1.8e-5;		//Kinematic viscosity of air
	const double G=6.6741e-11;  //Universal gravitational constant
	const vector3 YA0(1,0,0);	//Reference Yaw axis;
	const vector3 PA0(0,1,0);	//Reference Pitch axis;
	const vector3 RA0(0,0,1);   //Reference Roll axis;
	
	for(int i=0;i<z.size();i++)
	{
		if(z[i]!=z[i])
		{
			cout << i << endl;
		}
	}
	
	//Unpack z*****************************************************************
	double xn=z[0];			//postion vector
	double yn=z[1];			//"
	double zn=z[2];			//"
	double s=z[3]; 			//quaternion scalar
	double vx=z[4];			//quaternion vector
	double vy=z[5];			//"
	double vz=z[6];			//"
	double Px=z[7];			//Translational momentum vector
	double Py=z[8];			//"
	double Pz=z[9];			//"
	double Ltheta=z[10];	//Rotational momentum vector
	double Lphi=z[11];		//"
	double Lpsi=z[12];		//"
	
	if(tt>0.9)
	{
		bool stop_flag=true;
	}
	
	//gravity******************************************************************
	double g=G*5.9742E24/pow((6378100+zn),2);
	
	//EXTRACT DATA FROM INPUT TABLES 1 and 4***********************************
	
	//INTAB1*******************************************************************
	interp interper;
	vector<double> t=intab1.time; //Get time data from input table
	//t=scaladd(t,ig_delay); ig_delay not currently implemented. To be added later
	
	double Ti,Mi,Ixxi,Iyyi,Izzi,Ixyi,Ixzi,Iyzi,Xcmi,Cda1;
	
	if(tt<=t.front())
	{
		Ti=intab1.Thrust.front();
		Mi=intab1.Mass.front();
		Ixxi=intab1.Ixx.front();
		Iyyi=intab1.Iyy.front();
		Izzi=intab1.Izz.front();
		Ixyi=intab1.Ixy.front();
		Ixzi=intab1.Ixz.front();
		Iyzi=intab1.Iyz.front();
		Xcmi=intab1.Xcm.front();
		Cda1=intab1.Cda1.front();
	}
	else if ( ( tt<t.back() ) && ( tt>t.front() ) ) 
	{
		Ti=interper.one(t,intab1.Thrust,tt); // Thrust-time data
		Mi=interper.one(t,intab1.Mass,tt);   // Mass-time data
		Ixxi=interper.one(t,intab1.Ixx,tt);  //Moments of inertia-time data
		Iyyi=interper.one(t,intab1.Iyy,tt);  //"
		Izzi=interper.one(t,intab1.Izz,tt);  //"
		Ixyi=interper.one(t,intab1.Ixy,tt);  //Products of inertia-time data
		Ixzi=interper.one(t,intab1.Ixz,tt);  //"
		Iyzi=interper.one(t,intab1.Iyz,tt);  //"
		Xcmi=interper.one(t,intab1.Xcm,tt);  //C.O.M.-time data
		Cda1=interper.one(t,intab1.Cda1,tt); //Thrust damping moment coefficient
	}
	else
	{
		Ti=intab1.Thrust.back();
		Mi=intab1.Mass.back();
		Ixxi=intab1.Ixx.back();
		Iyyi=intab1.Iyy.back();
		Izzi=intab1.Izz.back();
		Ixyi=intab1.Ixy.back();
		Ixzi=intab1.Ixz.back();
		Iyzi=intab1.Iyz.back();
		Xcmi=intab1.Xcm.back();
		Cda1=intab1.Cda1.back();
	}

	//INTAB4***********************************************************
	vector<double> ztb=intab4.Alt; //Get altitude data from input table
	
	double Wxi,Wyi,Wzi,rho,temp;
	
	if(		(zn<=ztb.back())
		&&	(zn>=ztb.front()) )
	{
		Wxi=interper.one(ztb,intab4.Wx,zn);		//Wind velocity vector
		Wyi=interper.one(ztb,intab4.Wy,zn);		//"
		Wzi=interper.one(ztb,intab4.Wz,zn);		//"
		rho=interper.one(ztb,intab4.rho,zn);	//Atmospheric density
		temp=interper.one(ztb,intab4.temp,zn);	//Atmospheric Temperature
	} 
	else if(zn>ztb.back()) 
	{
		Wxi=intab4.Wx.back();
		Wyi=intab4.Wy.back();
		Wzi=intab4.Wz.back();
		rho=intab4.rho.back();
		temp=intab4.temp.back();
	}
	else if(zn<ztb.front()) 
	{
		Wxi=intab4.Wx.front();
		Wyi=intab4.Wy.front();
		Wzi=intab4.Wz.front();
		rho=intab4.rho.front();
		temp=intab4.temp.front();
	}
	else 
	{
		Wxi=0.0;
		Wyi=0.0;
		Wzi=0.0;
		temp=288;
		double Pressure=0.002488*pow((temp/216.6),-11.388);
		rho=Pressure/287*temp;
		cout << "warning: problem with atmosphere." << endl;
/*
		temp =(-131.21+(0.00299*zn))+273.15;
		double Pressure=0.002488*pow((temp/216.6),-11.388);
		rho=Pressure/287*temp;
*/
	}
	
	//Calculate angle of attack (alpha)****************************************
	vector3 Pt=vector3(Px,Py,Pz); 									// Rocket momentum vector
	vector3 Xt=vector3(xn,yn,zn);									// Position vector
	quaternion qt=quaternion(s,vx,vy,vz);							//Quaternion
	vector3 Lt=vector3(Ltheta,Lphi,Lpsi);							//Rotational momentum vector
	matrix3x3 Ibody(Ixxi,Ixyi,Ixzi,Ixyi,Iyyi,Iyzi,Ixzi,Iyzi,Izzi);	//Inertia tensor

	vector3 Wt;
	if ((Xt-X0).mag()<=RL)
	{
		vector3 Wpre(0.0,0.0,0.0);
		Wt = Wpre;
	}
	else
	{
		vector3 Wpre(Wxi,Wyi,Wzi);
		Wt = Wpre; // Wind vector
	}

	vector3 Ut=Pt/Mi;				// Rocket earth relative velocity vector
	vector3 Vt=Ut+Wt;				// Rocket atmosphere relative velocity vector

	qt=qt.norm();					//Normalise the quaternion

	s=qt.e1;						//quaternion scalar part;
	vector3 vt(qt.e2,qt.e3,qt.e4);	//quaternion vector part

	matrix3x3 Rt=qt.to_matrix();	//Transform quaternion to rotation matrix

	vector3 YA=Rt*YA0;				// Yaw axis vector
	vector3 PA=Rt*PA0;				// Pitch axis vector
	vector3 RA=Rt*RA0;				// Roll axis vector

	double Utmag=Ut.mag();			// Rocket earth reference velocity
	double Vtmag=Vt.mag();			// Rocket atmosphere reference velocity

	vector3 RAnorm=RA.norm();		// Normalized vector of the rockets roll axis
	vector3 Vtnorm=Vt.norm();		// Normalized vector of atmosphere relative velocity

	double alpha;

	// Calculate angle of attack alpha
	if (Vtmag==0.0) 
	{
		alpha = 0.0;
	}
	else 
	{
		double dprod = Vtnorm.dot(RAnorm);
		if(dprod>1)		{	dprod=1;	}
		if(dprod<-1)	{	dprod=-1;	}
		
		alpha=acos(dprod);
	}

	double Re = rho*Utmag*RBL / mu; // Calculate Reynolds number

	//********************************************************

	//EXTRACT DATA FROM INPUT TABLES 2 and 3***********************************
	//INTAB2*******************************************************************

	double Cd;
	vector<double> an=intab2.alpha;
	vector<double> Ren=intab2.Re;
	vector<vector<double> > Cddat=intab2.CD;
	vector<double> CdReEnd;
	vector<double> CdReBeg;

	vector<vector<double> >::iterator Cdit;

	for (Cdit=Cddat.begin();Cdit!=Cddat.end();Cdit++)
	{
		vector<double> temp =*Cdit;
		CdReEnd.push_back(temp.back());
		CdReBeg.push_back(temp.front());
		temp.clear();
	}
	
	if(			(alpha<an.back())
			&&	(alpha>=0.0)
			&&	(Re>=Ren.front())
			&&	(Re<Ren.back())		)
	{
		Cd=interper.two(an,Ren,Cddat,alpha,Re);
	}
	else if(	(alpha<an.back()
			&&	(alpha>=0.0)
			&&	(Re>=Ren.back())	)
	{
		Cd=interper.one(an,CdReEnd,alpha);
	}
	else if(	(alpha<an.back())
			&&	(alpha>=0.0)
			&&	(Re>=0)
			&&	(Re<Ren.front())	)
	{
		Cd=interper.one(an,CdReEnd,alpha); // bug carried over from old code for comparison!! Change CdReEnd to CdReBeg when happy
	}
	else if(alpha>an.back())
	{
		Cd=1.0;
	}
	else 
	{
		Cd=0.5;
		cout<<"Warning: Cd calculation is broken, check it, a:"<<alpha<<" R:"<< Re<<"\n" ;
	}

	//INTAB 3*******************************************************************
	double Cn=intab3.CNa;			// Coefficient of normal force
	double Cp=intab3.Xcp;			// Centre of pressure
	//*************************************************************************
	
	//Prandtl-Glauert Compressibility Correction********************************
	double c=sqrt(gamma*R*temp);	//Calculate local speed of sound
	double Ma=Vtmag/c;				//Calculate Mach number

	if(Ma<cll)
	{
		Cd=Cd;
		Cn=Cn;
	}
	else if(	(Ma>=cll)
			&&	(Ma<pgl)	)
	{
		Cd=Cd/sqrt(1-pow(Ma,2));
		Cn=Cn/sqrt(1-pow(Ma,2));
	}
	else if (Ma>=pgl && Ma<pgu)
	{
		Cd=Cd/sqrt(1-pow(pgl,2));
		Cn=Cn/sqrt(1-pow(pgl,2));
	}
	else if (Ma>=pgu)
	{
#if 0
		Cd=Cd/sqrt((pow(Ma,2))-1);
		Cn=Cn/sqrt((pow(Ma,2))-1);
#else
		Cd=Cd/sqrt(1-pow(pgl,2));
		Cn=Cn/sqrt(1-pow(pgl,2));		//High drag model of supersonic drag
#endif
	}
	else 
	{
		cout<<"Error in prandtl glauert calculation\n";
	}

	//**************************************************************************

	//CP AOA calculations*******************************************************
	matrix3x3 Itinv=Rt*Ibody.inv()*Rt.transpose();//Angular momentum to Angular velocity transform matrix
	double Xbar=Cp-Xcmi;//Torque moment arm
	vector3 omegat=Itinv*Lt;//Angular velocity vector

	vector3 omegaax=omegat.norm();
	vector3 omegaax2=omegaax*(-1);

	double omega= omegat.mag();
	double omegacp= omega*Xbar;

	vector3 omegacpd=RAnorm.cross(omegaax);
	vector3 omegacpt=omegacpd*omegacp;

	vector3 Vcpt=Vt+omegacpt;

	double Vcptmag=Vcpt.mag();
	vector3 Vcptnorm=Vcpt.norm();

	double alphacp;

	if(Vcptmag==0.0)
	{
		alphacp=0.0;
	}
	else
	{
		double dprod = Vcptnorm.dot(RAnorm);
		if(dprod>1)		{	dprod = 1;	}
		if(dprod<-1)	{	dprod=-1;	}
		
		alphacp=acos(dprod);
	}

	//**************************************************************************
	
	//Force Vector Calculation**************************************************
	double mg=Mi*g; 				// Gravity
	
	// reasonable thrust-to-weight ratio (source wikipedia)
	double maxThrustWeightRatio=250;
	if(Ti>(maxThrustWeightRatio*mg))
	{
		cout<<"warning, unreasonable thrust, ratio: "<<(Ti/mg)<<" > "<<maxThrustWeightRatio<<endl;
		Ti=maxThrustWeightRatio*mg;
	}
	
	double FA=Cd*0.5*rho*pow(Utmag,2)*Ar;						//Axial Drag
	
	double FNcp=Cn*0.5*rho*(pow(Vcptmag,2))*Ar*sin(alphacp);	//Normal Force
	
	vector3 Tt=RAnorm*Ti;										//Thrust Vector
	
	vector3 FAt=RAnorm*(-1)*FA;									//Axial drag vector
	
	vector3 momaxcp = RAnorm.cross(Vcptnorm);					//Axis of pitching motion
	momaxcp=momaxcp.norm();										//check normalized
	
	vector3 FNn = RAnorm.cross(momaxcp);						//Normal force direction vector;
	vector3 FNt=FNn*FNcp;										//Normal Force Vector
	//**************************************************************************
	
	//Torque vector calculations************************************************
	vector3 Tqf=momaxcp*FNcp*Xbar;								//Torque due to normal force
	vector3 Tqm=omegaax2*Cda1*omega;							//Torgue due to motor plume
	vector3 Tqp=Tqf+Tqm;
	//**************************************************************************
	
	//quaternion derivative*****************************************************
	double sdot = omegat.dot(vt)*(-1)*0.5;
	vector3 vtdot=((omegat*s)+(omegat.cross(vt)))*0.5;
	quaternion Qdot = qt.deriv(omegat);
	//**************************************************************************
	
	//Launch Rail Constraints***************************************************
	vector3 Ft(0.0,0.0,0.0);									//Total Force Vector
	vector3 Tqt(0.0,0.0,0.0);									//Total Torque Vector
	vector3 gt(0.0,0.0,-mg);									//gravity vector
	
	Ft = Tt + FAt + FNt + gt;
	
	if ( (Ft.e3 <= 0.0) && ((Xt - X0).mag() < 0.1) ) 
	{
		Ft.e1=0.0; Ft.e2=0.0; Ft.e3=0.0;
	} // Solid base to launch pad ** change zn < 0.1 in future
	
	if ( (Xt-X0).mag()<=RL )
	{ 
		//Rocket constrained on rail
	} 
	else
	{
		Tqt=Tqp;
	}
	
	// OUTPUT
	EqMotionData Output;
	Output.alpha = alphacp;
	Output.Thrust = Ti;
	Output.Mass = Mi;
	Output.CofM = Xcmi;
	Output.aDensity = rho;
	Output.aTemp = temp;
	Output.X = Xt;
	Output.Raxis = RAnorm;
	Output.Xdot = Ut;
	Output.Thetadot = omegat;
	Output.Xddot = Ft/Mi;
	Output.Thetaddot = Itinv*Tqt;
	Output.Force =Ft;
	Output.Torque = Tqt;
	Output.Wind = Wt;
	Output.Inertia = Ibody;
	Output.Qdot = Qdot;

	return(Output);
};

bool blastoff::stop_flag(double t,vector<double> z)
{
	/*
		\brief checks for stop_flag
		
		checks whether the simulation should continue
		
		\param t current time
		\param z holds the current states
		
		\return whether the simulation should continue
	*/
	
	bool temp;
	if(z[Kill.index]<Kill.Kval)
		temp=true;
	else
		temp=false;
	
	return(temp);
}

//blastoff::step function*******************************
vector<double> blastoff::step(double tt, vector<double> z)
{
	/*
		\brief calculate one time-step
		
		calculates the new equations of motion and returns those as EqMotionData
		
		\param tt current step
		\param z holds the current states
		
		\return whether the simulation should continue
	*/
	
	EqMotionData SuperZ = SolveEqMotion(tt,z);
	vector<double> Z;
	Z.push_back(SuperZ.Xdot.e1);
	Z.push_back(SuperZ.Xdot.e2);
	Z.push_back(SuperZ.Xdot.e3);
	Z.push_back(SuperZ.Qdot.e1);
	Z.push_back(SuperZ.Qdot.e2);
	Z.push_back(SuperZ.Qdot.e3);
	Z.push_back(SuperZ.Qdot.e4);
	Z.push_back(SuperZ.Force.e1);
	Z.push_back(SuperZ.Force.e2);
	Z.push_back(SuperZ.Force.e3);
	Z.push_back(SuperZ.Torque.e1);
	Z.push_back(SuperZ.Torque.e2);
	Z.push_back(SuperZ.Torque.e3);

	return(Z);
}
