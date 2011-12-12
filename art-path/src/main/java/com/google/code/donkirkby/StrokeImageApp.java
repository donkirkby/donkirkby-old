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
			Path2D path = new Double();
			path.moveTo(0, 0);
			path.lineTo(100, 100);
			path.lineTo(0, 100);
			path.lineTo(100, 0);
			path.closePath();
			g2d.draw(path);
			writer.write("output/output.svg", "output/output.png");
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
	}
}
