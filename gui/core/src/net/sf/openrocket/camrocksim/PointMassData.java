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

%## PointMassData.java

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
public class PointMassData extends RockPartsData {
	
	//*Class Members
	double RadialPosition;
	boolean built = false;
	
	//*Class Constructor
	public PointMassData() {
		EditMe();
	}
	
	public PointMassData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	/*
	 * PointMassData
	 * 
	 * @param mass [kg]
	 * @param position [m]
	 * @param radial_position [m]
	 * @param name
	 */
	public PointMassData(double mass, double position, double radial_position,
			String name) {
		
		PopulatePointMass(mass, position, radial_position, name);
	}
	
	public void PopulatePointMass(double d1, double d2, double d3, String s1) {
		Mass = d1;
		Xp = d2;
		RadialPosition = d3;
		
		Name = s1;
		Body = false;
		
		XcomPointMass();
		InertialPointMass();
		
		built = true;
	}
	
	//*Class Functions
	private void XcomPointMass() {
		Xcm = 0;
	}
	
	private void InertialPointMass() {
		Ixx = 0;
		Iyy = 0;
		Izz = Mass * Math.pow(RadialPosition, 2);
	}
	
	@Override
	public String toString() {
		return (Name + "[Point Mass]");
	}
	
	public void EditMe() {
		PointMassDialog PD = new PointMassDialog(null, true);
		if (built == true) {
			PD.FillFields(Mass, Xp, RadialPosition, Name);
		}
		PD.setVisible(true);
		if (PD.ReadOk == true) {
			PopulatePointMass(PD.Mass, PD.Position, PD.RadialPosition, PD.Name);
			built = true;
		}
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("PointMass");
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("RadialPosition", Double.toString(RadialPosition)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulatePointMass(Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.DbyName("RadialPosition"), Temp.SbyName("Name"));
		built = true;
		
	}
	
}
