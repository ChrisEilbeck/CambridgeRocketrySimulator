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

%## ParachuteData.java

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
public class ParachuteData extends RockPartsData {
	//*Class members
	double PackedLength,
			PackedDiameter,
			Area,
			DragCoefficient,
			Altitude;
	boolean built = false;
	boolean apogee = true;
	
	//*Class Constructor
	public ParachuteData() {
		EditMe();
	}
	
	/*
	 * ParachuteData
	 * 
	 * @param packed_length length before deployment (mass object) [m]
	 * @param packed_diameter diameter before deployment (mass object) [m]
	 * @param mass  [kg]
	 * @param position [m]
	 * @param surface_area surface area parachute after deployment [m^2]
	 * @param drag_coefficient [-]
	 * @param altitude altitude to deploy parachute (after apogee) [m]
	 * @param deploy_apogee (bool to deploy at apogee)
	 * @param name
	 */
	public ParachuteData(double packed_length, double packed_diameter, double mass, double position,
			double surface_area, double drag_coefficient, double altitude,
			boolean deploy_apogee, String name) {
		
		PopulateParachute(packed_length, packed_diameter, mass, position,
				surface_area, drag_coefficient, altitude, deploy_apogee, name);
	}
	
	public ParachuteData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	//*Function for populating class members
	public void PopulateParachute(double d1, double d2, double d3, double d4, double d5, double d6, double d7, boolean b1, String s1) {
		PackedLength = d1;
		PackedDiameter = d2;
		Mass = d3;
		Xp = d4;
		Area = d5;
		DragCoefficient = d6;
		Altitude = d7;
		apogee = b1;
		Name = s1;
		Body = false;
		
		XcomCylinder();
		InertiaCylinder();
		
		built = true;
	}
	
	//*Class functions
	private void XcomCylinder() {
		Xcm = PackedLength / 2;
	}
	
	private void InertiaCylinder() {
		double ri = 0;
		double ro = PackedDiameter / 2;
		Ixx = (Mass / 12) * (3 * (ri * ri + ro * ro) + PackedLength * PackedLength);
		Iyy = Ixx;
		Izz = (Mass / 2) * (ri * ri + ro * ro);
	}
	
	@Override
	public String toString() {
		return (Name + "[Parachute]");
	}
	
	public void EditMe() {
		ParachuteDialog PD = new ParachuteDialog(null, true);
		if (built == true) {
			PD.FillFields(PackedLength, PackedDiameter, Mass, Xp, Area, DragCoefficient, Altitude, apogee, Name);
		}
		PD.setVisible(true);
		if (PD.ReadOk == true) {
			PopulateParachute(PD.PackedLength, PD.PackedDiameter, PD.Mass, PD.Position, PD.ParaArea, PD.ParaCD, PD.Altitude, PD.Apogee, PD.Name);
			built = true;
		}
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Parachute");
		PartNode.appendChild(design.CreateDataNode("PackedLength", Double.toString(PackedLength)));
		PartNode.appendChild(design.CreateDataNode("PackedDiameter", Double.toString(PackedDiameter)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("Area", Double.toString(Area)));
		PartNode.appendChild(design.CreateDataNode("DragCoefficient", Double.toString(DragCoefficient)));
		PartNode.appendChild(design.CreateDataNode("Altitude", Double.toString(Altitude)));
		PartNode.appendChild(design.CreateDataNode("apogee", Boolean.toString(apogee)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateParachute(Temp.DbyName("PackedLength"), Temp.DbyName("PackedDiameter"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.DbyName("Area"), Temp.DbyName("DragCoefficient"),
				Temp.DbyName("Altitude"), Temp.BbyName("apogee"), Temp.SbyName("Name"));
		built = true;
	}
	
}
