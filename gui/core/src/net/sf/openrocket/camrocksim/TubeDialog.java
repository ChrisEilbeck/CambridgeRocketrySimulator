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

%## TubeDialog.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * BodyTubeDialog.java
 *
 * Created on 29 April 2009, 13:41
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

import javax.swing.JOptionPane;

/**
 *
 * @author  sb4p07
 */
public class TubeDialog extends javax.swing.JDialog {
	
	//**Class Members
	double length,
			ID,
			OD,
			Mass,
			Position,
			RadialPosition;
	boolean ReadOk;
	String Name;
	
	/** Creates new form BodyTubeDialog */
	public TubeDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		
		TubeSpecTitle = new javax.swing.JLabel();
		TubeNameLabel = new javax.swing.JLabel();
		TubeLengthLabel = new javax.swing.JLabel();
		TubeIDLabel = new javax.swing.JLabel();
		TubeODLablel = new javax.swing.JLabel();
		TubeMassLabel = new javax.swing.JLabel();
		TubePosLabel = new javax.swing.JLabel();
		TubeNameText = new javax.swing.JTextField();
		TubeLengthText = new javax.swing.JTextField();
		TubeIDText = new javax.swing.JTextField();
		TubeODText = new javax.swing.JTextField();
		TubeMassText = new javax.swing.JTextField();
		TubePosText = new javax.swing.JTextField();
		TubeLengthUnit = new javax.swing.JLabel();
		TubeIDUnit = new javax.swing.JLabel();
		TubeODUnit = new javax.swing.JLabel();
		TubeMassUnit = new javax.swing.JLabel();
		TubePosUnit = new javax.swing.JLabel();
		AddTubeButton = new javax.swing.JButton();
		CancelTubeButton = new javax.swing.JButton();
		TubeRadPosLabel = new javax.swing.JLabel();
		TubeRadPosText = new javax.swing.JTextField();
		TubeRadPosUnit = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		
		TubeSpecTitle.setText("Set tube specifications");
		
		TubeNameLabel.setText("Name");
		
		TubeLengthLabel.setText("Length");
		
		TubeIDLabel.setText("Inner Diameter");
		
		TubeODLablel.setText("Outer Diameter");
		
		TubeMassLabel.setText("Mass");
		
		TubePosLabel.setText("Position");
		
