package com.google.code.donkirkby;

import java.awt.Polygon;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Copied from ZDT project: http://zdt.sourceforge.net Copyright (c) 2004-2006
 * Chris Fong and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Written by Erik Peterson Reversed Engineered from "Chinese Font
 * Player" by Zhou Qiong & Dong Dong February 12, 2005 Permission is given to
 * used this code in any way as long as this notice is preserved
 * 
 * Converted to SWT by Chris Fong
 * 
 */
public class CharacterSegment {
	public static int EAST = 1;
	public static int SOUTH_EAST = 2;
	public static int SOUTH = 3;
	public static int SOUTH_WEST = 4;
	public static int WEST = 5;
	public static int NORTH_WEST = 6;
	public static int NORTH = 7;
	public static int NORTH_EAST = 8;
	
	private static Map<Integer, Vector<Double>> directionVectors;

	private Polygon shape; // the polygon bounding this stroke
	private boolean radical; // true for radical, false otherwise
	private boolean pause; // true if pause after writing stroke
	private int direction; // direction to draw stroke with
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private boolean isStartFound = false;
	
	static {
		directionVectors = new HashMap<Integer, Vector<Double>>();
		directionVectors.put(EAST, createVector(1, 0));
		directionVectors.put(SOUTH_EAST, createVector(1, 1));
		directionVectors.put(SOUTH, createVector(0, 1));
		directionVectors.put(SOUTH_WEST, createVector(-1, 1));
		directionVectors.put(WEST, createVector(-1, 0));
		directionVectors.put(NORTH_WEST, createVector(-1, -1));
		directionVectors.put(NORTH, createVector(0, -1));
		directionVectors.put(NORTH_EAST, createVector(1, -1));
	}
	
	private static Vector<Double> createVector(double x, double y){
		Vector<Double> v = new Vector<Double>(2);
		double length = Math.sqrt(x*x + y*y);
		double normalizedX = x / length;
		double normalizedY = y / length;
		v.add(normalizedX);
		v.add(normalizedY);
		
		return v;
	}

	/**
	 * Stroke data is formatted as follows: #<direction><pause><radical>:<x1>,<y1>;<x2>,<y2>;...
	 * Where: direction is the direction of the stroke. 1 is to the right and 2
	 * through 8 go clockwise around the compass. If x marks the starting
	 * position, here are the eight directions: 678 5x1 432 pause is P if this
	 * is the last piece of a logical stroke, so an animation should pause.
	 * Anything else means false. radical is R if this is a piece of the
	 * character's radical. Anything else means false. x1, y1, etc. are the
	 * coordinates along the polygon that makes up this stroke.
	 * 
	 * @param strokedata
	 */
	public CharacterSegment(String strokedata) {
		shape = new Polygon();
		int i, x, y;

		for (i = 0; i < strokedata.length(); i++) {
			if (strokedata.charAt(i) == '#') {
				direction = Integer
						.parseInt(strokedata.substring(i + 1, i + 2));
				if (strokedata.charAt(i + 2) == 'P') {
					pause = true;
				} else {
					pause = false;
				}
				if (strokedata.charAt(i + 3) == 'R') {
					radical = true;
				} else {
					radical = false;
				}
				i += 5;
				break;
			}
		}
		StringTokenizer st = new StringTokenizer(strokedata.substring(i,
				strokedata.length()), " \n\r\t;,");
		while (st.hasMoreTokens()) {
			x = Integer.parseInt(st.nextToken());
			y = Integer.parseInt(st.nextToken());
			// System.out.println("Point " + x + " " + y);
			shape.addPoint(x, y);
		}
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Polygon getShape() {
		return shape;
	}

	public boolean isRadical() {
		return radical;
	}

	public boolean isPause() {
		return pause;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public int getStartY() {
		checkStart(); 
		return startY;
	}

	public int getStartX() {
		checkStart(); 
		return startX;
	}

	private void checkStart() {
		if (isStartFound) {
			return;
		}
		double minDistance = Double.MAX_VALUE;
		int indexOfMinDistance = -1;
		double maxDistance = -Double.MAX_VALUE;
		int indexOfMaxDistance = -1;
		Vector<Double> directionVector = directionVectors.get(direction);
		double directionX = directionVector.get(0);
		double directionY = directionVector.get(1);
		for (int i = 0; i < shape.npoints; i++) {
			double distanceInDirection = 
				directionX * shape.xpoints[i]
				+ directionY * shape.ypoints[i];
			if (distanceInDirection < minDistance) {
				minDistance = distanceInDirection;
				indexOfMinDistance = i;
			}
			if (distanceInDirection > maxDistance) {
				maxDistance = distanceInDirection;
				indexOfMaxDistance = i;
			}
		}
		startX = shape.xpoints[indexOfMinDistance];
		startY = shape.ypoints[indexOfMinDistance];
		endX = shape.xpoints[indexOfMaxDistance];
		endY = shape.ypoints[indexOfMaxDistance];
		isStartFound = true;
	}

	public int getEndX() {
		checkStart(); 
		return endX;
	}

	public int getEndY() {
		checkStart(); 
		return endY;
	}

}
