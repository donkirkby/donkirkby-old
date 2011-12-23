package com.google.code.donkirkby;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 */
public class StrokeImageApp {
	private static Log log = LogFactory.getLog(StrokeImageApp.class);
	

	public static void main(String[] args) {
		log.info("Starting.");

		try {
			PaintWriter writer = new PaintWriter();
			Graphics2D g2d = writer.createGraphicsContext();
			Path2D path2d = new Double();
			Path path1 = new Path(new Cell(0, 0, 200, 200), Path.North, Path.East);
			Path path2 = new Path(new Cell(200, 0, 400, 200), Path.East, Path.South);
			Path path3 = new Path(new Cell(200, 200, 400, 400), Path.South, Path.West);
			Path path4 = new Path(new Cell(0, 200, 200, 400), Path.West, Path.North);
			path1.append(path2);
			path2.append(path3);
			path3.append(path4);
			Path start = path1;
			
			for (int i = 0; i < 5; i++)
			{
				start.getNext().split();
				start = start.getNext().getNext();
			}
			
			path2.split();

			Path path = path1;
			do
			{
				double [] coordinates = path.getCoordinates();
				if (path == path1)
				{
					path2d.moveTo(coordinates[0], coordinates[1]);
				}
				path2d.lineTo(coordinates[2], coordinates[3]);
				path2d.lineTo(coordinates[4], coordinates[5]);
				path = path.getNext();
			} while (path != path1);
			path2d.closePath();
			g2d.draw(path2d);
			writer.write("output/output.svg", "output/output.png");
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
	}
}
