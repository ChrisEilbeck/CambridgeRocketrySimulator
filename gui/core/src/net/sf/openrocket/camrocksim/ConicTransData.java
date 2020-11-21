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

%## ConicTransData.java

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
public class ConicTransData extends RockPartsData {
	
	//*Class Members
	double length,
			UD,
			DD,
			Dr,
			Thickness;
	boolean built = false;
	
	
	//*Class constructor
	public ConicTransData() {
		EditMe();
	}
	
	/*
	 * conic trans data
	 * 
	 * @param length [m]
	 * @param upstream_diameter [m]
	 * @param downstream_diameter [m]
	 * @param thickness [m]
	 * @param mass [kg]
	 * @param position [m]
	 * @param name
	 * 
	 */
	public ConicTransData(double length, double upstream_diameter, double downstream_diameter,
			double thickness, double mass, double position, String name) {
		PopulateConicTrans(length, upstream_diameter, downstream_diameter,
				thickness, mass, position, name);
	}
	
	public ConicTransData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	//*function to populate class members    
	public void PopulateConicTrans(double d1, double d2, double d3, double d5, double d6, double d7, String S1) {
		length = d1;
		UD = d2;
		DD = d3;
		Dr = Math.max(UD, DD);
		Thickness = d5;
		Mass = d6;
		Xp = d7;
		Name = S1;
		Body = true;
		
		BarrowmanConic();
		XcomConic();
		InertiaConic();
		
		built = true;
	}
	
	//*Class functions
	public void BarrowmanConic() {
		CN = 2 * (Math.pow((DD / Dr), 2) - Math.pow((UD / Dr), 2));
		
		double Drat = UD / DD;
		
		Xcp = length / 3 * (1 + (1 - Drat) / (1 - Math.pow(Drat, 2)));
	}
	
	private void XcomConic() {
		Xcm = length / 2; //Crude approximation, update later!
	}
	
	private void InertiaConic() {
		
		double Rao = (DD + UD) / 4;
		double Rai = Rao - Thickness;
		Ixx = (Mass / 12) * (3 * (Rai * Rai + Rao * Rao) + length * length);
		Iyy = Ixx;
		Izz = (Mass / 2) * (Rai * Rai + Rao * Rao);
	}
	
	public void EditMe() {
		ConicTransDialog CD = new ConicTransDialog(null, true);
		if (built == true) {
			CD.FillFields(length, UD, DD, Thickness, Mass, Xp, Name);
		}
		CD.setVisible(true);
		if (CD.ReadOk == true) {
			PopulateConicTrans(CD.length, CD.UD, CD.DD, CD.Thickness, CD.Mass, CD.Position, CD.Name);
			built = true;
		}
		
	}
	
	@Override
	public String toString() {
		return (Name + "[Conic Transition]");
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("ConicTrans");
		PartNode.appendChild(design.CreateDataNode("length", Double.toString(length)));
		PartNode.appendChild(design.CreateDataNode("UD", Double.toString(UD)));
		PartNode.appendChild(design.CreateDataNode("DD", Double.toString(DD)));
		PartNode.appendChild(design.CreateDataNode("Thickness", Double.toString(Thickness)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.BodyNode.appendChild(PartNode);
		
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateConicTrans(Temp.DbyName("length"), Temp.DbyName("UD"), Temp.DbyName("DD"), Temp.DbyName("Thickness"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.SbyName("Name"));
		built = true;
	}
	
}
