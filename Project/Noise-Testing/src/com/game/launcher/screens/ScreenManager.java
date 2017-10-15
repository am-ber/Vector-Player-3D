package com.game.launcher.screens;

import com.game.engine.Game;

public class ScreenManager {

	protected Game game;
	private Screen currentScreen;
	
	public ScreenManager(Game game){
		this.game = game;
	}
	
	// Checks if the current screen is not null then nulls it
	// then sets the screen and creates it
	public void setScreen(Screen screen){
		if(currentScreen != null) currentScreen = null;
		currentScreen = screen;
		currentScreen.create(this);
	}
	public Screen getCurrentScreen(){
		return currentScreen;
	}
	public Game game(){
		return game;
	}
}
