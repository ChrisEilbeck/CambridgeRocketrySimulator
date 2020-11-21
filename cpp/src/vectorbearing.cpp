/*
	%## Copyright (C) 2008 S.Box
	%## vectorbearing.cpp

	%## Author: S.Box
	%## Created: 2008-05-27
*/

/*in this header file the bearing class is declared and so are functions for converting
two element vectors to a bearing and vice versa*/

#include"vectorbearing.h"

//Functions***************************************************
//vector_to_bearing
bearing::bearing(vector2 vec)
{
	// vector to bearing
	if(vec.e1>0)
		if(vec.e2>0)
			bea=((PI/2)-atan(vec.e2/vec.e1))*180/PI;
		else
			bea=((PI/2)-atan(vec.e2/vec.e1))*180/PI;
	else
		if(vec.e2>0)
			bea=((3*PI/2)-atan(vec.e2/vec.e1))*180/PI;
		else
			bea=((3*PI/2)-atan(vec.e2/vec.e1))*180/PI;
	
	range=sqrt(pow(vec.e1,2)+pow(vec.e2,2));
}

//bearing_to_vector
vector2 bearing::to_vector()
{
	// bearing to vector
	vector2 temp;
	temp.e1=range*sin(bea*PI/180);
	temp.e2=range*cos(bea*PI/180);
	
	return (temp);
}
