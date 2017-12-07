package com.core;

import java.io.File;
import java.util.ArrayList;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import processing.event.MouseEvent;

public class SoundScape extends PApplet {

	public static void main(String args[]) {
		System.out.println("Launching GUI");
		PApplet.main("com.core.SoundScape");
	}
	
// General Imports
	PFont perfectDarkFont;
	PFont font;
	PFont btnFont, metaFont;
	
// Drawing vars
	int cols, rows;
	int scl = 60; // For slower computers obviously scale up
	// width and height of noise grid
	int w = 4000;
	int h = 4000;

// Camera control vars
	float rotateCameraZ = 0;
	float rotateCameraX = PI / 2.5f;
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

// Audio vars
	float lows = 0;	// Will be 
	float mids = 0;
	float highs = 0;

	float oldLow = lows;
	float oldMid = mids;
	float oldHigh = highs;
	float bandsComb = 0;

	float songGain = 0;

	int songPos = 0;

	// Determines how large each freq range is
	float specLow = 0.08f; // 8%
	float specMid = 0.15f; // 15%
	float specHi = 0.20f; // 20%

	float decreaseRate = 25;
	float intensity = 0;

// Colors vars
	PVector rgbVF = new PVector(lows * 0.67f, mids * 0.67f, highs * 0.67f);
	PVector rgbV = new PVector(100, 100, 100);
	PVector fontFade = new PVector(255,255,255);
	final int maxRGBstrokeValue = 230;	// MAX OF 255
	int displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
	int displayColor2 = color((int) rgbV.z, (int) rgbV.y, (int) rgbV.x);
	int displayColor3 = color((int) rgbV.x, (int) rgbV.y, (int) rgbV.z);
	
	
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
		// General initializing
		scale(2.0f);
		perfectDarkFont = createFont("res/pdark.ttf", 48);
		font = createFont("res/cs_regular.ttf", 24);
		btnFont = createFont("res/ariblk.ttf", 24);
		metaFont = createFont("res/ariblk.ttf", 26);
		textFont(btnFont);
		
		// Audio initializing
		minim = new Minim(this);
		
		cols = w / scl;
		rows = h / scl;
		terrain = new float[cols][rows];

		colorMode(RGB); // Can be in RGB or HSB
		
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
		
		drawFadeIntroText();	// We want to draw the font before translation of the camera
		
		
	// Getting the camera correct
		translate(width / 2, height / 2);
		rotateX(rotateCameraX);
		rotateZ(rotateCameraZ);
		translate(-w / 2, -h / 2);
		
		getMouseDragging();

		if (song.isPlaying()) {
			processSong();
			populateNoise();
		} else {
		    if (bandsComb > 0)
		    	bandsComb -= 5;
			populateNoise();
		}
		
		particleSystem.run();
		generateSomeLines();
		
