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
public class ArtPathApp {
	private static Log log = LogFactory.getLog(ArtPathApp.class);
	

	public static void main(String[] args) {
		log.info("Starting.");

		try {
			PaintWriter writer = new PaintWriter();
			Image image = loadImage();
			double minWidth = (double) 10; // 148, 145, 112, 91, 76, 67
			double pathWidth;
			double reportedWidth = java.lang.Double.MAX_VALUE;
			double totalIntensity = 
					image.getSquareIntensity(0, 0, image.getWidth(), image.getHeight());
			double totalArea = image.getWidth() * image.getHeight();
			Path start;
			start = SquarePath.createStartPath(image.getWidth(), image.getHeight());
			
			do
			{
				pathWidth = start.calculateWidthOfFullPath(totalIntensity,
						totalArea);
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
						double optimalPathWidth = path.calculateOptimalWidth(
								image);
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
			for (int i = 2; i < coordinates.length; i += 2)
			{
				path2d.lineTo(coordinates[i], coordinates[i+1]);
			}
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
}
