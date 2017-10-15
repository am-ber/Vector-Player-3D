package com.vexoid.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.vexoid.game.MainGame;
import com.vexoid.game.SoundManager;

public class ScreenManager {
	
	private Screen currentScreen;
	private String difficulty;
	private static int wait = 0;
	
	public void setScreen(Screen screen, String difficulty) {
		this.difficulty = difficulty;
		if (currentScreen !=null) currentScreen.dispose();
		currentScreen = screen;
		currentScreen.create(this, difficulty);
	}
	
	public Screen getCurrentScreen() {
		return currentScreen;
	}
	public void screenManagement(){
		difficulty = MainGame.getGameDifficulty();
		if(wait > 0){
			wait --;
		}
		if (Gdx.input.isKeyPressed(Keys.ENTER) && (getCurrentScreen().whatScreen() == "MenuScreen") && wait == 0){
			getCurrentScreen().dispose();
			SoundManager.stopMusic();
			setScreen(new GameScreen(), difficulty);
			wait = 50;
		} else
		if (Gdx.input.isKeyPressed(Keys.ENTER) && (getCurrentScreen().whatScreen() == "GameOverScreen") && wait == 0){
			getCurrentScreen().dispose();
			SoundManager.stopMusic();
			setScreen(new MenuScreen(), difficulty);
			wait = 50;
		} else
		if (Gdx.input.isKeyPressed(Keys.ESCAPE) && (getCurrentScreen().whatScreen() == "OptionsScreen")
				&& !OptionsScreen.isChanging() && wait == 0){
			getCurrentScreen().dispose();
			setScreen(new MenuScreen(), difficulty);
			wait = 50;
		} else
		if (Gdx.input.isKeyPressed(Keys.O) && (getCurrentScreen().whatScreen() == "MenuScreen") && wait == 0){
			getCurrentScreen().dispose();
			setScreen(new OptionsScreen(), difficulty);
			wait = 50;
		} else
		if (Gdx.input.isKeyPressed(Keys.ENTER) && (getCurrentScreen().whatScreen() == "SplashScreen") && wait == 0){
			getCurrentScreen().dispose();
			setScreen(new MenuScreen(), difficulty);
			wait = 50;
		} 
		else if (Gdx.input.isKeyPressed(Keys.ENTER) || Gdx.input.isKeyPressed(Keys.ESCAPE))
			wait = 20;
	}
}
