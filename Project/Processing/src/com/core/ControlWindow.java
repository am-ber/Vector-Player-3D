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
		
		btnAdjustColor = new IFButton("Change Color mode",0,50,120,20);
		
		btnAdjustColor.addActionListener(this);
		
		guiController.add(btnAdjustColor);
	}
	public void draw() {
		background(10);
		stroke(255);
	//titles
		fill(scape.displayColor2);
		text("Color Variables",2,12);
		text("Sound Variables",2,86);
		fill(255);
		text("Effector: "+scape.effector,2,30);
		text("HSB: "+scape.HSBColor,2,42);
		
		text("Subs: "+scape.subs,2,100);
		text("Lows: "+scape.lows,2,112);
		text("Mids: "+scape.mids,2,124);
		text("Highs: "+scape.highs,2,136);
		
	}
	
	public void actionPerformed(GUIEvent e) {
		if (e.getSource() == btnAdjustColor) {
			scape.toggleColorMode();
		}
	}

}
