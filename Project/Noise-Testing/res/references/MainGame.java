package com.vexoid.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vexoid.game.screen.MenuScreen;
import com.vexoid.game.screen.ScreenManager;

public class MainGame extends ApplicationAdapter {
	
	public static int WIDTH = 800, HEIGHT = 600;
	private SpriteBatch batch;
	
	private static String difficulty = "medium";

	static Music CurrentMusic;
	ScreenManager screenManager = new ScreenManager();
	
	public void create () {
		new Options();
		batch = new SpriteBatch();
		screenManager.setScreen(new MenuScreen(), difficulty);
	    System.out.println("Difficulty = " + difficulty);
	}
	public void render () {
		screenManager.screenManagement();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (screenManager.getCurrentScreen() !=null) {
			screenManager.getCurrentScreen().update();
			screenManager.getCurrentScreen().render(batch);
		} else {System.out.println("Screen not there");}
	}

	public static void setDifficulty(String difficulty){
		MainGame.difficulty = difficulty;
	}
	public static String getGameDifficulty(){
		return difficulty;
	}
	public void dispose() {
		if (screenManager.getCurrentScreen() !=null)
			screenManager.getCurrentScreen().dispose();
		batch.dispose();
	}
	public void resize(int width, int height) {
		if (screenManager.getCurrentScreen() !=null)
			screenManager.getCurrentScreen().resize(width, height);
	}
	public void pause() {
		if (screenManager.getCurrentScreen() !=null)
			screenManager.getCurrentScreen().pause();
	}
	public void resume() {
		if (screenManager.getCurrentScreen() !=null)
			screenManager.getCurrentScreen().resume();
	}
}
