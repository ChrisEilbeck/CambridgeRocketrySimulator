/*
%## Copyright (C) 2008 S.Box
%## RKF45.h

%## Author: S.Box
%## Created: 2008-05-27
*/

/*Runge Kutta Fehlberg  4th/5th order numerical integration algorithm for systems of
ordinary differential equations */

#ifndef RKF45_H
#define RKF45_H

#include<vector>
#include"vectorops.h"
#include<cmath>
#include<algorithm>
using namespace std;


class RKF_data{
	// class that holds all data for integration
	public:
		int num;
		vector<double> t;
		vector<vector<double> > z;
		vector<vector<double> > e;
		RKF_data(){};
		RKF_data(int a,vector<double> b,vector<vector<double> > c,vector<vector<double> > d){
			num=a;
			t=b;
			z=c;
			e=d;
		}
};

class integrator{
	// class to integrate
	// steps and stops
	public:
		virtual vector<double> step (double,vector<double>)=0;
		virtual bool stop_flag(double t,vector<double> z){return(false);}//optional stop flag can be called in the step function
};

class RKF{
	// RKF, calculates each step
	public:
		double Retol;//default relative error tolerance
		double Abtol;//default absolute error tolerance
		double h_init;//default initial step
		int max_it;//default maxixmum number of iterations
		double max_step;//default maximum step size
		RKF(){
			Retol=1.0e-3;
			Abtol=1.0e-6;
			h_init=0.01;
			max_it=1000;
			max_step=10.0;
		};
		RKF_data RKF45(vector<double>, vector<double>, integrator*);
};


#endif
