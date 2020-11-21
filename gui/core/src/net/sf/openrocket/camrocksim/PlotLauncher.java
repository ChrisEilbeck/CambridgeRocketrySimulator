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

%## Cylinder Dialog.java

%## Author: S.Box
%## Created: 2011-10-27
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

/**
 *
 * @author simon
 */
public class PlotLauncher {
	//class memberts
	public File FilePath;
	private File DataFile;
	boolean isWindows;
	
	//class constructor
	public PlotLauncher(String path) {
		isWindows = false;
		// File.separator + "Plotter"
		FilePath = new File(path + File.separator + "FlightPlotter.py");
		if (!FilePath.exists()) {
			FilePath = new File(path + File.separator + "FlightPlotter.exe");
			isWindows = true;
		}
	}
	
	public void MakePlots(File dF) {
		DataFile = dF;
		try {
			Thread T = new Thread(new LaunchFigures());
			T.start();
			
		} catch (Exception e) {
			
		}
	}
	
	private class LaunchFigures implements Runnable {
		
		public void run() {
			String OutputRecord = "";
			try {
				Runtime rtime = Runtime.getRuntime();
				String[] Command = new String[] { "" };
				if (isWindows) {
					Command = new String[] { FilePath.getPath(), "-f", DataFile.getPath() };
					
				} else {
					Command = new String[] { "python", FilePath.getPath(), "-f", DataFile.getPath() };
				}
				Process Pr2 = rtime.exec(Command);
				BufferedReader errors2 = new BufferedReader(new InputStreamReader(Pr2.getErrorStream()));
				String line = null;
				while ((line = errors2.readLine()) != null) {
					OutputRecord += line;
					
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ("Figure generation failed\nJava Message: " + e.getMessage() + "\nPython Message: " + OutputRecord));
			}
		}
		
	}
	
}
