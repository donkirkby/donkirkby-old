package com.google.code.donkirkby;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GridPainter {
	public void paintGrid(Graphics2D g2d, Rectangle bounds)
	{
		Color oldColor = g2d.getColor();
		g2d.setColor(Color.decode("#8080FF"));
		int x1, y1, x2, y2;
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		// upper left - lower right
		x1 = bounds.x;
		y1 = bounds.y;
		x2 = x1 + bounds.width;
		y2 = y1 + bounds.height;
		g2d.drawLine(x1, y1, x2, y2);
		
		// lower left - upper right
		x1 += bounds.width;
		x2 -= bounds.width;
		g2d.drawLine(x1, y1, x2, y2);
		
		// vertical centre
		x1 = x2 = bounds.x + (bounds.width / 2);
		g2d.drawLine(x1, y1, x2, y2);
		
		// horizontal centre
		x1 = bounds.x;
		x2 = x1 + bounds.width;
		y1 = y2 = bounds.y + (bounds.height / 2);
		g2d.drawLine(x1, y1, x2, y2);
		
		g2d.setColor(oldColor);
	}
}
