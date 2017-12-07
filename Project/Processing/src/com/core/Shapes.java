package com.core;

import processing.core.PVector;

public abstract class Shapes {
	
	PVector position;
	PVector rotation;
	PVector rotationSum;
	
	SoundScape scape;
	
	public Shapes(SoundScape scape) {
		this.scape = scape;
		position = new PVector();
		rotation = new PVector();
		rotationSum = new PVector();
	}
	
	/** <b>run()</b></br>
	 * Use this to run update() and then display()
	 * @param fillColor
	 * @param strokeColor
	 * @param xRange
	 * @param zRange
	 * Where x = lowerBounds y = upperBounds
	 * z is not used in this.
	 */
	public void run(int fillColor, int strokeColor, PVector xRange, PVector zRange) {
		update();
		display(fillColor, strokeColor, xRange, zRange);
	}

	public abstract void display(int fillColor, int strokeColor, PVector xRange, PVector zRange);

	public abstract  void update();
}
