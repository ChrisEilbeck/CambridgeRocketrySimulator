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

%## NoseConeData.java

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
public class NoseConeData extends RockPartsData {
	
	//*Class members
	double Ln, //Nosecone length
			Dn; //base diameter
	int ShapeID; // 0 = ogive, 1 = cone , 2 = parabola
	boolean built = false;
	
	
	//*Class Constructor
	public NoseConeData() {
		EditMe();
	}
	
	public NoseConeData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	/* Constructor for Nose Cone
	 * @param length length [m]
	 * @param diameter base diameter [m]
	 * @param pos position [m]
	 * @param mass mass [kg]
	 * @param shape shape parameter 0,1,2 = ogive, cone, parabola
	 * @param name object name
	 */
	public NoseConeData(double length, double diameter, double pos, double mass, int shape, String name) {
		// use existing function
		PopulateNoseCone(length, diameter, pos, mass, shape, name);
	}
	
	public void PopulateNoseCone(double d1, double d2, double d3, double d4, int i1, String S1) {
		Ln = d1;
		Dn = d2;
		Xp = d3;
		Mass = d4;
		ShapeID = i1;
		Body = true;
		Name = S1;
		
		BarrowmanNose();
		XCoMNose();
		InertiaNose();
		
		built = true;
	}
	
	//*Class Functions
	private void BarrowmanNose() {
		CN = 2;
		double iXcp = 0;
		
		switch (ShapeID) {
		case 0:
			// Ogive
			iXcp = 0.466 * Ln;
			break;
		case 1:
			// Cone
			iXcp = 2 * Ln / 3;
			break;
		case 2:
			// parabolic ??
			iXcp = Ln / 2;
			break;
		
		}
		Xcp = iXcp;
	}
	
	private void XCoMNose() {
		double iXcm;
		iXcm = 3 * Ln / 4;
		Xcm = iXcm;
	}
	
	private void InertiaNose() {
		double rn = Dn / 2;
		Ixx = Mass * Ln * Ln / 10 + 3 * Mass * rn * rn / 20;
		Iyy = Ixx;
		Izz = 3 * Mass * rn * rn / 10;
	}
	
	@Override
	public String toString() {
		return (Name + "[Nose Cone]");
	}
	
	public void EditMe() {
		NoseConeDialog ND = new NoseConeDialog(null, true);
		if (built == true) {
			ND.FillFields(Ln, Dn, Xp, Mass, ShapeID, Name);
		}
		ND.setVisible(true);
		if (ND.ReadOk == true) {
			PopulateNoseCone(ND.Length, ND.Diameter, ND.Position, ND.Mass, ND.ShapeI, ND.Name);
			built = true;
		}
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("NoseCone");
		PartNode.appendChild(design.CreateDataNode("Ln", Double.toString(Ln)));
		PartNode.appendChild(design.CreateDataNode("Dn", Double.toString(Dn)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("ShapeID", Integer.toString(ShapeID)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.BodyNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateNoseCone(Temp.DbyName("Ln"), Temp.DbyName("Dn"), Temp.DbyName("Xp"), Temp.DbyName("Mass"), Temp.IbyName("ShapeID"), Temp.SbyName("Name"));
		built = true;
	}
	
	
}
