/*
	%## Copyright (C) 2008 S.Box
	%## vectorbearing.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

/*in this header file the bearing class is declared and so are functions for converting
two element vectors to a bearing and vice versa*/

#ifndef vectorbearing_H
	#define vectorbearing_H

	//Headers*****************************************************
	#include"vmaths.h"
	#include<cmath>
	using namespace std;
	#ifndef PI
		#define PI 3.14159265
	#endif
	//************************************************************

	class vector2;// predeclared to avoid conflicts
	//Bearing class definition************************************

	class bearing{
		// class to handle bearings
		public:
			double bea,range;
			bearing(){};
			bearing(double a,double b)
			{
				/*
				a : bearing [degrees]
				b : range [magnitude]
				*/
				bea=a;
				range=b;
			}
			
			bearing(vector2);
			vector2 to_vector();
	};
	//************************************************************

#endif
