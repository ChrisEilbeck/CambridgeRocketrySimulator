#include <gtest/gtest.h>

#include "../src/descentcalc.h"

TEST(descentcalc, standard_operation) {
  /*
    important to note that descentcalc models parachute dynamics
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
  	double Px=z[3];//Translational momentum vector x
  	double Py=z[4];//" y
  	double Pz=z[5];//" z
  */
  for (int i = 0; i<6; i++){
		zz.push_back(0.0);
	}

  double rail_length = 1;

  /*
		Note: simulation stops at negative z
	*/

  // initialise parachute descent simulation
  descent ds1(tt, zz, intab);

  RKF_data rocket_data = ds1.fall();

  // check if time has passed
  double current_time = ds1.getShortData().time.back();
  ASSERT_GT(current_time, 0);

  // should have reached a zero altitude
  // X.back() returns latest position, e3 extracts altitude
  double current_altitude = ds1.getShortData().X.back().e3;
  ASSERT_LE(current_altitude, 0);

}
