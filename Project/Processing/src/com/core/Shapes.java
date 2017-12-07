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
	
	public void run(int fillColor, int strokeColor) {
		update();
		display(fillColor, strokeColor);
	}

	public abstract void display(int fillColor, int strokeColor);

	public abstract  void update();
}
