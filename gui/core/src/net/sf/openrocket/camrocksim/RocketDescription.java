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

%## RocketDescription.java

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author simon
 */
public class RocketDescription {
	//**Class Members
	//Vector<RockPartsData> RPartsList = new Vector<RockPartsData>();
	public TreeModel RocketTree;
	public Vector<StageDescription> Stages = new Vector<StageDescription>();
	public DefaultComboBoxModel StageCombo;
	private DefaultMutableTreeNode RootNode;
	
	//** Constructor
	public RocketDescription() {
		RootNode = new DefaultMutableTreeNode("Rocket");
		StageDescription MainStage = new StageDescription("MainStage");
		Stages.add(MainStage);
		StageCombo = new DefaultComboBoxModel();
		StageCombo.addElement(MainStage);
		TreeRefresh();
	}
	
	public void addNewStage() {
		int sCount = Stages.size();
		StageDescription NewBooster = new StageDescription(("Booster_" + Integer.toString(sCount)));
		Stages.add(NewBooster);
		StageCombo.addElement(NewBooster);
		TreeRefresh();
		
	}
	
	public void addTransition(TransitionData thisTransitionData) {
		// add transitiondata
		// Stages.lastElement().addTransition(thisTransitionData);
		Stages.firstElement().addTransition(thisTransitionData);
		TreeRefresh();
	}
	
	public void addTransition() {
		TransitionData TD = new TransitionData();
		TD.EditMe();
		Stages.lastElement().addTransition(TD);
		TreeRefresh();
	}
	
	//**Function to add part to body
	public void addBodyPart(RockPartsData Part, int Stage) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(Part);
		Stages.elementAt(Stage).addBodyPart(Part);
		TreeRefresh();
	}
	
	//**Function to add part to mass
	public void addMassPart(RockPartsData Part, int Stage) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(Part);
		Stages.elementAt(Stage).addMassPart(Part);
		TreeRefresh();
	}
	
	//**Function to add part to mass
	public void addMotor(RocketMotor Part, int Stage) {
		DefaultMutableTreeNode PartN = new DefaultMutableTreeNode(Part);
		Stages.elementAt(Stage).addMotor(Part);
		TreeRefresh();
	}
	
	//**Function to remove part
	public void RemovePart(DefaultMutableTreeNode Node) {
		Node.removeFromParent();
		TreeRefresh();
	}
	
	public void RemoveStage(DefaultMutableTreeNode Node) {
		String name = Node.toString();
		if (name.contains("Booster")) {
			int index = Integer.parseInt(Character.toString(name.charAt(8)));
			Stages.remove(index);
			Stages.elementAt(index - 1).RemoveTransition();
		}
		Node.removeFromParent();
		TreeRefresh();
	}
	
	public Vector<RockPartsData> ReturnBodyParts() {
		Vector<RockPartsData> Temp = new Vector<RockPartsData>();
		/*int Count = Body.getChildCount();
		for(int i =0; i<Count; i++)
		{
		    DefaultMutableTreeNode T1 = (DefaultMutableTreeNode)Body.getChildAt(i);
		    if(T1.isLeaf())
		    {
		        Temp.add((RockPartsData)T1.getUserObject());
		    }
		}*/
		return (Temp);
		
	}
	
	public Vector<RockPartsData> ReturnMassParts() {
		Vector<RockPartsData> Temp = new Vector<RockPartsData>();
		/*int Count = Mass.getChildCount();
		for(int i =0; i<Count; i++)
		{
		    DefaultMutableTreeNode T1 = (DefaultMutableTreeNode)Mass.getChildAt(i);
		    if(T1.isLeaf())
		    {
		        Temp.add((RockPartsData)T1.getUserObject());
		    }
		}*/
		return (Temp);
		
	}
	
	public RocketMotor ReturnMotorData() {
		RocketMotor Temp = new RocketMotor();
		/*try{
		    DefaultMutableTreeNode T1 = (DefaultMutableTreeNode)Motor.getChildAt(0);
		    if(T1.isLeaf())
		    {
		        Temp = (RocketMotor)T1.getUserObject();
		    }
		    else{
		        throw new Exception("Rocket Motor was not found");
		    }
		
		}
		catch(Exception e){
		    JOptionPane.showMessageDialog(null, e);
		}*/
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
		for (StageDescription stage : Stages) {
			RootNode.add(stage.RootNode);
		}
		RocketTree = new DefaultTreeModel(RootNode);
	}
	
	public boolean isEmpty() {
		boolean Temp = true;
		for (StageDescription s : Stages) {
			if (!s.isEmpty()) {
				Temp = false;
			}
		}
		
		return (Temp);
	}
	
	
	public Vector<StageDescription> buildSimulationStages() {
		Vector<StageDescription> StageBuffer = (Vector<StageDescription>) Stages.clone();
		Vector<StageDescription> SimulationStages = new Vector<StageDescription>();
		
		StageDescription TotalRocket = new StageDescription("TotalRocket");
		for (StageDescription stage : StageBuffer) {
			TotalRocket.addBodyPartList(stage.ReturnBodyParts());
			TotalRocket.addMassPartList(stage.ReturnMassParts());
			if (stage.hasMotor()) {
				TotalRocket.promoteMotor(stage.ReturnMotorData());
			}
		}
		SimulationStages.add(TotalRocket);
		
		int counting = 1;
		while (StageBuffer.size() > 1) {
			StageDescription Booster = new StageDescription("Booster_" + Integer.toString(counting));
			StageDescription Upper = new StageDescription("Upper_" + Integer.toString(counting));
			
			Booster.addBodyPartList(StageBuffer.lastElement().ReturnBodyParts());
			Booster.addMassPartList(StageBuffer.lastElement().ReturnMassParts());
			if (StageBuffer.lastElement().hasMotor()) {
				Booster.addEmptyMotor(StageBuffer.lastElement().ReturnMotorData());
			}
			
			Booster.ResetPartPositions();
			StageBuffer.removeElementAt(StageBuffer.size() - 1);
			
			for (StageDescription stage : StageBuffer) {
				Upper.addBodyPartList(stage.ReturnBodyParts());
				Upper.addMassPartList(stage.ReturnMassParts());
				if (stage.hasMotor()) {
					Upper.promoteMotor(stage.ReturnMotorData());
				}
			}
			SimulationStages.add(Upper);
			SimulationStages.add(Booster);
			counting++;
		}
		return (SimulationStages);
	}
	
	
}
