package com.core;

import java.io.File;
import java.util.ArrayList;

import ddf.minim.AudioInput;
import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

public class SoundScape extends PApplet {

	public static void main(String args[]) {
		System.out.println("Launching GUI");
		PApplet.main("com.core.SoundScape");
	}
	
// General Imports
	PFont perfectDarkFont, btnFont, metaFont, textFont;
	PImage icon;
	String[] args = {"VP3D Control Window"};
	ControlWindow cw;
	
// Drawing vars
	int cols, rows;
	int scl = 60; // For slower computers obviously scale up
	// width and height of noise grid
	int w = 4000;
	int h = 6000;
	boolean lowFps = false;
	int timeSenseLastFpsCheck = 0;
	
// Button Vars
	boolean btnFileOver, btnPlayOver, btnVerticalOver, btnMetaOver;
	int padding = 10;
	int btnHeight = 50;
	int btnWidth = 110;
	int btnFileX = padding, btnY = padding, btnPlayX = btnFileX + btnWidth + padding, btnMetaX = btnPlayX + btnWidth + padding;
	
	boolean debugOpen = false;

// Meta Vars
	int metaTextHeight = 40;
	int metaLabelWidth = 110;
	int metaTab = 30;
	int metaPanelX = padding;
	int metaLabelX = metaPanelX + padding;
	int metaPanelY = btnY + btnHeight + padding, metaTextWidth, metaTextX = metaLabelX + metaLabelWidth + metaTab, titleY = metaPanelY + padding;
	int albumY = titleY + metaTextHeight + padding, authorY = albumY + metaTextHeight + padding, genreY = authorY + metaTextHeight + padding;
	int metaPanelHeight = genreY + metaTextHeight + padding - metaPanelY;
	String songTitle = "", songAlbum = "", songAuthor = "", songGenre = "";
	
// Camera control vars
	float rotateCameraZ = 0;
	float rotateCameraX = PI / 2.5f;
	
	final float idealCamRotZ = 0, idealCamRotX = 1.75f;
	final float camRotZroam = 1, maxCamRotXroam = 2, minCamRotXroam = 1.2f; // for Z we can use positive 1 and negative 1
	float distanceBetweenX = 0, distanceBetweenZ = 0;
	float targetRoamX = 0, targetRoamZ = 0;
	float previousCamX = 0, previousCamZ = 0;
	int timeSenseLastDrag = 0, randomTimeToWait = 5500;
	long timeSenseLastRoam = 0;
	boolean startedRoam = false, dragedOneTime = true, roamedLastOneTime = false;
	int mouseLastX = 0, mouseLastY = 0;
	
// Noise vars
	float accel = 0, lineAccel = 0;
	int zoffset = -500, yoffset = -1000, xoffset = 0;
	float[][] terrain;
	float noiseAmplitude = 275, defaultNoiseAmplitude = 275;

// Audio imports
	Minim minim;
	AudioPlayer song;
	FFT fft;
	AudioMetaData meta;
	AudioInput lineIn;

// Audio vars
	float subs = 0;	
	float lows = 0;
	float mids = 0;
	float highs = 0;

	float oldSub = subs;
	float oldLow = lows;
	float oldMid = mids;
	float oldHigh = highs;
	float bandsComb = 0;

	float songGain = 0;

	int songPos = 0;
	
	boolean isThereSound = false;
	
	final float idealVol = 2.75f;
	float adjustmentVol = 1.0f;
	float avgVol = 0, lastAvgVol = 0;

	// Determines how large each freq range is
	float specSub = 0.01f; // 1%
	float specLow = 0.07f; // 6%
	float specMid = 0.15f; // 13.5%
	float specHi = 0.2f; // 20%

	float decreaseRate = 30;
	float intensity = 0;

// Colors vars
	PVector rgbVF = new PVector(lows * 0.67f, mids * 0.67f, highs * 0.67f);
	PVector rgbV = new PVector(100, 100, 100);
	PVector fontFade = new PVector(255,255,255);
	final int maxRGBstrokeValue = 230;	// MAX OF 255
	int displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
	int displayColor2 = color((int) rgbV.z, (int) rgbV.y, (int) rgbV.x);
	int displayColor3 = color((int) rgbV.x, (int) rgbV.y, (int) rgbV.z);
	int currentColorMode = 3; // 1 for RGB and 3 for HSB
	int HSBColor = 0;
	float colorEffector = 0;
	final int targetHSB = 160;
	
// Shapes and other things like it
	ParticleSystem particleSystem;
	ArrayList<Shapes> shapesList;
	ArrayList<Shapes> shapesList2;
	float previousBandValue;
	
