package com.google.code.donkirkby;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class DummyRandom extends Random {
	private ArrayList<Double> nextDoubles = new ArrayList<Double>();
	private Double defaultDouble = null;
	
	@Override
	public double nextDouble() {
		if (nextDoubles.size() > 0)
		{
			return nextDoubles.remove(0);
		}
		if (defaultDouble != null)
		{
			return defaultDouble;
		}
		throw new IllegalStateException("No double values available.");
	}
	
//		public void addDouble(double d){
//			nextDoubles.add(d);
//		}
	
	public void setDefaultDouble(Double d){
		defaultDouble = d;
	}
}
