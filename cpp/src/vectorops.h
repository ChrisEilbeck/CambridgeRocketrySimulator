/*
	%## Copyright (C) 2008 S.Box
	%## vectorops.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

/*Functions for basic scalar and vector operations (+,-,*,/)
for the "vector" class in the standard c++ library*/

#ifndef vectorops_H
	#define vectorops_H

	//Headers******************************************
	#include<iostream>
	#include<vector>
	using namespace std;
	//*************************************************

	class vecop{
	public:
		
		vecop()
		{
		};
		
		static vector<double> scaladd(vector<double>,double);
		static vector<double> scalsub(vector<double>,double);
		static vector<double> scalmult(vector<double>,double);
		static vector<double> scaldiv(vector<double>,double);
		
		static vector<double> vecadd(vector<double>,vector<double>);
		static vector<double> vecsub(vector<double>,vector<double>);
		static vector<double> vecmult(vector<double>,vector<double>);
		static vector<double> vecdiv(vector<double>,vector<double>);
	};

#endif
