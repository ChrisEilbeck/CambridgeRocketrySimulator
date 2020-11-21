/**
 * 
 */
package net.sf.openrocket.camrocsim;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sf.openrocket.camrocksim.AtmosphereData;
import net.sf.openrocket.camrocksim.LaunchData;
import net.sf.openrocket.camrocksim.MotorData;
import net.sf.openrocket.camrocksim.RWatmosXML;
import net.sf.openrocket.camrocksim.RWmotorXML;
import net.sf.openrocket.camrocksim.RWsiminXML;
import net.sf.openrocket.camrocksim.RocketDescription;

/**
 * @author will
 *
 */
public class WritingXmlTest {
	
	double margin = 0.00001; // margin for testing doubles
	
	@Test
	public void AtmosXmlTest() {
		
		// create an atmosphere -- standard ISA, no wind!
		AtmosphereData atmos_in = new AtmosphereData(true);
		
		// file handler
		RWatmosXML writer = new RWatmosXML("temp.xml");
		
		// write to (temporary) XML
		writer.WriteAtmosToXML(atmos_in);
		
		// new file handler (assure no cheating)
		RWatmosXML reader = new RWatmosXML("temp.xml");
		
		// read from XML
		reader.ReadXMLtoAtmos();
		
		// obtain atmosphere data
		AtmosphereData atmos_out = reader.getAtmosphereData();
		
		// check parameters
		assertEquals(atmos_in.name, atmos_out.name);
		
		// vectors..
		for (int i = 0; i < atmos_in.Xwind.size(); i++) {
			// wind speeds
			assertEquals(atmos_in.Xwind.get(i), atmos_out.Xwind.get(i), margin);
			assertEquals(atmos_in.Ywind.get(i), atmos_out.Ywind.get(i), margin);
			assertEquals(atmos_in.Zwind.get(i), atmos_out.Zwind.get(i), margin);
			// temperature
			assertEquals(atmos_in.Theta.get(i), atmos_out.Theta.get(i), margin);
			// density
			assertEquals(atmos_in.rho.get(i), atmos_out.rho.get(i), margin);
		}
		
	}
	
	@Test
	public void SimInXmlTest() {
		
		// WriteSimDataToXML(RocketDescription TheRocket, 
		// AtmosphereData TheAtmosphere, LaunchData LaunchPadSet, 
		// boolean Monte, int mIts, boolean fail)
		
		RocketDescription rocket = new RocketDescription();
		
		// pre-filled atmosphere
		AtmosphereData atmos = new AtmosphereData(true);
		
		// LaunchData(double railLength, double azimuth, double declination, double altitude)
		LaunchData launch = new LaunchData(1.0, 0.0, 0.0, 0.0);
		
		// writer
		RWsiminXML writer = new RWsiminXML("temp.xml");
		
		// write to XML -- doesn't read, only write, so no check
		writer.WriteSimDataToXML(rocket, atmos, launch, true, 2, false);
	}
	
	@Test
	public void MotorXmlTest() {
		
		// engine
		MotorData motor_in = new MotorData("motordata", 1.0, 0.1, 1.0, 0.1,
				new double[] { 0.0, 0.1, 0.2, 0.3, 0.4 },
				new double[] { 0.0, 1.0, 2.0, 3.0, 0.0 });
		
		// writer
		RWmotorXML writer = new RWmotorXML("temp.xml");
		
		// write to file
		writer.WriteMotorToXML(motor_in);
		
		// reader
		RWmotorXML reader = new RWmotorXML("temp.xml");
		
		// read from file
		reader.ReadXMLtoMotor();
		
		// obtain read motordata
		MotorData motor_out = reader.getMotorData();
		
		// test variables
		assertEquals(motor_in.Diameter, motor_out.Diameter, margin);
		assertEquals(motor_in.DryMass, motor_out.DryMass, margin);
		assertEquals(motor_in.LoadedMass, motor_out.LoadedMass, margin);
		assertEquals(motor_in.Length, motor_out.Length, margin);
		
		for (int i = 0; i < motor_in.Ixx.size(); i++) {
			// thrust, and time
			assertEquals(motor_in.Thrust.get(i), motor_out.Thrust.get(i), margin);
			assertEquals(motor_in.Time.get(i), motor_out.Time.get(i), margin);
		}
		
	}
	
	
	
}
