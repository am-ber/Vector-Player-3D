package com.game.engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class GameComponents extends JComponent implements ComponentListener{

	private final Game game;
	private Graphics g;
	public GameComponents(Game game){
		this.game = game;
	}
	
	//	This will run the graphics through the game draw
	@Override
	public void paintComponent(Graphics g){
		addComponentListener(this);
		game.draw((Graphics2D) g);
		this.g = g;
	}
	
	public Graphics getG(){
		return g;
	}
	// These would all have Override flags but java will put
	// them by default anyway so I wanted to clean it up
	
	public void componentHidden(ComponentEvent arg0) {}

	public void componentMoved(ComponentEvent arg0) {}

	public void componentResized(ComponentEvent arg0) {}

	public void componentShown(ComponentEvent arg0) {}
	
}
