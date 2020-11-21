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

%## BlockData.java

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
public class BlockData extends RockPartsData {
	//*Class Members
	double lengthX,
			lengthY,
			lengthZ,
			RadialPosition;
	boolean built = false;
	
	//*Class Constructor
	public BlockData() {
		EditMe();
	}
	
	public BlockData(Node Nin) {
		BuildFromXML(Nin);
	}
	
	public void PopulateBlock(double d1, double d2, double d3, double d4, double d5, double d6, String s1) {
		lengthX = d1;
		lengthY = d2;
		lengthZ = d3;
		Mass = d4;
		Xp = d5;
		RadialPosition = d6;
		
		Name = s1;
		Body = false;
		
		XcomBlock();
		InertialBlock();
		
	}
	
	//*Class functions
	
	private void XcomBlock() {
		Xcm = lengthZ / 2;
	}
	
	private void InertialBlock() {
		Ixx = Mass / 12 * (Math.pow(lengthY, 2) + Math.pow(lengthZ, 2));
		Iyy = Mass / 12 * (Math.pow(lengthX, 2) + Math.pow(lengthZ, 2));
		Izz = Mass / 12 * (Math.pow(lengthX, 2) + Math.pow(lengthY, 2)) + Mass * Math.pow(RadialPosition, 2);
		
	}
	
	@Override
	public String toString() {
		return (Name + "[Block]");
	}
	
	public void EditMe() {
		BlockDialog BD = new BlockDialog(null, true);
		if (built == true) {
			BD.FillFields(lengthX, lengthY, lengthZ, Mass, Xp, RadialPosition, Name);
		}
		BD.setVisible(true);
		if (BD.ReadOk == true) {
			PopulateBlock(BD.length, BD.Width, BD.Height, BD.Mass, BD.Position, BD.RadialPosition, BD.Name);
			built = true;
		}
		
	}
	
	public void WriteToXML(RWdesignXML design) {
		Node PartNode = design.CreateNode("Block");
		PartNode.appendChild(design.CreateDataNode("lengthX", Double.toString(lengthX)));
		PartNode.appendChild(design.CreateDataNode("lengthY", Double.toString(lengthY)));
		PartNode.appendChild(design.CreateDataNode("lengthZ", Double.toString(lengthZ)));
		PartNode.appendChild(design.CreateDataNode("Mass", Double.toString(Mass)));
		PartNode.appendChild(design.CreateDataNode("Xp", Double.toString(Xp)));
		PartNode.appendChild(design.CreateDataNode("RadialPosition", Double.toString(RadialPosition)));
		PartNode.appendChild(design.CreateDataNode("Name", Name));
		design.MassNode.appendChild(PartNode);
		
		
		
	}
	
	public void BuildFromXML(Node Nin) {
		XMLnodeParser Temp = new XMLnodeParser(Nin);
		
		PopulateBlock(Temp.DbyName("lengthX"), Temp.DbyName("lengthY"), Temp.DbyName("lengthZ"), Temp.DbyName("Mass"), Temp.DbyName("Xp"), Temp.DbyName("RadialPosition"), Temp.SbyName("Name"));
		built = true;//TODO fill this in
	}
	
}
