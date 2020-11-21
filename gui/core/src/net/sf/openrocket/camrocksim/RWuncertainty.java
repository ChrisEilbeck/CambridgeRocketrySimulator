/*
%## Copyright (C) 2016 W. Eerland
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

%## RWuncertainty.java

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author will
 */
public class RWuncertainty extends RWXML {
	
	double sigmaLaunchDeclination = 0.0;
	double sigmaThrust = 0.0;
	
	public RWuncertainty(String fn) {
		super(fn);
		
	}
	
	public void ReadXML() {
		
		ReadInit();
		
		sigmaLaunchDeclination = DbyName("LaunchDeclination");
		sigmaThrust = DbyName("Thrust");
		
	}
	
	public void UpdateXML() {
		
		ReadInit(); // fills Doc
		
		NodeList theseNodes;
		
		// edit launch declination
		theseNodes = Doc.getElementsByTagName("LaunchDeclination");
		
		for (int i = 0; i < theseNodes.getLength(); i++) {
			Node thisNode = (Node) theseNodes.item(i);
			
			thisNode.setTextContent(Double.toString((this.sigmaLaunchDeclination)));
		}
		
		// edit thrust
		theseNodes = Doc.getElementsByTagName("Thrust");
		
		for (int i = 0; i < theseNodes.getLength(); i++) {
			Node thisNode = (Node) theseNodes.item(i);
			
			thisNode.setTextContent(Double.toString((this.sigmaThrust)));
			
		}
		
		WriteOutput();
		
	}
	
	public double getSigmaLaunchDeclination() {
		return this.sigmaLaunchDeclination;
	}
	
	public double getSigmaThrust() {
		return this.sigmaThrust;
	}
	
	public void setSigmaLaunchDeclination(double sigmaLaunchDeclination) {
		this.sigmaLaunchDeclination = sigmaLaunchDeclination;
	}
	
	public void setSigmaThrust(double sigmaThrust) {
		this.sigmaThrust = sigmaThrust;
	}
	
	
	
}
