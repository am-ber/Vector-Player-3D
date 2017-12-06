package com.core;

import java.io.File;
import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

public class SoundScape extends PApplet {

	public static void main(String args[]) {
		PApplet.main("com.core.SoundScape");
	}
	
// General Imports

// Drawing vars
	int cols, rows;
	int scl = 30; // For slower computers obviously scale up
	// width and height of noise grid
	int w = 2000;
	int h = 2000;

// Camera control vars
	float rotateCameraZ = 0;
	float rotateCameraX = PI / 2.5f;
	int mouseLastX = 0, mouseLastY = 0;
	float zoom = 1.0f;
	
// Noise vars
	float accel = 0;
	int zoffset = -500;
	float[][] terrain;
	float noiseAmplitude = 100;

// Audio imports
	Minim minim;
	AudioPlayer song;
	FFT fft;

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
	final int maxRGBstrokeValue = 230;	// MAX OF 255
	int displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
	int displayColor2 = color((int) rgbV.z, (int) rgbV.y, (int) rgbV.x);
	int displayColor3 = color((int) rgbV.x, (int) rgbV.y, (int) rgbV.z);
	
	
// Shapes and other things like it
	ParticleSystem particleSystem;
	ArrayList<Shapes> shapesList;
	
	public void settings() {
		size(800, 600, P3D);
	}

	// This is the initializations method. This is called before anything else is
	public void setup() {
		// General initializing
		scale(2.0f);
		// Audio initializing
		minim = new Minim(this);
		
		cols = w / scl;
		rows = h / scl;
		terrain = new float[cols][rows];

		colorMode(RGB); // Can be in RGB or HSB
		
		camera(width / 2.0f, height / 2.0f, (height/2.0f) / tan(PI*30.0f / 180.0f),
				width/2.0f, height/2.0f, 0, 0,1,0);
		
		shapesList = new ArrayList<Shapes>();
		for (int i = 0; i < 30; i++) {
			shapesList.add(new Box1(this));
		}
		
		particleSystem = new ParticleSystem(new PVector(random(-width, 0),random(-height, 0),random(h)),this, 75);

		setSong("res/song.mp3");
		
	}

	// This runs in a loop as designed by the Processing Environment
	public void draw() {
		background(0);
		
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
		
		generateSomeLines();
		particleSystem.run();
		if(intensity > 252 & song.isPlaying()) {
			particleSystem.changePos();
		}
		for (int i = 0; i < shapesList.size(); i++) {
			shapesList.get(i).run(displayColor2, displayColor3);
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
			
			int mappedIntensity = (int) map(intensity * 5, 0, 300, 0, 255);
			
			beginShape(TRIANGLE_STRIP);
			if (rgbVF.x + rgbVF.y + rgbVF.z > 2)
				fill(displayColor, mappedIntensity);
			else
				noFill();
			stroke(displayColor2, mappedIntensity);
			for (int x = 0; x < cols; x++) {
				vertex(x * scl, y * scl, (terrain[x][y] + zoffset));
				vertex(x * scl, (y + 1) * scl, (terrain[x][y + 1] + zoffset));
			}
			endShape();
		}// end of double for
	}

	private void getMouseDragging() {
		if (!mousePressed & (mouseX < width) & (mouseY < height)) {
			mouseLastX = mouseX;
			mouseLastY = mouseY;
		}
		if (mousePressed & (mouseX < width) & (mouseY < height)) {
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

	public void keyPressed() {
		if (key == 'f' & (song.getGain() >= -30))
			songGain -= 2;
		if (key == 'r' & (song.getGain() <= 0))
			songGain += 2;
		if (key == 'q') {
			song.pause();
			//selectInput("Select a file to process:", "fileSelected"); // Will open a built in file explorer
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
	public void mouseWheel(MouseEvent event) {
		  zoom += event.getCount()*0.01f;
	}

	public void setSong(String file) {
		song = minim.loadFile(file);
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
	float temp = 0;
	private void populateNoise() {
		float xoff = accel;
		for (int y = 0; y < rows; y++) {
			float yoff = 0;
			for (int x = 0; x < cols; x++) {
				if (song.isPlaying())
					noiseAmplitude = map(intensity, 0, 255, 100, 400);
				else
					noiseAmplitude = 75;
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
/* TODO
* Change this to instead create a triangle strip in the back ground
*/
		float heightMult = 2;
		float dist = -((cols / fft.specSize()) + cols);
		float previousBandValue = fft.getBand(0);
		int n = -cols;
		for (int i = 0; i < ((fft.specSize() * specLow) + (fft.specSize() * specMid)); i++) {
			if (song.isPlaying())
				stroke(map(lows, 0, 1200, 0, 255), map(mids, 0, 800, 0, 255), map(highs, 0, 800, 0, 255), map(intensity * 5, 0, 50, 0, 255));
			else
				stroke(displayColor2, intensity * 5);
			
			float bandValue = fft.getBand(i)*(1 + (i/50));
			line(cols / 2, dist*(-n), (previousBandValue*heightMult), cols / 2, dist*(-n-1), (bandValue*heightMult));
			previousBandValue = bandValue;
			n ++;
		}
	}
}
