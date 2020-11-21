/*
%## Copyright (C) 2008 S.Box


%## descentcalc.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

#include "descentcalc.h"

//DESCENTCALC Builder**************************
descent::descent(vector<double> v1, vector<double> v2, INTAB IT1){
	/*
	\brief constructor of descent class

	this simulates the parachute dynamics

	\param v1 a vector for the time
	\param v2 a vector for the start position
	\param IT1 INTAB values for the rocket properties, see intabread.cpp

	\return void
	*/

	tt=v1,
	z0=v2;
	intab4=IT1.intab4;
	intab1=IT1.intab1;
	paratab=IT1.paratab;
	DatPop=false;
};

//FLOATDOWN Builder****************************

floatdown::floatdown(INTAB4 IT4,INTAB1 IT1,ParaTab P1){
	/*
	\brief initialises the floatdown phase

	this simulates the parachute dynamics

	\param IT4 intab4, see intabread.h
	\param IT1 intab1, see intabread.h
	\param P1 paratab, see intabread.h

	\return void
	*/

	intab4=IT4;
	intab1=IT1;
	paratab=P1;
};

//Ascent::fly function*******************************************
RKF_data descent::fall(void){
	/*
	\brief fall function for parachute dynamics

	starts simulating the dynamics

	\return RKF_data
	*/

	floatdown fall1(intab4,intab1,paratab);

	integrator* pfall1=&fall1;

	RKF paraint;
	paraint.max_it=2000;
	paraint.max_step=1.0;

	RKd1 = paraint.RKF45(tt,z0,pfall1);
	DatPop=true;
	return(RKd1);
};
//***************************************************************

//descent:EqMotionSolve function******************************************
EqMotionData2 descent::EqMotionSolve(double tt, vector<double> z){
	/*
	\brief single step using equation of motion

	uses the equation of motion for descent calculations of parachute

	\param tt time-step
	\param z a vector for the state

	\return EqMotionData2
	*/

	//Constants****************************************************************
	const double G=6.6741e-11;  //Universal gravitational constant

	//Unpack z*****************************************************************
	double xn=z[0];//postion vector
	double yn=z[1];//"
	double zn=z[2];//"
	double Px=z[3];//Translational momentum vector
	double Py=z[4];//"
	double Pz=z[5];//"

	//gravity******************************************************************
	double g=G*5.9742E24/pow((6378100+zn),2);

	//Extract data from tables
	//INTAB4***********************************************************
	vector<double> ztb=intab4.Alt; //Get altitude data from input table

	double Wxi,Wyi,Wzi,rho,temp;


	if (zn<=ztb.back () && zn>=ztb.front()){
		Wxi=interp::one(ztb,intab4.Wx,zn);//Wind velocity vector
		Wyi=interp::one(ztb,intab4.Wy,zn);//"
		Wzi=interp::one(ztb,intab4.Wz,zn);//"
		rho=interp::one(ztb,intab4.rho,zn);//Atmospheric density
		temp=interp::one(ztb,intab4.temp,zn);//Atmospheric Temperature
	}
	else{
		Wxi=intab4.Wx.back();
		Wyi=intab4.Wy.back();
		Wzi=intab4.Wz.back();

		temp =(-131.21+(0.00299*zn))+273.15;
		double Pressure=0.002488*pow((temp/216.6),-11.388);
		rho=Pressure/287*temp;
	}

	//INTAB1*******************************************************
	double Mi = intab1.Mass.back();//mass of the burned out rocket

	//ParaTab******************************************************
	vector<double>::iterator alt_it, cd_it;
	double CdA=0.0;
	cd_it=paratab.CdA.begin();
	for (alt_it = paratab.AltPd.begin(); alt_it != paratab.AltPd.end(); alt_it++){
		if (zn < *alt_it) {
			if (*cd_it > 0) {
				CdA+=*cd_it;
			}
		}
		cd_it++;
	}
	//**************************************************************

	double mg=Mi*g;//gravity

	vector3 Pt(Px,Py,Pz);//Linear Momentum
	vector3 Wt(Wxi,Wyi,Wzi);//Wind Vector

	vector3 Ut=Pt/Mi;
	vector3 Vt=Ut+Wt;

	double Vtmag = Vt.mag();
	vector3 Vtnorm = Vt.norm();

	vector3 Fdnorm = Vtnorm*(-1.0);

	double Fd=CdA*0.5*rho*pow(Vtmag,2.0);//drag force


	vector3 Fdt=Fdnorm*Fd;
	vector3 grav(0,0,(-mg));
	vector3 F = Fdt+grav;
	vector3 Xt(xn,yn,zn);

	/*vector<double> zd(6);
	zd[0] = Ut.e1;
	zd[1] = Ut.e2;
	zd[2] = Ut.e3;
	zd[3] = F.e1;
	zd[4] = F.e2;
	zd[5] = F.e3;*/

	EqMotionData2 Output;
	Output.Mass = Mi;
	Output.aDensity = rho;
	Output.aTemp = temp;
	Output.X = Xt;
	Output.Xdot = Ut;
	Output.Xddot = F/Mi;
	Output.Force = F;
	Output.Wind = Wt;

  return(Output);
};

bool floatdown::stop_flag(double t, vector<double> z){
	/*
	\brief sets when to stop

	sets the flag to stop the simulation, always at 0 altitude

	\param t (not used, but similar to KillSwitch)
	\param z a vector for the states

	\return bool
	*/

	bool temp;

	if (z[2] < 0.0) {
		temp=true;
	}
	else {
		temp=false;
	}

	return(temp);
}

//floatdown::step function******************************************
vector<double> floatdown::step(double tt, vector<double> z){
	/*
	\brief updates state vector with calculations

	\param tt a vector for the time
	\param z a vector for the state

	\return vector<double> new z
	*/
	EqMotionData2 SuperZ = EqMotionSolve(tt,z);
	vector<double> Z;
	Z.push_back(SuperZ.Xdot.e1);
	Z.push_back(SuperZ.Xdot.e2);
	Z.push_back(SuperZ.Xdot.e3);
	Z.push_back(SuperZ.Force.e1);
	Z.push_back(SuperZ.Force.e2);
	Z.push_back(SuperZ.Force.e3);

	return(Z);
}
