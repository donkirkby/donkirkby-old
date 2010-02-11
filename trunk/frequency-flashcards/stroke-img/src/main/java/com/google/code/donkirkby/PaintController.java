package com.google.code.donkirkby;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaintController {
	private int width;
	private int height;
	private int borderSize = 2;
	private String strokeOrderData;
	
	public void paint(Graphics2D g2d) {
		Rectangle bounds = new Rectangle(borderSize, borderSize, 300, 300);
		Color oldColor = g2d.getColor();
		List<CharacterSegment> segments = 
			CharacterStrokeReader.read(strokeOrderData);
		CharacterStroke stroke = new CharacterStroke();
		List<CharacterStroke> strokes = new ArrayList<CharacterStroke>();
		strokes.add(stroke);
		
		for (CharacterSegment characterSegment : segments) {
			if (!stroke.addSegment(characterSegment))
			{
				stroke = new CharacterStroke();
				strokes.add(stroke);
				stroke.addSegment(characterSegment);
			}
		}
		
		Collections.reverse(strokes);

		GridLayout gridLayout = new GridLayout();
		int items = strokes.size()+1;
		gridLayout.setItems(items);
		GridPainter gridPainter = new GridPainter();
		for (int i = 0; i < items; i++)
		{
			int row = i / gridLayout.getColumns();
			int column = i % gridLayout.getColumns();
			bounds.x = bounds.width * column + borderSize;
			bounds.y = bounds.height * row + borderSize;
			gridPainter.paintGrid(g2d, bounds);
			
			int strokeNum = strokes.size();
			for (CharacterStroke stroke2 : strokes)
			{
				for (CharacterSegment segment : stroke2.getSegments()) {
					if (i == 0)
					{
						g2d.setColor(Color.BLACK);
					}else if (strokeNum <= i)
					{
						g2d.setColor(Color.DARK_GRAY);
					}else
					{
						g2d.setColor(Color.LIGHT_GRAY);
					}
					Polygon shape = segment.getShape();
					shape.translate(bounds.x, bounds.y);
					g2d.fill(shape);
					g2d.draw(shape);
					shape.translate(-bounds.x, -bounds.y);
				}
				
				strokeNum--;
			}
			if (i > 0) {
				int strokeIndex = strokes.size() - i;
				List<CharacterSegment> strokeSegments = 
					strokes.get(strokeIndex).getSegments();
				ArrowBuilder arrowBuilder = new ArrowBuilder(); 
				for (CharacterSegment segment : strokeSegments) {
					arrowBuilder.addSegment(
							bounds.x + segment.getStartX(),
							bounds.y + segment.getStartY(),
							bounds.x + segment.getEndX(),
							bounds.y + segment.getEndY());
				}
				Stroke oldStroke = g2d.getStroke();
				g2d.setColor(Color.RED);
				g2d.setStroke(new BasicStroke(4));
				g2d.draw(arrowBuilder.getShape());
				g2d.setStroke(oldStroke);
				
				g2d.setColor(Color.GREEN);
				g2d.fillOval(
						bounds.x + strokeSegments.get(0).getStartX() - 7,
						bounds.y + strokeSegments.get(0).getStartY() - 7, 
						14,
						14);

			}
			//			Path2D path = new Path2D.Double();
			//			path.moveTo(100, 100);
			//			path.lineTo(185, 100);
			//			path.curveTo(200, 100, 200, 100, 200, 115);
			//			path.lineTo(200, 200);
			//			g2d.draw(path);
		}
		
		g2d.setColor(oldColor);
		
		width = bounds.width * gridLayout.getColumns() + 2 * borderSize;
		height = bounds.height * gridLayout.getRows() + 2 * borderSize;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getBorderSize() {
		return borderSize;
	}
	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}
	public String getStrokeOrderData() {
		return strokeOrderData;
	}
	public void setStrokeOrderData(String strokeOrderData) {
		this.strokeOrderData = strokeOrderData;
	}

}
