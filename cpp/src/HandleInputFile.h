/*
	%## Copyright (C) 2008 S.Box
	%## HandleInputFile.h

	%## Author: S.Box
	%## Created: 2008-05-27
*/

//HandleInputfile.h
#ifndef HandleInputFile_H
	#define HandleInputFile_H

	#include <string>
	#include <vector>
	#include "vectorops.h"
	#include <boost/property_tree/ptree.hpp>
	#include <boost/property_tree/xml_parser.hpp>
	#include <boost/foreach.hpp>
	//#include <boost/filesystem.hpp>
	#include <set>
	#include <iostream>
	#include "intabread.h"
	#include "RocketFlight.h"
	
	using namespace std;
	
	//Class Definition****************
	
	class HandleInputFile
	{
		// class to handle the input file (read, etc.)
		public:
			boost::property_tree::ptree PropTree;
			string FilePath;
			OutputData WriteData;
				
			//Construnctor
			HandleInputFile(string);
		private:
			//Functions
			void DealWithOSF();
			void DealWithTSF();
			void DealWithOSM();
			void DealWithTSM();
			Rocket_Flight OneStageSetUp();
			Rocket_Flight TwoStageSetUp();
			Rocket_Flight TestMiscData(Rocket_Flight);
	};
	
#endif