	public void settings() {
		size(800, 600, P3D);
	}

	// This is the initializations method. This is called before anything else is
	public void setup() {
		cw = new ControlWindow(this);
		runSketch(args, cw);
		cw.noLoop();
		cw.getSurface().setVisible(false);
		surface.setResizable(true);
		// General initializing
		
		scale(2.0f);
		
		icon = loadImage("res/icon.png");
		frame.setIconImage(icon.getImage());
		surface.setTitle("Vector Player 3D");

		perfectDarkFont = createFont("res/pdark.ttf", 48);
		btnFont = createFont("res/ariblk.ttf", 24);
		textFont = createFont("res/ariblk.ttf", 12);
		metaFont = createFont("res/ariblk.ttf", 26);

		// Audio initializing
		minim = new Minim(this);
		lineIn = minim.getLineIn();
		lineIn.mute();
		fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
		
		cols = w / scl;
		rows = h / scl;
		terrain = new float[cols][rows];

		colorMode(HSB); // Can be in RGB or HSB
		
		camera(width / 2.0f, height / 2.0f, (height/2.0f) / tan(PI*30.0f / 180.0f),
				width/2.0f, height/2.0f, 0, 0,1,0);
		
		shapesList = new ArrayList<Shapes>();
		shapesList2 = new ArrayList<Shapes>();
		
		for (int i = 0; i < 35; i++) {
			shapesList.add(new Box1(this, new PVector(-width, 0)));
			shapesList2.add(new Box1(this, new PVector(w, w + (w / 2))));
		}
		particleSystem = new ParticleSystem(new PVector(random(-width, 0),random(-height, 0),random(h)),this, 75);

		setSong("res/song.mp3");
	}

