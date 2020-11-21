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

%## TestUserTextInput.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author sb4p07
 */
public class TestUserTextInput {
	
	//*Class Members
	public javax.swing.JTextField InputField;
	public Vector<javax.swing.JTextField> InputFieldList;
	public javax.swing.JTable InputTable;
	public Vector<ArrayList> TableValues;
	public boolean Valid;
	
	//*Class Constructors
	public TestUserTextInput(javax.swing.JTextField inp1) {
		InputField = inp1;
		Valid = false;
	}
	
	public TestUserTextInput(Vector<javax.swing.JTextField> inp1) {
		InputFieldList = inp1;
		Valid = false;
	}
	
	public TestUserTextInput(javax.swing.JTable inp1) {
		InputTable = inp1;
		CustomTableCellRenderer Mytabcell = new CustomTableCellRenderer();
		InputTable.setDefaultRenderer(Object.class, Mytabcell);
		Valid = false;
	}
	
	//*Class functions
	public double TestDouble() {
		
		double DV = 0;
		Valid = true;
		
		InputField = SetValidCol(InputField);
		try {
			DV = Double.parseDouble(InputField.getText());
		} catch (Exception e) {
			Valid = false;
			InputField = SetInvalidCol(InputField);
		}
		return (DV);
		
	}
	
	public boolean TestDoubleList() {
		double TestDouble = 0;
		Valid = true;
		
		try {
			for (javax.swing.JTextField Field : InputFieldList) {
				TestDouble = Double.parseDouble(Field.getText());
				
			}
			
		} catch (Exception e) {
			Valid = false;
		}
		
		return (Valid);
	}
	
	public boolean TestDoubleColumn() {
		int Cnum = InputTable.getColumnCount();
		int Rnum = InputTable.getRowCount();
		
		for (int c = 0; c < Cnum; c++) {
			for (int r = 0; r < Rnum; r++) {
				Object O = InputTable.getValueAt(r, c);
				try {
					double Element = Double.parseDouble(O.toString());
				} catch (Exception e) {
					
				}
			}
		}
		
		
		return (true);
	}
	
	public Vector<Vector<Double>> TestTable() {
		Vector<Vector<Double>> Temp = new Vector<Vector<Double>>();
		Valid = true;
		//InputTable.setForeground(Color.black);
		for (int i = 0; i < InputTable.getColumnCount(); i++) {
			Temp.add(TestColumnFromTab(i));
		}
		return (Temp);
	}
	
	private Vector<Double> TestColumnFromTab(int Cnum) {
		Vector<Double> Temp = new Vector<Double>();
		
		for (int i = 0; i < InputTable.getRowCount(); i++) {
			if (InputTable.getValueAt(i, Cnum) != null) {
				Object O = InputTable.getValueAt(i, Cnum);
				
				try {
					double d = Double.valueOf(O.toString());
					Temp.add(d);
					//javax.swing.table.TableCellRenderer TCR = InputTable.getCellRenderer(i, Cnum);
					//Component Cell = TCR.getTableCellRendererComponent(InputTable, O, Valid, Valid, i, Cnum);
					//Cell.setForeground(Color.BLACK);
					//Component TCR = InputTable.getComponentAt(i, Cnum);
					//TCR.setForeground(Color.black);
				} catch (Exception e) {
					Valid = false;
					//InputTable.setForeground(Color.red);
					//Component TCR = InputTable.get.getComponentAt(i, Cnum);
					//TCR.setForeground(Color.red);
				}
			}
		}
		if (Temp.isEmpty()) {
			Valid = false;
		}
		return (Temp);
	}
	
	public javax.swing.JTextField SetValidCol(javax.swing.JTextField Field1) {
		Field1.setForeground(java.awt.Color.BLACK);
		return (Field1);
	}
	
	public javax.swing.JTextField SetInvalidCol(javax.swing.JTextField Field1) {
		Field1.setForeground(java.awt.Color.RED);
		return (Field1);
	}
	
	
	public class CustomTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			try {
				double d = Double.parseDouble(value.toString());
				cell.setForeground(Color.black);
			} catch (Exception e) {
				//cell.setBackground(Color.blue);
				cell.setForeground(Color.red);
			}
			/*if( value instanceof Integer )
			{
			Integer amount = (Integer) value;
			if( amount.intValue() < 0 )
			{
			    cell.setBackground( Color.blue );
			    // You can also customize the Font and Foreground this way
			    // cell.setForeground();
			    // cell.setFont();
			}
			else
			{
			    cell.setBackground( Color.white );
			}
			}*/
			return cell;
		}
	}
	
	
	
}
