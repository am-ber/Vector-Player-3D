package com.vexoid.game.entity;
import org.json.JSONException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vexoid.game.MainGame;
import com.vexoid.game.Options;
import com.vexoid.game.SoundManager;
import com.vexoid.game.TextureManager;
import com.vexoid.game.camera.OrthoCamera;
import com.vexoid.game.entity.bullets.Blue_Bullet2;
import com.vexoid.game.entity.bullets.Red_Bullet2;
import com.vexoid.game.entity.bullets.Yellow_Bullet2;

public class Player extends Entity {
	private final EntityManager entityManager;
	private long lastFire;
	private final OrthoCamera camera;
	public static float PositionX;
	private int shootDelay = 20;
	private float slow = 1f;
	private float spread = 0.5f;
	static String shootingMode = "Narrow", bulletMode = "light";
	private String difficulty;
	private boolean allowedRegen = true;
	private boolean redBulletFire = false;
	int tempClock = 0;
	

	Vector2 point1, point2; // These are the points the player shoots from

	public Player(Vector2 pos, Vector2 direction, EntityManager entityManager, OrthoCamera camera, String difficulty) {
		super(TextureManager.PLAYER, pos, direction);
		this.entityManager = entityManager;
		this.camera = camera;
		this.difficulty = difficulty;
		PositionX = pos.x;
		// if vexoid difficulty, the player cannot regen health
		if (this.difficulty == "vexoid")
			allowedRegen = false;
	}

	// Health stuff
	float healthPercent = 100;
	int clock = 0,clock2 = 0;
	boolean playerDied = false, playerOutOfLives = false;

	// method for health regen
	public void health() {
		clock++;
		clock2++;
		if (healthPercent >= 100.000) {
			healthPercent = 100.0f;
		}
		if (healthPercent <= 40.0f && healthPercent > 30.0f) {
			entityManager.doScreenEffects("RedScreen", 15);
		} else if (healthPercent <= 30.0f && healthPercent > 15.0f) {
			entityManager.doScreenEffects("RedScreen", 30);
		} else if (healthPercent <= 15.0f) {
			entityManager.doScreenEffects("RedScreen", 40);
			if(clock2 >= 100){
				SoundManager.warning2.play();
				clock2 = 0;
			}
		} else
			entityManager.doScreenEffects("BlueScreen", 0);
		if (clock > 750 && allowedRegen) {
			for (int i = (int) healthPercent; i < 100; i++) {
				healthPercent += 0.01f;
			}
			if (healthPercent >= 100)
				clock = 0;
		}
	}

	public void increaseHealth(float ammount) {
		healthPercent += ammount;
		if (healthPercent >= 100.0)
			healthPercent = 100;
	}

	public void decreaseHealth(float ammount) {
		clock = 0;
		healthPercent -= ammount;
		if (healthPercent <= 0) {
			playerDied = true;
		}
	}

	public float getHealth() {
		return healthPercent;
	}

	boolean tookLives = false;

	public void liveSystem() {
		if (playerDied && !tookLives) {
			tookLives = true;
			healthPercent = 100.0f;
			entityManager.lives--;
			System.out.println("Player died now has " + entityManager.lives + " lives");
			playerDied = false;
		}
	}

	public boolean playerOutOfLives() {
		return playerOutOfLives;
	}

	// Player hitbox
	public Rectangle getBounds() {
		return new Rectangle((pos.x + ((texture.getWidth() / 2) / 1.25f)),
				(pos.y + ((texture.getHeight() / 2) / 1.25f)), (texture.getWidth() - (texture.getWidth() / 1.25f)),
				(texture.getHeight() - (texture.getHeight() / 1.25f)));
	}

	public String shootingMode() {
		return shootingMode;
	}

	public String bulletMode() {
		return bulletMode;
	}

	int Switch = 0, Toggle = 1, Switch2 = 0, Toggle2 = 1;

