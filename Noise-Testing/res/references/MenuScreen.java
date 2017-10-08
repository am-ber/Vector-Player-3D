package com.vexoid.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vexoid.game.MainGame;
import com.vexoid.game.SoundManager;
import com.vexoid.game.TextureManager;
import com.vexoid.game.camera.OrthoCamera;
import com.vexoid.game.entity.effects.Stars_Class;
import com.vexoid.game.level.IntroLevel;
import com.vexoid.game.level.Level1;
import com.vexoid.game.level.LevelManager;

public class MenuScreen extends Screen{

	private OrthoCamera camera;
	private ScreenManager screenManager;
	String difficulty,options="Options";
	
	Texture title = TextureManager.TITLE_IMAGE;
	Texture menuTitle = TextureManager.MENU_IMAGE;
	Texture difficultyImage = TextureManager.DIFFICULTY_MEDIUM;
	
	BitmapFont displayOptions;
	
	public void create(ScreenManager screenManager, String difficulty) {
		this.screenManager = screenManager;
		this.difficulty = difficulty;
		camera = new OrthoCamera();
		camera.resize();
		
		displayOptions = new BitmapFont();
		SoundManager.setMusic(SoundManager.menuMusic, 0.8f, true);
		starVar= MathUtils.random(0,1);
	}

	int secondIncrease = 30;
	int starcount = 0, starVar= MathUtils.random(0,1);
	float speedx = 0;
	float y, x;
	static boolean clearedEntities = false;
	private int limit=104,starLimit=limit,starToggle=0;
	private final Array<Stars_Class> stars = new Array<Stars_Class>();
	
	private boolean cheatActive = false;
	private static boolean cheatActive2 = false;
	private int[] oneTime = {0,0,0,0};
	int Switch = 0, Toggle = 1, Switch2 = 0, Toggle2 = 1;
	
