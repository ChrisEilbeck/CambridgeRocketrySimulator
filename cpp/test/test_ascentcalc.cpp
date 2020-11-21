#include <gtest/gtest.h>

#include "../src/ascentcalc.h"

TEST(ascentcalc, standard_operation) {
  /*
    important to note that ascentcalc models rocket dynamics, which can also descent
  */

  /*
    reading rocket properties from the simulationInput.xml
  */

  boost::property_tree::ptree xml_tree;

	try{
		//Read data from file into property tree
		boost::property_tree::xml_parser::read_xml("SimulationInput.xml", xml_tree);
	}
	catch(exception e){
		cout << "Error: Unable to locate SimulationInput.xml" << endl;
	}

	boost::property_tree::ptree intab_tree = xml_tree.get_child("SimulationInput.INTAB_TR");

  INTAB intab(intab_tree);

  /*
    setting up the time and state vector
  */

  vector<double> tt, zz;

	tt.push_back(0);
	tt.push_back(2000);

  /*
    < state vector >
  	double xn=z[0];//postion vector x
  	double yn=z[1];//" y
  	double zn=z[2];//" z
  	double s=z[3]; //quaternion scalar
  	double vx=z[4];//quaternion vector
  	double vy=z[5];//"
  	double vz=z[6];//"
  	double Px=z[7];//Translational momentum vector x
  	double Py=z[8];//" y
  	double Pz=z[9];//" z
  	double Ltheta=z[10];//Rotational momentum vector
  	double Lphi=z[11];//"
  	double Lpsi=z[12];//"
  */
  for (int i = 0; i<13; i++){
		zz.push_back(0.0);
	}

  double rail_length = 1;

  /*
		Note: default killswitch in "as1" is set to apogee by negative vertical speed
		index = 9 (translational momentum vector in z direction)
		value = -0.001 (negative)
	*/

  // initaliase rocket ascent simulation
  ascent as1(tt, zz, intab, rail_length);

  RKF_data rocket_data = as1.fly();

  // check if time has passed
  double current_time = as1.getShortData().time.back();
  ASSERT_GT(current_time, 0.0);

  // should have reached a positive altitude
  // X.back() returns latest position, e3 extracts altitude
  double current_altitude = as1.getShortData().X.back().e3;
  ASSERT_GT(current_altitude, 0.0);

}
