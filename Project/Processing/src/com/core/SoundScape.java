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

// Drawing vars
	int cols, rows;
	int scl = 60; // For slower computers obviously scale up
	// width and height of noise grid
	int w = 4000;
	int h = 4000;
	PFont font;
	
// Button Vars
	boolean btnFileOver, btnPlayOver, btnVerticalOver, btnMetaOver;
	int padding = 10, btnFileX = padding, btnY = padding, btnHeight = 50, btnWidth = 110, btnPlayX = btnFileX + btnWidth + padding, btnMetaX = btnPlayX + btnWidth + padding;

// Meta Vars
	int metaPanelX = padding, metaPanelWidth = width - padding;
	int metaPanelY = btnY + btnHeight + padding, metaTextHeight = 40, metaTextX = metaPanelX + padding, titleY = metaPanelY + padding, albumY = titleY + metaTextHeight + padding, authorY = albumY + metaTextHeight + padding, genreY = authorY + metaTextHeight + padding;
	String songTitle = "", songAlbum = "", songAuthor = "", songGenre = "";
	
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
		font = createFont("res/ariblk.ttf", 26);
		textFont(font);
		//textMode(SHAPE);
		
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
		
	// Drawing UI Elements
		btnVerticalOver = (mouseY >= btnY && mouseY <= btnY + btnHeight);
		btnFileOver = btnVerticalOver && (mouseX >= btnFileX && mouseX <= btnFileX + btnWidth);
		btnPlayOver = btnVerticalOver && (mouseX >= btnPlayX && mouseX <= btnPlayX + btnWidth);
		btnMetaOver = btnVerticalOver && (mouseX >= btnMetaX && mouseX <= btnMetaX + btnWidth);
		
		fill(240, 240, 240, btnFileOver?255:128);
		rect(btnFileX, btnY, btnWidth, btnHeight);
		fill(240, 240, 240, btnPlayOver?255:128);
		rect(btnPlayX, btnY, btnWidth, btnHeight);
		fill(240, 240, 240, btnMetaOver?255:128);
		rect(btnMetaX, btnY, btnWidth, btnHeight);
		fill(0);
		text("File", btnFileX + 28, btnY + 5, 90, 40);
		text((song.isPlaying()?"Pause":"Play"), btnPlayX + (song.isPlaying()?12:22), btnY + 5, 90, 40);
		text("Meta", btnMetaX + 20, btnY + 5, 90, 40);
		
		if(btnMetaOver){
			fill(0);
			rect(10, btnY + btnHeight + 10, width-20, 300);
			fill(255);
			noStroke();
		}
		
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
			//particleSystem.changePos();
		}
		for (int i = 0; i < shapesList.size(); i++) {
			shapesList.get(i).run(displayColor2, displayColor);
		}
		
		fill(240);
		text("I AM STRING YES", width * (width / 2), height * 2);
		
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
				vertex((x * scl) + xoffset, (y * scl) + yoffset, (terrain[x][y] + zoffset));
				vertex((x * scl) + xoffset, ((y + 1) * scl) + yoffset, (terrain[x][y + 1] + zoffset));
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
	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
		songGain += map(e, -1, 1, 2, -2);
	}
	public void keyPressed() {
		if (key == 'f' & (song.getGain() >= -30))
			songGain -= 2;
		if (key == 'r' & (song.getGain() <= 0))
			songGain += 2;
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
	
	public void mousePressed(){
		if(btnFileOver){
			song.pause();
			mousePressed = false;
			selectInput("Select a file to process:", "fileSelected");
		}else if(btnPlayOver){
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
		float previousBandValue = fft.getBand(0);
		int n = - (int)(((fft.specSize() * specLow) + (fft.specSize() * specMid))/4);
		if (song.isPlaying()) {
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
			lineAccel -= 0.03;
		}
	}
	
	public void refreshMetadata(){
		songTitle = meta.title();
		songAlbum = meta.album();
		songAuthor = meta.author();
		songGenre = meta.genre();
	}
}
