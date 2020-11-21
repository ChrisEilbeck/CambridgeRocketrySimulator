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

%## Cylinder Dialog.java

%## Author: S.Box
%## Created: 2011-10-27

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import javax.swing.JTextField;

/**
 *
 * @author simon
 */
public class LaunchData {
	//*Class Members
	public double RailLength,
			Azimuth,
			Declination,
			Altitude;
	public boolean Built;
	
	public LaunchData() {
		// default constructor
		this.RailLength = 0.0;
		this.Azimuth = 0.0;
		this.Declination = 0.0;
		
		this.Built = false; // set not built.
	}
	
	/*
	 * LaunchData()
	 * 
	 * @param railLength [m]
	 * @param azimuth [deg]
	 * @param declination [deg]
	 * @param altitude [m]
	 */
	public LaunchData(double railLength, double azimuth, double declination, double altitude) {
		// constructor for parameters.
		
		// write parameters
		this.RailLength = railLength;
		this.Azimuth = azimuth;
		this.Declination = declination;
		this.Altitude = altitude;
		
		// with this constructor, set build
		this.Built = true;
		
	}
	
	public void SetData(JTextField F1, JTextField F2, JTextField F3) {
		
		TestUserTextInput TU1 = new TestUserTextInput(F1);
		RailLength = TU1.TestDouble();
		TestUserTextInput TU2 = new TestUserTextInput(F2);
		Azimuth = TU2.TestDouble();
		TestUserTextInput TU3 = new TestUserTextInput(F3);
		Declination = TU3.TestDouble();
		Altitude = 0; // not using the GUI to set..
		
		if (TU1.Valid && TU2.Valid && TU3.Valid) {
			Built = true;
		} else {
			Built = false;
		}
	}
	
}
