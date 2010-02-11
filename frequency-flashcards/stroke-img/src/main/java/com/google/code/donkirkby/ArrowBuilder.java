package com.google.code.donkirkby;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

public class ArrowBuilder {
	private Path2D path = new Double();

	public Shape getShape() {
		return path;
	}

	public void addSegment(int x1, int y1, int x2, int y2) {
		if (path.getCurrentPoint() == null)
		{
			path.moveTo(x1, y1);
		}
		path.lineTo(x2, y2);
	}
}
