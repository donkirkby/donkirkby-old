package com.google.code.donkirkby;

public abstract class Path {

	protected Path next;

	public abstract double calculateOptimalWidth(Image image);

	public abstract double getLength();

	public abstract double[] getCoordinates();

	public abstract Path[] split();

	protected Path previous;

	public Path() {
		this.next = this.previous = this;
	}

	public Path getNext() {
		return next;
	}

	public Path getPrevious() {
		return previous;
	}

	public Path append(Path other) {
		if (other.next != other)
		{
			throw new IllegalStateException(
					"Path cannot be appended if it is already attached.");
		}
		other.next = this.next;
		this.next.previous = other;
		this.next = other;
		other.previous = this;
		return other;
	}

	public void remove() {
		next.previous = previous;
		previous.next = next;
		previous = next = this;
	}

	public double calculateWidthOfFullPath(double totalIntensity, double totalArea) {
		return calculateWidth(totalIntensity, totalArea, getTotalLength());
	}

	protected double calculateWidth(double intensity, double area, double length) {
		return (1 - intensity) * area / length;
	}

	public double getTotalLength() {
		double totalLength = 0;
		Path path = this;
		do
		{
			totalLength += path.getLength();
			path = path.getNext();
		}while (path != this);
		return totalLength;
	}

}