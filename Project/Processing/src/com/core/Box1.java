package com.core;

import processing.core.PApplet;
import processing.core.PVector;

public class Box1 extends Shapes {
	
	float startingY = -10000;
	float maxY = 10000;
	float toggleFill = 0;
	
	public Box1(SoundScape scape, PVector startingPos) {
		super(scape);
		position.x = scape.random(startingPos.x, startingPos.y);
		position.y = scape.random(startingY, startingY / 2);
		position.z = scape.random(-scape.width, scape.h);
		
		rotation.x = scape.random(0, 1);
	    rotation.y = scape.random(0, 1);
	    rotation.z = scape.random(0, 1);
	    
	    toggleFill = scape.random(0, 1);
	}

	@Override
	public void display(int fillColor, int strokeColor, PVector xRange, PVector zRange) {
		scape.pushMatrix();	// Start matrix
		
	// We start a matrix to run translate or rotate and not effect the camera
		scape.translate(position.x, position.y, position.z);
		float size = 0;
		if (scape.song.isPlaying()) {
			scape.rotateX(rotationSum.x);
			scape.rotateY(rotationSum.y);
			scape.rotateZ(rotationSum.z);
			size = (75 + PApplet.map(scape.lows, 0, 1000, 0, 75)) * 2.5f;
		}
		
		if (toggleFill > 0.7) {
			scape.fill(fillColor,255);
			scape.noStroke();
		} else {
			scape.noFill();
			scape.stroke(strokeColor,255);
		}
		scape.box(size);
		
		scape.popMatrix();	// End matrix for shape
		
		position.y += (1+(scape.intensity/3.5f)+(scape.bandsComb/150));
		if (position.y >= maxY) {
			position.x = scape.random(xRange.x, xRange.y);
			position.y = scape.random(startingY/2,startingY+(startingY/2));
			position.z = scape.random(zRange.x, zRange.y);
		}
	}

	@Override
	public void update() {
		rotationSum.x += scape.intensity*(rotation.x/200);
		rotationSum.y += scape.intensity*(rotation.y/200);
		rotationSum.z += scape.intensity*(rotation.z/200);
	}

}
