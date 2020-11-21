/*
%## Copyright (C) 2008 S.Box
%## vmaths.cpp

%## Author: S.Box
%## Created: 2008-05-27
*/

/*This file contains three classes "vector2", "vector3" and "quaternion"
it also contains functions for vector and quaternion addition, subtraction,
multiplication, dot and cross products and functions to return the magnitude
of a vector and normalize a vector.*/

//Headers*********************************************

#include"vmaths.h"


//****************************************************


//****************************************************

//Functions - vector operators*******************************

//Addition and subtraction
vector2 vector2::operator + (vector2 param){
	vector2 temp;
	temp.e1=e1+param.e1;
	temp.e2=e2+param.e2;
	return(temp);
}

vector2 vector2::operator - (vector2 param){
	vector2 temp;
	temp.e1=e1-param.e1;
	temp.e2=e2-param.e2;
	return(temp);
}

vector3 vector3::operator + (vector3 param){
	vector3 temp;
	temp.e1=e1+param.e1;
	temp.e2=e2+param.e2;
	temp.e3=e3+param.e3;
	return(temp);
}

vector3 vector3::operator - (vector3 param){
	vector3 temp;
	temp.e1=e1-param.e1;
	temp.e2=e2-param.e2;
	temp.e3=e3-param.e3;
	return(temp);
}

quaternion quaternion::operator + (quaternion param){
	quaternion temp;
	temp.e1=e1+param.e1;
	temp.e2=e2+param.e2;
	temp.e3=e3+param.e3;
	temp.e4=e4+param.e4;
	return(temp);
}

quaternion quaternion::operator - (quaternion param){
	quaternion temp;
	temp.e1=e1-param.e1;
	temp.e2=e2-param.e2;
	temp.e3=e3-param.e3;
	temp.e4=e4-param.e4;
	return(temp);
}

//Scalar multiplication and division

vector2 vector2::operator* (double mult){
	vector2 temp;
	temp.e1=e1*mult;
	temp.e2=e2*mult;
	return(temp);
}

vector2 vector2::operator/ (double mult){
	vector2 temp;
	temp.e1=e1/mult;
	temp.e2=e2/mult;
	return(temp);
}

vector3 vector3::operator* (double mult){
	vector3 temp;
	temp.e1=e1*mult;
	temp.e2=e2*mult;
	temp.e3=e3*mult;
	return(temp);
}

vector3 vector3::operator/ (double mult){
	vector3 temp;
	temp.e1=e1/mult;
	temp.e2=e2/mult;
	temp.e3=e3/mult;
	return(temp);
}

quaternion quaternion::operator* (double mult){
	quaternion temp;
	temp.e1=e1*mult;
	temp.e2=e2*mult;
	temp.e3=e3*mult;
	temp.e4=e4*mult;
	return(temp);
}

quaternion quaternion::operator/ (double mult){
	quaternion temp;
	temp.e1=e1/mult;
	temp.e2=e2/mult;
	temp.e3=e3/mult;
	temp.e4=e4/mult;
	return(temp);
}



//Functions ******************************************
//printvector
void vector2::print(){
	cout<<"["<<e1<<","<<e2<<"]\n";
}
void vector3::print(){
	cout<<"["<<e1<<","<<e2;
	cout<<","<<e3<<"]\n";
}
void quaternion::print(){
	cout<<"["<<e1<<","<<e2;
	cout<<","<<e3<<","<<e4<<"]\n";
}

//vectormag
double vector2::mag(){
	double a=pow(e1,2);
	double b=pow(e2,2);
	double c=pow((a+b),0.5);
	return(c);
}
double vector3::mag(){
	double a=pow(e1,2);
	double b=pow(e2,2);
	double c=pow(e3,2);
	double d=pow((a+b+c),0.5);
	return(d);
}
double quaternion::mag(){
	double a=pow(e1,2);
	double b=pow(e2,2);
	double c=pow(e3,2);
	double d=pow(e4,2);
	double e=pow((a+b+c+d),0.5);
	return(e);
}

//vectornorm
vector2 vector2::norm(){
	double svec=(*this).mag();
	vector2 temp;
	if (svec == 0.0){
		temp.e1=0.0;
		temp.e2=0.0;
	}
	else{
		temp.e1=e1/svec;
		temp.e2=e2/svec;
	}
return(temp);
}

vector3 vector3::norm(){
	double svec=(*this).mag();
	vector3 temp;
	if (svec == 0.0){
		temp.e1=0.0;
		temp.e2=0.0;
		temp.e3=0.0;
	}
	else{
		temp.e1=e1/svec;
		temp.e2=e2/svec;
		temp.e3=e3/svec;
	}
return(temp);
}

quaternion quaternion::norm(){
	double svec=(*this).mag();
	quaternion temp;
	if (svec == 0.0){
		temp.e1=0.0;
		temp.e2=0.0;
		temp.e3=0.0;
		temp.e4=0.0;
	}
	else{
		temp.e1=e1/svec;
		temp.e2=e2/svec;
		temp.e3=e3/svec;
		temp.e4=e4/svec;
	}
return(temp);
}

//Dot and Cross Products
double vector3::dot(vector3 B){
	return((e1*B.e1)+(e2*B.e2)+(e3*B.e3));
}

vector3 vector3::cross(vector3 B){
	vector3 temp;
	temp.e1=(e2*B.e3)-(e3*B.e2);
	temp.e2=(e3*B.e1)-(e1*B.e3);
	temp.e3=(e1*B.e2)-(e2*B.e1);
	return (temp);
}

