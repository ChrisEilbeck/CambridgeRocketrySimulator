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

%## AtmosphereData.java

%## Author: S.Box
%## Created: 2010-05-27
*/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

/**
 *
 * @author sb4p07
 */
public class AtmosphereData {
	//*class members
	public String name = null;
	public Vector<Double> Altitude,
			Xwind,
			Ywind,
			Zwind,
			rho,
			Theta;
	public boolean built;
	public boolean AddToList;
	
	//*class constuctor
	public AtmosphereData() {
		name = "Not Initialized";
		built = false;
		AddToList = false;
	}
	
	/**
	 * Construct atmosphere
	 * 
	 * @param preFill		bool to pre-fill with ISA, no wind data
	 * @return				void
	 */
	public AtmosphereData(boolean preFill) {
		
		PopulateISA();
		
	}
	
	/**
	 * Populate atmosphere with ISA, no wind
	 * 
	 * @return				void
	 */
	private void PopulateISA() {
		
		name = "ISA";
		built = true;
		AddToList = false;
		
		Altitude = new Vector<Double>();
		Xwind = new Vector<Double>();
		Ywind = new Vector<Double>();
		Zwind = new Vector<Double>();
		rho = new Vector<Double>();
		Theta = new Vector<Double>();
		
		// pass altitude
		for (double a1 = 0; a1 < 10000; a1 += 100) {
			
			Altitude.add(a1);
			Xwind.add(0.0);
			Ywind.add(0.0);
			Zwind.add(0.0);
			double rho1 = getDensity(a1);
			rho.add(rho1);
			double temp1 = getTemperature(a1);
			Theta.add(temp1);
		}
	}
	
	/**
	 * obtain ISA density
	 * 
	 * @param altitude		double with altitude
	 * @return				double with density
	 */
	private double getDensity(double altitude) {
		double dense = -(0.0001 * altitude) + 1.2208;
		if (dense < 0) {
			dense = 0;
		}
		return dense;
	}
	
	/**
	 * obtain ISA temperature
	 * 
	 * @param altitude		double with altitude
	 * @return				double with temperature
	 */
	private double getTemperature(double altitude) {
		
		double temperature = 0;
		
		double[][] LapseRate = new double[3][7];
		
		LapseRate[0][0] = 11000;
		LapseRate[1][0] = -0.0065;
		LapseRate[2][0] = 15;
		LapseRate[0][1] = 20000;
		LapseRate[1][1] = 0;
		LapseRate[2][1] = -56.5;
		LapseRate[0][2] = 32000;
		LapseRate[1][2] = 0.001;
		LapseRate[2][2] = -56.5;
		LapseRate[0][3] = 47000;
		LapseRate[1][3] = 0.0028;
		LapseRate[2][3] = -44.5;
		LapseRate[0][4] = 51000;
		LapseRate[1][4] = 0;
		LapseRate[2][4] = -2.5;
		LapseRate[0][5] = 71000;
		LapseRate[1][5] = -0.0028;
		LapseRate[2][5] = -2.5;
		LapseRate[0][6] = 84852;
		LapseRate[1][6] = -0.002;
		LapseRate[2][6] = -58.5;
		
		if (altitude < LapseRate[0][0]) {
			temperature = (273 + LapseRate[2][0] + altitude * LapseRate[1][0]);
		} else {
			for (int i = 1; i < 7; i++) {
				if (altitude < LapseRate[0][i]) {
					temperature = (273 + LapseRate[2][i] + (altitude - LapseRate[0][i - 1]) * LapseRate[1][i]);
					break;
				} else {
					temperature = (273 + LapseRate[2][6]);
				}
			}
		}
		
		return temperature;
	}
	
	/**
	 * launch a window to edit the atmosphere
	 * 
	 * @return				void
	 */
	public void EditMeRaw() {
		RawAtmosphereDialog RAD = new RawAtmosphereDialog(null, true);
		if (built == true) {
			RAD.FillFields(Altitude, Xwind, Ywind, Zwind, rho, Theta, name);
		}
		RAD.setVisible(true);
		if (RAD.ReadOk == true) {
			AddToList = true;
			name = RAD.name;
			Altitude = RAD.Altitude;
			Xwind = RAD.Xwind;
			Ywind = RAD.Ywind;
			Zwind = RAD.Zwind;
			rho = RAD.density;
			Theta = RAD.Temperature;
			built = true;
			
			/*
			try {
				RWatmosXML Wxml = new RWatmosXML(RAD.FileName);
				Wxml.WriteAtmosToXML(this);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ("Saving " + RAD.FileName + " failed. \nSystem msg: " + e));
			} */
			
		}
		
	}
	
	@Override
	public String toString() {
		return (name);
	}
	
	
}
