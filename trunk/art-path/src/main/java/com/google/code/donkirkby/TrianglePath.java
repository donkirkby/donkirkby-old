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
		if (oppositeDistanceSquared > exitDistanceSquared && 
				oppositeDistanceSquared > entryDistanceSquared)
		{
			Point splitBase = entryVertex.moveToward(
					exitVertex, 
					0.25 + 0.5*random.nextDouble());
			Point splitPath = fulcrum.moveToward(
					splitBase, 
					0.25 + 0.5*random.nextDouble());
			TrianglePath entryPath = new TrianglePath(
					fulcrum, 
					entryVertex, 
					splitBase, 
					entry, 
					splitPath, 
					random);
			TrianglePath exitPath = new TrianglePath(
					fulcrum, 
					splitBase, 
					exitVertex, 
					splitPath, 
					exit, 
					random);
			replaceWithChildren(entryPath, exitPath);
			
			return new Path[] { entryPath, exitPath };
		}
		if (exitDistanceSquared > entryDistanceSquared &&
				exitDistanceSquared > oppositeDistanceSquared)
		{
			Point splitExit = fulcrum.moveToward(
					exit, 
					0.25 + 0.5*random.nextDouble());
			Point splitPath = entryVertex.moveToward(
					splitExit, 
					0.25 + 0.5*random.nextDouble());
			TrianglePath entryPath = new TrianglePath(
					entryVertex, 
					fulcrum, 
					splitExit, 
					entry, 
					splitPath, 
					random);
			TrianglePath exitPath = new TrianglePath(
					splitExit, 
					entryVertex, 
					exitVertex, 
					splitPath, 
					exit, 
					random);
			replaceWithChildren(entryPath, exitPath);
			return new Path[] { entryPath, exitPath };
		}
		Point splitEntry = fulcrum.moveToward(
				entry, 
				0.25 + 0.5*random.nextDouble());
		Point splitPath = exitVertex.moveToward(
				splitEntry, 
				0.25 + 0.5*random.nextDouble());
		TrianglePath entryPath = new TrianglePath(
				splitEntry, 
				entryVertex, 
				exitVertex, 
				entry, 
				splitPath, 
				random);
		TrianglePath exitPath = new TrianglePath(
				exitVertex, 
				splitEntry, 
				fulcrum, 
				splitPath, 
				exit, 
				random);
		replaceWithChildren(entryPath, exitPath);
		return new Path[] { entryPath, exitPath };
	}

	private void replaceWithChildren(TrianglePath entryPath,
			TrianglePath exitPath) {
		getPrevious().append(entryPath);
		entryPath.append(exitPath);
		remove(); // remove this path from the chain.
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
