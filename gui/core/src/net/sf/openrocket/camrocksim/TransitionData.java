/*
%## Copyright (C) 2011, 2016 S.Box
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

%## TransitionData.java

%## Author: S.Box
%## Created: 2011-10-27

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
public class TransitionData extends RockPartsData {
	//Members
	public double separationTime;
	public double ignitionDelay;
	boolean built;
	
	public TransitionData() {
	}
	
	/*	
	 *  TransitionData 
	 *  
	 *  @param separationtime second stage releases [s]
	 *  @param ignitiondelay time after separation to ignite [s]
	 * 
	 */
	public TransitionData(double separationtime, double ignitiondelay) {
		PopulateTransition(separationtime, ignitiondelay);
	}
	
	public TransitionData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	public void PopulateTransition(double d1, double d2) {
		separationTime = d1;
		ignitionDelay = d2;
		built = true;
	}
	
	@Override
	public String toString() {
		return ("Stage Transition");
	}
	
	public void EditMe() {
		TransitionDialog TD = new TransitionDialog(null, true);
		if (built == true) {
			TD.FillFields(separationTime, ignitionDelay, "Stage Transition");
		}
		TD.setVisible(true);
		if (TD.ReadOk == true) {
			PopulateTransition(TD.separationTime, TD.ignitionDelay);
		}
	}
	
	
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("TransitionData");
		PartNode.appendChild(design.CreateDataNode("separationTime", Double.toString(separationTime)));
		PartNode.appendChild(design.CreateDataNode("ignitionDelay", Double.toString(ignitionDelay)));
		design.TransitionNode.appendChild(PartNode);
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateTransition(Temp.DbyName("separationTime"), Temp.DbyName("ignitionDelay"));
		built = true;
	}
	
	
}
