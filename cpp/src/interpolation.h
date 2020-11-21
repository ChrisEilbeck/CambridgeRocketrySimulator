/*
%## Copyright (C) 2008 S.Box
%## interpolation.h

%## Author: S.Box
%## Created: 2008-05-27
*/

/*Functions for linear interpolation of 1d and 2d data tables with values stored
in class "vector". For interp1::one(x,y,v) the renturned value is interpolated
in vector y at the point corresponding to the interpolated location of v in
vector x. For interp::two(x,y,z,p,q) z is vector of vectors with each element
being a vector that represents the row of a two dimentional data table.
x is a vector containing title values for the rows of z and y is a vector
containing title values for the columns of z. p and q are the variables that are
interpolated in x and y respectively and the corresponing interpolated value
from z is returned*/

#ifndef interpolation_H
#define interpolation_H
#include<iostream>
#include<vector>
using namespace std;

class interp{
	// class to interpolate values
	public:
		interp(){};
		static double one(vector<double>,vector<double>,double);
		static double two(vector<double>,vector<double>,vector<vector<double> >,double,double);
};
#endif
