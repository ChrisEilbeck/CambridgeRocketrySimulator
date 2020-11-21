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

%## StageDescription.java

%## Author: S.Box
%## Created: 2011-10-27

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author simon
 */
public class StageDescription implements Cloneable {
	//**Class Members
	private DefaultMutableTreeNode Body;
	private DefaultMutableTreeNode Mass;
	private DefaultMutableTreeNode Motor;
	private DefaultMutableTreeNode Transition;
	public DefaultMutableTreeNode RootNode;
	
	//** Constructor
	
	public StageDescription(String StageName) {
		RootNode = new DefaultMutableTreeNode(StageName);
		Body = new DefaultMutableTreeNode("Body Parts");
		Mass = new DefaultMutableTreeNode("Internal Parts");
		Motor = new DefaultMutableTreeNode("Motor");
		Transition = new DefaultMutableTreeNode("Transition");
		RootNode.add(Body);
		RootNode.add(Mass);
		RootNode.add(Motor);
		RootNode.add(Transition);
		
	}
	
	//**Function to add part to body
	public void addBodyPart(RockPartsData Part) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(Part);
		Body.add(PartN);
		TreeRefresh();
	}
	
	public void addBodyPartList(Vector<RockPartsData> pList) {
		for (RockPartsData Part : pList) {
			try {
				addBodyPart((RockPartsData) Part.clone());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ("Well, we tried to clone an object but I guess something went wrong."));
			}
		}
	}
	
	//**Function to add part to mass
	public void addMassPart(RockPartsData Part) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(Part);
		Mass.add(PartN);
		TreeRefresh();
	}
	
	public void addMassPartList(Vector<RockPartsData> pList) {
		for (RockPartsData Part : pList) {
			try {
				addMassPart((RockPartsData) Part.clone());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ("Well, we tried to clone an object but I guess something went wrong."));
			}
		}
	}
	
	//**Function to add part to mass
	public void addMotor(RocketMotor Part) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(Part);
		Motor.add(PartN);
		TreeRefresh();
	}
	
	public void addEmptyMotor(RocketMotor Part) {
		try {
			addMassPart((CylinderData) Part.asEmptyCylinder().clone());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, ("Well, we tried to clone an object but I guess something went wrong."));
		}
	}
	
	public void promoteMotor(RocketMotor Part) {
		if (hasMotor()) {
			RocketMotor OldRocket;
			DefaultMutableTreeNode T1 = (DefaultMutableTreeNode) Motor.getChildAt(0);
			if (T1.isLeaf()) {
				OldRocket = (RocketMotor) T1.getUserObject();
				RemovePart(T1);
				addMassPart(OldRocket.asFullCylinder());
				addMotor(Part);
			}
			
		} else {
			addMotor(Part);
		}
	}
	
	public void addTransition(TransitionData TD) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(TD);
		Transition.add(PartN);
		TreeRefresh();
	}
	
	//**Function to remove part
	public void RemovePart(DefaultMutableTreeNode Node) {
		Node.removeFromParent();
		TreeRefresh();
	}
	
	public void RemoveTransition() {
		Transition.removeAllChildren();
		TreeRefresh();
	}
	
	public Vector<RockPartsData> ReturnBodyParts() {
		Vector<RockPartsData> Temp = new Vector<RockPartsData>();
		int Count = Body.getChildCount();
		for (int i = 0; i < Count; i++) {
			DefaultMutableTreeNode T1 = (DefaultMutableTreeNode) Body.getChildAt(i);
			if (T1.isLeaf()) {
				Temp.add((RockPartsData) T1.getUserObject());
			}
		}
		return (Temp);
		
	}
	
	public Vector<RockPartsData> ReturnMassParts() {
		Vector<RockPartsData> Temp = new Vector<RockPartsData>();
		int Count = Mass.getChildCount();
		for (int i = 0; i < Count; i++) {
			DefaultMutableTreeNode T1 = (DefaultMutableTreeNode) Mass.getChildAt(i);
			if (T1.isLeaf()) {
				Temp.add((RockPartsData) T1.getUserObject());
			}
		}
		return (Temp);
		
	}
	
	public RocketMotor ReturnMotorData() {
		RocketMotor Temp = new RocketMotor();
		
		if (Motor.getChildCount() != 0) {
			DefaultMutableTreeNode T1 = (DefaultMutableTreeNode) Motor.getChildAt(0);
			if (T1.isLeaf()) {
				Temp = (RocketMotor) T1.getUserObject();
			}
		}
		
		return (Temp);
	}
	
	public TransitionData ReturnTransitionData() {
		TransitionData Temp = new TransitionData();
		if (isUpper()) {
			DefaultMutableTreeNode T1 = (DefaultMutableTreeNode) Transition.getChildAt(0);
			if (T1.isLeaf()) {
				Temp = (TransitionData) T1.getUserObject();
			}
		}
		return (Temp);
	}
	
	public Vector<ParachuteData> ReturnParachutes() {
		Vector<RockPartsData> Temp = ReturnMassParts();
		Vector<ParachuteData> Temp2 = new Vector<ParachuteData>();
		for (RockPartsData part : Temp) {
			if (part.toString().contains("[Parachute]")) {
				Temp2.add((ParachuteData) part);
			}
		}
		return (Temp2);
		
	}
	
	private void TreeRefresh() {
		RootNode.removeAllChildren();
		RootNode.add(Body);
		RootNode.add(Mass);
		RootNode.add(Motor);
		RootNode.add(Transition);
	}
	
	public boolean isEmpty() {
		return (Body.getChildCount() == 0 && Mass.getChildCount() == 0 && Motor.getChildCount() == 0);
	}
	
	public boolean hasMotor() {
		return (Motor.getChildCount() != 0);
	}
	
	public boolean isUpper() {
		return (Transition.getChildCount() != 0);
	}
	
	public boolean hasParachutes() {
		boolean tB = false;
		Vector<RockPartsData> Temp = ReturnMassParts();
		for (RockPartsData part : Temp) {
			if (part.toString().contains("[Parachute]")) {
				tB = true;
			}
		}
		return (tB);
	}
	
	@Override
	public String toString() {
		return (RootNode.toString());
	}
	
	
	public void ResetPartPositions() {
		
		Vector<RockPartsData> AllBodyparts = ReturnBodyParts();
		Vector<RockPartsData> AllMassparts = ReturnMassParts();
		Vector<RocketMotor> AllMotors = new Vector<RocketMotor>();
		if (Motor.getChildCount() != 0) {
			AllMotors.add(ReturnMotorData());
		}
		
		Body.removeAllChildren();
		Mass.removeAllChildren();
		Motor.removeAllChildren();
		
		
		double MinLength = 999999.0;//a very large number
		
		for (RockPartsData part : AllBodyparts) {
			if (part.Xp < MinLength) {
				MinLength = part.Xp;
			}
		}
		for (RockPartsData part : AllMassparts) {
			if (part.Xp < MinLength) {
				MinLength = part.Xp;
			}
		}
		for (RocketMotor part : AllMotors) {
			if (part.MountX < MinLength) {
				MinLength = part.MountX;
			}
		}
		
		for (RockPartsData part : AllBodyparts) {
			part.Xp = part.Xp - MinLength;
			addBodyPart(part);
		}
		for (RockPartsData part : AllMassparts) {
			part.Xp = part.Xp - MinLength;
			addMassPart(part);
		}
		for (RocketMotor part : AllMotors) {
			part.MountX = part.MountX - MinLength;
			addMotor(part);
		}
		
		
		
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}
