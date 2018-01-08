package com.gui;

import processing.core.PApplet;

public class Slider {
	
	private PApplet ap;
	private float xPos, yPos, length, width;
	
	public Slider(PApplet parent, float xPos, float yPos, float length, float width) {
		ap = parent;
		
		this.xPos = xPos; this.yPos = yPos;
		this.length = length; this.width = width;
	}
	
	/** <b>draw()</b>
	 * This should be ran in the draw loop.
	 */
	public void draw() {
		
	}
	
	/** <b>setSlider</b>
	 * Use this in a mouse pressed method and pass the mouse X through.
	 */
	public void setSlider(float mouseX) {
		
	}
}
