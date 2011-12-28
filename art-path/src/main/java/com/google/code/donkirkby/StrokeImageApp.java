package com.google.code.donkirkby;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 
 */
public class StrokeImageApp {
	private static Log log = LogFactory.getLog(StrokeImageApp.class);
	

	public static void main(String[] args) {
		log.info("Starting.");

		try {
			PaintWriter writer = new PaintWriter();
			Image image = loadImage();
			float minWidth = (float) 1.25; // 148, 145, 112, 91, 76, 67
			float pathWidth;
			float reportedWidth = Float.MAX_VALUE;
			double totalIntensity = 
					image.getCell(0, 0, image.getWidth(), image.getHeight());
			double totalArea = image.getWidth() * image.getHeight();
			Path start = createStartPath(image.getWidth(), image.getHeight());
			
			do
			{
				pathWidth = calculatePathWidth(
						totalIntensity, 
						totalArea,
						start.getTotalLength());
				if (pathWidth > minWidth)
				{
					if (pathWidth < reportedWidth * 0.9)
					{
						reportedWidth = pathWidth;
						log.debug("width " + pathWidth + " length " + start.getTotalLength());
					}
					Path worstPath = start;
					double maxDiff = 0;
					Path path = start;
					do
					{
						Cell cell = path.getCell();
						double cellIntensity = image.getCell(
								(int)cell.getLeft(),
								(int)cell.getTop(),
								(int)cell.getRight(),
								(int)cell.getBottom());
						double cellArea = 
								(cell.getRight() - cell.getLeft()) *
								(cell.getBottom() - cell.getTop());
						double optimalPathWidth = calculatePathWidth(
								cellIntensity,
								cellArea,
								path.getLength());
						double diff = optimalPathWidth - pathWidth;
						if (diff > maxDiff)
						{
							worstPath = path;
							maxDiff = diff;
						}
						path = path.getNext();
					}while (path != start);
					if (worstPath == start)
					{
						start = start.getNext();
					}
					worstPath.split();
				}
			} while (pathWidth > minWidth);
			
			Path2D path2d = createPath2D(start, log);
			Graphics2D g2d = writer.createGraphicsContext(image.getWidth(), image.getHeight());
			g2d.setStroke(new BasicStroke((float) 0.1));
			g2d.draw(path2d);
			writer.write("output/output.svg", "output/output.png");
			log.info("Success");
		} catch (Exception e) {
			log.error("Failure", e);
			System.exit(-1);
		}
	}

	private static float calculatePathWidth(double intensity, double area,
			double pathLength) {
		float pathWidth;
		pathWidth = 
				(float) ((1 - intensity) * area / pathLength);
		return pathWidth;
	}

	private static Path2D createPath2D(Path startPath, Log log) {
		Path2D path2d = new Double();
		Path path = startPath;
		do
		{
			double [] coordinates = path.getCoordinates();
			if (path == startPath)
			{
				path2d.moveTo(coordinates[0], coordinates[1]);
			}
//			log.debug(coordinates[2] + ", " + coordinates[3]);
//			log.debug(coordinates[4] + ", " + coordinates[5]);
			path2d.lineTo(coordinates[2], coordinates[3]);
			path2d.lineTo(coordinates[4], coordinates[5]);
			path = path.getNext();
		} while (path != startPath);
		path2d.closePath();
		return path2d;
	}

	private static Image loadImage() {
		try
		{
			Image image = new Image();
			Resource templateResource = 
				new ClassPathResource("/balcony.pgm");
			InputStream inputStream = templateResource.getInputStream();
			BufferedReader reader = 
				new BufferedReader(new InputStreamReader(inputStream, "utf8"));
			try
			{
				image.load(reader);
			}
			finally
			{
				inputStream.close();
			}
			return image;
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private static Path createStartPath(double width, double height) {
		Path path1 = new Path(new Cell(0, 0, width/2, height/2), Path.North, Path.East);
		Path path2 = new Path(new Cell(width/2, 0, width, height/2), Path.East, Path.South);
		Path path3 = new Path(new Cell(width/2, height/2, width, height), Path.South, Path.West);
		Path path4 = new Path(new Cell(0, width/2, width/2, height), Path.West, Path.North);
		path1.append(path2);
		path2.append(path3);
		path3.append(path4);
		return path1;
	}
}
