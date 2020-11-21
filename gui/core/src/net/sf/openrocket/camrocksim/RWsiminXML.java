/*
%## Copyright (C) 2010, 2016 S.Box
%## 
%## This program is free software; you can redistribute it and/or modify
%## it under the terms of the GNU General Public License as published by
%## the Free Software Foundation; either version 2 of the License, or
%## (at your option) any later version.
%## 
%## This program is distributed in the hope that it will be useful,
%## but WITHOUT ANY WARRANTY; without even the implied warranty of
%## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
%## GNU General Public License for more details.
%## 
%## You should have received a copy of the GNU General Public License
%## along with this program; if not, write to the Free Software
%## Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA

%## RWsiminXML.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

import org.w3c.dom.Node;

/**
 *
 * @author simon
 */
public class RWsiminXML extends RWXML {
	//GenerateSimData SimDataStruct;
	
	//*Class constructor
	public RWsiminXML(String fn) {
		super(fn);
		
	}
	
	public void WriteSimDataToXML(RocketDescription TheRocket, AtmosphereData TheAtmosphere, LaunchData LaunchPadSet, boolean Monte, int mIts, boolean fail) {
		WriteInit();
		
		Node RootNode = Doc.appendChild(CreateNode("SimulationInput"));
		RootNode.appendChild(CreateDataNode("Time", tStamp.DateTimeNow()));
		
		if (TheRocket.Stages.size() == 1) {
			if (Monte) {
				RootNode.appendChild(CreateDataNode("Function", "OneStageMonte"));
			} else {
				RootNode.appendChild(CreateDataNode("Function", "OneStageFlight"));
			}
		} else if (TheRocket.Stages.size() == 2) {
			if (Monte) {
				RootNode.appendChild(CreateDataNode("Function", "TwoStageMonte"));
			} else {
				RootNode.appendChild(CreateDataNode("Function", "TwoStageFlight"));
			}
		}
		
		RootNode.appendChild(GenerateSimSettings(Monte, mIts, fail));
		RootNode.appendChild(GenerateLaunchSettings(LaunchPadSet));
		RootNode.appendChild(GenerateStageTransition(TheRocket));
		
		Vector<StageDescription> SimulationStages = TheRocket.buildSimulationStages();
		for (StageDescription stage : SimulationStages) {
			String stageName = "NoName";
			if (stage.RootNode.toString().contains("TotalRocket")) {
				stageName = "INTAB_TR";
			} else if (stage.RootNode.toString().contains("Upper")) {
				stageName = "INTAB_US";
			} else if (stage.RootNode.toString().contains("Booster")) {
				stageName = "INTAB_BS";
			}
			
			GenerateSimData gsd = new GenerateSimData(stage, TheAtmosphere);
			RootNode.appendChild(GenerateIntabTR(stageName, gsd));
		}
		WriteOutput();
		
	}
	
	private Node GenerateSimSettings(boolean Monte, int mIts, boolean fail) {
		Node RootNode = Doc.createElement("SimulationSettings");
		RootNode.appendChild(CreateDataNode("BallisticFailure", Boolean.toString(fail)));
		RootNode.appendChild(CreateDataNode("ShortData", Boolean.toString(Monte)));
		RootNode.appendChild(CreateDataNode("NumberOfIterations", Integer.toString(mIts)));
		RootNode.appendChild(CreateDataNode("MaxTimeSpan", "2000"));//TODO maybe unhardcode this
		return (RootNode);
	}
	
	private Node GenerateLaunchSettings(LaunchData LD) {
		Node RootNode = Doc.createElement("LaunchSettings");
		RootNode.appendChild(CreateDataNode("Eastings", "0.0"));//TODO unhardcode this
		RootNode.appendChild(CreateDataNode("Northings", "0.0"));//TODO unhardcode this
		RootNode.appendChild(CreateDataNode("Altitude", Double.toString(LD.Altitude)));
		RootNode.appendChild(CreateDataNode("LaunchRailLength", Double.toString(LD.RailLength)));
		RootNode.appendChild(CreateDataNode("LaunchAzimuth", Double.toString(LD.Azimuth)));
		RootNode.appendChild(CreateDataNode("LaunchDeclination", Double.toString(LD.Declination)));
		
		return (RootNode);
		
	}
	
	private Node GenerateStageTransition(RocketDescription Rocket) {
		Node RootNode = Doc.createElement("StageTransition");
		StageDescription TempS = Rocket.Stages.firstElement();
		if (TempS.isUpper()) {
			RootNode.appendChild(CreateDataNode("SeparationTime", Double.toString(TempS.ReturnTransitionData().separationTime)));
			RootNode.appendChild(CreateDataNode("IgnitionDelay", Double.toString(TempS.ReturnTransitionData().ignitionDelay)));
		}
		return (RootNode);
	}
	
