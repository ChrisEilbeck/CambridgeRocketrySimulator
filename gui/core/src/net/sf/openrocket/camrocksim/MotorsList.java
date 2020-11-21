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

%## MotorsList.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author simon
 */
public class MotorsList {
	//class members
	DefaultListModel MotListMod;
	DefaultComboBoxModel MotListCombo;
	
	
	public MotorsList() {
		MotListMod = new DefaultListModel();
		MotListCombo = new DefaultComboBoxModel();
		//MotListCombo.addElement("null");
	}
	
	public void AddtoList(MotorData MD) {
		MotListMod.addElement(MD);
		MotListCombo.addElement(MD);
	}
	
	public void RemoveFromList(int i) {
		MotListMod.removeElementAt(i);
		MotListCombo.removeElementAt(i);
	}
	
	
}
