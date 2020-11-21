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

%## RWmotorXML.java

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
public class RWmotorXML extends RWXML {
	
	//*Class member
	
	MotorData MotDat;
	
	//*Class Constructor
	public RWmotorXML(String fn) {
		super(fn);
		MotDat = new MotorData();
		
	}
	
	//*Function
	public void ReadXMLtoMotor() {
		ReadInit();
		
		MotDat.Name = Doc.getElementsByTagName("Name").item(0).getTextContent();
		MotDat.Length = DbyName("Length");
		MotDat.Diameter = DbyName("Diameter");
		MotDat.LoadedMass = DbyName("LoadedMass");
		MotDat.DryMass = DbyName("DryMass");
		MotDat.Time = VDbyName("Time");
		MotDat.Thrust = VDbyName("Thrust");
		MotDat.built = true;
	}
	
	public void WriteMotorToXML(MotorData MD) {
		MotDat = MD;
		WriteInit();
		
		Node RootNode = Doc.createElement("Motor");
		RootNode.appendChild(CreateDataNode("Name", MotDat.Name));
		RootNode.appendChild(CreateDataNode("Length", Double.toString(MotDat.Length)));
		RootNode.appendChild(CreateDataNode("Diameter", Double.toString(MotDat.Diameter)));
		RootNode.appendChild(CreateDataNode("LoadedMass", Double.toString(MotDat.LoadedMass)));
		RootNode.appendChild(CreateDataNode("DryMass", Double.toString(MotDat.DryMass)));
		RootNode.appendChild(CreateDataNode("Time", VectorToDstring(MotDat.Time)));
		RootNode.appendChild(CreateDataNode("Thrust", VectorToDstring(MotDat.Thrust)));
		
		Doc.appendChild(RootNode);
		WriteOutput();
		
		
	}
	
	public MotorData getMotorData() {
		return MotDat;
	}
	
	
	
	
	
}