	private Node GenerateIntabTR(String Name, GenerateSimData SimDataStruct) {
		Node RootNode = Doc.createElement(Name);
		Node intab1N = RootNode.appendChild(CreateNode("intab1"));
		intab1N.appendChild(CreateDataNode("time", VectorToDstring(SimDataStruct.Time) + ";"));
		intab1N.appendChild(CreateDataNode("Thrust", VectorToDstring(SimDataStruct.Thrust) + ";"));
		intab1N.appendChild(CreateDataNode("Mass", VectorToDstring(SimDataStruct.Mass) + ";"));
		intab1N.appendChild(CreateDataNode("Ixx", VectorToDstring(SimDataStruct.Ixx) + ";"));
		intab1N.appendChild(CreateDataNode("Iyy", VectorToDstring(SimDataStruct.Iyy) + ";"));
		intab1N.appendChild(CreateDataNode("Izz", VectorToDstring(SimDataStruct.Izz) + ";"));
		
		Vector<Double> ZeroVec = new Vector<Double>();
		for (double d : SimDataStruct.Ixx) {
			ZeroVec.add(0.0);
		}
		intab1N.appendChild(CreateDataNode("Ixy", VectorToDstring(ZeroVec) + ";"));
		intab1N.appendChild(CreateDataNode("Ixz", VectorToDstring(ZeroVec) + ";"));
		intab1N.appendChild(CreateDataNode("Iyz", VectorToDstring(ZeroVec) + ";"));
		intab1N.appendChild(CreateDataNode("CentreOfMass", VectorToDstring(SimDataStruct.CoM) + ";"));
		intab1N.appendChild(CreateDataNode("ThrustDampingCoefficient", VectorToDstring(SimDataStruct.TDC) + ";"));
		
		Node intab2N = RootNode.appendChild(CreateNode("intab2"));
		intab2N.appendChild(CreateDataNode("AngleOfAttack", VectorToDstring(SimDataStruct.AoA) + ";"));
		intab2N.appendChild(CreateDataNode("ReynoldsNumber", VectorToDstring(SimDataStruct.Re) + ";"));
		intab2N.appendChild(CreateDataNode("CD", MatrixToDstring(SimDataStruct.CD)));
		
		Node intab3N = RootNode.appendChild(CreateNode("intab3"));
		intab3N.appendChild(CreateDataNode("CN", VectorToDstring(SimDataStruct.Cn)));
		intab3N.appendChild(CreateDataNode("CentreOfPressure", VectorToDstring(SimDataStruct.Cp)));
		
		Node intab4N = RootNode.appendChild(CreateNode("intab4"));
		intab4N.appendChild(CreateDataNode("Altitude", VectorToDstring(SimDataStruct.Altitude) + ";"));
		// intab4N.appendChild(CreateDataNode("Altitude", VectorToDstring(SimDataStruct.Altitude) + ";"));
		intab4N.appendChild(CreateDataNode("XWind", VectorToDstring(SimDataStruct.Xwind) + ";"));
		intab4N.appendChild(CreateDataNode("YWind", VectorToDstring(SimDataStruct.Ywind) + ";"));
		intab4N.appendChild(CreateDataNode("ZWind", VectorToDstring(SimDataStruct.Zwind) + ";"));
		intab4N.appendChild(CreateDataNode("AtmosphericDensity", VectorToDstring(SimDataStruct.rho) + ";"));
		intab4N.appendChild(CreateDataNode("AtmosphericTemperature", VectorToDstring(SimDataStruct.Theta) + ";"));
		
		Node LandA = RootNode.appendChild(CreateNode("LengthAndArea"));
		LandA.appendChild(CreateDataNode("RocketLength", Double.toString(SimDataStruct.Length)));
		LandA.appendChild(CreateDataNode("RocketXsectionArea", Double.toString(SimDataStruct.Xarea)));
		
		Node Parachute = RootNode.appendChild(CreateNode("ParachuteData"));
		Parachute.appendChild(CreateDataNode("ParachuteSwitchAltitude", VectorToDstring(SimDataStruct.SwitchAlt) + ";"));
		Parachute.appendChild(CreateDataNode("ParachuteCDxA", VectorToDstring(SimDataStruct.CDA) + ";"));
		
		return (RootNode);
		
	}
	
	
	
}
