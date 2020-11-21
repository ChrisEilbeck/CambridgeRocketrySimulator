# CambridgeRocketrySimulator

This is a fork of Simon Box's Cambridge Rocketry Simulator to look into
active flight stability through

-	thrust vectoring
-	fin actuation
-	dynamic drag control

As this is now a rather old source tree (but one with rigorous validation
efforts and good compartmentalised design), I have forked it into this
github repository in the hope it will be the basis for some collaboration as
part of the Amateur Rocketry Society of England.  This group can be found on
Facebook if anyone wishes to join.

The source was originally released via Sourceforge under the GPL v3 license.

The original README.md file is included below.

Cambridge Rocketry Simulator v3
==========

Overview
--------

The Cambridge rocketry simulator can be used to produce six-degree-of-freedom simulations of rocket flights, including the parachute descent. Simulations can also be run stochastically, generating splash-down plots.

The software can be used to simulate many different types of rocket (including single and two-stage rockets) and many different scenarios (including uncertain launch- and atmospheric conditions).

License
-------

The Cambridge rocketry simulator is an Open Source project licensed under the [GNU GPL]. This means that the software is free to use for whatever purposes, and the source code is also available for studying and extending.

Contributing
------------

The Cambridge rocketry simulator needs users, developers, students and rocketeers to contribute, in a variety of different ways, and create an even better rocket flight simulator.


***

[FOLDER STRUCTURE]

cpp/
contains the c++ code, the simulation core of the project. The simulator  model has been peer-reviewed and published in a scientific journal.
( free copy found at http://eprints.soton.ac.uk/73938/ )

Data/
contains example atmospheres and motors (rocket engines). Also holds Uncertainty.xml, which holds default stochastic conditions.

doc/
contains the user manual, including .tex source for editing

gui/
This holds all the Java code related to the GUI - most of this work comes from OpenRocket and additional functions have been added to suit the format for camrocksim. Several extra functions have been removed, as camrocksim doesn't model these or they were simply redundant because of the modifications. The starting file is located at:
gui/swing/src/net/sf/openrocket/startup/SwingStartup.java

help_build_files/
contains additional files, useful when building a release for Windows and Linux.

Plotter/
This holds the Python code that provides the user with information. When writing an extension on the analysis or want to do anything with the data, this would be the best place to do so, leaving the Java and c++ code as it is.

simulator/
this folder contains the binary of the simulator, to be compiled via running "make" in cpp/

***

[NOTES]

- when developing (assumed Linux), the folders Data/, Plotter/, and simulator/ are required. Therefore, please run  "prepare_linux.sh" to prepare the program.

***

***************
*** [LINUX] ***
***************

[BUILD INFORMATION]

[c++]

1) install Boost library (if required, this method is Ubuntu specific)
>> sudo apt-get install libboost-all-dev

2) compile binary in <camrocsim-code>/cpp/
>> make

3) copy binary to <camrocsim-code>/simulator/ folder
>> make copy

[Python]

1) install Python 2.7+ (no compiling required)

[Java] via Eclipse

The JAR file is build via Eclipse (eclipse.org) by selecting

File -> Export, Java/Runnable JAR file

and input these data:
Launch configuration: SwingStartup - OpenRocket Swing
Export destination: <user choice>/camrocsim.jar
Library handling: Extract required libraries into generated JAR

Finish

[building an installer]

1) prepare a folder structure:
- Data (copy from repo)
- Plotter (.py files)
- simulator (compiled rocketc simulator)
camrocsim.jar

2) include a simple start script camrocsim, found at help_build_files/camrocsim
>> java -jar ~/.camrocsim/camrocsim.jar

3) start creating a .deb via Debreate, include the folder structure as in step (1), the default install directory is /usr/lib/camrocsim. The setup file is available at help_build_files/Debreate_CRS_v31

4) set-up the following scripts

Post-Install:
#! /bin/bash -e
ln -fs "/usr/lib/camrocsim/camrocsim" "/usr/bin/camrocsim"
cd /home
for i in $(echo */); do mkdir -p -m 777 ${i%/}/.camrocsim;cp -rf /usr/lib/camrocsim/* ${i%/}/.camrocsim; chmod -R 777 ${i%/}/.camrocsim; done

Pre-Remove:
#! /bin/bash -e
rm "/usr/bin/camrocsim"

Post-Remove:
#! /bin/bash -e
rm -rf /usr/lib/camrocsim
cd /home
for i in $(echo */); do rm -rf ${i%/}/.camrocsim; done

5) build the .deb, and ready for release **

[TESTING]

[c++]

1) prepare googletest environment
>> wget https://github.com/google/googletest/archive/release-1.8.0.tar.gz
>> tar -xvzf release-1.8.0.tar.gz
>> cd googletest-release-1.8.0/googletest
>> cmake .
>> make

2) build test binaries (from cpp directory)
>> make tests

3) run tests
>> ./runtests

[Java] via Eclipse

1) select OpenRocket Core

2) (top) Run -> Run as -> JUnit Test

*****************
*** [WINDOWS] ***
*****************

[BUILD INFORMATION]

[c++] via Visual Studio

1) install Visual Studio Community (http://www.visualstudio.com)

2) install boost library (http://www.boost.org/)

2) compile binary in <camrocsim-code>/cpp/ , by typing in the Developer Command Prompt (installed with Visual Studio)
>> cl /EHsc /I "C:\Boost\boost_1_62_0" /OUT rocketc.exe src/*.cpp main.cpp

** note that "C:\Boost\boost_1_62_0" is to be replaced by the relevant boost library location

3) copy rocketc.exe to the simulator/ folder

[Python]

1) set-up a suitable Python environment, e.g. via Anaconda
>> conda create -n py2 matplotlib pywin32
>> activate py2

2) download PyInstaller (http://www.pyinstaller.org/) from GitHub repo (https://github.com/pyinstaller/pyinstaller/commit/b78bfe530cdc2904f65ce098bdf2de08c9037abb), note this is from the development branch. Navigate to the folder with setup.py and install PyInstaller
>> python setup.py install

3) in <camrocsim-code>\Plotter execute:
>> pyinstaller.exe --onefile --noconsole FlightPlotter.py

4) the FlightPlotter.exe is found in /dist, copy this to the /Plotter folder

[Java] via Eclipse

The JAR file is build via Eclipse (eclipse.org) by selecting

File -> Export, Java/Runnable JAR file

and input these data:
Launch configuration: SwingStartup - OpenRocket Swing
Export destination: <user choice>
Library handling: Extract required libraries into generated JAR

Finish

[building an installer]

1) Download and install Advanced Installer (www.advancedinstaller.com, free version available)

2) prepare a folder structure
- Data (copy from repo)
- Plotter (FlightPlotter.exe)
- simulator (rocketc.exe)
camrocsim.jar
camrocsim.bat (found in help_build_files) >> java -jar ./camrocsim.jar

3) in help_build_files, AdvanedInstaller_CRS_v31.aip is included to show how the installer is created. Also CR_logo.ico is available.
