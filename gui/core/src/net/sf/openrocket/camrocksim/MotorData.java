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

%## MotorData.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

import net.sf.openrocket.motor.Manufacturer;
import net.sf.openrocket.motor.Motor;
import net.sf.openrocket.motor.ThrustCurveMotor;
import net.sf.openrocket.util.Coordinate;

/**
 *
 * @author simon
 */
public class MotorData {
	//class members
	public String Name;
	public double Length,
			Diameter,
			LoadedMass,
			DryMass,
			CoM;
	
	public double Position;
	public Vector<Double> Time = new Vector<Double>();
	public Vector<Double> Thrust = new Vector<Double>();
	public Vector<Double> Mass = new Vector<Double>();
	public Vector<Double> Ixx = new Vector<Double>();
	public Vector<Double> Iyy = new Vector<Double>();
	public Vector<Double> Izz = new Vector<Double>();
	public Vector<Double> MassFlow = new Vector<Double>();
	public boolean AddToList;
	public boolean built;
	
	//*Constructor
	public MotorData() {
		Name = "Not Initiated";
		AddToList = false;
		built = false;
	}
	
	/*
	 * MotorData()
	 * 
	 * @param name
	 * @param length
	 * @param diameter
	 * @param loadedMass
	 * @param dryMass
	 * @param time
	 * @param thrust
	 * 
	 */
	public MotorData(String name, double length, double diameter,
			double loadedMass, double dryMass, double[] time, double[] thrust) {
		
		AddToList = true;
		Name = name;
		Length = length;
		Diameter = diameter;
		LoadedMass = loadedMass;
		DryMass = dryMass;
		Time = fromArray2Vector(time);
		Thrust = fromArray2Vector(thrust);
		
		CalculateSimpson();
		
		built = true;
	}
	
	private Vector<Double> fromArray2Vector(double[] inputArray) {
		// converts from array to vector
		
		Vector<Double> outputVector = new Vector<Double>();
		
		for (int i = 0; i < inputArray.length; i++) {
			outputVector.add(inputArray[i]);
		}
		
		return outputVector;
	}
	
	@Override
	public String toString() {
		return (Name);
	}
	
	public double getMaxBurnTime() {
		return this.Time.lastElement(); // assume ordered
	}
	
	
	public void CalculateSimpson() {
		double FuelMass = LoadedMass - DryMass;
		double TotalImpulse = 0;
		Vector<Double> Impulse = new Vector<Double>();
		
		for (int i = 1; i < Time.size(); i++) {
			double iStep = (Thrust.get(i) + Thrust.get(i - 1)) * (Time.get(i) - Time.get(i - 1)) * 0.5;
			Impulse.add(iStep);
			TotalImpulse += iStep;
		}
		
		Vector<Double> ScaleV = new Vector<Double>();
		for (double I : Impulse) {
			ScaleV.add(I / TotalImpulse * FuelMass);
		}
		
		Mass.clear();
		Mass.add(DryMass + FuelMass);
		for (int i = 1; i < Time.size(); i++) {
			double rFM = FuelMass - ScaleV.get(i - 1);
			Mass.add(DryMass + rFM);
			FuelMass = rFM;
		}
		MassFlow.clear();
		
		for (int i = 1; i < Mass.size(); i++) {
			MassFlow.add((Mass.get(i - 1) - Mass.get(i)) / (Time.get(i) - Time.get(i - 1)));
		}
		MassFlow.add(0.0);
		
		XcomCylinder();
		
		Ixx.clear();
		Iyy.clear();
		Izz.clear();
		
		for (double m : Mass) {
			double[] IIs = InertiaCylinder(m);
			Ixx.add(IIs[0]);
			Iyy.add(IIs[1]);
			Izz.add(IIs[2]);
		}
	}
	
	private void XcomCylinder() {
		CoM = Length / 2;
	}
	
	private double[] InertiaCylinder(double M) {
		double ri = 0;
		double ro = Diameter / 2;
		double Ixn = (M / 12) * (3 * (ri * ri + ro * ro) + Length * Length);
		double Iyn = Ixn;
		double Izn = (M / 2) * (ri * ri + ro * ro);
		double[] Temp = new double[] { Ixn, Iyn, Izn };
		return (Temp);
	}
	
	public void EditMe() {
		CreateMotorDialog CMD = new CreateMotorDialog(null, true);
		if (built == true) {
			CMD.FillFields(Name, Length, Diameter, LoadedMass, DryMass, Time, Thrust);
		}
		CMD.setVisible(true);
		if (CMD.ReadOk == true) {
			AddToList = true;
			Name = CMD.Name;
			Length = CMD.Length;
			Diameter = CMD.Diameter;
			LoadedMass = CMD.LMass;
			DryMass = CMD.DMass;
			Time = CMD.Time;
			Thrust = CMD.Thrust;
			built = true;
			CalculateSimpson();
			/*
			try {
				RWmotorXML Wxml = new RWmotorXML(CMD.FileName);
				Wxml.WriteMotorToXML(this);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ("Saving " + CMD.FileName + " failed. \nSystem msg: " + e));
			}
			*/
		}
	}
	
	public boolean isBuilt() {
		return this.built;
	}
	
	// returns the openrocket format
	public Motor getMotor() {
		
		Manufacturer thisManufacturer = new Manufacturer(this.Name,
				this.Name, Motor.Type.UNKNOWN);
		String designation = this.Name,
				description = this.Name,
				digest = this.Name;
		Motor.Type thisType = Motor.Type.UNKNOWN;
		double[] delays = { 0 }; // no delays
		double diameter = this.Diameter;
		double length = this.Length;
		
		double[] time = new double[this.Time.size()];
		double[] thrust = new double[this.Thrust.size()];
		
		for (int i = 0; i < this.Time.size(); i++) {
			// time
			time[i] = this.Time.get(i);
		}
		
		for (int i = 0; i < this.Thrust.size(); i++) {
			// thrust
			thrust[i] = this.Thrust.get(i);
		}
		
		double weightLaunch = this.LoadedMass;
		double weightEmpty = this.DryMass;
		
		Coordinate[] cg = new Coordinate[time.length];
		
		for (int i = 0; i < time.length; i++) {
			Coordinate thisCoordinate = null;
			if (i == 0) {
				// start
				thisCoordinate = new Coordinate(0, 0, 0, weightLaunch);
			} else if (i == (time.length - 1)) {
				// end
				thisCoordinate = new Coordinate(0, 0, 0, weightEmpty);
			} else {
				// irrelevant
				thisCoordinate = new Coordinate(0, 0, 0, weightLaunch);
			}
			cg[i] = thisCoordinate;
		}
		
		ThrustCurveMotor thisThrustCurveMotor = new ThrustCurveMotor(
				thisManufacturer, designation, description, thisType,
				delays, diameter, length, time, thrust, cg,
				digest);
		
		Motor thisMotor = (Motor) thisThrustCurveMotor;
		
		return thisMotor;
	}
	
}