		TubeNameText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubeNameTextFocusLost(evt);
			}
		});
		
		TubeLengthText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubeLengthTextFocusLost(evt);
			}
		});
		
		TubeIDText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubeIDTextFocusLost(evt);
			}
		});
		
		TubeODText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubeODTextFocusLost(evt);
			}
		});
		
		TubeMassText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubeMassTextFocusLost(evt);
			}
		});
		
		TubePosText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubePosTextFocusLost(evt);
			}
		});
		
		TubeLengthUnit.setText("m");
		
		TubeIDUnit.setText("m");
		
		TubeODUnit.setText("m");
		
		TubeMassUnit.setText("kg");
		
		TubePosUnit.setText("m");
		
		AddTubeButton.setText("Add");
		AddTubeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				AddTubeButtonActionPerformed(evt);
			}
		});
		
		CancelTubeButton.setText("Cancel");
		CancelTubeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				CancelTubeButtonActionPerformed(evt);
			}
		});
		
		TubeRadPosLabel.setText("Radial Position");
		
		TubeRadPosText.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TubeRadPosTextFocusLost(evt);
			}
		});
		
		TubeRadPosUnit.setText("m");
		
		jButton1.setText("?");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(TubeSpecTitle)
														.addGroup(layout.createSequentialGroup()
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubeNameLabel)
																		.addComponent(TubeLengthLabel)
																		.addComponent(TubeIDLabel)
																		.addComponent(TubeODLablel)
																		.addComponent(TubeMassLabel)
																		.addComponent(TubePosLabel))
																.addGap(49, 49, 49)
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
																		.addComponent(TubeRadPosText, javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubeNameText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 136,
																				Short.MAX_VALUE)
																		.addComponent(TubeLengthText, javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubeIDText, javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubeODText, javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubeMassText, javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubePosText, javax.swing.GroupLayout.Alignment.LEADING))
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(TubeRadPosUnit)
																		.addComponent(TubePosUnit)
																		.addComponent(TubeMassUnit)
																		.addComponent(TubeODUnit)
																		.addComponent(TubeIDUnit)
																		.addComponent(TubeLengthUnit)))))
										.addGroup(layout.createSequentialGroup()
												.addGap(62, 62, 62)
												.addComponent(AddTubeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(CancelTubeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addContainerGap()
												.addComponent(TubeRadPosLabel)))
								.addContainerGap(58, Short.MAX_VALUE)));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(TubeSpecTitle)
								.addGap(36, 36, 36)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubeNameLabel)
										.addComponent(TubeNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubeLengthLabel)
										.addComponent(TubeLengthText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(TubeLengthUnit))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubeIDLabel)
										.addComponent(TubeIDText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(TubeIDUnit))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubeODLablel)
										.addComponent(TubeODText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(TubeODUnit))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubeMassLabel)
										.addComponent(TubeMassText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(TubeMassUnit))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubePosLabel)
										.addComponent(TubePosText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(TubePosUnit))
								.addGap(5, 5, 5)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(TubeRadPosLabel)
										.addComponent(TubeRadPosText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(TubeRadPosUnit))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(AddTubeButton)
										.addComponent(CancelTubeButton)
										.addComponent(jButton1))
								.addGap(25, 25, 25)));
		
		pack();
	}// </editor-fold>//GEN-END:initComponents
	
	private void TubeLengthTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubeLengthTextFocusLost
		//Function to test the validity of the user input
		TestUserTextInput LengthTest = new TestUserTextInput(TubeLengthText);
		length = LengthTest.TestDouble();
		TubeLengthText = LengthTest.InputField;
	}//GEN-LAST:event_TubeLengthTextFocusLost
	
	private void TubeIDTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubeIDTextFocusLost
		///Function to test the validity of the user input
		TestUserTextInput IDTest = new TestUserTextInput(TubeIDText);
		ID = IDTest.TestDouble();
		TubeIDText = IDTest.InputField;
	}//GEN-LAST:event_TubeIDTextFocusLost
	
	private void TubeODTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubeODTextFocusLost
		///Function to test the validity of the user input
		TestUserTextInput ODTest = new TestUserTextInput(TubeODText);
		OD = ODTest.TestDouble();
		TubeODText = ODTest.InputField;
	}//GEN-LAST:event_TubeODTextFocusLost
	
	private void TubeMassTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubeMassTextFocusLost
		///Function to test the validity of the user input
		TestUserTextInput MassTest = new TestUserTextInput(TubeMassText);
		Mass = MassTest.TestDouble();
		TubeMassText = MassTest.InputField;
	}//GEN-LAST:event_TubeMassTextFocusLost
	
	private void TubePosTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubePosTextFocusLost
		///Function to test the validity of the user input
		TestUserTextInput PositionTest = new TestUserTextInput(TubePosText);
		Position = PositionTest.TestDouble();
		TubePosText = PositionTest.InputField;
	}//GEN-LAST:event_TubePosTextFocusLost
	
	private void TubeNameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubeNameTextFocusLost
		// TODO add your handling code here:
		Name = TubeNameText.getText();
	}//GEN-LAST:event_TubeNameTextFocusLost
	
	private void CancelTubeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelTubeButtonActionPerformed
		// TODO add your handling code here:
		this.dispose();
	}//GEN-LAST:event_CancelTubeButtonActionPerformed
	
	private void AddTubeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddTubeButtonActionPerformed
		// TODO add your handling code here:
		boolean Goer = CheckValidity();
		if (Goer == true) {
			ReadOk = true;
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(null, ("Something is wrong with the data that you entered"));
		}
	}//GEN-LAST:event_AddTubeButtonActionPerformed
	
	private void TubeRadPosTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TubeRadPosTextFocusLost
		// TODO add your handling code here:
		TestUserTextInput RadPosTest = new TestUserTextInput(TubeRadPosText);
		RadialPosition = RadPosTest.TestDouble();
		TubeRadPosText = RadPosTest.InputField;
	}//GEN-LAST:event_TubeRadPosTextFocusLost
	
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		Tips tDiag = new Tips(null, true);
		tDiag.setTipTxt(
				"Tube Settings \n\n Use this window to specify the parameters of the Tube part. Position is the distance, along the rocket's axis, from the nose tip to the foremost edge of the part. Radial position is the offset between the centre of the part and the rocket's axis. ");
		tDiag.setVisible(true);
	}//GEN-LAST:event_jButton1ActionPerformed
	
	private boolean CheckValidity() {
		boolean answer;
		Vector<javax.swing.JTextField> FieldsList = new Vector<javax.swing.JTextField>();
		FieldsList.add(TubeLengthText);
		FieldsList.add(TubeIDText);
		FieldsList.add(TubeODText);
		FieldsList.add(TubeMassText);
		FieldsList.add(TubePosText);
		FieldsList.add(TubeRadPosText);
		
		TestUserTextInput TUI1 = new TestUserTextInput(FieldsList);
		answer = TUI1.TestDoubleList();
		return (answer);
	}
	
	/**
	* @param args the command line arguments
	*/
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				TubeDialog dialog = new TubeDialog(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
	
	public void FillFields(double d1, double d2, double d3, double d4, double d5, double d6, String s1) {
		length = d1;
		ID = d2;
		OD = d3;
		Mass = d4;
		Position = d5;
		RadialPosition = d6;
		Name = s1;
		
		TubeLengthText.setText(Double.toString(length));
		TubeIDText.setText(Double.toString(ID));
		TubeODText.setText(Double.toString(OD));
		TubeMassText.setText(Double.toString(Mass));
		TubePosText.setText(Double.toString(Position));
		TubeNameText.setText(Name);
		TubeRadPosText.setText(Double.toString(RadialPosition));
	}
	
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton AddTubeButton;
	private javax.swing.JButton CancelTubeButton;
	private javax.swing.JLabel TubeIDLabel;
	private javax.swing.JTextField TubeIDText;
	private javax.swing.JLabel TubeIDUnit;
	private javax.swing.JLabel TubeLengthLabel;
	private javax.swing.JTextField TubeLengthText;
	private javax.swing.JLabel TubeLengthUnit;
	private javax.swing.JLabel TubeMassLabel;
	private javax.swing.JTextField TubeMassText;
	private javax.swing.JLabel TubeMassUnit;
	private javax.swing.JLabel TubeNameLabel;
	private javax.swing.JTextField TubeNameText;
	private javax.swing.JLabel TubeODLablel;
	private javax.swing.JTextField TubeODText;
	private javax.swing.JLabel TubeODUnit;
	private javax.swing.JLabel TubePosLabel;
	private javax.swing.JTextField TubePosText;
	private javax.swing.JLabel TubePosUnit;
	private javax.swing.JLabel TubeRadPosLabel;
	private javax.swing.JTextField TubeRadPosText;
	private javax.swing.JLabel TubeRadPosUnit;
	private javax.swing.JLabel TubeSpecTitle;
	private javax.swing.JButton jButton1;
	// End of variables declaration//GEN-END:variables
	
}
