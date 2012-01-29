package com.google.code.donkirkby;

import java.util.Random;

public class TrianglePath extends Path {
	private Point fulcrum, entryVertex, exitVertex, entry, exit;
	private Random random;

	/**
	 * Initialise a new instance.
	 * @param fulcrum the vertex that is adjacent to both the entry point
	 * 		and the exit point.
	 * @param entryVertex the vertex that is only adjacent to the entry point.
	 * @param exitVertex the vertex that is only adjacent to the exit point.
	 * @param entry the point where the path enters.
	 * @param exit the point where the path exits.
	 * @param random used to decide where to split.
	 */
	public TrianglePath(
			Point fulcrum,
			Point entryVertex,
			Point exitVertex,
			Point entry,
			Point exit,
			Random random) {
		this.fulcrum = fulcrum;
		this.entryVertex = entryVertex;
		this.exitVertex = exitVertex;
		this.entry = entry;
		this.exit = exit;
		this.random = random;
	}

	@Override
	public double calculateWidthOfFullPath(double totalIntensity,
			double totalArea) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateOptimalWidth(Image image) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTotalLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path[] split() {
		double entryDistanceSquared = fulcrum.distanceSquaredTo(entryVertex);
		double exitDistanceSquared = fulcrum.distanceSquaredTo(exitVertex);
		double oppositeDistanceSquared = entryVertex.distanceSquaredTo(exitVertex);
		Point peak, base1, base2;
		if (entryDistanceSquared > exitDistanceSquared)
		{
			if (entryDistanceSquared > oppositeDistanceSquared)
			{
				peak = exitVertex;
				base1 = fulcrum;
				base2 = entryVertex;
			}
			else
			{
				peak = fulcrum;
				base1 = entryVertex;
				base2 = exitVertex;
			}
		}
		else
		{
			if (exitDistanceSquared > oppositeDistanceSquared)
			{
				peak = entryVertex;
				base1 = exitVertex;
				base2 = fulcrum;
			}
			else
			{
				peak = fulcrum;
				base1 = entryVertex;
				base2 = exitVertex;
			}
		}
		Point splitBase = base1.moveToward(base2, 0.25 + 0.5*random.nextDouble());
		Point splitPath = peak.moveToward(splitBase, 0.25 + 0.5*random.nextDouble());
		return new Path[] { 
				new TrianglePath(peak, base1, splitBase, entry, splitPath, random),
				new TrianglePath(peak, splitBase, base2, splitPath, exit, random)
		};
	}

	@Override
	public String toString() {
		return String.format(
				"TrianglePath(%s, %s, %s, %s->%s",
				fulcrum,
				entryVertex,
				exitVertex,
				entry,
				exit);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
		    return true;
		}
		if (!(obj instanceof TrianglePath)) 
		{
			return false;
		}
		TrianglePath other = (TrianglePath)obj;
		return 
			this.fulcrum.equals(other.fulcrum) &&
			this.entryVertex.equals(other.entryVertex) &&
			this.exitVertex.equals(other.exitVertex) &&
			this.entry.equals(other.entry) &&
			this.exit.equals(other.exit);
	}
}
