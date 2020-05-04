package core;

import java.io.File;
import java.util.ArrayList;

import core.UI.ButtonStruct;
import core.UI.Slider;
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
	public ArrayList<ButtonStruct> buttons;				// Button array
	private Slider scruberSlider;						// Used for song position of current song
	private boolean scruberLocked = false;				// Used to lock scrub position
	private boolean scruberMusicState = false;			// Retains the music state of playing or not
	private int bTransition = 0;						// Band transition value
	private boolean drawDebug = false;					// Used for the debug displaying
	private boolean uiMenuActive = true;				// Used for dynamic ui menu along bottom
	private float uiMenuSize = 100;						// Used for ui menu
	
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
		
		colorMode(HSB, 360, 100, 100, 1);
		surface.setTitle("Procedural Player 3D");
		
		displayFloatBands = new float[4];
		noiseMovement = new PVector();
		musicHandler = new MusicHandler(this);
		musicHandler.setSong("res/Nelver-Save Yourself.mp3");
		
		initButtons();
		initSliders();
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
		int index = 0;
		float rotation = 0;
		translate(-((gridScale * pointCountX) / 2), -((gridScale * pointCountY) / 2), 0);
		pushMatrix();
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
				
				index ++;
			}
			endShape();
		}
		noiseMovement.y -= (noiseIncre * map(musicHandler.intensity, 0, 1000, 0.3f, 1.5f));
		noiseMovement.x += noiseIncre;
		popMatrix();
		
		// ui drawing
		camera.beginHUD();
		drawUI();
		if (drawDebug)
			drawFreqBands();
		camera.endHUD();
	}
	
	public void update() {
		if (musicHandler.isThereSound) {
			bTransition = (int) lerp(bTransition, 100, 0.05f);
			musicHandler.update();
			
			scruberSlider.updateValue(musicHandler.songPos);
			
			// intensity height display
			oldHeightDistDisp = heightDistDisp;
			heightDistDisp = lerp(oldHeightDistDisp, (heightDist * map(musicHandler.intensity, 0, 1000, 1, 2)), 0.2f);
			
			if (drawDebug)
				displayFloatBands = Helper.lerpFloatArray(musicHandler.getBandArray(), displayFloatBands, 0.02f);
		} else {
			bTransition = (int) lerp(bTransition, 0, 0.05f);
			heightDistDisp = heightDist;
		}
	}
	
	// button init
	public void initButtons() {
		buttons = new ArrayList<ButtonStruct>();
		
		buttons.add(new ButtonStruct(this, "playSong", new PVector(10, 10), new PVector(40, 24), color(10, 100, 100), false, () -> {
			musicHandler.toggleSong();
		}).setFont("Play", 2, 18, color(100, 0, 100)));
	}
	
	public void initSliders() {
		scruberSlider = new Slider(this, new PVector(10, height - 50), new PVector(width - 10, 10), 0, musicHandler.songLength, true);
	}
	
	// processing mouse press method
	public void mousePressed() {
		for (ButtonStruct bs : buttons) {
			if (bs.clicked(mouseX, mouseY))
				bs.function();
		}
		if (scruberSlider.clicked(mouseX, mouseY)) {
			println("Scruber locked");
			scruberLocked = true;
			scruberMusicState = musicHandler.isThereSound;
			if (scruberMusicState)
				musicHandler.toggleSong(false);
		}
	}
	
	// processing mouse released method
	public void mouseReleased() {
		if (scruberLocked) {
			println("Scruber unlocked");
			if (scruberMusicState)
				musicHandler.toggleSong(scruberMusicState);
			scruberLocked = false;
		}
	}
	
	// processing key press method
	public void keyPressed() {
		if (keyCode == 32)		// Space bar is 32 for OpenGL
			musicHandler.toggleSong();
		if (keyCode == 97)		// F1 key is 97 for OpenGL
			drawDebug = !drawDebug;
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
	
	// call to draw bands long the bottom for debugging
	public void drawFreqBands() {
		noFill();
		float barWidth = width / musicHandler.fft.specSize();
		for (int i = 0; i < musicHandler.fft.specSize(); i++) {
			if (i < (musicHandler.fft.specSize() * musicHandler.sub_range))
				stroke(0, 35, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range))
				stroke(90, 35, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range)
					 + (musicHandler.fft.specSize() * musicHandler.mid_range))
				stroke(180, 35, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range)
					+ (musicHandler.fft.specSize() * musicHandler.mid_range) + (musicHandler.fft.specSize() * musicHandler.high_range))
				stroke(270, 35, 100);
			else if (i < (musicHandler.fft.specSize() * musicHandler.sub_range) + (musicHandler.fft.specSize() * musicHandler.low_range)
					+ (musicHandler.fft.specSize() * musicHandler.mid_range) + (musicHandler.fft.specSize() * musicHandler.high_range)
					+ (musicHandler.fft.specSize() * musicHandler.unused_range))
				stroke(360, 0, 25);
			line((i * barWidth), height - 10, (i * barWidth), (height - 10) - (musicHandler.fft.getBand(i) * 5) - 10);
		}
	}
	
	// called to draw the UI
	public void drawUI() {
		if (drawDebug) {
			noStroke();
			fill(17, 20, 100);
			textSize(14);
			textAlign(RIGHT);
			text("FPS: " + (int) (frameRate), width - 2, 14);
			textAlign(LEFT);
			text("Intensity: " + (int) (musicHandler.intensity) +
					"\nSubs: " + (int) (displayFloatBands[0]) +
					"\nLows: " + (int) (displayFloatBands[1]) +
					"\nMids: " + (int) (displayFloatBands[2]) +
					"\nHighs: " + (int) (displayFloatBands[3]), 60,  16);
			text("Highest individual: " + (int) (musicHandler.highestIndividual)+
					"\nHighest Sub: " + (int) (musicHandler.highestSub) +
					"\nHighest Low: " + (int) (musicHandler.highestLow) +
					"\nHighest Mid: " + (int) (musicHandler.highestMid) +
					"\nHighest High: " + (int) (musicHandler.highestHigh), 180,  16);
			textSize(16);
			text("DEBUG ON", 520, 16);
			noFill();
			stroke(17, 20, 100);
			strokeWeight(2);
			rect(0, 0, width - 1, height - 1);
		} else {
			if (uiMenuActive) {
				if (mouseY > height - uiMenuSize)
					camera.setActive(false);
				else
					camera.setActive(true);
				
				noStroke();
				fill(0, 0, 0, 0.75f);
				rect(0, height - uiMenuSize, width, uiMenuSize);
				scruberSlider.draw();
				
				if (scruberLocked) {
					float mappedMouse = map(mouseX, 0, width, scruberSlider.beginningValue, scruberSlider.endingValue);
					scruberSlider.updateValue(mappedMouse);
					musicHandler.songPos = (int) mappedMouse;
				}
			}
		}
		for (ButtonStruct bs : buttons) {
			bs.draw();
		}
	}
}