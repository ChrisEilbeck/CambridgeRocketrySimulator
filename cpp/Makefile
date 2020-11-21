CPP_FILES=$(wildcard src/*.cpp)
OBJ_FILES=$(CPP_FILES:.cpp=.o)
CPP_MAIN=main.cpp
TEST_CPP_FILES=$(wildcard test/*.cpp)
TEST_OBJ_FILES=$(TEST_CPP_FILES:.cpp=.o)
GTEST_DIR=googletest-release-1.8.0
GTEST=$(GTEST_DIR)/googletest
GTEST_HEADERS=$(GTEST)/include
TARGET_BIN=rocketc
TARGET_TEST=runtests
CC=g++

all: rocketc

test_%.o : test_%.cpp
	$(CC) -pthread -I$(GTEST_HEADERS) -L$(GTEST) -lgtest -c $< -o $@

%.o: %.cpp
	$(CC) -O2 -c -o $@ $<

test: tests

tests: rocketc runtests
	./$(TARGET_TEST)

runtests: $(TEST_OBJ_FILES)
	$(CC) -pthread -I$(GTEST_HEADERS) -L$(GTEST) $(OBJ_FILES) -o $(TARGET_TEST) $(TEST_OBJ_FILES) -lgtest

rocketc: $(OBJ_FILES) $(CPP_MAIN)
	$(CC) -O2 $(CPP_MAIN) $(OBJ_FILES) -o $(TARGET_BIN)

debug: $(OBJ_FILES)
	$(CC) -g $(OBJ_FILES) -o $(TARGET_BIN)

copy:
	mkdir -p ../simulator/
	cp -f $(TARGET_BIN) ../simulator/$(TARGET_BIN)

clean:
	rm -f $(OBJ_FILES) $(TEST_OBJ_FILES) $(TARGET_BIN) $(TARGET_TEST)
