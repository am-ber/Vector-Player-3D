package com.vexoid.game.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vexoid.game.MainGame;
import com.vexoid.game.SoundManager;
import com.vexoid.game.TextureManager;
import com.vexoid.game.camera.OrthoCamera;
import com.vexoid.game.entity.EntityManager;
import com.vexoid.game.level.IntroLevel;
import com.vexoid.game.level.Level;
import com.vexoid.game.level.LevelManager;

public class GameScreen extends Screen{
	private OrthoCamera camera;
	private static EntityManager entityManager;
	private static LevelManager levelManager;
	public static int basicEnemiesCount = 4;
	public String gameDifficulty;
	int distance;
	public static int count = 0, step = 1;
	private static Level level = new IntroLevel();

	String displayDistance;
	String displayScore;
	String displayPlayerHealth;
	String displayPlayerLives;
	String displayPlayerBulletMode;
	String displayDifficulty;
	String displayLevel;
	String displayIntroText;
	String infinity = "";

	BitmapFont displayDistanceFont;
	BitmapFont displayScoreFont;
	BitmapFont displayPlayerHealthFont;
	BitmapFont displayPlayerLivesFont;
	BitmapFont displayPlayerBulletModeFont;
	BitmapFont displayDifficultyFont;
	BitmapFont displayLevelFont;
	BitmapFont displayIntroTextFont;
	BitmapFont displayEndLevelDistanceTextFont;
	BitmapFont displayEndLevelScoreTextFont;
	
	
	Texture shootingMode = TextureManager.HUD_SPREAD2;
	
	
	public void create(ScreenManager screenManager, String difficulty) {
		gameDifficulty = difficulty;
		camera = new OrthoCamera();
		camera.resize();
		
		entityManager = new EntityManager(camera,screenManager ,gameDifficulty);
		levelManager = new LevelManager(screenManager, entityManager,gameDifficulty);
		LevelManager.setCurrentLevel(level);
		LevelManager.setCurrentLevelStep(step);
		
	    displayDistanceFont = new BitmapFont();
	    displayScoreFont = new BitmapFont();
	    displayPlayerHealthFont = new BitmapFont();
	    displayPlayerLivesFont = new BitmapFont();
	    displayPlayerBulletModeFont = new BitmapFont();
	    displayDifficultyFont = new BitmapFont();
	    displayLevelFont = new BitmapFont();
	    displayIntroTextFont = new BitmapFont();
	    displayEndLevelDistanceTextFont = new BitmapFont();
	    displayEndLevelScoreTextFont = new BitmapFont();
	}
	
	public void update() {
		camera.update();
		entityManager.update();
		levelManager.update();
		
		count = LevelManager.getCurrentLevel().getCount();
		if(levelManager.getLevel() == 0){
			if(LevelManager.getCurrentLevel().getCount() <= 5)
				displayIntroText = "Light Years from home, you fly your ship...";
			if(LevelManager.getCurrentLevel().getCount() > 5)
				displayIntroText = "Get ready to fight...";
		}
		if(levelManager.getLevel() > 0){
			entityManager.listenForKeys();
		}
		if(levelManager.getLevel() == 9)
			infinity = "Infinity";
		else
			infinity = "" + levelManager.getLevel();
		
		if(entityManager.getPlayerShootingMode() == "Narrow")
			shootingMode = TextureManager.HUD_SPREAD2;
		else
			shootingMode = TextureManager.HUD_SPREAD1;
		
		displayScore = "Score : " + entityManager.getScore();
		displayPlayerHealth = "Health : " + (int) entityManager.checkPlayerHealth() + "%";
		displayPlayerLives = "Lives : " + entityManager.getPlayerLives();
		displayPlayerBulletMode = "Mode : " + entityManager.getPlayerBulletMode();
		displayDifficulty = "Difficulty : " + gameDifficulty;
		displayDistance = "Distance : " + LevelManager.getCurrentLevel().getDistance() + " Km";
		displayLevel = "Level : " + infinity;
		
		if(entityManager.isGameOver()){
			SoundManager.setMusic(SoundManager.endMusic, 0.8f, true);
		}
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		entityManager.render(sb);
		
		sb.draw(entityManager.getScreenEffect(), 0, 0);
		
	if(levelManager.getLevel() == 0){
		displayIntroTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayIntroTextFont.draw(sb, displayIntroText, (MainGame.WIDTH/2)-100, (MainGame.HEIGHT/2));
	}
	
	if(LevelManager.getCurrentLevel().didPlayerFinish()){
		displayEndLevelDistanceTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayEndLevelDistanceTextFont.draw(sb, "You completed the level in: " + LevelManager.getCurrentLevel().getDistance() + " Km",
				(MainGame.WIDTH/2)-100, (MainGame.HEIGHT/2));
		displayEndLevelScoreTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayEndLevelScoreTextFont.draw(sb, "With a score of: " + LevelManager.getCurrentLevel().getLevelScore(),
				(MainGame.WIDTH/2)-100, (MainGame.HEIGHT/3));
		
	} else
		if(entityManager.isGameOver()){
			displayEndLevelDistanceTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			displayEndLevelDistanceTextFont.draw(sb, "You died at: " + LevelManager.getCurrentLevel().getDistance() + " Km",
					(MainGame.WIDTH/2)-100, (MainGame.HEIGHT/2));
			displayEndLevelScoreTextFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			displayEndLevelScoreTextFont.draw(sb, "With a score of: " + LevelManager.getCurrentLevel().getLevelScore(),
					(MainGame.WIDTH/2)-100, (MainGame.HEIGHT/3));
		}
	
	if(levelManager.getLevel() > 0){
		displayDistanceFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayDistanceFont.draw(sb, displayDistance, 25, (MainGame.HEIGHT)-20);
		
		displayScoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayScoreFont.draw(sb, displayScore, (MainGame.WIDTH/2)-50, (MainGame.HEIGHT)-20);
		
		displayPlayerHealthFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayPlayerHealthFont.draw(sb, displayPlayerHealth, 25, 35);

		sb.draw(shootingMode, ((MainGame.WIDTH - TextureManager.HUD_SPREAD2.getWidth())),
				TextureManager.HUD_SPREAD2.getHeight() / 3);
		//(MainGame.WIDTH)-125, 35
		
		displayPlayerBulletModeFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayPlayerBulletModeFont.draw(sb, displayPlayerBulletMode, (MainGame.WIDTH)-100, 25);
		
		displayPlayerLivesFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayPlayerLivesFont.draw(sb, displayPlayerLives, 25, 50);
		
		displayDifficultyFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayDifficultyFont.draw(sb, displayDifficulty, (MainGame.WIDTH)-125, (MainGame.HEIGHT)-20);
		
		displayLevelFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayLevelFont.draw(sb, displayLevel, (MainGame.WIDTH)-225, (MainGame.HEIGHT)-20);
		
	}
		sb.end();
	}
	public static boolean isGameOver(){
		return entityManager.isGameOver();
	}
	public static int getDistance(){
		return LevelManager.getCurrentLevel().getDistance();
	}
	public static int getScore(){
		return entityManager.getScore();
	}
	public static void setLevel(Level level){
		GameScreen.level = level;
	}
	
	public static void setLevelStep(int step){
		GameScreen.step = step;
		level.setStep(step);
	}
	
	public void resize(int width, int height) {
		camera.resize();
	}

	public void dispose() {}

	public void pause() {}

	public void resume() {}
	
	public String whatScreen() {
		return "GameScreen";
	}
}