	// This runs in a loop as designed by the Processing Environment
	public void draw() {
		background(0);
		
		if (song.isPlaying())
			isThereSound = true;
		if (lineIn.isMonitoring())
			isThereSound = true;
		
		if (isThereSound)
			particleSystem.run();
		
		drawFadeIntroText();	// We want to draw the font before translation of the camera
		
		generateSomeLines();
		
	// Drawing UI Elements
		btnVerticalOver = (mouseY >= btnY && mouseY <= btnY + btnHeight);
		btnFileOver = btnVerticalOver && (mouseX >= btnFileX && mouseX <= btnFileX + btnWidth);
		btnPlayOver = btnVerticalOver && (mouseX >= btnPlayX && mouseX <= btnPlayX + btnWidth);
		btnMetaOver = btnVerticalOver && (mouseX >= btnMetaX && mouseX <= btnMetaX + btnWidth);
		
		int buttonRGB = color(240,240,240);
		fill(180);
		textAlign(RIGHT);
		textFont(textFont);
		text("FPS: "+round(frameRate),width-10,12);
		
		textAlign(LEFT);
		textFont(btnFont);
		fill(buttonRGB, btnFileOver?255:50);
		stroke(displayColor, btnFileOver?intensity:30);
		rect(btnFileX, btnY, btnWidth, btnHeight);
		fill(buttonRGB, btnPlayOver?255:50);
		stroke(displayColor, btnPlayOver?intensity:30);
		rect(btnPlayX, btnY, btnWidth, btnHeight);
		fill(buttonRGB, btnMetaOver?255:50);
		stroke(displayColor, btnMetaOver?intensity:30);
		rect(btnMetaX, btnY, btnWidth, btnHeight);
		fill(0);
		text("File", btnFileX + 30, btnY + 5, 90, 40);
		text((song.isPlaying()?"Pause":"Play"), btnPlayX + (song.isPlaying()?15:25), btnY + 5, 90, 40);
		text("Meta", btnMetaX + 20, btnY + 5, 90, 40);
		
		if(btnMetaOver){
			fill(0);
			stroke(255);
			rect(metaPanelX, metaPanelY, width - (padding*2), metaPanelHeight);
			fill(255);
			noStroke();
			textFont(metaFont);
			text("Title:", metaLabelX, titleY, metaLabelWidth, metaTextHeight);
			text("Author:", metaLabelX, authorY, metaLabelWidth, metaTextHeight);
			text("Album:", metaLabelX, albumY, metaLabelWidth, metaTextHeight);
			text("Genre:", metaLabelX, genreY, metaLabelWidth, metaTextHeight);
			text(songTitle, metaTextX, titleY, width - (padding*2) - metaTextX, metaTextHeight);
			text(songAuthor, metaTextX, authorY, width - (padding*2) - metaTextX, metaTextHeight);
			text(songAlbum, metaTextX, albumY, width - (padding*2) - metaTextX, metaTextHeight);
			text(songGenre, metaTextX, genreY, width - (padding*2) - metaTextX, metaTextHeight);
		}
		
	// Getting the camera correct
		translate(width / 2, height / 2);
		rotateX(rotateCameraX);
		rotateZ(rotateCameraZ);
		translate(-w / 2, -h / 2);
		
		getMouseDragging();
		checkFps();
		
		if (timeSenseLastDrag > 8000 || startedRoam) {
			cameraRoam();
		}

		if (isThereSound) {
			processSong();
			populateNoise();
		} else {
		    if (bandsComb > 0)
		    	bandsComb -= 5;
			populateNoise();
		}
		
		if (intensity > 240 & isThereSound) {
			particleSystem.changePos();
		}
		
		if (isThereSound)
		for (int i = 0; i < shapesList.size(); i++) {
			shapesList.get(i).run(rgbV, rgbVF);
			shapesList2.get(i).run(rgbVF,rgbV);
		}
    
	// Acctually draw it
		for (int y = 0; y < rows - 1; y++) {
			// Handle colors first
			if (currentColorMode == RGB) {
				if (isThereSound) {
					intensity = fft.getBand(y % (int) (fft.specSize() * (specLow + specMid + specHi)));
					rgbVF = new PVector(lows * 0.47f, mids * 0.37f, highs * 0.37f);
					rgbV = new PVector(lows * 0.47f, mids * 0.37f, highs * 0.37f);
				} else {
				// Stroke rgb
					if (rgbV.x <= maxRGBstrokeValue) rgbV.x += 0.01f;
					else if (rgbV.x >= maxRGBstrokeValue+1) rgbV.x -= 0.01f;
					if (rgbV.y <= maxRGBstrokeValue) rgbV.y += 0.01f;
					else if (rgbV.y >= maxRGBstrokeValue+1) rgbV.y -= 0.01f;
					if (rgbV.z <= maxRGBstrokeValue) rgbV.z += 0.01f;
					else if (rgbV.z >= maxRGBstrokeValue+1) rgbV.z -= 0.01f;
				// Fill rgb
					if (rgbVF.x > 1) rgbVF.x -= 0.01f;
					if (rgbVF.y > 1) rgbVF.y -= 0.01f;
					if (rgbVF.z > 1) rgbVF.z -= 0.01f;
					if (intensity <= 254) intensity += 0.01f;
				}
				displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
				displayColor2 = color((int) rgbV.z, (int) rgbV.y, (int) rgbV.x);
				displayColor3 = color((int) rgbV.x, (int) rgbV.y, (int) rgbV.z);
				
				int mappedIntensity = (int) map(intensity * 5, 10, 250, 10, 255);
				
				beginShape(TRIANGLE_STRIP);
				if (rgbVF.x + rgbVF.y + rgbVF.z > 2)
					fill(displayColor, mappedIntensity);
				else
					noFill();
				stroke(displayColor2, mappedIntensity);
			} else {
				if (isThereSound) {
					intensity = fft.getBand(y % (int) (fft.specSize() * (specLow + specMid + specHi))) * 1.05f;
					HSBColor = (int) (map(bandsComb * colorEffector, 0, 2675, 0, 360));
					
					if (lastAvgVol <= 0.005f)
						HSBColor = targetHSB;
					if (HSBColor >= targetHSB + 35)
						colorEffector -= 0.00005f;
					else if (HSBColor <= targetHSB - 35)
						colorEffector += 0.00005f;
					rgbVF = new PVector(HSBColor, 255, 255);
					rgbV = new PVector(HSBColor, 255, 255);
				} else {
					rgbVF.x = 0;
					if (rgbVF.y > 1) rgbVF.y -= 0.01f;
					if (rgbVF.z > 1) rgbVF.z -= 0.01f;
					if (rgbV.y > 0) rgbV.y -= 0.01f;
					if (rgbV.z > 220) rgbV.z -= 0.01f;
					else if (rgbV.z < 219) rgbV.z += 0.1f;
					if (intensity <= 200) intensity += 0.01f;
				}
				displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
				displayColor2 = color((int) map(rgbV.x, 255, 0, 0, 255), (int) rgbV.y, (int) rgbV.z);
				displayColor3 = color((int) rgbV.x, (int) rgbV.y, (int) rgbV.z);
				
				int mappedIntensity = (int) map(intensity * 5, 10, 250, 10, 255);
				
				beginShape(TRIANGLE_STRIP);
				if (rgbVF.x + rgbVF.y + rgbVF.z > 2)
					fill(displayColor, mappedIntensity);
				else
					noFill();
				stroke(displayColor2, intensity);
			}
			for (int x = 0; x < cols; x++) {
				vertex((x * scl) + xoffset, (y * scl) + yoffset, (terrain[x][y] + zoffset));
				vertex((x * scl) + xoffset, ((y + 1) * scl) + yoffset, (terrain[x][y + 1] + zoffset));
			}
			endShape();
		}// end of double for
	}
	
