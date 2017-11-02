package com.game.launcher;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.game.engine.Game;
import com.game.engine.GameThread;
import com.game.launcher.screens.Mp3Screen;
import com.game.launcher.screens.ScreenManager;

public class Launcher1D extends Game{


	protected ScreenManager screenManager;
	
	// The main method which starts the game thread
	public static void main(String[] args){
		
		try{
			
			BufferedImage iconImage = ImageIO.read(new File("res/images/iconImage/controller-icon.png"));
			
			Launcher1D start = new Launcher1D("Noise Testing 1-D", 800, 600, 1, iconImage);
			
			new GameThread(start).start();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Launcher1D(String title, int width, int height, int scale, BufferedImage iconImage){
		super(title, width, height, scale, iconImage);
		screenManager = new ScreenManager(this);
		screenManager.setScreen(new Mp3Screen());
	}
	
	// Anything inside the update() function will run from the loop
	@Override
	public void update() {
		screenManager.getCurrentScreen().update();
	}
	public boolean oneTime = true;
	@Override
	public void draw(Graphics2D g) {
		screenManager.getCurrentScreen().draw(g);
		
	}
}
