package com.game.engine;

import javax.swing.JFrame;

public class GameWindow {
	
	private JFrame window;
	private GameComponents component;
	
	public GameWindow(Game game){
		
		component = new GameComponents(game);
		window = new JFrame(game.title);
		
		window.setSize(game.width, game.height);
		window.setIconImage(game.iconImage);
		
		window.add(component);
		component.requestFocus();
		
		window.setLocale(null);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setVisible(true);
	}
	
	public GameComponents getComponents(){
		return component;
	}
	
	public void resize(int width, int height){
		window.setSize(width, height);
	}
}
