#include <gtest/gtest.h>

#include "../src/vmaths.h"

TEST(vector2, basic_operations) {
  // new vector
  vector2 vec1, vec2, vec3;

  vec1 = vector2( 2,  2);
  vec2 = vector2(-1, -1);

  vec3 = vec1 + vec2;
  ASSERT_EQ(vec3.mag(), std::pow(2, 0.5));

  vec3 = vec1 - vec2;
  ASSERT_EQ(vec3.mag(), std::pow(18, 0.5));

  vec3 = vec1*2;
  ASSERT_EQ(vec3.mag(), 2*vec1.mag());

  vec3 = vec1/2;
  ASSERT_EQ(vec3.mag(), vec1.mag()/2);
}

TEST(vector3, basic_operations) {
  // new vector
  vector3 vec1, vec2, vec3;

  vec1 = vector3( 2,  2,  2);
  vec2 = vector3(-1, -1, -1);

  vec3 = vec1 + vec2;
  ASSERT_EQ(vec3.mag(), std::pow(3, 0.5));

  vec3 = vec1 - vec2;
  ASSERT_EQ(vec3.mag(), std::pow(27, 0.5));

  vec3 = vec1*2;
  ASSERT_EQ(vec3.mag(), 2*vec1.mag());

  vec3 = vec1/2;
  ASSERT_EQ(vec3.mag(), vec1.mag()/2);
}
