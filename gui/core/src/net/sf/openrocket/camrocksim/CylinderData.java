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

%## CylinderData.java

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
 * @author sb4p07
 */
public class CylinderData extends RockPartsData {
	//*Class members
	double length,
			Diameter,
			RadialPosition;
	boolean built = false;
	
	//*Class Constructor
	public CylinderData() {
		EditMe();
	}
	
	public CylinderData(boolean t) {
		
	}
	
	/*
	 * @param length [m]
	 * @param diameter [m]
	 * @param mass [kg]
	 * @param position [m]
	 * @param radial_position [m]
	 * @param name 
	 */
	public CylinderData(double length, double diameter, double mass, double position,
			double radial_position, String name) {
		
		PopulateCylinder(length, diameter, mass, position, radial_position, name);
	}
	
	public CylinderData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	
	
	//*Function for populating class members
	public void PopulateCylinder(double d1, double d2, double d3, double d4, double d5, String s1) {
		length = d1;
		Diameter = d2;
		Mass = d3;
		Xp = d4;
		RadialPosition = d5;
		Name = s1;
		Body = false;
		
		XcomCylinder();
		InertiaCylinder();
		built = true;
	}
	
	//*Class functions
	private void XcomCylinder() {
		Xcm = length / 2;
	}
	
	private void InertiaCylinder() {
		double ri = 0;
		double ro = Diameter / 2;
		Ixx = (Mass / 12) * (3 * (ri * ri + ro * ro) + length * length);
		Iyy = Ixx;
		Izz = (Mass / 2) * (ri * ri + ro * ro) + Mass * Math.pow(RadialPosition, 2);
	}
	
	@Override
	public String toString() {
		return (Name + "[Cylinder]");
	}
	
	public void EditMe() {
		CylinderDialog CD = new CylinderDialog(null, true);
		if (built == true) {
			CD.FillFields(length, Diameter, Mass, Xp, RadialPosition, Name);
		}
		CD.setVisible(true);
		if (CD.ReadOk == true) {
			PopulateCylinder(CD.length, CD.Diameter, CD.Mass, CD.Position, CD.RadialPosition, CD.Name);
			built = true;
		}
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Cylinder");
		PartNode.appendChild(design.CreateDataNode("length", Double.toString(length)));
		PartNode.appendChild(design.CreateDataNode("Diameter", Double.toString(Diameter)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("RadialPosition", Double.toString(RadialPosition)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateCylinder(Temp.DbyName("length"), Temp.DbyName("Diameter"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.DbyName("RadialPosition"), Temp.SbyName("Name"));
		built = true;
	}
	
}
