#include <gtest/gtest.h>

#include "../src/vectorbearing.h"

TEST(vectorbearing, standard_operation) {
  /*
    testing some bearing / vector logic
  */

  // convert from bearing to vector

  // 0 degrees (pointing north), magnitude 1
  bearing bear1(0.0, 1.0);

  vector2 vec1 = bear1.to_vector();

  ASSERT_DOUBLE_EQ(vec1.e1, 0.0);
  ASSERT_DOUBLE_EQ(vec1.e2, 1.0);

  // convert from vector to bearing

  vector2 vec2(1.0, 0.0);

  bearing bear2(vec2);

  ASSERT_DOUBLE_EQ(bear2.bea, 90.0);
  ASSERT_DOUBLE_EQ(bear2.range, 1.0);
}
