package core;

import java.io.File;
import java.util.ArrayList;

import core.UI.ButtonStruct;
import core.components.Helper;
import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PVector;

public class VP3D_Launcher extends PApplet {

	public static void main(String[] args) {
		PApplet.main("core.VP3D_Launcher");
	}
	
	// object imports
	private PeasyCam camera;
	private MusicHandler musicHandler;
	
	// ui components
	private ArrayList<ButtonStruct> buttons;
	private int bTransition = 0;
	
	// procedural controllers
	private PVector noiseMovement;
	private int pointCountX = 40, pointCountY = 40;
	private float gridScale = 100;
	private float noiseSpread = 0.08f;
	private float noiseIncre = 0.005f;
	private float heightDist = 300;
	private float heightDistDisp = 300;
	private float oldHeightDistDisp = 300;
	private float displayFloatBands[];
	
	public void settings() {
		size(1280, 720, P3D);
	}
	
	// processing setup method
	public void setup() {
		// camera init
		camera = new PeasyCam(this, 2000);
		camera.setSuppressRollRotationMode();
		perspective(PI / 2, (float)width/height, 0.01f, 10000000);
		
		colorMode(HSB, 100);
		surface.setTitle("Procedural Player 3D");
		
		displayFloatBands = new float[4];
		noiseMovement = new PVector();
		musicHandler = new MusicHandler(this);
		musicHandler.setSong("res/Nelver-Save Yourself.mp3");
		
		initButton();
	}
	
	// processing draw loop
	public void draw() {
		update();
		// first things to do when drawing
		background(0);
		rotateX(PI / 3);
		
		noFill();
		strokeWeight(1);
		
		// shape drawing
		float colorIntensity = 90;
		pushMatrix();
		translate(-((gridScale * pointCountX) / 2), -((gridScale * pointCountY) / 2), 0);
		for (int y = 0; y < pointCountY; y++) {
			float colorNoise = map(noise(y * noiseSpread + noiseMovement.x), 0, 1, 0, 100);
			if (musicHandler.isThereSound) {
				float indexRange = ((float) y / pointCountY);
				if (indexRange < musicHandler.sub_range) {
					colorIntensity = map(musicHandler.subs, 0, 500, 0, 100);
				} else if (indexRange < (musicHandler.low_range + musicHandler.sub_range)) {
					colorIntensity = map(musicHandler.lows, 0, 400, 0, 100);
				} else if (indexRange < (musicHandler.mid_range + musicHandler.low_range + musicHandler.sub_range)) {
					colorIntensity = map(musicHandler.mids, 0, 300, 0, 100);
				} else if (indexRange < (musicHandler.high_range + musicHandler.mid_range
											+ musicHandler.low_range + musicHandler.sub_range)) {
					colorIntensity = map(musicHandler.highs, 0, 200, 0, 100);
				}
			} else {
				colorIntensity = lerp(colorIntensity, 90, 0.05f);
			}
			
			// Begin a triangle strip row
			beginShape(TRIANGLE_STRIP);
			for (int x = 0; x < pointCountX; x++) {
				stroke(colorNoise, bTransition, colorIntensity);
				// We first calculate noise
				float noise1 = map(noise(x * noiseSpread, y * noiseSpread + noiseMovement.y), 0, 1, -heightDistDisp, heightDistDisp);
				float noise2 = map(noise(x * noiseSpread, (y + 1) * noiseSpread + noiseMovement.y), 0, 1, -heightDistDisp, heightDistDisp);
				// Then add vertex to each shape
				vertex(x * gridScale, y * gridScale, noise1);
				vertex(x * gridScale, (y + 1) * gridScale, noise2);
			}
			endShape();
		}
		noiseMovement.y -= (noiseIncre * map(musicHandler.intensity, 0, 1000, 0.3f, 1.5f));
		noiseMovement.x += noiseIncre;
		popMatrix();
		
		// ui drawing
		camera.beginHUD();
		drawUI();
		drawFreqBands();
		camera.endHUD();
	}
	
