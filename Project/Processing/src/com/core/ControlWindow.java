package com.core;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import interfascia.GUIController;
import interfascia.GUIEvent;
import interfascia.IFButton;
import processing.core.PApplet;

public class ControlWindow extends PApplet {

	SoundScape scape;
	GUIController guiController;
	IFButton btnAdjustColor;
	DecimalFormat df;

	public ControlWindow(SoundScape parent) {
		this.scape = parent;
	}

	public void settings() {
		size(scape.width / 3, 350);
	}

	public void setup() {
		df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		
		getSurface().setFrameRate(10);
		getSurface().setAlwaysOnTop(true);
		getSurface().setTitle("Debug VP3D");
		
		guiController = new GUIController(this);
		
		btnAdjustColor = new IFButton("Change Color mode",width-122,2,120,20);
		
		btnAdjustColor.addActionListener(this);
		
		guiController.add(btnAdjustColor);
	}
	public void draw() {
		background(10);
		stroke(255);
	//titles
		fill(scape.displayColor2);
		text("Color Variables",2,16);
		text("Sound Variables",2,128);
		text("Camera Variables",2,282);
		
		fill(255);
		//Color vars
		text("Effector: "+df.format(scape.colorEffector),2,30);
		text("Target HSB: "+scape.targetHSB,2,44);
		text("HSB: "+scape.HSBColor,2,58);
		text("rgbV x: "+PApplet.round(scape.rgbV.x),2,72);
		text("rgbV y: "+PApplet.round(scape.rgbV.y),2,86);
		text("rgbV z: "+PApplet.round(scape.rgbV.z),2,100);
		//Sound vars
		text("Subs: "+ (int)scape.subs,2,142);
		text("Lows: "+(int)scape.lows,2,156);
		text("Mids: "+(int)scape.mids,2,170);
		text("Highs: "+(int)scape.highs,2,184);
		text("Bands Comb: "+(int)scape.bandsComb,2,198);
		text("Intensity: "+(int)scape.intensity,2,212);
		text("Target Vol: "+scape.idealVol,2,226);
		text("Avg vol: "+df.format(scape.avgVol),2,240);
		text("Adjustment Vol: "+df.format(scape.adjustmentVol),2,254);
		//Camera vars
		text("Camera X: "+df.format(scape.rotateCameraX),2,296);
		text("Camera Z: "+df.format(scape.rotateCameraZ),2,310);
		text("CameraX roam: "+df.format(scape.rotateCameraZ),2,324);
		text("CameraZ roam: "+df.format(scape.rotateCameraZ),2,338);
	}
	public void exit() {
		scape.toggleDebug();
	}
	public void keyPressed() {
		if (keyCode == 112) {	// F1 is 112 for java 2D
			scape.toggleDebug();
		}
	}
	
	public void actionPerformed(GUIEvent e) {
		if (e.getSource() == btnAdjustColor) {
			scape.toggleColorMode();
		}
	}

}
