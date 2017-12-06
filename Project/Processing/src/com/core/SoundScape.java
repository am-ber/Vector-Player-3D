package com.core;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

public class SoundScape extends PApplet {
	
	public static void main(String args[]) {
		PApplet.main("com.core.SoundScape");
	}

	// Drawing vars
	int cols, rows;
	int scl = 20;	// For slower computers obviously scale up
	int w = 1200;
	int h = 1200;

	// Camera control vars
	float rotateCameraZ = 0;
	float rotateCameraX = PI / 2.5f;

	// Noise vars
	float accel = 0;
	float[][] terrain;

	// Audio imports
	Minim minim;
	AudioPlayer song;
	AudioMetaData meta;
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

	public void settings() {
		size(800, 600, P3D);
	}

	public void setup() {
		cols = w / scl;
		rows = h / scl;
		terrain = new float[cols][rows];
		
		colorMode(RGB);	// Can be in RGB or HSB

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
			populateNoise();
		}
		
		int bandIncr = 0;
		// Actually draw it
		for (int y = 0; y < rows - 1; y++) {
			beginShape(TRIANGLE_STRIP);
			if (song.isPlaying()) {
				bandIncr = (int) (lows * 0.03f);
				float intensity = fft.getBand(y % (int) (fft.specSize() * specHi));
				int displayColor = color(lows * 0.67f, mids * 0.67f, highs * 0.67f);
				//int displayColor = color(fft.getBand((int) (fft.specSize() * specMid)) % 256, 255, 255);
				fill(displayColor, intensity * 5);
				stroke(intensity * 5);
			} else {
				noFill();
				stroke(200);
			}
			for (int x = 0; x < cols; x++) {
				vertex(x * scl, y * scl, terrain[x][y]);
				vertex(x * scl, (y + 1) * scl, terrain[x][y + 1]);
			}
			endShape();
		}
		bandIncr = 0;
	}

	public void keyPressed() {
		if (keyCode == UP) {
			rotateCameraX -= 0.1;
		}
		if (keyCode == DOWN) {
			rotateCameraX += 0.1;
		}
		if (keyCode == RIGHT) {
			rotateCameraZ -= 0.1;
		}
		if (keyCode == LEFT) {
			rotateCameraZ += 0.1;
		}
		if (key == 'f' & (song.getGain() >= -80)) {
			songGain -= 2;
		}
		if (key == 'r' & (song.getGain() <= 0)) {
			songGain += 2;
		}
		if (key == 'o') {
			song.play(songPos);
		}
		if (key == 'p') {
			songPos = song.position();
			song.pause();
		}
	}

	public void setSong(String file) {
		try {
			song = minim.loadFile(file);
			meta = song.getMetaData();
		} catch (Exception NullPointerException) {
			// Literally do nothing cause minim will have problems with you
		}
		fft = new FFT(song.bufferSize(), song.sampleRate());
	}

	private void populateNoise() {
		float xoff = accel;
		for (int y = 0; y < rows; y++) {
			float yoff = 0;
			for (int x = 0; x < cols; x++) {
				terrain[x][y] = map(noise(xoff, yoff), 0, 1, -75, 75);
				/*
				 * What map will do is take perlin noise's double output,-1 to
				 * 1, and will multiply that between some lower range and some
				 * positive range numbers.
				 */ yoff += 0.22;
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

		for (int i = 0; i < fft.specSize() * specLow; i++) {
			lows += fft.getBand(i);
		}

		for (int i = (int) (fft.specSize() * specLow); i < fft.specSize() * specMid; i++) {
			mids += fft.getBand(i);
		}

		for (int i = (int) (fft.specSize() * specMid); i < fft.specSize() * specHi; i++) {
			highs += fft.getBand(i);
		}

		// Will slow down any instant loss in sound by decrease rate
		if (oldLow > lows) {
			lows = oldLow - decreaseRate;
		}

		if (oldMid > mids) {
			mids = oldMid - decreaseRate;
		}

		if (oldHigh > highs) {
			highs = oldHigh - decreaseRate;
		}

		bandsComb = 0.66f * lows + 0.8f * mids + 1 * highs;
	}
	
	//creates an array of strings that hold the title, album, genre, and author
	
	public String[] MetaString (){
		String[] metadata = {meta.title(), meta.album(), meta.genre(), meta.author()};
		return metadata;
	}

	public class TerrainPart {

	}

}
