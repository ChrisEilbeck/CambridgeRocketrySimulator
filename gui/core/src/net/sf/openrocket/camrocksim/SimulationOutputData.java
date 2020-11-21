/*
%## Copyright (C) 2011, 2016 S.Box
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

%## SimulationOutputData.java

%## Author: S.Box
%## Created: 2011-10-27

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Collections;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author simon
 */
public class SimulationOutputData {
	
	//*Class members
	
	private Vector<Double> Time = new Vector<Double>();
	private Vector<Double> PosX = new Vector<Double>();
	private Vector<Double> PosY = new Vector<Double>();
	private Vector<Double> PosZ = new Vector<Double>();
	private Vector<Double> Vel = new Vector<Double>();
	private Vector<Double> Acc = new Vector<Double>();
	private Vector<Double> Fce = new Vector<Double>();
	private Vector<Double> AngV = new Vector<Double>();
	private Vector<Double> AngA = new Vector<Double>();
	private Vector<Double> Torque = new Vector<Double>();
	private Vector<Double> Thrust = new Vector<Double>();
	private Vector<Double> Mass = new Vector<Double>();
	private Vector<Double> CoM = new Vector<Double>();
	private Vector<Double> AoA = new Vector<Double>();
	private Vector<Double> Mach = new Vector<Double>();
	private Vector<Double> WindV = new Vector<Double>();
	private Vector<Double> AtDen = new Vector<Double>();
	private Vector<Double> AtTemp = new Vector<Double>();
	
	private Boolean Converted;
	
	public String StageName;
	public String ID;
	
	// stores timing of new event
	public Vector<Double> mEvents = new Vector<Double>();
	
	//RawMembersFromFILE
	public Vector<Vector<Double>> mTime = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mAoA = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mThrust = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mMass = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mCom = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mDensity = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mTemp = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mPosition = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mVelocity = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mAccel = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mPose = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mAngV = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mAndA = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mForce = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mTorque = new Vector<Vector<Double>>();
	public Vector<Vector<Double>> mWind = new Vector<Vector<Double>>();
	//class constructor
	
	public SimulationOutputData() {
		Converted = false;
		
	}
	
	private void DataConversion() {
		
		if (!Converted) {
			Time = mTime.firstElement();
			PosX = mPosition.elementAt(0);
			PosY = mPosition.elementAt(1);
			PosZ = mPosition.elementAt(2);
			Vel = Magnitude(mVelocity);
			Acc = Magnitude(mAccel);
			Fce = Magnitude(mForce);
			AngV = Magnitude(mAngV);
			AngA = Magnitude(mAndA);
			Torque = Magnitude(mTorque);
			Thrust = mThrust.firstElement();
			Mass = mMass.firstElement();
			CoM = mCom.firstElement();
			AoA = mAoA.firstElement();
			WindV = Magnitude(mWind);
			AtDen = mDensity.firstElement();
			AtTemp = mTemp.firstElement();
			
			for (int i = 0; i < Vel.size(); i++) {
				double c = Math.sqrt(1.4 * 287 * AtTemp.elementAt(i));
				Mach.add(Vel.elementAt(i) / c);
			}
			Converted = true;
		}
		
	}
	
	public DefaultTableModel toTable() {
		DataConversion();
		DefaultTableModel TheTable = new DefaultTableModel();
		TheTable.addColumn("Time (s)", Time);
		TheTable.addColumn("Easting (m)", PosX);
		TheTable.addColumn("Northing (m)", PosY);
		TheTable.addColumn("Altitude (m)", PosZ);
		TheTable.addColumn("Velocity (m/s)", Vel);
		TheTable.addColumn("Acceleration (m/s2)", Acc);
		TheTable.addColumn("Force (N)", Fce);
		TheTable.addColumn("AngularVel (rad/s)", AngV);
		TheTable.addColumn("AnularAcc (rad/s2)", AngA);
		TheTable.addColumn("Torque (Nm)", Torque);
		TheTable.addColumn("Thrust (N)", Thrust);
		TheTable.addColumn("Mass (kg)", Mass);
		TheTable.addColumn("Centre of Mass (m)", CoM);
		TheTable.addColumn("Angle of Attack (rad)", AoA);
		TheTable.addColumn("Mach No", Mach);
		TheTable.addColumn("Wind Speed (m/2)", WindV);
		TheTable.addColumn("Density (kg/m3)", AtDen);
		TheTable.addColumn("Temperature (K)", AtTemp);
		
		return (TheTable);
	}
	
	
	public String SummaryData() {
		DataConversion();
		double MaxV = Collections.max(Vel);
		double MaxA = Collections.max(Acc);
		double MaxF = Collections.max(Fce);
		
		double Apogee = Collections.max(PosZ);
		int Aindex = PosZ.indexOf(Apogee);
		double Easting = PosX.elementAt(Aindex);
		double Northing = PosY.elementAt(Aindex);
		double AscentTime = Time.elementAt(Aindex);
		double TotalTime = Time.lastElement();
		double DescentTime = TotalTime - AscentTime;
		double Leasting = PosX.lastElement();
		double Lnorthing = PosY.lastElement();
		
		
		String Sdat = "Flight Summarry Data\n\n"
				+ "Apogee\n"
				+ "\tEasting: " + Double.toString(Easting) + " m.\n"
				+ "\tNorthing: " + Double.toString(Northing) + " m.\n"
				+ "\tAltitude: " + Double.toString(Apogee) + " m.\n\n"
				+ "Landing\n"
				+ "\tEasting: " + Double.toString(Leasting) + " m.\n"
				+ "\tNorthing: " + Double.toString(Lnorthing) + " m.\n\n"
				+ "Times\n"
				+ "\tTotal flight time: " + Double.toString(TotalTime) + "s.\n"
				+ "\tAscent time: " + Double.toString(AscentTime) + "s.\n"
				+ "\tDescent time: " + Double.toString(DescentTime) + "s.\n\n"
				+ "Maximum values\n"
				+ "\tVelocity: " + Double.toString(MaxV) + "m/s.\n"
				+ "\tAcceleration: " + Double.toString(MaxA) + "m/s2.\n"
				+ "\tForce: " + Double.toString(MaxF) + "N.";
		
		
		return (Sdat);
	}
	
	private Vector<Double> Magnitude(Vector<Vector<Double>> Matrix) {
		Vector<Double> X = Matrix.elementAt(0);
		Vector<Double> Y = Matrix.elementAt(1);
		Vector<Double> Z = Matrix.elementAt(2);
		
		Vector<Double> Temp = new Vector<Double>();
		
		if (X.size() != Y.size() || X.size() != Z.size()) {
			throw new Error("The matrix that you are trying to parse has a raggedy edge");
		}
		
		int Limit = X.size();
		for (int i = 0; i < Limit; i++) {
			Temp.add(Math.sqrt(Math.pow(X.elementAt(i), 2) + Math.pow(Y.elementAt(i), 2) + Math.pow(Z.elementAt(i), 2)));
		}
		
		return (Temp);
	}
	
	@Override
	public String toString() {
		return (StageName);
	}
	
}