//quaternion derivative

quaternion quaternion::deriv(vector3 v){
	quaternion temp;
	double S=e1;
	vector3 qvec(e2,e3,e4);
	temp.e1 = v.dot(qvec)*(-1)*0.5;
	vector3 qvec2=((v*S)+(v.cross(qvec)))*0.5;

	temp.e2=qvec2.e1;
	temp.e3=qvec2.e2;
	temp.e4=qvec2.e3;
	return(temp);
};

//quaternion to matrix**********************************
matrix3x3 quaternion::to_matrix(){
	// convert quaternion to matrix
	double s,vx,vy,vz;
	matrix3x3 m;

	s=e1;
	vx=e2;
	vy=e3;
	vz=e4;

	m.e11=1-2*pow(vy,2)-2*pow(vz,2);
	m.e12=2*vx*vy-2*s*vz;
	m.e13=2*vx*vz+2*s*vy;
	m.e21=2*vx*vy+2*s*vz;
	m.e22=1-2*pow(vx,2)-2*pow(vz,2);
	m.e23=2*vy*vz-2*s*vx;
	m.e31=2*vx*vz-2*s*vy;
	m.e32=2*vy*vz+2*s*vx;
	m.e33=1-2*pow(vx,2)-2*pow(vy,2);

	return(m);


};
//bearing conversion***********************************
bearing vector2::to_bearing(){
	// convert a two-dim vector to a bearing
	bearing temp;
	if (e1>0)
		if(e2>0)
			temp.bea=((PI/2)-atan(e2/e1))*180/PI;
		else
			temp.bea=((PI/2)-atan(e2/e1))*180/PI;
	else
		if(e2>0)
			temp.bea=((3*PI/2)-atan(e2/e1))*180/PI;
		else
			temp.bea=((3*PI/2)-atan(e2/e1))*180/PI;

	temp.range=sqrt(pow(e1,2)+pow(e2,2));

	return(temp);
}

//******************************************************

//Matrix operators**************************************

//operators*******************************************
vector3 vector3::operator *(matrix3x3 b){
	vector3 temp;
	temp.e1=e1*b.e11+e2*b.e21+e3*b.e31;
	temp.e2=e1*b.e12+e2*b.e22+e3*b.e32;
	temp.e3=e1*b.e13+e2*b.e23+e3*b.e33;

	return(temp);
};



matrix3x3 matrix3x3::operator * (matrix3x3 b){
	matrix3x3 temp;
	temp.e11=e11*b.e11+e12*b.e21+e13*b.e31;
	temp.e12=e11*b.e12+e12*b.e22+e13*b.e32;
	temp.e13=e11*b.e13+e12*b.e23+e13*b.e33;

	temp.e21=e21*b.e11+e22*b.e21+e23*b.e31;
	temp.e22=e21*b.e12+e22*b.e22+e23*b.e32;
	temp.e23=e21*b.e13+e22*b.e23+e23*b.e33;

	temp.e31=e31*b.e11+e32*b.e21+e33*b.e31;
	temp.e32=e31*b.e12+e32*b.e22+e33*b.e32;
	temp.e33=e31*b.e13+e32*b.e23+e33*b.e33;
	return(temp);
};


vector3 matrix3x3::operator * (vector3 b){
	vector3(temp);

	temp.e1=e11*b.e1+e12*b.e2+e13*b.e3;
	temp.e2=e21*b.e1+e22*b.e2+e23*b.e3;
	temp.e3=e31*b.e1+e32*b.e2+e33*b.e3;
	return(temp);

};


matrix3x3 matrix3x3::operator * (double b){
	matrix3x3(temp);
	temp.e11=e11*b;
	temp.e12=e12*b;
	temp.e13=e13*b;
	temp.e21=e21*b;
	temp.e22=e22*b;
	temp.e23=e23*b;
	temp.e31=e31*b;
	temp.e32=e32*b;
	temp.e33=e33*b;
	return(temp);

};
//****************************************************

matrix3x3 matrix3x3::transpose(void){
	matrix3x3(temp);
	temp.e11=e11;
	temp.e21=e12;
	temp.e31=e13;
	temp.e12=e21;
	temp.e22=e22;
	temp.e32=e23;
	temp.e13=e31;
	temp.e23=e32;
	temp.e33=e33;
	return(temp);
};

double matrix3x3::det(void){
	double temp;
	temp=e11*(e22*e33-e23*e32)-e21*(e12*e33-e13*e32)+e31*(e12*e23-e13*e22);

	return(temp);
};

matrix3x3 matrix3x3::cofactor(void){
	matrix3x3 temp;
	temp.e11=(e22*e33-e23*e32);
	temp.e12=-(e21*e33-e23*e31);
	temp.e13=(e21*e32-e22*e31);
	temp.e21=-(e12*e33-e13*e32);
	temp.e22=(e11*e33-e13*e31);
	temp.e23=-(e11*e32-e12*e31);
	temp.e31=(e12*e23-e13*e22);
	temp.e32=-(e11*e23-e13*e21);
	temp.e33=(e11*e22-e12*e21);
	return temp;
};

matrix3x3 matrix3x3::inv(void){
	matrix3x3 temp=*this;
	double dettemp;
	matrix3x3 coftemp;
	matrix3x3 tcoftemp;
	matrix3x3 out;
	coftemp=temp.cofactor();
	tcoftemp=coftemp.transpose();
	dettemp=temp.det();

	out=tcoftemp*(1/dettemp);

	return(out);
};