	public void update() {
		if (musicHandler.isThereSound) {
			bTransition = (int) lerp(bTransition, 100, 0.05f);
			musicHandler.update();
			displayFloatBands = Helper.lerpFloatArray(musicHandler.getBandArray(), displayFloatBands, 0.02f);
			oldHeightDistDisp = heightDistDisp;
			heightDistDisp = lerp(oldHeightDistDisp, (heightDist * map(musicHandler.intensity, 0, 1000, 1, 2)), 0.2f);
		} else {
			bTransition = (int) lerp(bTransition, 0, 0.05f);
			heightDistDisp = heightDist;
		}
	}
	
	// button init
	public void initButton() {
		buttons = new ArrayList<ButtonStruct>();
		
		buttons.add(new ButtonStruct(this, "playSong", new PVector(10, 10), new PVector(40, 24), color(10, 100, 100), false, () -> {
			musicHandler.toggleSong();
		}).setFont("Play", 2, 18, color(100, 0, 100)));
	}
	
	// processing mouse press method
	public void mousePressed() {
		for (ButtonStruct bs : buttons) {
			if (bs.clicked(mouseX, mouseY))
				bs.function();
		}
	}
	
	// processing key press method
	public void keyPressed() {
		if (keyCode == 32)		// Space bar is 32 for OpenGL
			musicHandler.toggleSong();
	}
	
	// file select callback
	public void fileSelected(File selection) {
		if (selection == null) {
			println("Window was closed or the user hit cancel.");
		} else {
			println("User selected " + selection.getAbsolutePath());
			try {
				musicHandler.setSong(selection.getAbsolutePath());
			} catch(Exception e) {
				println("Which is not an audio file...");
				println("Please use supported audio files from the library minim.");
			}
		}
	}
	
	// call to draw bands long the bottom
	public void drawFreqBands() {
		noFill();
		float barWidth = width / musicHandler.fft.specSize();
		for (int i = 0; i < musicHandler.fft.specSize(); i++) {
			if (i < (musicHandler.fft.specSize() * musicHandler.sub_range))
				stroke(0, 25, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range))
				stroke(50, 25, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range)
					 + (musicHandler.fft.specSize() * musicHandler.mid_range))
				stroke(75, 25, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range)
					+ (musicHandler.fft.specSize() * musicHandler.mid_range) + (musicHandler.fft.specSize() * musicHandler.high_range))
				stroke(100, 25, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range)
					+ (musicHandler.fft.specSize() * musicHandler.mid_range) + (musicHandler.fft.specSize() * musicHandler.high_range)
					+ (musicHandler.fft.specSize() * musicHandler.unused_range))
				stroke(100, 0, 25);
			line((i * barWidth), height - 10, (i * barWidth), (height - 10) - (musicHandler.fft.getBand(i) * 5) - 10);
		}
	}
	
	// called to draw the UI
	public void drawUI() {
		noFill();
		stroke(0, 0, 100);
		textSize(12);
		textAlign(RIGHT);
		text("FPS: " + (int) (frameRate), width - 2, 14);
		textAlign(LEFT);
		text("Intensity: " + (int) (musicHandler.intensity) +
				"\nSubs: " + (int) (displayFloatBands[0]) +
				"\nLows: " + (int) (displayFloatBands[1]) +
				"\nMids: " + (int) (displayFloatBands[2]) +
				"\nHighs: " + (int) (displayFloatBands[3]), 60,  10);
		text("Highest individual: " + (int) (musicHandler.highestIndividual)+
				"\nHighest Sub: " + (int) (musicHandler.highestSub) +
				"\nHighest Low: " + (int) (musicHandler.highestLow) +
				"\nHighest Mid: " + (int) (musicHandler.highestMid) +
				"\nHighest High: " + (int) (musicHandler.highestHigh), 180,  10);
		
		for (ButtonStruct bs : buttons) {
			bs.draw();
		}
	}
}