#include <gtest/gtest.h>

#include "../src/HandleInputFile.h"

TEST(handleinputfile, standard_operation) {

  // simulation input
  string filename = "SimulationInput.xml";

  // start capturing output
  testing::internal::CaptureStdout();

  // launch rocket
  // expected output (1,10) (2,10) (3,10)
  HandleInputFile HI(filename);

  // obtain output
  string output = testing::internal::GetCapturedStdout();

  // check if simulation has run smoothly
  ASSERT_EQ("(1,3)\n(2,3)\n(3,3)\nSimulation Complete.\n", output);
}
