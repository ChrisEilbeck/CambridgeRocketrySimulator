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

%## ConeData.java

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
public class ConeData extends RockPartsData {
	
	//*Class members
	double Lc, //length of the cone
			Dc; //Diameter of cone base
	boolean built = false;
	
	//*Class Constructor
	public ConeData() {
		EditMe();
	}
	
	public ConeData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	public void PopulateCone(double d1, double d2, double d3, double d4, String S1) {
		Lc = d1;
		Dc = d2;
		Xp = d4;
		Mass = d3;
		Body = false;
		Name = S1;
		XCoMCone();
		InertiaCone();
	}
	
	//*Class Functions
	private void XCoMCone() {
		double iXcm;
		iXcm = 2 * Lc / 3;
		Xcm = iXcm;
	}
	
	private void InertiaCone() {
		double rn = Dc / 2;
		Ixx = Mass * Lc * Lc / 10 + 3 * Mass * rn * rn / 20;
		Iyy = Ixx;
		Izz = 3 * Mass * rn * rn / 10;
	}
	
	@Override
	public String toString() {
		return (Name + "[Cone]");
	}
	
	public void EditMe() {
		ConeDialog CD = new ConeDialog(null, true);
		if (built == true) {
			CD.FillFields(Lc, Dc, Mass, Xp, Name);
		}
		CD.setVisible(true);
		if (CD.ReadOk == true) {
			PopulateCone(CD.Length, CD.Diameter, CD.Mass, CD.Position, CD.Name);
			built = true;
		}
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Cone");
		PartNode.appendChild(design.CreateDataNode("Lc", Double.toString(Lc)));
		PartNode.appendChild(design.CreateDataNode("Dc", Double.toString(Dc)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateCone(Temp.DbyName("Lc"), Temp.DbyName("Dc"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.SbyName("Name"));
		built = true;//TODO fill this in
	}
	
}
