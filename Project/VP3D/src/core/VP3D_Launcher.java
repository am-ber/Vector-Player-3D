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
	private boolean uiMenuActive = false;				// Used for dynamic ui menu along bottom
	private float uiMenuSize = 100;						// Used for ui menu
	private float colorLerp = 0.015f;					// Color interpolation based on intensity (0.0 - 1.0)
	private float oldWidth = 0, oldHeight = 0;			// Screen size retention
	private PVector targetRotation;						// Used for roaming rotation
	private boolean roaming = false;					// Toggles camera to roaming mode
	private boolean toggleRoaming = false;				// Used for camera roaming
	private int timeToRoam = 0;							// Used for roaming timing
	
	// procedural controllers
	private PVector noiseMovement;
	private int pointCountX = 40, pointCountY = 40;
	private float gridScale = 100;
	private float noiseSpread = 0.08f;
	private float noiseIncre = 0.005f;
	private float heightDist = 300;
	private float heightDistDisp = 300;
	private float oldHeightDistDisp = 300;
	private float colorNoise = 0;
	private float oldColorNoise = 0;
	private float displayFloatBands[];
	private float oldIntensities[];
	private float averageIntensities[];
	
	public void settings() {
		size(1280, 720, P3D);
//		fullScreen(P3D, 1);
	}
	
	// processing setup method
	public void setup() {
		surface.setTitle("Procedural Player 3D");
		oldWidth = width;
		oldHeight = height;
		
		colorMode(HSB, 360, 100, 100, 1);
		
		// camera init
		camera = new PeasyCam(this, 2900);
		camera.setSuppressRollRotationMode();
		perspective(PI / 2, (float) width / height, 0.01f, 10000000);
		targetRotation = new PVector();
		
		displayFloatBands = new float[4];
		oldIntensities = new float[4];
		averageIntensities = new float[4];
		
		noiseMovement = new PVector();
		musicHandler = new MusicHandler(this);
		musicHandler.setSong("res/audiofreq-caged.mp3");
		
		initButtons();
		initSliders();
	}
	
	// processing draw loop
	public void draw() {
		if (checkResize())
			println("Window size changed!");
		update();
		// first things to do when drawing
		background(0);
		rotateX(PI / 3 + 0.1f);
		
		noFill();
		strokeWeight(1);
		
		// display noise loop
		float colorIntensity = 90;
		translate(-((gridScale * pointCountX) / 2), -((gridScale * pointCountY) / 2), 0);
		pushMatrix();
		// drawing columns
		for (int y = 0; y < pointCountY; y++) {
			if (musicHandler.isThereSound) {
				float indexRange = ((float) y / pointCountY);
				// set the intensity based on row and intensity mapping
				if (indexRange < musicHandler.sub_range) {
					colorIntensity = lerp(oldIntensities[0], map(musicHandler.subs, 0, averageIntensities[0], 0, 100), colorLerp);
					oldIntensities[0] = colorIntensity;
				} else if (indexRange < (musicHandler.low_range + musicHandler.sub_range)) {
					colorIntensity = lerp(oldIntensities[1], map(musicHandler.lows, 0, averageIntensities[1], 0, 100), colorLerp);
					oldIntensities[1] = colorIntensity;
				} else if (indexRange < (musicHandler.mid_range + musicHandler.low_range + musicHandler.sub_range)) {
					colorIntensity = lerp(oldIntensities[2], map(musicHandler.mids, 0, averageIntensities[2], 0, 100), colorLerp);
					oldIntensities[2] = colorIntensity;
				} else if (indexRange < (musicHandler.high_range + musicHandler.mid_range
											+ musicHandler.low_range + musicHandler.sub_range)) {
					colorIntensity = lerp(oldIntensities[3], map(musicHandler.highs, 0, averageIntensities[3], 0, 100), colorLerp);
					oldIntensities[3] = colorIntensity;
				}
			} else {
				colorIntensity = lerp(colorIntensity, 90, 0.05f);
			}
			
			// draw half a parabola
			float h = 0;
			float k = -((gridScale * pointCountX) * 0.1f);
			
			// Begin a triangle strip row
			beginShape(TRIANGLE_STRIP);
			for (int x = 0; x < pointCountX; x++) {
				float z = (2 * pow(((x - (pointCountX / 2)) - h), 2)) + k;
				
				colorNoise = lerp(oldColorNoise, map(noise(y * (noiseSpread * map(musicHandler.intensity, 0, 2000, 0.2f, 0.3f)) +
						noiseMovement.x), 0, 1, 0, 360), colorLerp);
				oldColorNoise = colorNoise;
				if (colorIntensity < 50)
					colorIntensity += musicHandler.fft.getBand((int) (map(y, 0, pointCountY, 0, musicHandler.fft.specSize())));
				stroke(colorNoise, bTransition, colorIntensity);
				// We first calculate noise
				float noise1 = map(noise(x * noiseSpread, y * noiseSpread + noiseMovement.y), 0, 1, -heightDistDisp, heightDistDisp);
				float noise2 = map(noise(x * noiseSpread, (y + 1) * noiseSpread + noiseMovement.y), 0, 1, -heightDistDisp, heightDistDisp);
				// Then add vertex to each shape
				vertex(x * gridScale, y * gridScale, noise1 + z);
				vertex(x * gridScale, (y + 1) * gridScale, noise2 + z);
			}
			endShape();
		}
		noiseMovement.y -= (noiseIncre * map(musicHandler.intensity, 0, 1000, 0.3f, 1.5f));
		noiseMovement.x += (noiseIncre * map(musicHandler.intensity, 0, 1000, 0.3f, 1.5f));
		popMatrix();
		
		// ui drawing
		camera.beginHUD();
		drawUI();
		if (drawDebug)
			drawFreqBands();
		camera.endHUD();
	}
	
	// processing mouse press method
	public void mousePressed() {
		println("Mouse Pressed");
		if (roaming)
			roaming = false;
		if (uiMenuActive) {
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
			if (mouseY < height - uiMenuSize)
				uiMenuActive = false;
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
		println("KeyPressed: " + keyCode);
		if (keyCode == 32)		// Space bar is 32 for OpenGL
			musicHandler.toggleSong();
		if (keyCode == 97)		// F1 key is 97 for OpenGL
			drawDebug = !drawDebug;
		if (keyCode == 82)
			roaming = true;
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
	
	//	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-
	// private methods
	
	// called from draw loop to update non-drawing variables
	private void update() {
		if (roaming) {
			doCameraRoaming();
		}
		if (musicHandler.isThereSound) {
			bTransition = (int) lerp(bTransition, 100, 0.05f);
			musicHandler.update();
			
			scruberSlider.updateValue(musicHandler.songPos);
			
			// average intensity ranges
			averageIntensities[0] = musicHandler.highestSub + ((musicHandler.fft.specSize() * musicHandler.sub_range) * 2);
			averageIntensities[1] = musicHandler.highestLow + ((musicHandler.fft.specSize() * musicHandler.low_range) * 2);
			averageIntensities[2] = musicHandler.highestMid + ((musicHandler.fft.specSize() * musicHandler.mid_range) * 2);
			averageIntensities[3] = musicHandler.highestHigh + ((musicHandler.fft.specSize() * musicHandler.high_range) * 2);
			
			
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
	
	// camera init
	private void initCamera() {
		camera = new PeasyCam(this, 2900);
		camera.setSuppressRollRotationMode();
		camera.setMinimumDistance(100);
		camera.setMaximumDistance(3500);
		perspective(PI / 2, (float) width / height, 0.01f, 10000000);
	}
	
	// button init
	private void initButtons() {
		buttons = new ArrayList<ButtonStruct>();
		
		buttons.add(new ButtonStruct(this, "playPause", new PVector(width / 2 - 20, height - uiMenuSize + 2),
				new PVector(40, 24), color(10, 100, 100), false, () -> {
			musicHandler.toggleSong();
		}).setFont("Play", 2, 18, color(100, 0, 100)));
		
		buttons.add(new ButtonStruct(this, "toggleRoaming", new PVector(width - 90, height - uiMenuSize + 2),
				new PVector(80, 24), color(10, 100, 100), false, () -> {
			roaming = true;
			uiMenuActive = false;
		}).setFont("Roaming", 2, 18, color(100, 0, 100)));
	}
	// slider init
	private void initSliders() {
		scruberSlider = new Slider(this, new PVector(10, height - 50), new PVector(width - 20, 10), 0, musicHandler.songLength, true);
	}
	
	// call to draw bands long the bottom for debugging
	private void drawFreqBands() {
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
	private void drawUI() {
		if (drawDebug) {
			float[] positions = camera.getPosition();
			float[] rotations = camera.getRotations();
			
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
					"\nHighs: " + (int) (displayFloatBands[3]), 10,  16);
			text("Highest individual: " + (int) (musicHandler.highestIndividual)+
					"\nHighest Sub: " + (int) (musicHandler.highestSub) +
					"\nHighest Low: " + (int) (musicHandler.highestLow) +
					"\nHighest Mid: " + (int) (musicHandler.highestMid) +
					"\nHighest High: " + (int) (musicHandler.highestHigh), 140,  16);
			text("Old Intensities\nSub: " + (int) (oldIntensities[0]) +
					"\nLow: " + (int) (oldIntensities[1]) +
					"\nMid: " + (int) (oldIntensities[2]) +
					"\nHigh: " + (int) (oldIntensities[3]), 320, 16);
			text("Noise Variables:\nColor Noise: " + (int) (colorNoise) +
					"\nnMovement X: " + nfc(noiseMovement.x, 2) +
					"\nnMovementy: " + nfc(noiseMovement.y, 2), 480, 16);
			text("Camera variables:\nPosition (x,y,z): " +
					(int) (positions[0]) + ", " + (int) (positions[1]) + ", " + (int) (positions[2]) +
					"\nRotation (x,y,z): " + nfc(rotations[0], 3) + ", " + nfc(rotations[1], 3) +
					", " + nfc(rotations[2], 3), 650, 16);
			text("Camera Roaming: " + roaming + "\nTarget (x,y,z): " +
					nfc(targetRotation.x, 3) + ", " + nfc(targetRotation.y, 3) + ", " + nfc(targetRotation.z, 3), 950, 16);
			textSize(16);
			textAlign(CENTER);
			fill(0, 0, 0);
			rect(width / 2 - 45, height / 2 - 15, 90, 20);
			fill(17, 20, 100);
			text("DEBUG ON", width / 2, height / 2);
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
				
				fill(0, 0, 0, 0.75f);
				stroke(0, 0, 100);
				rect(0, height - uiMenuSize, width, uiMenuSize);
				scruberSlider.draw();
				fill(0, 0, 100);
				textSize(14);
				textAlign(LEFT);
				text(Helper.printNiceMillis((int) (musicHandler.songPos)), 10, scruberSlider.position.y - 10);
				textAlign(RIGHT);
				text(Helper.printNiceMillis((int) (musicHandler.songLength)), width - 10, scruberSlider.position.y - 10);
				if (scruberLocked) {
					if (mouseX > scruberSlider.position.x & mouseX < scruberSlider.size.x) {
						float mappedMouse = map(mouseX, scruberSlider.position.x, scruberSlider.size.x,
								scruberSlider.beginningValue, scruberSlider.endingValue);
						scruberSlider.updateValue(mappedMouse);
						musicHandler.songPos = (int) mappedMouse;
					}
				}
				for (ButtonStruct bs : buttons) {
					bs.draw();
				}
			} else {
				if (mouseY > height - 30)
					uiMenuActive = true;
			}
		}
	}
	
	// camera roaming feature
	private void doCameraRoaming() {
		if (millis() > timeToRoam & !toggleRoaming) {
			toggleRoaming = true;
			targetRotation = new PVector(random(-PI / 2, PI / 2), random(-PI / 2, PI / 2), 0);
		}
		if (toggleRoaming) {
			float[] rotations = camera.getRotations();
			camera.setRotations(lerp(rotations[0], targetRotation.x, 0.01f),
					lerp(rotations[1], targetRotation.y, 0.01f), 0);
			
			if (rotations[0] >= targetRotation.x - 0.01f & rotations[0] <= targetRotation.x + 0.01f &
					rotations[1] >= targetRotation.y - 0.01f & rotations[1] <= targetRotation.y + 0.01f) {
				timeToRoam = millis() + (int) random(3000, 5000);
				toggleRoaming = false;
			}
		}
	}
	
	// checks if the screen changed sizes to alter UI
	private boolean checkResize() {
		if (oldWidth != width & oldHeight != height) {
			oldWidth = width;
			oldHeight = height;
			
			initCamera();
			initButtons();
			initSliders();
			
			return true;
		}
		return false;
	}
}