	public void checkFps() {
		if (millis() % 2 == 0) {
			timeSenseLastFpsCheck ++;
		}
		if (frameRate < 59) {
			if (!lowFps)
				lowFps = true;
		} else {
			timeSenseLastFpsCheck = 0;
		}
		
		if (timeSenseLastFpsCheck > 60 & lowFps) {
			println("Low fps detected. Increasing scale.");
			scl *= 1.1;
			cols = w / scl;
			rows = h / scl;
			terrain = new float[cols][rows];
			lowFps = false;
			timeSenseLastFpsCheck = 0;
		}
	}
	
	public void stop() {
		song.close();
		minim.stop();
		super.stop();
	}
	private void drawFadeIntroText() {
		textMode(SHAPE);
		noStroke();
		textFont(perfectDarkFont);
		textAlign(CENTER,CENTER);
		if(fontFade.x < 2)
			noFill();
		else
			fill(fontFade.x,fontFade.y,fontFade.z);
		text("Vector",(width / 2), (height / 2)-50);
		text("Player",(width / 2), (height / 2));
		text(" 3 D",(width / 2), (height / 2)+50);
		if (fontFade.z > 1) fontFade.z -= 3;
		else if (fontFade.y > 1) fontFade.y -= 3;
		else if (fontFade.x > 1) fontFade.x -= 3;
		textMode(MODEL);
	}
	private void getMouseDragging() {
		if (!mousePressed & (mouseX > 0) & (mouseY > 0) & (mouseX < width) & (mouseY < height)) {
			mouseLastX = mouseX;
			mouseLastY = mouseY;
		}
		if (mousePressed & (mouseX > 0) & (mouseY > 0) & (mouseX < width) & (mouseY < height)) {
			timeSenseLastDrag = 0;
			if (mouseX < (mouseLastX)-5) {
				rotateCameraZ += map(mouseX,mouseLastX,(mouseLastX - width),0,0.07f);
			} else if (mouseX > (mouseLastX)+5) {
				rotateCameraZ -= map(mouseX,mouseLastX,(mouseLastX + width),0,0.07f);
			}
			if (mouseY < (mouseLastY)-5) {
				rotateCameraX += map(mouseY,mouseLastY,(mouseLastY - width),0,0.07f);
			} else if (mouseY > (mouseLastY)+5) {
				rotateCameraX -= map(mouseY,mouseLastY,(mouseLastY + width),0,0.07f);
			}
			dragedOneTime = true;
		} else if (dragedOneTime) {
			dragedOneTime = false;
			startedRoam = false;
		}
		timeSenseLastDrag = millis();
	}
	private void cameraRoam() {
		if (!startedRoam) {
			previousCamX = rotateCameraX;
			previousCamZ = rotateCameraZ;
			targetRoamX = random(minCamRotXroam, maxCamRotXroam);
			targetRoamZ = random(-camRotZroam, camRotZroam);
			distanceBetweenX = abs(targetRoamX - previousCamX);
			distanceBetweenZ = abs(targetRoamZ - previousCamZ);
			startedRoam = true;
		} else {
			if (roamedLastOneTime) {
				randomTimeToWait = (int) random(2500,5500);
				timeSenseLastRoam = millis();
				roamedLastOneTime = false;
			}
			if (timeSenseLastRoam > randomTimeToWait) {
				float accelX = map(rotateCameraX, previousCamX, distanceBetweenX, 0.1f, 1);
				float deccelX = map(rotateCameraX, distanceBetweenX, targetRoamX, 0.1f, 1);
				rotateCameraX += 0.01 * accelX;
				
				/* TODO Finish camera roam method
				 * Accelerate evenly to the distance between from the starting pos
				 * and decelerate evenly to the target from the distance between
				 * BUT must end at the target without falling below 0
				 */
			}
		}
	}
	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
		if (songGain < 0) songGain += map(e, 1, 0, 0, 2);
		if (songGain > -80) songGain += map(e, -1, 0, 0, -2);
		if (songGain > 0) songGain = 0;
		if (songGain < -80) songGain = -80;
	}
	public void keyPressed() {
		if (keyCode == 97 & !debugOpen) {	// F1 is 97 for OpenGL
			toggleDebug();
		} else if (keyCode == 97 & debugOpen) {
			toggleDebug();
		}
		if (keyCode == LEFT || keyCode == RIGHT)
			toggleColorMode();
		
		if (key == 'm')
			toggleLineIn();
		
		if (keyCode == 32) {
			toggleSong();
		}
		if (key == 'q') {
			if (lineIn.isMonitoring())
				toggleLineIn();
			if (song.isPlaying())
				toggleSong();
			selectInput("Select a file to process:", "fileSelected"); // Will open a built in file explorer
		}
	}
	public void toggleDebug() {
		if (cw.isLooping()) {
			cw.noLoop();
			cw.getSurface().setVisible(false);
			println("************\nDEBUG MENUE CLOSED\n*************");
			debugOpen = false;
		} else {
			cw.loop();
			cw.getSurface().setVisible(true);
			println("************\nDEBUG MENUE\n*************");
			debugOpen = true;
		}
	}
	public void toggleSong() {
		if (lineIn.isMonitoring())
			toggleLineIn();
		if (song.isPlaying()) {
			song.pause();
			songPos = song.position();
			isThereSound = false;
		} else {
			adjustmentVol = 1;
		    song.play(songPos);
		}
	}
	public void mousePressed(){
		if(btnFileOver){
			if (lineIn.isMonitoring())
				toggleLineIn();
			if (song.isPlaying())
				toggleSong();
			mousePressed = false;
			selectInput("Select a file to process:", "fileSelected");
		}else if(btnPlayOver){
			toggleSong();
		}
	}
	public void toggleLineIn() {
		if (song.isPlaying()) {
		    songPos = song.position();
			song.pause();
		}
		if (!lineIn.isMonitoring()) {
			println("IS MONITORING");
			adjustmentVol = 1;
			lineIn.enableMonitoring();
			fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
		} else {
			lineIn.disableMonitoring();
			isThereSound = false;
		}
	}
	public void setSong(String file) {
		try {
			song = minim.loadFile(file);
			meta = song.getMetaData();
		} catch (Exception e) {
			println("We got a "+e.toString()+" error. So uh, yea.");
			println("It's ok though. We'll play the default song.");
		}
		songPos = 0;
		isThereSound = false;
		fft = new FFT(song.bufferSize(), song.sampleRate());
		refreshMetadata();
	}
	
	public int toggleColorMode() {
		if (currentColorMode == 1) {
			colorMode(HSB);
			currentColorMode = 3;
			return 0;
		}
		if (currentColorMode == 3) {
			colorMode(RGB);
			currentColorMode = 1;
			return 0;
		}
		return 1;
	}
	
