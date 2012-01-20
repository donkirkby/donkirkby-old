package com.google.code.donkirkby;

public abstract class Path {

	protected Path next;

	public abstract double calculateWidthOfFullPath(double totalIntensity, double totalArea);

	public abstract double calculateOptimalWidth(Image image);

	public abstract double getTotalLength();

	public abstract double getLength();

	public abstract double[] getCoordinates();

	public abstract Path[] split();

	protected Path previous;

	public Path() {
		super();
	}

	public Path getNext() {
		return next;
	}

	public Path getPrevious() {
		return previous;
	}

	public void append(Path other) {
		if (other.next != other)
		{
			throw new IllegalStateException(
					"Path cannot be appended if it is already attached.");
		}
		other.next = this.next;
		this.next.previous = other;
		this.next = other;
		other.previous = this;
	}

	public void remove() {
		next.previous = previous;
		previous.next = next;
		previous = next = this;
	}

}