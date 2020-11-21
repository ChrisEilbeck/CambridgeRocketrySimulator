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

%## ConeSecData.java

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
public class ConeSecData extends RockPartsData {
	//*Class Members
	double length,
			UD,
			DD,
			RadPos,
			Thickness;
	boolean built = false;
	
	
	//*Class constructor
	public ConeSecData() {
		EditMe();
	}
	
	public ConeSecData(Node Nde) {
		BuildFromXML(Nde);
	}
	
	//*function to populate class members
	public void PopulateConicTrans(double d1, double d2, double d3, double d4, double d5, double d6, double d7, String S1) {
		length = d1;
		UD = d2;
		DD = d3;
		RadPos = d4;
		Thickness = d5;
		Mass = d6;
		Xp = d7;
		Name = S1;
		Body = false;
		
		XcomConic();
		InertiaConic();
		
		
	}
	
	//*Class functions
	
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
		ConeSecDialog CD = new ConeSecDialog(null, true);
		if (built == true) {
			CD.FillFields(length, UD, DD, RadPos, Thickness, Mass, Xp, Name);
		}
		CD.setVisible(true);
		if (CD.ReadOk == true) {
			PopulateConicTrans(CD.length, CD.UD, CD.DD, CD.RadialPosition, CD.Thickness, CD.Mass, CD.Position, CD.Name);
			built = true;
		}
		
	}
	
	@Override
	public String toString() {
		return (Name + "[Cone Section]");
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("ConeSec");
		PartNode.appendChild(design.CreateDataNode("length", Double.toString(length)));
		PartNode.appendChild(design.CreateDataNode("UD", Double.toString(UD)));
		PartNode.appendChild(design.CreateDataNode("DD", Double.toString(DD)));
		PartNode.appendChild(design.CreateDataNode("RadPos", Double.toString(RadPos)));
		PartNode.appendChild(design.CreateDataNode("Thickness", Double.toString(Thickness)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateConicTrans(Temp.DbyName("length"), Temp.DbyName("UD"), Temp.DbyName("DD"), Temp.DbyName("RadPos"), Temp.DbyName("Thickness"), Temp.DbyName("Mass"), Temp.DbyName("Xp"),
				Temp.SbyName("Name"));
		built = true;//TODO fill this in
	}
	
}