// Runs when a song needs to be selected
	public void fileSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			println("User selected " + selection.getAbsolutePath());
			try {
				setSong(selection.getAbsolutePath());
			} catch(Exception e) {
				println("Which is not an audio file...");
				println("Please use supported audio files from the library minim.");
			}
		}
	}
	private void populateNoise() {
		float xoff = accel;
		for (int y = 0; y < rows; y++) {
			float yoff = 0;
			for (int x = 0; x < cols; x++) {
				if (isThereSound)
					noiseAmplitude = map(intensity, 0, 255, defaultNoiseAmplitude, defaultNoiseAmplitude + intensity);
				else
					noiseAmplitude = defaultNoiseAmplitude;
				terrain[x][y] = map(noise(xoff, yoff), 0, 1, -noiseAmplitude, noiseAmplitude) * map(intensity, 0, 250, 1, 2f);
				yoff += 0.15;
			}
			xoff += 0.15;
		}
		accel -= (0.03 + (bandsComb * 0.0001));
	}
	
	private void processSong() {
		stroke(50);
		song.setGain(songGain);

		// Forwards the song on draw() for each "frame" of the song
		if (song.isPlaying())
			fft.forward(song.mix);
		if (lineIn.isMonitoring())
			fft.forward(lineIn.mix);
		
		// TODO add smoothing to averaging of volume
		
		// This adjusts to an ideal volume based on default song (avg: 250)
		float temp = 0;
		for (int i=0; i < fft.specSize(); i++) {
			temp += fft.getBand(i);
		}
		avgVol = temp / fft.specSize();
		lastAvgVol = avgVol;
		if (avgVol >= 0.005f)
			for (int i=0; i < fft.specSize(); i++)
				fft.scaleBand(i, (adjustmentVol * 0.63f));
		temp = 0;
		for (int i=0; i < fft.specSize(); i++) {
			temp += fft.getBand(i);
		}
		avgVol = temp / fft.specSize();
		if (avgVol <= 0.005f)
			avgVol = idealVol;
		
		if (avgVol < idealVol - 2)
			adjustmentVol += 0.1f;
		else if (avgVol > idealVol + 2)
			adjustmentVol -= 0.1f;
		
		// Setting vars
		oldSub = subs;
		oldLow = lows;
		oldMid = mids;
		oldHigh = highs;
		subs = 0;
		lows = 0;
		mids = 0;
		highs = 0;
		
		// Adds the bands that are present to the 3 sections
		for (int i = 0; i < fft.specSize() * specSub; i++)
			subs += fft.getBand(i);
		for (int i = 0; i < fft.specSize() * specLow + specSub; i++)
			lows += fft.getBand(i);
		for (int i = (int) (fft.specSize() * specLow); i < fft.specSize() * specMid; i++)
			mids += fft.getBand(i);
		for (int i = (int) (fft.specSize() * specMid); i < fft.specSize() * specHi; i++)
			highs += fft.getBand(i);
		
		// Will slow down any instant loss in sound by decrease rate
		if (oldSub > subs)
			subs = oldSub - decreaseRate;
		if (oldLow > lows)
			lows = oldLow - decreaseRate;
		if (oldMid > mids)
			mids = oldMid - decreaseRate;
		if (oldHigh > highs)
			highs = oldHigh - decreaseRate;
		
		bandsComb = 0.66f * lows + 0.8f * mids + 1 * highs;
	}
	private void generateSomeLines() {
		float heightMult = 2.5f;
		int specRange = (int)((fft.specSize() * specLow) + (fft.specSize() * specMid));
		if (isThereSound) {
			previousBandValue = fft.getBand(1);
			for (int i = 1; i < width; i++) {
				if (currentColorMode == RGB)
					stroke(map(lows, 0, 1200, 0, 255), map(mids, 0, 800, 0, 255), map(highs, 0, 800, 0, 255));
				else
					stroke(displayColor2);
				float bandValue = fft.getBand((int)(map(i, 0, width, 0, specRange)))*(1 + (map(i, 0, width, 0, specRange)/100));
				line(i, height - (previousBandValue*heightMult), i+1, height - (bandValue*heightMult));
				previousBandValue = bandValue;
			}
		}
	}
	public String[] MetaString () {
		String[] metadata = {meta.title(), meta.album(), meta.genre(), meta.author()};
		return metadata;
	}
    
	public void refreshMetadata() {
		songTitle = meta.title();
		songAlbum = meta.album();
		songAuthor = meta.author();
		songGenre = meta.genre();
	}
}
