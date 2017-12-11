package com.core;

import interfascia.*;
import processing.core.PApplet;

public class ControlWindow extends PApplet {

	SoundScape scape;
	GUIController guiController;
	IFButton btnAdjustColor;

	public ControlWindow(SoundScape parent) {
		this.scape = parent;
	}

	public void settings() {
		size(scape.width / 2, scape.height);
	}

	public void setup() {
		guiController = new GUIController(this);
		
		btnAdjustColor = new IFButton("Change Color mode",100,50,50,20);
		
		btnAdjustColor.addActionListener(this);
		
		guiController.add(btnAdjustColor);
	}
	public void draw() {
		background(10);
		
	}
	
	public void actionPerformed(GUIEvent e) {
		if (e.getSource() == btnAdjustColor) {
			scape.toggleColorMode();
		}
	}

}
