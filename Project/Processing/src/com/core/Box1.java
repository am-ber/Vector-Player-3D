package com.core;

import processing.core.PApplet;
import processing.core.PVector;

public class Box1 extends Shapes {
	
	float startingY = -5000;
	float maxY = 5000+scape.h;
	float toggleFill = 0;
	float intensity = 0, alpha = 0;
	PVector xRange, zRange;
	
	public Box1(SoundScape scape, PVector startingPos) {
		super(scape);
		xRange = startingPos;
		position.x = scape.random(startingPos.x, startingPos.y);
		position.y = scape.random(startingY, maxY);
		position.z = scape.random(-scape.width, scape.h);
		zRange = new PVector(-scape.width, scape.h);
		
		rotation.x = scape.random(0, 1);
	    rotation.y = scape.random(0, 1);
	    rotation.z = scape.random(0, 1);
	    
	    toggleFill = scape.random(0, 1);
	}

	public void display(PVector fillColor, PVector strokeColor) {
		scape.pushMatrix();	// Start matrix
		
	// We start a matrix to run translate or rotate and not effect the camera
		scape.translate(position.x, position.y, position.z);
		float size = 0;
		if (scape.isThereSound) {
			scape.rotateX(rotationSum.x);
			scape.rotateY(rotationSum.y);
			scape.rotateZ(rotationSum.z);
			if (intensity < 0.9f)
				intensity += 0.01f;
		}
		if (position.y < 0)
			alpha = PApplet.map(position.y, startingY, 0, 0, 255) * intensity;
		else
			alpha = PApplet.map(position.y, maxY, 0, 0, 255) * intensity;
		if (scape.currentColorMode == PApplet.RGB) {
			if (toggleFill > 0.7) {
					size = (75 + PApplet.map(scape.subs, 0, 800, 0, 75)) * 2.5f;
					scape.fill(fillColor.x,fillColor.y,fillColor.z,alpha);
					scape.noStroke();
			} else {
				size = (75 + PApplet.map(scape.highs, 0, 800, 0, 75)) * 2.5f;
				scape.noFill();
				scape.stroke(strokeColor.x,strokeColor.y,strokeColor.z,alpha);
			}
		} else {
			if (toggleFill > 0.7) {
				size = (75 + PApplet.map(scape.subs, 0, 800, 0, 75)) * 2.5f;
				scape.fill(fillColor.x,fillColor.y,fillColor.z);
				scape.noStroke();
			} else {
				size = (75 + PApplet.map(scape.highs, 0, 800, 0, 75)) * 2.5f;
				scape.noFill();
				scape.stroke(strokeColor.x,strokeColor.y,strokeColor.z);
			}
		}
		scape.box(size);
		
		scape.popMatrix();	// End matrix for shape
		
		position.y += (1+(scape.intensity/3.5f)+(scape.bandsComb/150));
		if (position.y >= maxY) {
			position.x = scape.random(xRange.x, xRange.y);
			position.y = scape.random(startingY,startingY+(startingY/2));
			position.z = scape.random(zRange.x, zRange.y);
		}
	}

	public void update() {
		rotationSum.x += (scape.intensity / 1.5)*(rotation.x/200);
		rotationSum.y += (scape.intensity / 1.5)*(rotation.y/200);
		rotationSum.z += (scape.intensity / 1.5)*(rotation.z/200);
	}

}
