package com.game.engine;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * This 2D game engine was made by Zachary Vanscoit for a class project
 * of Computer Science 220 at SIU university 2017
 * 
 * This is version 0.1
 */
public abstract class Game {

	protected String title="My2DGame";
	protected int width=800, height=600, scale=1;
	protected BufferedImage iconImage=null;
	protected boolean gameOver, paused;
	
	public static int frameRate = 60;
	
	public Game(){}
	
	public Game(String title){
		this.title = title;
	}
	
	public void setFrameRate(int rate) {
		frameRate = rate;
	}
	
	public Game(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	public Game(String title, int width, int height, int scale){
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
	}
	
	public Game(String title, BufferedImage iconImage){
		this.title = title;
		this.iconImage = iconImage;
	}
	
	public Game(String title, int width, int height, BufferedImage iconImage){
		this.title = title;
		this.width = width;
		this.height = height;
		this.iconImage = iconImage;
	}
	
	public Game(String title, int width, int height, int scale, BufferedImage iconImage){
		this.title = title;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.iconImage = iconImage;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public abstract void update();
	
	//	We make this to pass through to the game window class for when the launcher extends this class.
	public abstract void draw(Graphics2D g);
}
