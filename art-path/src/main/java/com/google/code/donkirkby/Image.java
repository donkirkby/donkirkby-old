package com.google.code.donkirkby;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Image {
	private int width, height;
	private double maxValue;
	private int[] pixels;

	public void load(BufferedReader reader) {
		String header = "";
		Pattern pattern = Pattern.compile(
				"P2\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\S.*)", 
				Pattern.DOTALL);
		Pattern spacePattern = Pattern.compile("\\s+");
		String line = null;
		try {
			boolean isDone = false;
			while ( ! isDone)
			{
				line = reader.readLine();
				if ( ! line.startsWith("#"))
				{
					header += line;
					header += "\n";
				}
				Matcher matcher = pattern.matcher(header);
				if (matcher.matches())
				{
					isDone = true;
					width = Integer.parseInt(matcher.group(1));
					height = Integer.parseInt(matcher.group(2));
					maxValue = Double.parseDouble(matcher.group(3));
					line = matcher.group(4);
				}
			}
			
			pixels = new int[getWidth() * getHeight()];
			int i = 0;
			while (line != null)
			{
				String[] tokens = spacePattern.split(line);
				for (String token : tokens) {
					pixels[i++] = Integer.parseInt(token);
				}
				line = reader.readLine();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public double getPixel(int x, int y) {
		return pixels[x + y*getWidth()] / maxValue;
	}

	/**
	 * Calculate the intensity of the pixels in the given cell boundaries.
	 * @param x1 the left boundary
	 * @param y1 the top boundary.
	 * @param x2 the right boundary.
	 * @param y2 the bottom boundary.
	 * @return 0 for all black, 1 for all white, otherwise an average of all
	 * pixels where x1 <= x < x2 and y1 <= y < y2.
	 */
	public double getSquareIntensity(int x1, int y1, int x2, int y2) {
		double total = 0;
		for (int x = x1; x < x2; x++)
		{
			for (int y = y1; y < y2; y++)
			{
				total += getPixel(x, y);
			}
		}
		return total / (x2 - x1) / (y2 - y1);
	}
	
	/** Calculate the average intensity and the area of the triangle.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return [intensity, pixelCount]
	 */
	public double[] measureTriangle(
			double x1,
			double y1,
			double x2,
			double y2,
			double x3,
			double y3) {
		Polygon triangle = new Polygon(
				new int[] { (int) x1, (int) x2, (int) x3 },
				new int[] { (int) y1, (int) y1, (int) y3 },
				3);
		Rectangle bounds = triangle.getBounds();
		int area = 0;
		double total = 0;
		for (int x = bounds.x; x <= bounds.x + bounds.width; x++) {
			for (int y = bounds.y; y <= bounds.y + bounds.height; y++) {
				if (triangle.contains(x, y))
				{
					total += getPixel(x,y);
					area++;
				}
			}
		}
		return new double[] {total / area, area};
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
