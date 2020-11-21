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

%## XMLnodeParser.java

%## Author: S.Box
%## Created: 2011-10-27

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author simon
 */
public class XMLnodeParser {
	Node MainNode;
	Element MainElement;
	
	public XMLnodeParser(Node Nin) {
		MainNode = Nin;
		MainElement = (Element) MainNode;
	}
	
	//function to return double under MainElement given a name
	protected double DbyName(String name) {
		return (Double.valueOf(MainElement.getElementsByTagName(name).item(0).getTextContent()));
	}
	
	//function to returnstring under MainElement given a name
	protected String SbyName(String name) {
		return (MainElement.getElementsByTagName(name).item(0).getTextContent());
	}
	
	//function to return int under MainElement by given name
	protected int IbyName(String name) {
		return (Integer.valueOf(MainElement.getElementsByTagName(name).item(0).getTextContent()));
	}
	
	//function to return int under MainElement by given name
	protected boolean BbyName(String name) {
		return (Boolean.parseBoolean(MainElement.getElementsByTagName(name).item(0).getTextContent()));
	}
	
	//function to return Vector<double> under Doc given a name
	protected Vector<Double> VDbyName(String name) {
		return (DstringToVector(MainElement.getElementsByTagName(name).item(0).getTextContent()));
	}
	
	protected Vector<Double> DstringToVector(String Dstring) {
		String[] SplitString = Dstring.split(",");
		Vector<Double> Temp = new Vector<Double>();
		
		for (String S : SplitString) {
			Temp.add(Double.valueOf(S));
		}
		return (Temp);
	}
	
}