	private void addStars(Stars_Class entity) {
		stars.add(entity);
	}
	private int touchedSwitch;
	public void update() {
		camera.update();
		MainGame.setDifficulty(difficulty);
		starcount ++;
		if(starcount == 4) {		//	Controls spawing ammount
			if(starToggle>=1){
				starLimit=99;
				starToggle --;
			} else					//	This controls aditional planets and such
				starLimit=limit;
			int text = MathUtils.random(0,starLimit);
			if(text>=100)
				starToggle=100;						//	This sets the wait for a planet to spawn
			
	//	Sets the position of the stars and speed
			if(starVar == 0){
				y = MathUtils.random(0, MainGame.HEIGHT - TextureManager.STAR1.getHeight());
				x = -(TextureManager.PLANET1.getWidth())- MathUtils.random(10, MainGame.WIDTH / 2);
				speedx = MathUtils.random(2.5f,4.5f);
				if(text>=100)
					speedx -= 2;
			} else {
				y = MathUtils.random(0, MainGame.HEIGHT - TextureManager.STAR1.getHeight());
				x = MainGame.WIDTH + TextureManager.STAR1.getWidth();
				speedx = MathUtils.random(-4.5f,-2.5f);
				if(text>=100)
					speedx += 2;
			}
			addStars(new Stars_Class(new Vector2(x,y), new Vector2(speedx, 0), text));
			starcount = 0;
		}
		for (Stars_Class s : stars)
			s.update();

		for (Stars_Class s : stars)
			if (s.checkEnd())
				stars.removeValue(s, false);
		
		if (Gdx.input.isTouched()) {
			Vector2 touch = camera.unprojectCoordinates(Gdx.input.getX(), Gdx.input.getY());
			if ((touch.x > 215 && touch.y > 200) && (touch.x < 255 && touch.y < 250))
				touchedSwitch = 1;
			else
			if((touch.x > 540 && touch.y > 200) && (touch.x < 585 && touch.y < 250))
				touchedSwitch = 2;
			else
			if((touch.x > (MainGame.WIDTH / 2 - 60)&& touch.y > 125) && (touch.x < (MainGame.WIDTH / 2 + 20) && touch.y < 150))
				touchedSwitch = 3;
			else touchedSwitch = 0;
		} else touchedSwitch = 0;
		
// hidden switches
		if(Gdx.input.isKeyPressed(Keys.NUM_1)){
			if(Gdx.input.isKeyPressed(Keys.NUM_2)){
				if(Gdx.input.isKeyPressed(Keys.NUM_3)){
					if(Gdx.input.isKeyPressed(Keys.NUM_4) && oneTime[0] == 0){
						GameScreen.setLevel(new Level1());
						GameScreen.setLevelStep(8);
						SoundManager.sound2.play(0.7f);
						cheatActive2 = true;
						oneTime[0] = 1;
					}
				}
			}
		}
		if(Gdx.input.isKeyPressed(Keys.NUM_2)){
			if(Gdx.input.isKeyPressed(Keys.NUM_3)){
				if(Gdx.input.isKeyPressed(Keys.NUM_4)){
					if(Gdx.input.isKeyPressed(Keys.NUM_5) && oneTime[2] == 0){
						GameScreen.setLevel(new Level1());
						GameScreen.setLevelStep(3);
						LevelManager.setCurrentLevel(new Level1());
						LevelManager.setCurrentLevelStep(3);
						SoundManager.sound2.play(0.7f);
						oneTime[2] = 1;
					}
				}
			}
		}
		if(Gdx.input.isKeyPressed(Keys.NUM_1)){
			if(Gdx.input.isKeyPressed(Keys.NUM_2)){
				if(Gdx.input.isKeyPressed(Keys.NUM_4)){
					if(Gdx.input.isKeyPressed(Keys.NUM_5) && oneTime[1] == 0){
						SoundManager.sound2.play(0.7f);
						cheatActive = true;
						oneTime[1] = 1;
					}
				}
			}
		}
		if(!cheatActive2){
			if(oneTime[3]==0){
			GameScreen.setLevel(new IntroLevel());
			GameScreen.setLevelStep(1);
			LevelManager.setCurrentLevel(new IntroLevel());
			oneTime[3] = 1;
			}
		}
		if(touchedSwitch == 3){
			screenManager.setScreen(new OptionsScreen(), difficulty);
		}
//	Difficulty Switch
		if(cheatActive){
//			Difficulty Switch
			if (((Gdx.input.isKeyPressed(Keys.RIGHT) || touchedSwitch==2) && Switch2 != 1 && Toggle2 == 3) ||
					((Gdx.input.isKeyPressed(Keys.LEFT) || touchedSwitch==1) && Switch2 != 1 && Toggle2 == 4)){
				difficulty = "vexoid";
				difficultyImage = TextureManager.DIFFICULTY_VEXOID;
				SoundManager.sound1.play(1);
				if(Gdx.input.isKeyPressed(Keys.LEFT)|| touchedSwitch==1)
					Toggle2 = 2;
				else
					Toggle2 = 2;
			} else
			if (((Gdx.input.isKeyPressed(Keys.RIGHT)||touchedSwitch==2) && Switch2 != 1 && Toggle2 == 1) ||
					((Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1) && Switch2 != 1 && Toggle2 == 2)){
				difficulty = "hard";
				difficultyImage = TextureManager.DIFFICULTY_HARD;
				SoundManager.sound1.play(1);
				if(Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1)
					Toggle2 = 3;
				else
					Toggle2 = 3;
			} else
			if (((Gdx.input.isKeyPressed(Keys.RIGHT)||touchedSwitch==2) && Switch2 != 1 && Toggle2 == 2) ||
					((Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1) && Switch2 != 1 && Toggle2 == 1)){
				difficulty = "easy";
				difficultyImage = TextureManager.DIFFICULTY_EASY;
				SoundManager.sound1.play(1);
				if(Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1)
					Toggle2 = 4;
				else
					Toggle2 = 4;
			} else
			if (((Gdx.input.isKeyPressed(Keys.RIGHT)||touchedSwitch==2) && Switch2 != 1 && Toggle2 == 4) ||
					((Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1) && Switch2 != 1 && Toggle2 == 3)){
				difficulty = "medium";
				difficultyImage = TextureManager.DIFFICULTY_MEDIUM;
				SoundManager.sound1.play(1);
				if(Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1)
					Toggle2 = 1;
				else
					Toggle2 = 1;
			}			
			if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1||touchedSwitch==2)
				Switch2 = 1;
			else
				Switch2 = 0;
			
		} else {	//	This is the else for checking if the cheat active or not
			
		// without cheat
		if (((Gdx.input.isKeyPressed(Keys.RIGHT)||touchedSwitch==2) && Switch2 != 1 && Toggle2 == 1) ||
				((Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1) && Switch2 != 1 && Toggle2 == 3)){
			difficulty = "hard";
			difficultyImage = TextureManager.DIFFICULTY_HARD;
			SoundManager.sound1.play(1);
			if(Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1)
				Toggle2 = 2;
			else
				Toggle2 = 2;
		} else
		if (((Gdx.input.isKeyPressed(Keys.RIGHT)||touchedSwitch==2) && Switch2 != 1 && Toggle2 == 2) ||
				((Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1) && Switch2 != 1 && Toggle2 == 1)){
			difficulty = "easy";
			difficultyImage = TextureManager.DIFFICULTY_EASY;
			SoundManager.sound1.play(1);
			if(Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1)
				Toggle2 = 3;
			else
				Toggle2 = 3;
		} else
		if (((Gdx.input.isKeyPressed(Keys.RIGHT)||touchedSwitch==2) && Switch2 != 1 && Toggle2 == 3) ||
				((Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1) && Switch2 != 1 && Toggle2 == 2)){
			difficulty = "medium";
			difficultyImage = TextureManager.DIFFICULTY_MEDIUM;
			SoundManager.sound1.play(1);
			if(Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1)
				Toggle2 = 1;
			else
				Toggle2 = 1;
		}			
		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)||touchedSwitch==1||touchedSwitch==2)
			Switch2 = 1;
		else
			Switch2 = 0;
		}
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		for (Stars_Class s : stars)
			s.render(sb);
		
		displayOptions.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		displayOptions.draw(sb, options, (MainGame.WIDTH / 2) - 60, 150);
		
		sb.draw(title, ((MainGame.WIDTH/2) - (TextureManager.TITLE_IMAGE.getWidth()/2)), (MainGame.HEIGHT / 2));
		sb.draw(menuTitle, ((MainGame.WIDTH/2) - (TextureManager.MENU_IMAGE.getWidth()/2)),
				(MainGame.HEIGHT / 2) - (TextureManager.TITLE_IMAGE.getHeight()/2));
		sb.draw(difficultyImage, ((MainGame.WIDTH/2) - (TextureManager.DIFFICULTY_MEDIUM.getWidth()/2)),
				(MainGame.HEIGHT / 2) - (TextureManager.MENU_IMAGE.getHeight() - 7));
		sb.end();
	}

	public void resize(int width, int height) {
		camera.resize();
	}

	public boolean isCheat2Active(){
		return cheatActive2;
	}
	public void dispose() {}

	public void pause() {}
	
	public void resume() {}

	public String whatScreen() {
		return "MenuScreen";
	}

}
