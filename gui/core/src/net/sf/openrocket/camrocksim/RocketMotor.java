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

%## RocketMotor.java

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
public class RocketMotor extends RockPartsData {
	//Members
	public MotorData Mot;
	public double MountX;
	public boolean built;
	
	public RocketMotor() {
	}
	
	/*
	 * RocketMotor()
	 * 
	 * @param MotorData
	 * @param position
	 * 
	 */
	public RocketMotor(MotorData M, double position) {
		Mot = M;
		MountX = position;
		built = true;
	}
	
	public RocketMotor(Node Nin) {
		BuildFromXML(Nin);
	}
	
	@Override
	public String toString() {
		return (Mot.Name + "[Motor]");
	}
	
	public void EditMe() {
		MotorPositionDialog MPD = new MotorPositionDialog(null, true);
		MPD.SetMotName(Mot.toString());
		if (built) {
			MPD.SetPosition(MountX);
		}
		MPD.setVisible(true);
		if (MPD.ReadOk) {
			MountX = MPD.Length;
			built = true;
		}
		
	}
	
	public CylinderData asFullCylinder() {
		CylinderData FullMot = new CylinderData(true);
		
		FullMot.PopulateCylinder(Mot.Length, Mot.Diameter, Mot.LoadedMass, MountX, 0.0, Mot.Name);
		
		return (FullMot);
		
	}
	
	public CylinderData asEmptyCylinder() {
		CylinderData FullMot = new CylinderData(true);
		
		FullMot.PopulateCylinder(Mot.Length, Mot.Diameter, Mot.DryMass, MountX, 0.0, Mot.Name);
		
		return (FullMot);
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Motor");
		PartNode.appendChild(design.CreateDataNode("Length", Double.toString(Mot.Length)));
		PartNode.appendChild(design.CreateDataNode("Diameter", Double.toString(Mot.Diameter)));
		PartNode.appendChild(design.CreateDataNode("LoadedMass", Double.toString(Mot.LoadedMass)));
		PartNode.appendChild(design.CreateDataNode("DryMass", Double.toString(Mot.DryMass)));
		PartNode.appendChild(design.CreateDataNode("Time", design.VectorToDstring(Mot.Time)));
		PartNode.appendChild(design.CreateDataNode("Thrust", design.VectorToDstring(Mot.Thrust)));
		PartNode.appendChild(design.CreateDataNode("MountX", Double.toString(MountX)));
		PartNode.appendChild(design.CreateDataNode("Name", Mot.Name));
		design.MotorNode.appendChild(PartNode);
	}
	
	public final void BuildFromXML(Node Nin) {
		Mot = new MotorData();
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		Mot.Length = Temp.DbyName("Length");
		Mot.Diameter = Temp.DbyName("Diameter");
		Mot.LoadedMass = Temp.DbyName("LoadedMass");
		Mot.DryMass = Temp.DbyName("DryMass");
		Mot.Time = Temp.VDbyName("Time");
		Mot.Thrust = Temp.VDbyName("Thrust");
		Mot.Name = Temp.SbyName("Name");
		MountX = Temp.DbyName("MountX");
		Mot.CalculateSimpson();
		built = true;
		
	}
	
}
