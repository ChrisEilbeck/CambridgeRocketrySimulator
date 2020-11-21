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

%## RWatmosXML.java

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
public class RWatmosXML extends RWXML {
	
	//class members
	AtmosphereData Atmosdat;
	
	//class constructor
	public RWatmosXML(String fn) {
		super(fn);
		Atmosdat = new AtmosphereData();
	}
	
	public void ReadXMLtoAtmos() {
		ReadInit();
		
		Atmosdat.name = Doc.getElementsByTagName("Name").item(0).getTextContent();
		Atmosdat.Altitude = VDbyName("Altitude");
		Atmosdat.Xwind = VDbyName("XWind");
		Atmosdat.Ywind = VDbyName("YWind");
		Atmosdat.Zwind = VDbyName("ZWind");
		Atmosdat.rho = VDbyName("Rho");
		Atmosdat.Theta = VDbyName("Theta");
		Atmosdat.built = true;
	}
	
	public void WriteAtmosToXML(AtmosphereData AD) {
		Atmosdat = AD;
		
		WriteInit();
		
		Node RootNode = Doc.createElement("WindProfile");
		RootNode.appendChild(CreateDataNode("Name", Atmosdat.name));
		RootNode.appendChild(CreateDataNode("Altitude", VectorToDstring(Atmosdat.Altitude)));
		RootNode.appendChild(CreateDataNode("XWind", VectorToDstring(Atmosdat.Xwind)));
		RootNode.appendChild(CreateDataNode("YWind", VectorToDstring(Atmosdat.Ywind)));
		RootNode.appendChild(CreateDataNode("ZWind", VectorToDstring(Atmosdat.Zwind)));
		RootNode.appendChild(CreateDataNode("Rho", VectorToDstring(Atmosdat.rho)));
		RootNode.appendChild(CreateDataNode("Theta", VectorToDstring(Atmosdat.Theta)));
		
		Doc.appendChild(RootNode);
		WriteOutput();
	}
	
	public AtmosphereData getAtmosphereData() {
		// return AtmosphereData
		
		return this.Atmosdat;
	}
	
}
