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

%## GenerateSimData

%## Author: S.Box
%## Created: 2010-05-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.util.Vector;

/**
 *
 * @author simon
 */
public class GenerateSimData {
	//*class Members
	StageDescription RocD;
	//INTAB1
	Vector<Double> Time,
			Thrust,
			Mass,
			Ixx,
			Iyy,
			Izz,
			CoM,
			TDC;
	//**************
	//INTAB2
	Vector<Double> AoA,
			Re;
	double[][] CD;
	//***************
	//INTAB3
	Vector<Double> Cn,
			Cp;
	//***************
	//INTAB4
	Vector<Double> Altitude,
			Xwind,
			Ywind,
			Zwind,
			rho,
			Theta;
	//***************
	//Parachute
	Vector<Double> SwitchAlt,
			CDA;
	//****************
	//LengthAndArea
	double Length,
			Xarea;
	//****************
	
	//******************
	//Miscelaneous constants
	double Rec;//critical reynolds number
	
	//**********************
	
	//Constructor
	public GenerateSimData(StageDescription RD, AtmosphereData AD) {
		//Define some Constants
		Rec = 500000;
		double cRe = 1;
		Re = new Vector<Double>();
		Re.add(cRe);
		while (cRe <= 100000) {
			cRe = cRe * Math.pow(10, 0.5);
			Re.add(cRe);
		}
		while (cRe <= 10000000) {
			cRe = cRe * Math.pow(10, 0.16666667);
			Re.add(cRe);
		}
		
		double calpha = 0;
		AoA = new Vector<Double>();
		AoA.add(calpha);
		while (calpha <= 0.16) {
			calpha += 0.02;
			AoA.add(calpha);
		}
		CD = new double[AoA.size()][Re.size()];
		//*********************
		
		RocD = RD;
		Intab1Dat();
		Intab2Dat();
		Intab3Dat();
		ParachuteData();
		Intab4Dat(AD);
	}
	
	private void Intab1Dat() {
		//*Calculate totals for the rocket (sans motor)*********************
		Vector<RockPartsData> MassParts = RocD.ReturnMassParts();
		Vector<RockPartsData> BodyParts = RocD.ReturnBodyParts();
		
		double rMoment = 0;
		double rMass = 0;
		
		for (RockPartsData rpd : MassParts) {
			rMoment += (rpd.Xp + rpd.Xcm) * rpd.Mass;
			rMass += rpd.Mass;
		}
		for (RockPartsData rpd : BodyParts) {
			rMoment += (rpd.Xp + rpd.Xcm) * rpd.Mass;
			rMass += rpd.Mass;
		}
		
		double rCoM = rMoment / rMass;
		
		double rIx = 0;
		double rIy = 0;
		double rIz = 0;
		
		for (RockPartsData rpd : MassParts) {
			double sep = Math.abs((rpd.Xp + rpd.Xcm) - rCoM);
			rIx += rpd.Ixx + rpd.Mass * Math.pow(sep, 2.0);//parallel axis
			rIy += rpd.Iyy + rpd.Mass * Math.pow(sep, 2.0);//parallel axis
			rIz += rpd.Izz;
		}
		for (RockPartsData rpd : BodyParts) {
			double sep = Math.abs((rpd.Xp + rpd.Xcm) - rCoM);
			rIx += rpd.Ixx + rpd.Mass * Math.pow(sep, 2.0);//parallel axis
			rIy += rpd.Iyy + rpd.Mass * Math.pow(sep, 2.0);//parallel axis
			rIz += rpd.Izz;
		}
		//***************************************************************
		
		
		if (RocD.hasMotor()) {
			//Combine With Motor*********************************************
			RocketMotor Mot = RocD.ReturnMotorData();
			
			Time = Mot.Mot.Time;
			Thrust = Mot.Mot.Thrust;
			
			double dist = Mot.MountX + Mot.Mot.CoM;
			double NozX = Mot.MountX + Mot.Mot.Length;
			
			Mass = new Vector<Double>();
			CoM = new Vector<Double>();
			for (double M : Mot.Mot.Mass) {
				Mass.add(M + rMass);
				CoM.add((rMoment + M * dist) / (rMass + M));
			}
			
			
			Ixx = new Vector<Double>();
			Iyy = new Vector<Double>();
			Izz = new Vector<Double>();
			TDC = new Vector<Double>();
			
			for (int i = 0; i < Time.size(); i++) {
				double dr = Math.abs(CoM.get(i) - rCoM);
				double dm = Math.abs(dist - CoM.get(i));
				
				Ixx.add(rIx + rMass * Math.pow(dr, 2.0) + Mot.Mot.Ixx.get(i) + Mot.Mot.Mass.get(i) * Math.pow(dm, 2.0));
				Iyy.add(rIy + rMass * Math.pow(dr, 2.0) + Mot.Mot.Iyy.get(i) + Mot.Mot.Mass.get(i) * Math.pow(dm, 2.0));
				Izz.add(rIz + Mot.Mot.Izz.get(i));
				
				double lcn = Math.abs(NozX - CoM.get(i));
				double lcc = Math.abs(dist - CoM.get(i));
				
				/*double a1 = Mot.Mot.MassFlow.get(i);
				double a2 = Math.pow(lcn, 2);
				double a3 = Math.pow(lcc, 2);
				double a4 = a1*(a2-a3);*/
				TDC.add(Mot.Mot.MassFlow.get(i) * Math.pow((lcn - lcc), 2.0));
				//TDC.add(a4);
			}
		} else {
			Time = new Vector<Double>();
			Time.add(0.0);
			
			Thrust = new Vector<Double>();
			Thrust.add(0.0);
			
			Mass = new Vector<Double>();
			Mass.add(rMass);
			
			CoM = new Vector<Double>();
			CoM.add(rCoM);
			
			Ixx = new Vector<Double>();
			Ixx.add(rIx);
			
			Iyy = new Vector<Double>();
			Iyy.add(rIy);
			
			Izz = new Vector<Double>();
			Izz.add(rIz);
			
			TDC = new Vector<Double>();
			TDC.add(0.0);
			
			
		}
		
	}
	
