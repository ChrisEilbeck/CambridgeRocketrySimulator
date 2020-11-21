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

%## BodyTubeData

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import org.w3c.dom.Node;

/**
 * BodyTubeData component, has aerodynamic properties
 * 
 */
public class BodyTubeData extends RockPartsData {
	
	//*Class members
	double length,
			ID,
			OD;
	boolean built = false;
	
	// constructor via pop-up dialogue
	public BodyTubeData() {
		EditMe();
	}
	
	// constructor via property tree node
	public BodyTubeData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	/*
	 * building a body tube
	 * 
	 * @param length length [m]
	 * @param inner_diameter [m]
	 * @param outer_diameter [m]
	 * @param mass [kg]
	 * @param position [m]
	 * @param name
	 */
	public BodyTubeData(double length, double inner_diameter, double outer_diameter,
			double mass, double position, String name) {
		PopulateBodyTube(length, inner_diameter, outer_diameter, mass,
				position, name);
	}
	
	//function for populating the class members
	public void PopulateBodyTube(double d1, double d2, double d3, double d4, double d5, String s1) {
		length = d1;
		ID = d2;
		OD = d3;
		Mass = d4;
		Xp = d5;
		Name = s1;
		Body = true;
		
		BarrowmanBody();
		XcomBody();
		InertiaBody();
		
		built = true;
	}
	
	//*Class functions
	private void BarrowmanBody() {
		CN = 0;
		Xcp = length / 2;
	}
	
	private void XcomBody() {
		Xcm = length / 2;
	}
	
	private void InertiaBody() {
		double ri = ID / 2;
		double ro = OD / 2;
		Ixx = (Mass / 12) * (3 * (ri * ri + ro * ro) + length * length);
		Iyy = Ixx;
		Izz = (Mass / 2) * (ri * ri + ro * ro);
	}
	
	@Override
	public String toString() {
		return (Name + "[Body Tube]");
	}
	
	public void EditMe() {
		BodyTubeDialog BD = new BodyTubeDialog(null, true);
		if (built == true) {
			BD.FillFields(length, ID, OD, Mass, Xp, Name);
		}
		BD.setVisible(true);
		if (BD.ReadOk == true) {
			PopulateBodyTube(BD.length, BD.ID, BD.OD, BD.Mass, BD.Position, BD.Name);
			built = true;
		}
		
	}
	
	// write part to xml
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("BodyTube");
		PartNode.appendChild(design.CreateDataNode("length", Double.toString(length)));
		PartNode.appendChild(design.CreateDataNode("ID", Double.toString(ID)));
		PartNode.appendChild(design.CreateDataNode("OD", Double.toString(OD)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.BodyNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateBodyTube(Temp.DbyName("length"), Temp.DbyName("ID"), Temp.DbyName("OD"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.SbyName("Name"));
		built = true;
	}
	
}
