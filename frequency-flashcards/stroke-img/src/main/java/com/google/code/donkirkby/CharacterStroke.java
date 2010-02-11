package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.List;

public class CharacterStroke {
	private List<CharacterSegment> segments = new ArrayList<CharacterSegment>();

	public List<CharacterSegment> getSegments() {
		return segments;
	}

	public boolean addSegment(CharacterSegment characterSegment) {
		if (!segments.isEmpty())
		{
			CharacterSegment previousSegment = segments.get(segments.size()-1);
			if (previousSegment.isPause())
			{
				return false;
			}
			int deltaX = 
				characterSegment.getStartX() - previousSegment.getEndX();
			int deltaY =
				characterSegment.getStartY() - previousSegment.getEndY();
			int squareOfDistance  = deltaX * deltaX + deltaY * deltaY;
			if (squareOfDistance > 1600)
			{
				//start of new segment > 40 pixels away from end of previous
				// 30 is too small for 21st most frequent character: hao
				return false;
			}
		}
		segments.add(characterSegment);
		return true;
	}
}
