package core;

import java.util.ArrayList;

import core.UI.ButtonStruct;
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
	
	public void settings() {
		size(1280, 720, P3D);
	}
	
	// processing setup method
	public void setup() {
		// camera init
		camera = new PeasyCam(this, 1000);
		camera.setSuppressRollRotationMode();
		perspective(PI / 2, (float)width/height, 0.01f, 10000000);
		
		colorMode(HSB, 100);
		surface.setTitle("Procedural Player 3D");
		
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
				if ((y / pointCountY) < musicHandler.sub_range) {
					colorIntensity = map(musicHandler.subs, 0, 400, 0, 100);
				} else if ((y / pointCountY) + musicHandler.sub_range < musicHandler.low_range) {
					colorIntensity = map(musicHandler.lows, 0, 400, 0, 100);
				} else if ((y / pointCountY) + musicHandler.low_range < musicHandler.mid_range) {
					colorIntensity = map(musicHandler.mids, 0, 400, 0, 100);
				} else if ((y / pointCountY) + musicHandler.mid_range < musicHandler.high_range) {
					colorIntensity = map(musicHandler.highs, 0, 400, 0, 100);
				}
			} else {
				colorIntensity = lerp(colorIntensity, 90, 0.05f);
			}
			stroke(colorNoise, bTransition, colorIntensity);
			
			// Begin a triangle strip row
			beginShape(TRIANGLE_STRIP);
			for (int x = 0; x < pointCountX; x++) {
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
		camera.endHUD();
	}
	
	public void update() {
		if (musicHandler.isThereSound) {
			bTransition = (int) lerp(bTransition, 100, 0.05f);
			musicHandler.update();
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
	
	public void mousePressed() {
		for (ButtonStruct bs : buttons) {
			if (bs.clicked(mouseX, mouseY))
				bs.function();
		}
	}
	
	// called to draw the UI
	public void drawUI() {
		noFill();
		stroke(255);
		textSize(12);
		textAlign(RIGHT);
		text("FPS: " + (int) (frameRate), width - 2, 14);
		textAlign(LEFT);
		text("Intensity: " + (int) (musicHandler.intensity), 10, height - 16);
		
		for (ButtonStruct bs : buttons) {
			bs.draw();
		}
	}
}