	public void update() {

		if (canPlayerMove) {
			pos.add(direction);
			PositionX = pos.x;
			health();
			liveSystem();
			int dir = 0;
			if (Gdx.input.isTouched()) {
				Vector2 touch = camera.unprojectCoordinates(Gdx.input.getX(), Gdx.input.getY());
				if (touch.x < MainGame.WIDTH / 2 && pos.x > 0)
					dir = 1;
				else if (pos.x < (MainGame.WIDTH - TextureManager.PLAYER.getWidth()))
					dir = 2;
			}

			/*
			 * * * * * * * * Here is the player's control logic * * * * * *
			 */
			final int highSpeed = 400;
			final int lowSpeed = 300;
			//try block for the player control schema
			try {
				if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("slow"))) {
					slow = 0.6f;
					this.texture = TextureManager.PLAYER_GLOW;
				} else {
					slow = 1f;
					this.texture = TextureManager.PLAYER;
				}
			
			// left if block
			if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("left")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("right"))) && (pos.x > 0) || (dir == 1)
					|| ((Gdx.input.isKeyPressed(Options.leftArrow) && !Gdx.input.isKeyPressed(Options.rightArrow)) && (pos.x > 0))) {
				// if up and not down
				if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("up")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("down")))
						&& (pos.y < MainGame.HEIGHT - this.texture.getHeight())
						|| (Gdx.input.isKeyPressed(Options.upArrow) && !Gdx.input.isKeyPressed(Options.downArrow))
								&& (pos.y < MainGame.HEIGHT - this.texture.getHeight())) {
					setDirection(-lowSpeed*slow, lowSpeed*slow);
				} // if down and not up
				else if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("down")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("up"))) && (pos.y > 0)
						|| (Gdx.input.isKeyPressed(Options.downArrow) && !Gdx.input.isKeyPressed(Options.upArrow)) && (pos.y > 0)) {
					setDirection(-lowSpeed*slow, -lowSpeed*slow);
				} // all else
				else {
					setDirection(-highSpeed * slow, 0);
				}
			} else {
				// right if block
				if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("right")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("left")))
						&& (pos.x < MainGame.WIDTH - this.texture.getWidth()) || (dir == 2)
						|| ((Gdx.input.isKeyPressed(Options.rightArrow) && !Gdx.input.isKeyPressed(Options.leftArrow))
								&& (pos.x < MainGame.WIDTH - this.texture.getWidth()))) {
					// if up and not down
					if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("up")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("down")))
							&& (pos.y < MainGame.HEIGHT - this.texture.getHeight())
							|| (Gdx.input.isKeyPressed(Options.upArrow) && !Gdx.input.isKeyPressed(Options.downArrow))
									&& (pos.y < MainGame.HEIGHT - this.texture.getHeight())) {
						setDirection(lowSpeed*slow, lowSpeed*slow);
					} // if down and not up
					else if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("down")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("up"))) && (pos.y > 0)
							|| (Gdx.input.isKeyPressed(Options.downArrow) && !Gdx.input.isKeyPressed(Options.upArrow)) && (pos.y > 0)) {
						setDirection(lowSpeed*slow, -lowSpeed *slow);
					} // all else
					else {
						setDirection(highSpeed * slow, 0);
					}
				} else {
					// up if block
					if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("up")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("down")))
							&& (pos.y < MainGame.HEIGHT - this.texture.getHeight())
							|| (Gdx.input.isKeyPressed(Options.upArrow) && !Gdx.input.isKeyPressed(Options.downArrow))
									&& (pos.y < MainGame.HEIGHT - this.texture.getHeight())) {
						setDirection(0, highSpeed*slow);
					} else {
						// down if block
						if ((Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("down")) && !Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("up"))) && (pos.y > 0)
								|| (Gdx.input.isKeyPressed(Options.downArrow) && !Gdx.input.isKeyPressed(Options.upArrow))
										&& (pos.y > 0)) {
							setDirection(0, -highSpeed*slow);
						} else {
							setDirection(0, 0);
						}
					}
				}
			}

			// Shooting modes (spread)
			if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeSpread")) && Switch != 1 && Toggle == 1) {
				shootingMode = "Wide";
				spread = 3.5f;
				SoundManager.sound1.play(1);
				Toggle = 2;
			} else if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeSpread")) && Switch != 1 && Toggle == 2) {
				shootingMode = "Narrow";
				spread = 0.5f;
				SoundManager.sound1.play(1);
				Toggle = 1;
			}
			if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeSpread")))
				Switch = 1;
			else
				Switch = 0;

			// Switch Bullets
			if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeMode")) && Switch2 != 1 && Toggle2 == 1) {
				bulletMode = "energy";
				shootDelay = 10;
				SoundManager.sound1.play(1);
				Toggle2 = 2;
			} else if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeMode")) && Switch2 != 1 && Toggle2 == 2) {
				bulletMode = "heavy";
				shootDelay = 40;
				SoundManager.sound1.play(1);
				Toggle2 = 3;
			} else if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeMode")) && Switch2 != 1 && Toggle2 == 3) {
				bulletMode = "light";
				shootDelay = 17;
				SoundManager.sound1.play(1);
				Toggle2 = 1;
			}
			if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("changeMode")))
				Switch2 = 1;
			else
				Switch2 = 0;

			// Shoot bullets
			if (this.texture == TextureManager.PLAYER || this.texture == TextureManager.PLAYER_GLOW) {
				point1 = new Vector2(pos.cpy().add(-14, TextureManager.PLAYER.getHeight() / 2));
				point2 = new Vector2(
						pos.cpy().add(TextureManager.PLAYER.getWidth() - 17, TextureManager.PLAYER.getHeight() / 2));
			}
			float var = MathUtils.random(2, 6);
			float var2 = 3;

			tempClock++;
			if (tempClock >= 80) {
				if(entityManager.getPlayerRedBullets().size > 0)
					SoundManager.shot5.play(0.2f);
				redBulletFire = true; 			// This all just handles the red bullet
				tempClock = 0; 					// and when it should fire
			} else 								//
				redBulletFire = false;

			if (Gdx.input.isKeyPressed(Options.options.getJSONObject("controls").getInt("fire")) || dir == 1 || dir == 2) {
				if (bulletMode == "light")
					if (System.currentTimeMillis() - lastFire >= shootDelay) {
						int r = MathUtils.random(0, 1);
						if (r == 0)
							entityManager.addEntity(new Blue_Bullet2(point1,
									new Vector2(MathUtils.random(-spread, spread + var2), 18)));
						if (r == 1)
							entityManager.addEntity(new Blue_Bullet2(point2,
									new Vector2(MathUtils.random(-spread - var2, spread), 18)));

						SoundManager.shot2.play(0.2f);
						lastFire = System.currentTimeMillis();
					}
				if (bulletMode == "heavy")
					if (System.currentTimeMillis() - lastFire >= shootDelay) {
						int r = MathUtils.random(0, 1);
						if (r == 0)
							entityManager.addEntity(new Red_Bullet2(point1,
									new Vector2(MathUtils.random(-spread, spread + var2), 18), this));
						if (r == 1)
							entityManager.addEntity(new Red_Bullet2(point2,
									new Vector2(MathUtils.random(-spread - var2, spread), 18), this));
						SoundManager.shot6.play(0.05f);
						lastFire = System.currentTimeMillis();
					}
				if (bulletMode == "energy")
					if (System.currentTimeMillis() - lastFire >= shootDelay) {
						int r = MathUtils.random(0, 1);
						if (r == 0)
							entityManager.addEntity(new Yellow_Bullet2(point1,
									new Vector2(MathUtils.random(-spread - var, spread), 10)));
						if (r == 1)
							entityManager.addEntity(new Yellow_Bullet2(point2,
									new Vector2(MathUtils.random(-spread, spread + var), 10)));
						SoundManager.shot3.play(0.1f);
						lastFire = System.currentTimeMillis();
					}
				
			}
			} catch (JSONException e) {
				e.printStackTrace();
				System.err.println("reverting controls file due to an error in Player control schema");
				Options.generateDefaults(Options.validateFile("C:\\\\Vexoids\\options.txt"));
			}
		}
	}

	boolean canPlayerMove = true;

	public void playerCanMove(boolean moving) {
		canPlayerMove = moving;
	}

	public boolean getFireRedBullet() {
		return redBulletFire;
	}

	public Vector2 getPointOfShooting(int pointNumber) {
		int r = MathUtils.random(1, 2);
		if (pointNumber == 0)
			if (r == 1)
				return point1;
			else
				return point2;
		else

		if (pointNumber == 1)
			return point1;
		else
			return point2;
	}

	public Texture getTexture() {
		return this.texture;
	}
}
