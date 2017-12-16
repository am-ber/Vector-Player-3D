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
		size(scape.width / 3, scape.height);
	}

	public void setup() {
		df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		
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
		text("Sound Variables",2,86);
		text("Other Variables",2,206);
		
		fill(255);
		//Color vars
		text("Effector: "+df.format(scape.colorEffector),2,30);
		text("Target HSB: "+scape.targetHSB,2,44);
		text("HSB: "+scape.HSBColor,2,58);
		//Sound vars
		text("Subs: "+ (int)scape.subs,2,100);
		text("Lows: "+(int)scape.lows,2,114);
		text("Mids: "+(int)scape.mids,2,128);
		text("Highs: "+(int)scape.highs,2,142);
		text("Target Vol: "+scape.idealVol,2,154);
		text("Avg vol: "+df.format(scape.avgVol),2,168);
		text("Adjustment Vol: "+df.format(scape.adjustmentVol),2,180);
		//Other vars
		text("rgbV x: "+PApplet.round(scape.rgbV.x),2,220);
		text("rgbV y: "+PApplet.round(scape.rgbV.y),2,232);
		text("rgbV z: "+PApplet.round(scape.rgbV.z),2,244);
		text("Camera X: "+df.format(scape.rotateCameraX),2,268);
		text("Camera Z: "+df.format(scape.rotateCameraZ),2,280);
		
	}
	
	public void actionPerformed(GUIEvent e) {
		if (e.getSource() == btnAdjustColor) {
			scape.toggleColorMode();
		}
	}

}
