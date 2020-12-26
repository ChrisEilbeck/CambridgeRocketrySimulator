/*
	%## Copyright (C) 2008 S.Box
	%## intabread.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

#ifndef intabread_H
	#define intabread_H

	#include<iostream>
	#include<string>
	#include<sstream>
	#include<fstream>
	#include<vector>
	#include"vectorops.h"
	#include <boost/property_tree/ptree.hpp>
	#include <boost/foreach.hpp>
	#include <boost/algorithm/string.hpp>
	#include <boost/lexical_cast.hpp>

	using namespace std;

	//Class definitions************************************************************
	
	class INTAB1
	{
		// class with data on rocket characteristics
		
		public:
			vector<double> time;
			vector<double> Thrust;
			vector<double> Mass;
			vector<double> Ixx;
			vector<double> Iyy;
			vector<double> Izz;
			vector<double> Ixy;
			vector<double> Ixz;
			vector<double> Iyz;
			vector<double> Xcm;
			vector<double> Cda1;
			
			INTAB1()
			{
				// empty constructor
			};
			
			INTAB1(vector<double> a,vector<double> b,vector<double> c,vector<double>d,
				vector<double> e,vector<double> f,vector<double> g,vector<double> h,
				vector<double> i,vector<double> j,vector<double> k)
			{
				time=a; Thrust=b; Mass=c; Ixx=d; Iyy=e;
				Izz=f; Ixy=g; Ixz=h; Iyz=i; Xcm=j; Cda1=k;
			};
			
			void FileFill(const char*);
			void addDelay(double);
	};

	class INTAB2
	{
		// class with data on aerodynamics
		
		public:
			vector<double> Re;
			vector<double> alpha;
			
			vector<vector<double>> CD;
			
			INTAB2()
			{
				// empty constructor
			};
			
			INTAB2(vector<double> a,vector<double> b,vector<vector<double>> c)
			{
				Re=a;
				alpha=b;
				CD=c;
			};
			
			void FileFill(const char*);
	};
	
	class INTAB3
	{
		// class with data on normal coefficient and centre of pressure
		
		public:
			double CNa;
			double Xcp;
			
			INTAB3()
			{
				// empty constructor
			};
			
			INTAB3(double a,double b)
			{
				CNa=a;
				Xcp=b;
			};
			
			void FileFill(const char*);
	};
	
	class INTAB4
	{
		// class with atmospheric data
		
		public:
			vector<double> Alt;
			vector<double> Wx;
			vector<double> Wy;
			vector<double> Wz;
			vector<double> rho;
			vector<double> temp;
			
			INTAB4()
			{
				// empty constructor
			};
			
			INTAB4(vector<double> a,vector<double> b,vector<double> c,vector<double>d,
				vector<double> e,vector<double> f)
			{
				Alt=a; 
				Wx=b; 
				Wy=c; 
				Wz=d; 
				rho=e; 
				temp=f;
			};
			
			void FileFill(const char*);
	};

	class ParaTab
	{
		// class with data on parachute
		
		public:
			vector<double> AltPd;
			vector<double> CdA;
			
			/*
				Note, special flags for altitude parachute deployment (AltPd)
				1000 000+ (i.e. large number) : deploy at apogee
				-1 (i.e. negative number) : never deploy
			*/
			
			ParaTab()
			{
				// empty constructor
			};
			
			ParaTab(vector<double> a, vector<double> b, vector<double> c)
			{
				AltPd=a;
				CdA=vecop::vecmult(b,c);
			}
	};

	class LandA
	{
		// class with data on landing
		
		public:
			double RBL;
			double Ar;
			
			LandA()
			{
				// empty constructor
			};
			
			LandA(double a, double b)
			{
				RBL=a;
				Ar=b;
			}
	};
	
	class INTAB
	{
		// class with all information (to be filled from XML)
		
		public:
			INTAB1 intab1;
			INTAB2 intab2;
			INTAB3 intab3;
			INTAB4 intab4;
			ParaTab paratab;
			LandA landa;
			
			INTAB()
			{
				// empty constructor
			};
			
			INTAB(INTAB1 a,INTAB2 b,INTAB3 c,INTAB4 d, ParaTab e, LandA f)
			{
				intab1=a;
				intab2=b;
				intab3=c;
				intab4=d;
				paratab=e;
				landa=f;
			};
			
			INTAB(boost::property_tree::ptree);
			void FileFill(const char*,const char*,const char*,const char*);
			
		private:
			vector<double> TreeToVector(boost::property_tree::ptree,string);
			vector<vector<double> > TreeToMatrix(boost::property_tree::ptree,string);
	};
	
	//***************************************************************************
	
#endif
