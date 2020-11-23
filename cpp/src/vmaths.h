/*
	%## Copyright (C) 2008 S.Box
	%## vmaths.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

/*Header file containing class and function definitions
for vmaths.cpp*/
#ifndef vmaths_H
	#define vmaths_H

	//Headers*********************************************
	#include<iostream>
	#include<cmath>
	#include"vectorbearing.h"

	using namespace std;
	//****************************************************

	class vector2;
	class vector3;
	class matrix3x3;
	class quaternion;
	class bearing;

	//Class definitions***********************************
	class vector2
	{
		// class for two-dim vectors
		public:
			double e1,e2;
			
			vector2()
			{
			}
			
			vector2(double x, double y)
			{
				e1=x;
				e2=y;
			}
			
			vector2 operator + (vector2);
			vector2 operator - (vector2);
			vector2 operator * (double);
			vector2 operator / (double);
			
			void print();
			double mag();
			vector2 norm();
			bearing to_bearing();
	};
	
	class vector3
	{
		// class for 3-dim vectors
		public:
			double e1,e2,e3;
			
			vector3()
			{
			}
			
			vector3(double x,double y,double z)
			{
				e1=x;
				e2=y;
				e3=z;
			}
			
			vector3 operator + (vector3);
			vector3 operator - (vector3);
			vector3 operator * (double);
			vector3 operator * (matrix3x3);
			vector3 operator / (double);
			
			void print();
			double mag();
			vector3 norm();
			double dot(vector3);
			vector3 cross(vector3);
	};
	
	class quaternion
	{
		// class for quaternion
		public:
			double e1,e2,e3,e4;
			
			quaternion()
			{
			}
			
			quaternion(double theta,double x,double y,double z)
			{
				e1=theta;
				e2=x;
				e3=y;
				e4=z;
			}
			
			quaternion operator + (quaternion);
			quaternion operator - (quaternion);
			quaternion operator * (double);
			quaternion operator / (double);
			
			void print();
			double mag();
			quaternion norm();
			quaternion deriv(vector3);
			matrix3x3 to_matrix();
	};

	class matrix3x3
	{
		public:
			double e11,e12,e13,e21,e22,e23,e31,e32,e33;
			
			matrix3x3()
			{
			}
			
			matrix3x3(double d1,double d2,double d3,double d4,double d5,double d6,double d7,double d8,double d9)
			{
				e11=d1;
				e12=d2;
				e13=d3;
				e21=d4;
				e22=d5;
				e23=d6;
				e31=d7;
				e32=d8;
				e33=d9;
			}
			
			matrix3x3 inv (void);
			matrix3x3 transpose (void);
			double det (void);
			matrix3x3 cofactor (void);
			matrix3x3 operator * (matrix3x3);
			vector3 operator * (vector3);
			matrix3x3 operator * (double);
	};
#endif
