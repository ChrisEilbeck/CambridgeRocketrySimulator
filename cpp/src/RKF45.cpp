/*
%## Copyright (C) 2008 S.Box
%## RKF45.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

/*Runge Kutta Fehlberg  4th/5th order numerical integration algorithm for systems of
ordinary differential equations */

#include"RKF45.h"
#include"vectorops.h"


RKF_data RKF::RKF45(vector<double> xs, vector<double> ys, integrator* pint){
	/*
	\brief Runge Kutta Fehlrberg integration algortihm

	Runge Kutta Fehlberg  4th/5th order numerical integration algorithm for systems of ordinary differential equations

	\param xs
	\param ys
	\param pint

	\return RKF_data
	*/
	bool stopper;
	double h=h_init;
	double x=xs[0];
	int itnum=0;
	int sucstep=0;
	int num=ys.size();
	vector<double> y(ys);

	static const double c[7] = {0,  1.0/5.0,  3.0/10.0,  4.0/5.0,  8.0/9.0,  1.0,  1.0};
	static const double a[][6] = {
		{0,               0,              0,               0,            0,              0        },
		{1.0/5.0,         0,              0,               0,            0,              0        },
		{3.0/40.0,        9.0/40.0,       0,               0,            0,              0        },
		{44.0/45.0,      -56.0/15.0,      32.0/9.0,        0,            0,              0        },
		{19327.0/6561.0, -25360.0/2187.0, 64448.0/6561.0, -212.0/729.0,  0,              0        },
		{9017.0/3168.0,  -355.0/33.0,     46732.0/5247.0,  49.0/176.0,  -5103.0/18656.0, 0        },
		{35.0/384.0,      0,              500.0/1113.0,    125.0/192.0, -2187.0/6784.0,  11.0/84.0}
		};

	//static const double b [7] = {35.0/384.0,  0,  500.0/1113.0, 125.0/192.0, -2187.0/6784.0,  11.0/84.0,  0};
	//static const double bstar [7] = {5179.0/57600.0,  0,  7571.0/16695.0,  393.0/640.0,  -92097.0/339200.0, 187.0/2100.0,  1.0/40.0};
	static const double b [7] = {71.0/57600.0,   0,  -71.0/16695.0,  71.0/1920.0,  -17253.0/339200.0,  22.0/525.0,   -1.0/40.0};

	vector<double> ttab;
	vector<vector<double> > ztab;
	vector<vector<double> > etab;
	vector<vector<double> > K;

	do{
		vector<double>yn;
		vector<double>e(y.size());

		vector<double> kprot(y.size());
		vector<vector<double> > K;
		for(int i=0; i<7; i++){K.push_back(kprot);}

		for (int i=0; i<7; i++)
		{
			double xnew = x+(c[i]*h);
			vector<double> ynew=y;
			for (int j=0; j<6; j++)
			{
				ynew=vecop::vecadd(ynew,vecop::scalmult(K[j],a[i][j]));
				if (i==6){yn=ynew;}
			}
			K[i]=vecop::scalmult(pint->step(xnew,ynew),h);

		}


		for (int i=0; i<7; i++)
		{
			e=vecop::vecadd(e,vecop::scalmult(K[i],b[i]));
		}

    //******************************************************************
    //Step size control method roughly the same as that given in sec 17.2
    //of Numerical Recipes 3rd Ed.**************************************

    vector<double>::iterator e_it;
		for (e_it=e.begin();e_it!=e.end();e_it++){//modulus of e
			*e_it=fabs(*e_it);
		}

    vector<double>::iterator y_it;
    vector<double> ymod;
		for(y_it=y.begin();y_it!=y.end();y_it++){//modulus of y
      ymod.push_back(fabs(*y_it));
    }

    //error normalisation
    vector<double> scale=vecop::scaladd(vecop::scalmult(ymod,Retol),Abtol);

    vector<double> ediv=vecop::vecdiv(e,scale);
    double sqerr=0.0;
    for(e_it=ediv.begin();e_it!=ediv.end();e_it++){
      sqerr+=pow(*e_it,2);
    }
    double err=sqrt(sqerr/ediv.size());



		//double eloc=*max_element(e.begin(),e.end());

		if (err<=1.0){

			x=x+h;
			y=yn;

			ttab.push_back(x);
			ztab.push_back(y);
			etab.push_back(e);


			sucstep++;
		}

		//choose new stepsize

		if (err==0) h*=5.0;
		else{
		double htest=0.9*h*pow(fabs(1.0/err),0.2);
		htest>(5.0*h)? h*=5.0 : htest<(0.2*h)? h*=0.2 : h=htest;
		}
		if (h > max_step){h=max_step;}
    //*****************************************************************
    //*****************************************************************
		itnum++;


		stopper = pint->stop_flag(x,y);
	}while ((x<xs[1] && itnum < max_it && stopper == false) || (sucstep == 0));

	/*
	cout<<"total number of iterations: "<<itnum<<endl;
	cout<<"number of successful steps: "<<sucstep<<endl;
	*/

	RKF_data output(num,ttab,ztab,etab);
return(output);
}
