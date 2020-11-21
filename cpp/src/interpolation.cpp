/*
%## Copyright (C) 2008 S.Box
%## interpolation.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

/*Functions for linear interpolation of 1d and 2d data tables of values stored
in class "vector". For interp1::one(x,y,v) the renturned value is interpolated
in vector y at the point corresponding to the interpolated location of v in
vector x. For interp::two(x,y,z,p,q) z is vector containing a two dimentional
data with rows of the data appended row1<<row2<<row3 etc. x is a vector containing title values for the rows of z and y is a vector containing title values for the columns of z. p and q are the variables that are interpolated in x and y respectively and the corresponing interpolated value from z is returned*/

#include "interpolation.h"

double interp::one(vector<double> x,vector<double> y,double v){
	/*
	interp1::one(x,y,v) the renturned value is interpolated
	in vector y at the point corresponding to the interpolated location of v in
	vector x
	*/
	vector<double>::pointer x_it,y_it;
	x_it=&x.front();
	y_it=&y.front();

	do{x_it++;}while(*x_it<v && x_it<&x.back());


	y_it+=(x_it-&x.front());


	double xu=*x_it;
	double xl=*(x_it-1);
	double yu=*y_it;
	double yl=*(y_it-1);


	double mult=(v-xl)/(xu-xl);
	double yplus=mult*(yu-yl);
	return(yl+yplus);
}

double interp::two(vector<double> x, vector<double> y, vector<vector<double> > z, double p, double q) {
	/*
	interp::two(x,y,z,p,q) z is vector containing a two dimentional
	data with rows of the data appended row1<<row2<<row3 etc. x is a vector containing title values for the rows of z and y is a vector containing title values for the columns of z. p and q are the variables that are interpolated in x and y respectively and the corresponing interpolated value from z is returned
	*/
	vector<double>::pointer x_it, y_it;

	x_it=&x.front();
	y_it=&y.front();

	do{x_it++;}while(*x_it<p);
	do{y_it++;}while(*y_it<q);

	double xu=*x_it;
	double xl=*(--x_it);
	double yu=*y_it;
	double yl=*(--y_it);

	int xuind=x_it-&x.front()+1;
	int xlind=(xuind-1);
	int yuind=y_it-&y.front()+1;
	int ylind=(yuind-1);

	double zyuxl=z[xlind][yuind];
	double zylxl=z[xlind][ylind];
	double zyuxu=z[xuind][yuind];
	double zylxu=z[xuind][ylind];

	double m1=(p-xl)/(xu-xl);
	double m2=(q-yl)/(yu-yl);

  double zxl=zylxl+(m2*(zyuxl-zylxl));
	double zxu=zylxu+(m2*(zyuxu-zylxu));

	double zout=zxl+(m1*(zxu-zxl));

	return(zout);
};