	private void Intab2Dat() {
		Vector<RockPartsData> BodyParts = RocD.ReturnBodyParts();
		
		double pRd = 0; // max diameter
		double pRfd = 0;// reference diameter
		double pRTL = 0;// Total rocket length
		double pBd = 0;// Base diameter
		double pRBL = 0;// rocket body length
		double pBTL = 0;// boat tail length
		double pNCL = 0;// nose cone length
		
		Vector<FinsetData> Fins = new Vector<FinsetData>();
		
		for (RockPartsData Part : BodyParts) {
			if (Part.toString().contains("[Finset]")) {
				FinsetData fsd = (FinsetData) Part;
				Fins.add(fsd);
				double d1 = fsd.Xp + fsd.RootChord;
				double d2 = fsd.Xp + fsd.Sweep + fsd.Span;
				if (d1 > pRTL) {
					pRTL = d1;
				}
				if (d2 > pRTL) {
					pRTL = d2;
				}
				
			} else if (Part.toString().contains("[Body Tube]")) {
				BodyTubeData btd = (BodyTubeData) Part;
				double l = btd.Xp + btd.length;
				if (l > pRTL) {
					pRTL = l;
					pBd = btd.OD;
				}
				if (l > pRBL) {
					pRBL = l;
					pBTL = 0;
				}
				double d = btd.OD;
				if (d > pRd) {
					pRd = d;
				}
				
				
			} else if (Part.toString().contains("[Nose Cone]")) {
				NoseConeData ncd = (NoseConeData) Part;
				double l = ncd.Xp + ncd.Ln;
				if (l > pRTL) {
					pRTL = l;
					pBd = ncd.Dn;
				}
				if (l > pRBL) {
					pRBL = l;
					pBTL = 0;
				}
				double d = ncd.Dn;
				if (d > pRd) {
					pRd = d;
				}
				if (ncd.Xp == 0) {
					pRfd = d;
					pNCL = ncd.Ln;
				}
				
			} else if (Part.toString().contains("[Conic Trans]")) {
				ConicTransData ctd = (ConicTransData) Part;
				double l = ctd.Xp + ctd.length;
				if (l > pRTL) {
					pRTL = l;
					pBd = ctd.DD;
				}
				if (l > pRBL) {
					pRBL = l;
					pBTL = ctd.length;
				}
				double d1 = ctd.DD;
				double d2 = ctd.UD;
				if (d1 > pRd) {
					pRd = d1;
				}
				if (d2 > pRd) {
					pRd = d2;
				}
			}
		}
		
		///*Add length and area*******************
		Length = pRTL;
		Xarea = Math.PI * Math.pow(pRd, 2.0) / 4.0;
		///***************************************
		
		///*GetMaxArea as RefArea.
		for (RockPartsData Part : BodyParts) {
			if (Part.toString().contains("[Conic Trans]")) {
				ConicTransData TheTrans = (ConicTransData) Part;
				TheTrans.Dr = pRd;
				TheTrans.BarrowmanConic();
				
			} else if (Part.toString().contains("[Finset]")) {
				FinsetData TheFins = (FinsetData) Part;
				TheFins.RefDiam = pRd;
				TheFins.BarrowmanFinset();
			}
		}
		
		
		int i = 0;
		for (double rRe : Re) {
			//*Viscous Body drag***************************
			double bRe = rRe * pRBL / pRTL;
			double CFfb = ViscousDrag(bRe, Rec);
			//*********************************************
			//*Calculate Body Drag*************************
			double t1 = 60 / Math.pow((pRTL / pRd), 3.0);
			double t2 = 0.0025 * (pRBL - pNCL - pBTL) / pRd;
			double t3 = 2.7 * pNCL / pRd;
			double t4 = 4.0 * (pRBL - pNCL - pBTL) / pRd;
			double t5 = 2 * (1 - (pBd / pRd)) * pBTL / pRd;
			
			double CDfb = (1 + t1 + t2) * (t3 + t4 + t5) * CFfb;
			//*********************************************
			
			//*Calculate Base Drag**************************
			double CDb = 0.029 * Math.pow((pBd / pRd), 3.0) / Math.sqrt(CDfb);
			//**********************************************
			
			
			//*Calculate Fin Drag***************************
			
			double CDf = 0;
			double CDi = 0;
			
			for (FinsetData f : Fins) {
				double fmc = (f.RootChord + f.TipChord) / 2;
				double Afe = fmc * f.Span;
				double Afp = Afe + 0.5 * f.BodyDiam * f.RootChord;
				double fRe = rRe * fmc / pRTL;
				double Cff = ViscousDrag(fRe, Rec);
				
				double ft1 = (1 + 2 * f.Thickness / fmc);
				double ft2a = 4 * f.NoOfFins * (Afp);
				double ft2b = 4 * f.NoOfFins * (Afp - Afe);
				double ft3 = Math.PI * Math.pow(f.BodyDiam, 2.0);
				
				CDf += 2 * Cff * ft1 * ft2a / ft3;
				CDi += 2 * Cff * ft1 * ft2b / ft3;
				
				
				
			}
			//**********************************************
			
			CD[0][i] = CDfb + CDb + CDf + CDi;//Populate the zero angle of attack line of the CD table
			if (CD[0][i] > 0.9) {
				CD[0][i] = 0.9;
			}
			i++;
		}
		
		//*Fill in the alpha drag***********************************
		
		//Initialize vector of terms for later ;)******
		Vector<Double> CfaT = new Vector<Double>();
		//*********************************************
		
		for (FinsetData f : Fins) {
			double fmc = (f.RootChord + f.TipChord) / 2;
			double Afe = fmc * f.Span;
			double Afp = Afe + 0.5 * f.BodyDiam * f.RootChord;
			double fea = Afe * f.NoOfFins;
			double fpa = Afp * f.NoOfFins;
			double Rsec = f.BodyDiam / (2 * f.Span + f.BodyDiam);
			double kfb = 0.8065 * Math.pow(Rsec, 2.0) + 1.1553 * Rsec;
			double kbf = 0.1935 * Math.pow(Rsec, 2.0) + 0.8174 * Rsec + 1;
			
			double cft1 = (1.2 * fpa * 4) / (Math.PI * Math.pow(f.BodyDiam, 2.0));
			double cft2 = 3.12 * (kfb + kbf - 1);
			double cft3 = (fea * 4) / (Math.PI * Math.pow(f.BodyDiam, 2.0));
			
			CfaT.add(cft1 + cft2 * cft3);
			
		}
		
		
		double Srat = pRBL / pRd;
		
		double eta = -0.0002 * Math.pow(Srat, 2.0) + 0.0174 * Srat + 0.5332;
		double delta = 0.0001 * Math.pow(Srat, 3.0) - 0.005 * Math.pow(Srat, 2.0) + 0.0815 * Srat + 0.5298;
		
		double cdat1 = (3.6 * eta * (1.36 * pRBL - 0.55 * pNCL)) / (Math.PI * pRd);
		
		i = 0;
		for (double alpha : AoA) {
			if (alpha != 0.0) {
				double CDba = 2 * delta * Math.pow(alpha, 2.0) + cdat1 * Math.pow(alpha, 3.0);
				double CDfa = 0;
				
				for (double cfat1 : CfaT) {
					CDfa += Math.pow(alpha, 2.0) * cfat1;
				}
				
				for (int j = 0; j < Re.size(); j++) {
					CD[i][j] = CD[0][j] + CDba + CDfa;
					if (CD[i][j] > 0.9) {
						CD[i][j] = 0.9;
					}
				}
				
			}
			i++;
		}
		
	}
	
