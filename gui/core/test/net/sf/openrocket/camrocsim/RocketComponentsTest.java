package net.sf.openrocket.camrocsim;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import net.sf.openrocket.camrocksim.AtmosphereData;
import net.sf.openrocket.camrocksim.BodyTubeData;
import net.sf.openrocket.camrocksim.ConicTransData;
import net.sf.openrocket.camrocksim.CylinderData;
import net.sf.openrocket.camrocksim.FinsetData;
import net.sf.openrocket.camrocksim.MotorData;
import net.sf.openrocket.camrocksim.NoseConeData;
import net.sf.openrocket.camrocksim.ParachuteData;
import net.sf.openrocket.camrocksim.PointMassData;
import net.sf.openrocket.camrocksim.RockPartsData;
import net.sf.openrocket.camrocksim.RocketDescription;
import net.sf.openrocket.camrocksim.RocketMotor;
import net.sf.openrocket.camrocksim.TransitionData;

public class RocketComponentsTest {
	
	@Test
	public void AtmosphereDataTest() {
		// initialise an empty atmospheric data
		AtmosphereData atmospheredata = new AtmosphereData();
		assertFalse(atmospheredata.built);
	}
	
	
	@Test
	public void SingleStageRocketTest() {
		/*
		 * test for a single stage rocket
		 */
		
		// build an camrocsim rocket
		RocketDescription thisRocketDescription = new RocketDescription();
		
		// initialise empty (re-use!)
		RockPartsData thisRockPartsData = null;
		
		// first row "Body components and fin sets"
		
		// add "Nose cone"
		thisRockPartsData = new NoseConeData(1.0, .2, 0.0, 1.0, 0, "Nose cone");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// add "Body tube"
		thisRockPartsData = new BodyTubeData(5.0, 0.15, 0.2, 5.0, 6.0, "Body tube");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// add "Transition"
		thisRockPartsData = new ConicTransData(3.0, 0.2, 0.3, 0.1, 1.0, 9.0, "Transition");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// add "Trapezoidal"
		thisRockPartsData = new FinsetData(3, 2.0, 2.0, 3.0, 0.0, 0.2, 0.2, 1.0, 4.0, "Trapezoidal");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// second row "Inner component"
		
		// add "Inner tube" / "Coupler" / "Centering ring" / "Bulkhead"
		// ALL CylinderData
		thisRockPartsData = new CylinderData(2.0, 0.1, 1.0, 6.0, 0.0, "Cylinder");
		thisRocketDescription.addMassPart(thisRockPartsData, 0);
		
		// third row "Mass objects"
		
		// add "Parachute"
		thisRockPartsData = new ParachuteData(0.1, 0.1, 0.5, 6.0, 10.0, 0.9, 1000, false, "Parachute");
		thisRocketDescription.addMassPart(thisRockPartsData, 0);
		
		// add "Mass component"
		thisRockPartsData = new PointMassData(5.0, 6.0, 0.0, "Mass component");
		thisRocketDescription.addMassPart(thisRockPartsData, 0);
		
		// add an engine
		
		// 1. MotorData
		MotorData thisMotorData = new MotorData("motordata", 1.0, 0.1, 1.0, 0.1,
				new double[] { 0.0, 0.1, 0.2, 0.3, 0.4 },
				new double[] { 0.0, 1.0, 2.0, 3.0, 0.0 });
		
		// 2. RocketMotor (positioning the Motor)
		RocketMotor thisRocketMotor = new RocketMotor(thisMotorData, 6.0);
		thisRocketDescription.addMotor(thisRocketMotor, 0);
		
	}
	
	@Test
	public void TwoStageRocketTest() {
		/*
		 * test for a second stage rocket
		 */
		
		// build an camrocsim rocket
		RocketDescription thisRocketDescription = new RocketDescription();
		
		// initialise empty (re-use!)
		RockPartsData thisRockPartsData = null;
		
		// first row "Body components and fin sets"
		
		// add "Nose cone"
		thisRockPartsData = new NoseConeData(1.0, .2, 0.0, 1.0, 0, "Nose cone");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// add "Body tube"
		thisRockPartsData = new BodyTubeData(5.0, 0.15, 0.2, 5.0, 6.0, "Body tube");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// add "Transition"
		thisRockPartsData = new ConicTransData(3.0, 0.2, 0.3, 0.1, 1.0, 9.0, "Transition");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// add "Trapezoidal"
		thisRockPartsData = new FinsetData(3, 2.0, 2.0, 3.0, 0.0, 0.2, 0.2, 1.0, 4.0, "Trapezoidal");
		thisRocketDescription.addBodyPart(thisRockPartsData, 0);
		
		// second row "Inner component"
		
		// add "Inner tube" / "Coupler" / "Centering ring" / "Bulkhead"
		// ALL CylinderData
		thisRockPartsData = new CylinderData(2.0, 0.1, 1.0, 6.0, 0.0, "Cylinder");
		thisRocketDescription.addMassPart(thisRockPartsData, 0);
		
		// third row "Mass objects"
		
		// add "Parachute"
		thisRockPartsData = new ParachuteData(0.1, 0.1, 0.5, 6.0, 10.0, 0.9, 1000, false, "Parachute");
		thisRocketDescription.addMassPart(thisRockPartsData, 0);
		
		// add "Mass component"
		thisRockPartsData = new PointMassData(5.0, 6.0, 0.0, "Mass component");
		thisRocketDescription.addMassPart(thisRockPartsData, 0);
		
		// introduce a second stage
		thisRocketDescription.addNewStage();
		
		// transition data (when to release)
		TransitionData thisTransitionData = new TransitionData(5.0, 0.0);
		thisRocketDescription.addTransition(thisTransitionData);
		
		// add "Body tube" - second stage
		thisRockPartsData = new BodyTubeData(2.0, 0.15, 0.2, 5.0, 8.0, "Body tube [2]");
		thisRocketDescription.addBodyPart(thisRockPartsData, 1);
		
		// add "Mass component"
		thisRockPartsData = new PointMassData(5.0, 7.0, 0.0, "Mass component");
		thisRocketDescription.addMassPart(thisRockPartsData, 1);
		
		// add an engine
		
		// 1. MotorData
		MotorData thisMotorData = new MotorData("motordata", 1.0, 0.1, 1.0, 0.1,
				new double[] { 0.0, 0.1, 0.2, 0.3, 0.4 },
				new double[] { 0.0, 1.0, 2.0, 3.0, 0.0 });
		
		// 2. RocketMotor (positioning the Motor)
		RocketMotor thisRocketMotor = new RocketMotor(thisMotorData, 6.0);
		thisRocketDescription.addMotor(thisRocketMotor, 0);
		
		thisRocketMotor = new RocketMotor(thisMotorData, 8.0);
		thisRocketDescription.addMotor(thisRocketMotor, 1);
		
	}
	
	
	
}
