#include <gtest/gtest.h>

#include "../src/MonteFy.h"

TEST(montefy, standard_operation) {
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

  MonteFy monty = MonteFy(intab, "Uncertainty.xml");

  // xml uncertainty has no thrust variation
  ASSERT_EQ(monty.sigmaThrust, 0);

  // wiggle the rocket properties
  INTAB intab_random = monty.Wiggle();

  // added stochastic wind conditions, thus will never be exactly zero
  // unlike the non-randomised version.
  ASSERT_EQ(intab.intab4.Wx.back(),0.0);
  ASSERT_NE(intab_random.intab4.Wx.back(),0.0);

  /*
    test the sub- methods to sample
  */

  // there are no bounds here.. so what to test?
  double rd = monty.SampleNormal(0.0, 10.0);

  // test truncated sampling
  for (int i=1;i<4;i++) {
    double rdt = monty.SampleNormalTruncated(0.0, 1.0*i, 1.0);
    ASSERT_LE(rdt, 1.0*i);
    ASSERT_GE(rdt, -1.0*i);
  }






}