		if(intensity > 252 & song.isPlaying()) {
			particleSystem.changePos();
		}
		for (int i = 0; i < shapesList.size(); i++) {
			shapesList.get(i).run(displayColor2, displayColor, new PVector(-width, 0), new PVector(0,height));
			shapesList2.get(i).run(displayColor3, displayColor2, new PVector(w, w + (w / 2)), new PVector(0,height));
		}
	// Acctually draw it
		for (int y = 0; y < rows - 1; y++) {
			if (song.isPlaying()) {
				intensity = fft.getBand(y % (int) (fft.specSize() * specHi));
				rgbVF = new PVector(lows * 0.37f, mids * 0.37f, highs * 0.37f);
				rgbV = new PVector(lows * 0.37f, mids * 0.37f, highs * 0.37f);
			} else {
			// Stroke rgb
				if (rgbV.x <= maxRGBstrokeValue) rgbV.x += 0.001f;
				else if (rgbV.x >= maxRGBstrokeValue+1) rgbV.x -= 0.001f;
				if (rgbV.y <= maxRGBstrokeValue) rgbV.y += 0.001f;
				else if (rgbV.y >= maxRGBstrokeValue+1) rgbV.y -= 0.001f;
				if (rgbV.z <= maxRGBstrokeValue) rgbV.z += 0.001f;
				else if (rgbV.z >= maxRGBstrokeValue+1) rgbV.z -= 0.001f;
			// Fill rgb
				if (rgbVF.x > 1) rgbVF.x -= 0.01f;
				if (rgbVF.y > 1) rgbVF.y -= 0.01f;
				if (rgbVF.z > 1) rgbVF.z -= 0.01f;
				if (intensity <= 254) intensity += 0.01f;
			}
			displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
			displayColor2 = color((int) rgbV.z, (int) rgbV.y, (int) rgbV.x);
			displayColor3 = color((int) rgbV.x, (int) rgbV.y, (int) rgbV.z);
			
			int mappedIntensity = (int) map(intensity * 5, 0, 300, 0, 255);
			
			beginShape(TRIANGLE_STRIP);
			if (rgbVF.x + rgbVF.y + rgbVF.z > 2)
				fill(displayColor, mappedIntensity);
			else
				noFill();
			stroke(displayColor2, mappedIntensity);
			for (int x = 0; x < cols; x++) {
				vertex((x * scl) + xoffset, (y * scl) + yoffset, (terrain[x][y] + zoffset));
				vertex((x * scl) + xoffset, ((y + 1) * scl) + yoffset, (terrain[x][y + 1] + zoffset));
			}
			endShape();
		}// end of double for
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
		if (key == 'q') {
			song.pause();
			selectInput("Select a file to process:", "fileSelected"); // Will open a built in file explorer
		}
		if (key == 'p') {
			if (song.isPlaying()) {
			    songPos = song.position();
			    song.pause();
			  } else {
			    song.play(songPos);
			  }
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
		fft = new FFT(song.bufferSize(), song.sampleRate());
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
				if (song.isPlaying())
					noiseAmplitude = map(intensity, 0, 255, defaultNoiseAmplitude, defaultNoiseAmplitude + intensity);
				else
					noiseAmplitude = defaultNoiseAmplitude;
				terrain[x][y] = map(noise(xoff, yoff), 0, 1, -noiseAmplitude, noiseAmplitude);
				yoff += 0.22;
			}
			xoff += 0.22;
		}
		accel -= (0.03 + (bandsComb * 0.0001));
	}
	private void processSong() {
		stroke(50);
		song.setGain(songGain);

		// Forwards the song on draw() for each "frame" of the song
		fft.forward(song.mix);

		// Setting vars
		oldLow = lows;
		oldMid = mids;
		oldHigh = highs;
		lows = 0;
		mids = 0;
		highs = 0;
		
		// Adds the bands that are present to the 3 sections
		for (int i = 0; i < fft.specSize() * specLow; i++)
			lows += fft.getBand(i);
		for (int i = (int) (fft.specSize() * specLow); i < fft.specSize() * specMid; i++)
			mids += fft.getBand(i);
		for (int i = (int) (fft.specSize() * specMid); i < fft.specSize() * specHi; i++)
			highs += fft.getBand(i);
		
		// Will slow down any instant loss in sound by decrease rate
		if (oldLow > lows)
			lows = oldLow - decreaseRate;
		if (oldMid > mids)
			mids = oldMid - decreaseRate;
		if (oldHigh > highs)
			highs = oldHigh - decreaseRate;
		bandsComb = 0.66f * lows + 0.8f * mids + 1 * highs;
	}
	private void generateSomeLines() {
		float heightMult = 4;
		float dist = -((cols / fft.specSize()) + cols);
		int zOffset = -300;
		int n = - (int)(((fft.specSize() * specLow) + (fft.specSize() * specMid))/4);
		if (song.isPlaying()) {
			previousBandValue = fft.getBand(0);
			for (int i = 0; i < (((fft.specSize() * specLow) + (fft.specSize() * specMid))/4)+(((fft.specSize() * specLow) + (fft.specSize() * specMid))); i++) {
				stroke(map(lows, 0, 1200, 0, 255), map(mids, 0, 800, 0, 255), map(highs, 0, 800, 0, 255));
				float bandValue = fft.getBand(i)*(1 + (i/50));
				line(dist*(-n), -(cols / 2), (previousBandValue*heightMult) + zOffset, dist*(-n-1), -(cols / 2), (bandValue*heightMult) + zOffset);
				previousBandValue = bandValue;
				n ++;
			}
		} else {
			float xoff = lineAccel;
			for (int i = 0; i < (((fft.specSize() * specLow) + (fft.specSize() * specMid))/4)+(((fft.specSize() * specLow) + (fft.specSize() * specMid))); i++) {
				stroke(displayColor2, intensity * 5);
				float bandValue = map(noise(xoff), 0, 1, -125, 125);
				line(dist*(-n), -(cols / 2), (previousBandValue*heightMult) + zOffset, dist*(-n-1), -(cols / 2), (bandValue*heightMult) + zOffset);
				previousBandValue = bandValue;
				n ++;
				xoff += 0.01;
			}
			lineAccel -= 0.01;
		}
	}
	public String[] MetaString (){
		String[] metadata = {meta.title(), meta.album(), meta.genre(), meta.author()};
		return metadata;
	}
}
