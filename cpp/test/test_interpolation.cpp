#include <gtest/gtest.h>

#include "../src/interpolation.h"

TEST(interp1, basic_operations) {
  /*
    1d
  */

  // linear line
  vector<double> x, y;

  for (int i=0; i<4; i++)
  {
    x.push_back(i);
    y.push_back(i);
  }

  // create class
  interp interpolation = interp();

  // .5
  ASSERT_DOUBLE_EQ(interpolation.one(x, y, 0.5), 0.5);

  // 1.5
  ASSERT_DOUBLE_EQ(interpolation.one(x, y, 1.5), 1.5);

}

TEST(interp2, basic_operations) {
  /*
    2d
  */

  // two vectors, {0,1,2,3}
  vector<double> x, y;

  for (int i=0; i<4; i++)
  {
    x.push_back(i);
    y.push_back(i);
  }

  // per columns identical, thus x values independent
  vector< vector<double> > z;

  for (int i=0; i<4; i++)
  {
    z.push_back(x); // x or y irrelevant, it's the order that's important
  }

  // create class
  interp interpolation = interp();

  // x = 0.0, y = 0.0
  ASSERT_DOUBLE_EQ( interpolation.two(x, y, z, 0.0, 0.0), 0.0);

  // x = 1.0, y = 0.0 (no dependency on x!)
  ASSERT_DOUBLE_EQ( interpolation.two(x, y, z, 1.0, 0.0), 0.0);

  // x = 0.0, y = 1.0 (linear dependency on y)
  ASSERT_DOUBLE_EQ( interpolation.two(x, y, z, 0.0, 1.0), 1.0);
}
