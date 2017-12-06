package com.core;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;
import processing.core.PVector;

public class SoundScape extends PApplet {

	public static void main(String args[]) {
		PApplet.main("com.core.SoundScape");
	}

	// Drawing vars
	int cols, rows;
	int scl = 30; // For slower computers obviously scale up
	int w = 1000;
	int h = 1500;

	// Camera control vars
	float rotateCameraZ = 0;
	float rotateCameraX = PI / 2.5f;

	// Noise vars
	float accel = 0;
	float[][] terrain;

	// Audio imports
	Minim minim;
	AudioPlayer song;
	FFT fft;

	// Audio vars
	float lows = 0;
	float mids = 0;
	float highs = 0;

	float oldLow = lows;
	float oldMid = mids;
	float oldHigh = highs;
	float bandsComb = 0;

	float songGain = 0;

	int songPos = 0;

	// Determines how large each freq range is
	float specLow = 0.05f; // 5%
	float specMid = 0.125f; // 12.5%
	float specHi = 0.20f; // 20%

	float decreaseRate = 25;
	float intensity = 0;

	// Colors vars
	PVector rgbVF = new PVector(lows * 0.67f, mids * 0.67f, highs * 0.67f);
	PVector rgbV = new PVector(100, 100, 100);
	final int maxRGBstrokeValue = 230;	// MAX OF 255
	int displayColor = color((int) rgbVF.x, (int) rgbVF.y, (int) rgbVF.z);
	int displayColor2 = color((int) rgbV.z, (int) rgbV.y, (int) rgbV.x);

	public void settings() {
		size(800, 600, P3D);
	}

	public void setup() {
		cols = w / scl;
		rows = h / scl;
		terrain = new float[cols][rows];

		colorMode(RGB); // Can be in RGB or HSB

		// Audio initializing
		minim = new Minim(this);

		setSong("res/song.mp3");
	}

	public void draw() {
		background(0);

		// Getting the camera correct
		translate(width / 2, height / 2);
		rotateX(rotateCameraX);
		rotateZ(rotateCameraZ);
		translate(-w / 2, -h / 2);

		if (song.isPlaying()) {
			processSong();
			populateNoise();
		} else {
		    if (bandsComb > 0)
		    	bandsComb -= 5;
			populateNoise();
		}
		
		generateSomeLines();
		
		// Acctually draw it
		for (int y = 0; y < rows - 1; y++) {
			beginShape(TRIANGLE_STRIP);
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

			if (rgbVF.x + rgbVF.y + rgbVF.z > 2)
				fill(displayColor, intensity * 5);
			else
				noFill();
			stroke(displayColor2, intensity * 5);
			for (int x = 0; x < cols; x++) {
				vertex(x * scl, y * scl, terrain[x][y]);
				vertex(x * scl, (y + 1) * scl, terrain[x][y + 1]);
			}
			endShape();
		}
	}

	public void keyPressed() {
		if (keyCode == UP)
			rotateCameraX -= 0.1;
		if (keyCode == DOWN)
			rotateCameraX += 0.1;
		if (keyCode == RIGHT)
			rotateCameraZ -= 0.1;
		if (keyCode == LEFT)
			rotateCameraZ += 0.1;
		if (key == 'f' & (song.getGain() >= -30))
			songGain -= 2;
		if (key == 'r' & (song.getGain() <= 0))
			songGain += 2;
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
		song = minim.loadFile(file);
		fft = new FFT(song.bufferSize(), song.sampleRate());
	}

	private void populateNoise() {
		float xoff = accel;
		for (int y = 0; y < rows; y++) {
			float yoff = 0;
			for (int x = 0; x < cols; x++) {
				terrain[x][y] = map(noise(xoff, yoff), 0, 1, -75, 75);
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
