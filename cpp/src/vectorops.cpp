/*
	%## Copyright (C) 2008 S.Box
	%## vectorops.cpp

	%## Author: S.Box
	%## Created: 2008-05-27
*/

/*Functions for basic scalar and vector operations (+,-,*,/)
for the "vector" class in the standard c++ library*/

#include"vectorops.h"

vector<double> vecop::scaladd(vector<double> vec,double S)
{
	int num=vec.size();
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec[i]+S;
	
	return(out);
}

vector<double> vecop::scalsub(vector<double> vec,double S)
{
	int num=vec.size();
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec[i]-S;
	
	return(out);
}

vector<double> vecop::scalmult(vector<double> vec,double S)
{
	int num=vec.size();
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec[i]*S;
	
	return(out);
}

vector<double> vecop::scaldiv(vector<double> vec,double S)
{
	int num=vec.size();
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec[i]/S;
	
	return(out);
}

vector<double> vecop::vecadd(vector<double> vec1,vector<double> vec2)
{
	int num=vec1.size();
	int num2=vec2.size();
	if(!(num==num2))
		cout<<"Error vector sizes don't match\n";
	
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec1[i]+vec2[i];
	
	return(out);
}

vector<double> vecop::vecsub(vector<double> vec1,vector<double> vec2)
{
	int num=vec1.size();
	int num2=vec2.size();
	if(!(num==num2))
		cout<<"Error vector sizes don't match\n";
	
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec1[i]-vec2[i];
	
	return(out);
}

vector<double> vecop::vecmult(vector<double> vec1,vector<double> vec2)
{
	int num=vec1.size();
	int num2=vec2.size();
	if(!(num==num2))
		cout<<"Error vector sizes don't match\n";
	
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec1[i]*vec2[i];
	
	return(out);
}

vector<double> vecop::vecdiv(vector<double> vec1,vector<double> vec2)
{
	int num=vec1.size();
	int num2=vec2.size();
	if(!(num==num2))
		cout<<"Error vector sizes don't match\n";
	
	vector<double> out(num);
	for (int i=0;i<num;i++)
		out[i]=vec1[i]/vec2[i];
	
	return(out);
}

