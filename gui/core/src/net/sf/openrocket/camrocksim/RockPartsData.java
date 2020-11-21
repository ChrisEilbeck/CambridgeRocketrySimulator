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

%## RocketPartsData.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import org.w3c.dom.Node;

/**
 *
 * @author simon
 */
public abstract class RockPartsData implements Cloneable {
	//*Class Members
	public double Xp, //part position (distance from nose tip)
			Mass, // mass of the part
			Xcm, //Centre of mass of part
			Ixx, //Moments of inertia about yaw axis
			Iyy, //Moments of inertia about pitch axis
			Izz, //Moments of inertia about roll axis
			Xcp, //Centre of pressure of part
			CN; //Normal force coefficient
	public boolean Body; // is the part a member of the rocket body
	public String Name;//User defined name for the part
	public RockPartsData Parent;// The parent part (if it exists)
	
	//*Class Constructor
	/*public RockPartsData(double d1,double d2,boolean b1){
	    Xp = d1;
	    Mass = d2;
	    Body = b1;
	}*/
	
	public abstract void EditMe();
	
	public abstract void WriteToXML(RWdesignXML design);
	
	public abstract void BuildFromXML(Node Nin);
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}

