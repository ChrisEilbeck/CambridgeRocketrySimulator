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

%## TimeStamper.java

%## Author: S.Box
%## Created: 2011-10-27

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.openrocket.camrocksim;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sb4p07
 */
public class TimeStamper {
	Date TheDate;
	SimpleDateFormat parser;
	
	public TimeStamper() {
		parser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	}
	
	String DateTimeNow() {
		return (parser.format(new Date()));
	}
	
}