	private void Intab3Dat() {
		Vector<RockPartsData> BodyParts = RocD.ReturnBodyParts();
		
		double pCN = 0;
		double nXcp = 0;
		
		for (RockPartsData Part : BodyParts) {
			pCN += Part.CN;
			nXcp += Part.CN * (Part.Xcp + Part.Xp);
		}
		
		Cn = new Vector<Double>();
		Cp = new Vector<Double>();
		
		Cn.add(pCN);
		// prevent division by zero (ends up as NaN and throws an error in the c++ code)
		if (pCN < .0001) {
			Cp.add(0.0);
		} else {
			Cp.add(nXcp / pCN);
		}
	}
	
	private double ViscousDrag(double re, double cre) {
		double t1 = 1.328 / Math.sqrt(re);
		double t2 = 0.074 / Math.pow(re, 0.2);
		double B = cre * (t2 - t1);
		
		double temp = 0;
		if (re <= cre) {
			temp = t1;
		} else {
			temp = t2 - B / re;
		}
		return (temp);
	}
	
	private void ParachuteData() {
		Vector<ParachuteData> ScrambledP = RocD.ReturnParachutes();
		SwitchAlt = new Vector<Double>();
		CDA = new Vector<Double>();
		
		
		if (!ScrambledP.isEmpty()) {
			Vector<ParachuteData> OrderedP = new Vector<ParachuteData>();
			
			int passes = ScrambledP.size();
			
			for (int i = 0; i < passes; i++) {
				ParachuteData TempP = ScrambledP.firstElement();
				for (ParachuteData chute : ScrambledP) {
					if (TempP.apogee && !chute.apogee) {
						TempP = chute;
					} else if (!chute.apogee && !TempP.apogee && chute.Altitude < TempP.Altitude) {
						TempP = chute;
					} else {
						//nothing
					}
				}
				ScrambledP.remove(TempP);
				OrderedP.add(TempP);
			}
			
			/*
			 * 
			 */
			for (int i = 0; i < OrderedP.size(); i++) {
				/*
				if (i == 0) {
					SwitchAlt.add(0.0);
				} else {
					SwitchAlt.add(OrderedP.elementAt(i - 1).Altitude);
				}
				*/
				SwitchAlt.add(OrderedP.elementAt(i).Altitude);
				CDA.add(OrderedP.elementAt(i).DragCoefficient * OrderedP.elementAt(i).Area);
			}
		} else {
			
			//if no parachutes are found add zeros as place holders
			SwitchAlt.add(0.0);
			CDA.add(0.007);
			
		}
	}
	
	private void Intab4Dat(AtmosphereData AD) {
		Altitude = AD.Altitude;
		Xwind = AD.Xwind;
		Ywind = AD.Ywind;
		Zwind = AD.Zwind;
		rho = AD.rho;
		Theta = AD.Theta;
	}
	
	
	
}
