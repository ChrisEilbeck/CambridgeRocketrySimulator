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

%## FinsetData.java

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
public class FinsetData extends RockPartsData {
	
	//*Class Members
	double RootChord,
			TipChord,
			MidChord,
			Span,
			Sweep,
			Thickness,
			BodyDiam,
			RefDiam;
	int NoOfFins;
	boolean built = false;
	
	//*Class Constructor
	public FinsetData() {
		EditMe();
	}
	
	public FinsetData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	/*
	 * FinsetData
	 * 
	 * @param number_of_fins
	 * @param root_chord
	 * @param tip_chord
	 * @param span
	 * @param sweep
	 * @param thickness
	 * @param body_diameter
	 * @param mass
	 * @param position
	 * @param name
	 */
	public FinsetData(int number_of_fins, double root_chord, double tip_chord,
			double span, double sweep, double thickness, double body_diameter,
			double mass, double position, String name) {
		PopulateFinset(number_of_fins, root_chord, tip_chord, span, sweep, thickness,
				body_diameter, mass, position, name);
	}
	
	public void PopulateFinset(int i1, double d1, double d2, double d4, double d5, double d6, double d7, double d9, double d10, String s1) {
		NoOfFins = i1;
		RootChord = d1;
		TipChord = d2;
		Span = d4;
		Sweep = d5;
		Thickness = d6;
		BodyDiam = d7;
		RefDiam = BodyDiam;
		Mass = d9;
		Xp = d10;
		Name = s1;
		
		Body = true;
		
		BarrowmanFinset();
		XcomFinset();
		InertialFinset();
		
		built = true;
	}
	
	//*Class Functions
	public void BarrowmanFinset() {
		MidChord = Math.sqrt(Math.pow(Sweep + (0.5 * TipChord) - (0.5 * RootChord), 2) + Math.pow(Span, 2));
		double Kfb = 1 + (BodyDiam / 2) / (Span + (BodyDiam / 2));
		
		CN = (Kfb * 4 * NoOfFins * Math.pow((Span / RefDiam), 2)) / (1 + Math.sqrt(1 + Math.pow(((2 * MidChord) / (RootChord + TipChord)), 2)));
		
		double rpt = RootChord + TipChord;
		double t1 = Sweep * (RootChord + 2 * TipChord);
		double t2 = 3 * rpt;
		double t3 = rpt - (RootChord * TipChord / rpt);
		
		Xcp = t1 / t2 + t3 / 6;
		
		
	}
	
	private void XcomFinset() {
		Xcm = RootChord / 2;//TODO Crude, improve
	}
	
	private void InertialFinset() {
		double FinPm = Mass / NoOfFins;
		double axisD = (BodyDiam + Span) / 2;
		
		switch (NoOfFins) {
		case 3:
			Ixx = FinPm * (Math.pow(axisD, 2) + 2 * Math.pow((axisD * Math.cos(2 * Math.PI / 3)), 2));
			Iyy = 2 * FinPm * Math.pow((axisD * Math.sin(2 * Math.PI / 3)), 2);
			Izz = 3 * FinPm * Math.pow(axisD, 2);
			break;
		case 4:
			Ixx = 2 * FinPm * Math.pow(axisD, 2);
			Iyy = Ixx;
			Izz = 4 * FinPm * Math.pow(axisD, 2);
			break;
		default:
			Ixx = Iyy = 0;
			Izz = 0;
			break;
		
		}
		
	}
	
	@Override
	public String toString() {
		return (Name + "[Finset]");
	}
	
	public void EditMe() {
		FinsetDialog FD = new FinsetDialog(null, true);
		if (built == true) {
			FD.FillFields(NoOfFins, RootChord, TipChord, Span, Sweep, Thickness, BodyDiam, Mass, Xp, Name);
		}
		FD.setVisible(true);
		if (FD.ReadOk == true) {
			PopulateFinset(FD.FinNum, FD.RootChord, FD.TipChord, FD.Span, FD.Sweep, FD.Thickness, FD.BodyDiam, FD.Mass, FD.Position, FD.Name);
			built = true;
		}
		
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Finset");
		PartNode.appendChild(design.CreateDataNode("NoOfFins", Integer.toString(NoOfFins)));
		PartNode.appendChild(design.CreateDataNode("RootChord", Double.toString(RootChord)));
		PartNode.appendChild(design.CreateDataNode("TipChord", Double.toString(TipChord)));
		PartNode.appendChild(design.CreateDataNode("Span", Double.toString(Span)));
		PartNode.appendChild(design.CreateDataNode("Sweep", Double.toString(Sweep)));
		PartNode.appendChild(design.CreateDataNode("Thickness", Double.toString(Thickness)));
		PartNode.appendChild(design.CreateDataNode("BodyDiam", Double.toString(BodyDiam)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.BodyNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateFinset(Temp.IbyName("NoOfFins"), Temp.DbyName("RootChord"), Temp.DbyName("TipChord"), Temp.DbyName("Span"), Temp.DbyName("Sweep"), Temp.DbyName("Thickness"), Temp.DbyName("BodyDiam"),
				Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.SbyName("Name"));
		built = true;
	}
}
