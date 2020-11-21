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

%## TubeData.java

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
public class TubeData extends RockPartsData {
	//*Class members
	double length,
			ID,
			OD,
			RadialPosition;
	boolean built = false;
	
	//*Class Constructor
	public TubeData() {
		EditMe();
	}
	
	public TubeData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	//*function for populating class members
	public void PopulateTube(double d1, double d2, double d3, double d4, double d5, double d6, String s1) {
		length = d1;
		ID = d2;
		OD = d3;
		Mass = d4;
		Xp = d5;
		RadialPosition = d6;
		Name = s1;
		Body = false;
		
		XcomTube();
		InertiaTube();
	}
	
	//*Class functions
	private void XcomTube() {
		Xcm = length / 2;
	}
	
	private void InertiaTube() {
		double ri = ID / 2;
		double ro = OD / 2;
		Ixx = (Mass / 12) * (3 * (ri * ri + ro * ro) + length * length);
		Iyy = Ixx;
		Izz = (Mass / 2) * (ri * ri + ro * ro) + Mass * Math.pow(RadialPosition, 2);
	}
	
	@Override
	public String toString() {
		return (Name + "[Tube]");
	}
	
	public void EditMe() {
		TubeDialog BD = new TubeDialog(null, true);
		if (built == true) {
			BD.FillFields(length, ID, OD, Mass, Xp, RadialPosition, Name);
		}
		BD.setVisible(true);
		if (BD.ReadOk == true) {
			PopulateTube(BD.length, BD.ID, BD.OD, BD.Mass, BD.Position, BD.RadialPosition, BD.Name);
			built = true;
		}
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Tube");
		PartNode.appendChild(design.CreateDataNode("length", Double.toString(length)));
		PartNode.appendChild(design.CreateDataNode("ID", Double.toString(ID)));
		PartNode.appendChild(design.CreateDataNode("OD", Double.toString(OD)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("RadialPosition", Double.toString(RadialPosition)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateTube(Temp.DbyName("length"), Temp.DbyName("ID"), Temp.DbyName("OD"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.DbyName("RadialPosition"), Temp.SbyName("Name"));
		built = true;
	}
	
}
