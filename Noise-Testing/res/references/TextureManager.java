package com.vexoid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {
	
	//	GUI
	public static Texture TITLE_IMAGE = new Texture(Gdx.files.internal("assets/image/gui/Vexoids Title.png"));
	public static Texture MENU_IMAGE = new Texture(Gdx.files.internal("assets/image/gui/titlescreen_1.png"));
	
	public static Texture HUD_SPREAD1 = new Texture(Gdx.files.internal("assets/image/gui/HUD_Fire_1 clear.png"));
	public static Texture HUD_SPREAD2 = new Texture(Gdx.files.internal("assets/image/gui/HUD_Fire_2 clear.png"));

	public static Texture DIFFICULTY_EASY = new Texture(Gdx.files.internal("assets/image/gui/difficulty_1.png"));
	public static Texture DIFFICULTY_MEDIUM = new Texture(Gdx.files.internal("assets/image/gui/difficulty_2.png"));
	public static Texture DIFFICULTY_HARD = new Texture(Gdx.files.internal("assets/image/gui/difficulty_3.png"));
	public static Texture DIFFICULTY_VEXOID = new Texture(Gdx.files.internal("assets/image/gui/difficulty_4.png"));
	
	public static Texture BUTTON_ROUNDED = new Texture(Gdx.files.internal("assets/image/gui/button_rounded.png"));
	public static Texture BUTTON_SQUARE_OFF = new Texture(Gdx.files.internal("assets/image/gui/button_square_off.png"));
	public static Texture BUTTON_SQUARE_ON = new Texture(Gdx.files.internal("assets/image/gui/button_square_on.png"));
	
//	Bullets
	//	Player Bullets
	public static Texture BULLET1 = new Texture(Gdx.files.internal("assets/image/bullets/bullet1.png"));
	public static Texture BULLET1_1 = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-1.png"));
	public static Texture BULLET1_2 = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-2.png"));
	public static Texture BULLET1_3 = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-3.png"));
	public static Texture BULLET1_4 = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-4.png"));
	
	public static Texture BULLET1r = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-r.png"));
	public static Texture BULLET1y = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-y.png"));
	public static Texture BULLET1g = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-g.png"));
	public static Texture BULLET1b = new Texture(Gdx.files.internal("assets/image/bullets/bullet1-b.png"));

	//	Enemy Bullets
	public static Texture BLUE_BULLET2 = new Texture(Gdx.files.internal("assets/image/bullets/blue-bullet2.png"));
	public static Texture RED_BULLET2 = new Texture(Gdx.files.internal("assets/image/bullets/red-bullet2.png"));
	public static Texture YELLOW_BULLET2 = new Texture(Gdx.files.internal("assets/image/bullets/yellow-bullet2.png"));
	
	public static Texture LASER_BULLET = new Texture(Gdx.files.internal("assets/image/bullets/laser1.png"));
	public static Texture LASER_BULLET1 = new Texture(Gdx.files.internal("assets/image/bullets/laser2.png"));
	public static Texture LASER_BULLET2 = new Texture(Gdx.files.internal("assets/image/bullets/laser3.png"));
	public static Texture LASER_BULLET3 = new Texture(Gdx.files.internal("assets/image/bullets/laser4.png"));
	public static Texture LASER_BULLET4 = new Texture(Gdx.files.internal("assets/image/bullets/laser5.png"));

//	Other Entities

	public static Texture PLAYER = new Texture(Gdx.files.internal("assets/image/ship2.png"));
	public static Texture PLAYER_GLOW = new Texture(Gdx.files.internal("assets/image/ship2-glow.png"));
	//	Enemies
	public static Texture BASIC_ENEMY = new Texture(Gdx.files.internal("assets/image/jellies2.png"));
	public static Texture ADVANCED_ENEMY = new Texture(Gdx.files.internal("assets/image/jellies4.png"));
	public static Texture LASER_ENEMY = new Texture(Gdx.files.internal("assets/image/enemyship_2.png"));

	//	Bosses
	public static Texture BOSS_1 = new Texture(Gdx.files.internal("assets/image/jelly boss blue.png"));
	
//	General Effects
	// Laser Warning effect
	public static Texture LASER_WARNING1 = new Texture(Gdx.files.internal("assets/image/bullets/laser-indicator1.png"));
	public static Texture LASER_WARNING2 = new Texture(Gdx.files.internal("assets/image/bullets/laser-indicator2.png"));
	
	//	Screen Effects
	public static Texture EFFECT_SCREEN_RED_40 = new Texture(Gdx.files.internal("assets/image/effects/screenEffectsRedCover40.png"));
	public static Texture EFFECT_SCREEN_RED_30 = new Texture(Gdx.files.internal("assets/image/effects/screenEffectsRedCover30.png"));
	public static Texture EFFECT_SCREEN_RED_15 = new Texture(Gdx.files.internal("assets/image/effects/screenEffectsRedCover15.png"));
	
	public static Texture EFFECT_SCREEN_BLUE = new Texture(Gdx.files.internal("assets/image/effects/screenEffectsBlue.png"));
	//	Red effect
	public static Texture RED_EFFECT1 = new Texture(Gdx.files.internal("assets/image/effects/red-effect1.png"));
	public static Texture RED_EFFECT2 = new Texture(Gdx.files.internal("assets/image/effects/red-effect2.png"));
	public static Texture RED_EFFECT3 = new Texture(Gdx.files.internal("assets/image/effects/red-effect3.png"));
	public static Texture RED_EFFECT4 = new Texture(Gdx.files.internal("assets/image/effects/red-effect4.png"));
	public static Texture RED_EFFECT5 = new Texture(Gdx.files.internal("assets/image/effects/red-effect5.png"));

	//	Blue effect
	public static Texture BLUE_EFFECT1 = new Texture(Gdx.files.internal("assets/image/effects/blue-effect1.png"));
	public static Texture BLUE_EFFECT2 = new Texture(Gdx.files.internal("assets/image/effects/blue-effect2.png"));
	public static Texture BLUE_EFFECT3 = new Texture(Gdx.files.internal("assets/image/effects/blue-effect3.png"));
	public static Texture BLUE_EFFECT4 = new Texture(Gdx.files.internal("assets/image/effects/blue-effect4.png"));
	public static Texture BLUE_EFFECT5 = new Texture(Gdx.files.internal("assets/image/effects/blue-effect5.png"));
	
	//	Yellow effect
	public static Texture YELLOW_EFFECT1 = new Texture(Gdx.files.internal("assets/image/effects/yellow-effect1.png"));
	public static Texture YELLOW_EFFECT2 = new Texture(Gdx.files.internal("assets/image/effects/yellow-effect2.png"));
	public static Texture YELLOW_EFFECT3 = new Texture(Gdx.files.internal("assets/image/effects/yellow-effect3.png"));
	public static Texture YELLOW_EFFECT4 = new Texture(Gdx.files.internal("assets/image/effects/yellow-effect4.png"));
	public static Texture YELLOW_EFFECT5 = new Texture(Gdx.files.internal("assets/image/effects/yellow-effect5.png"));
	

	//	Stars
	public static Texture STARS = new Texture(Gdx.files.internal("assets/image/stars/stars.png"));
	public static Texture STAR1 = new Texture(Gdx.files.internal("assets/image/stars/star-1.png"));
	public static Texture STAR2 = new Texture(Gdx.files.internal("assets/image/stars/star-2.png"));
	public static Texture STAR3 = new Texture(Gdx.files.internal("assets/image/stars/star-3.png"));
	public static Texture STAR4 = new Texture(Gdx.files.internal("assets/image/stars/star-4.png"));
	public static Texture STAR5 = new Texture(Gdx.files.internal("assets/image/stars/star-5.png"));
	public static Texture STAR6 = new Texture(Gdx.files.internal("assets/image/stars/star-6.png"));
	public static Texture STAR7 = new Texture(Gdx.files.internal("assets/image/stars/star-7.png"));
	public static Texture STAR8 = new Texture(Gdx.files.internal("assets/image/stars/star-8.png"));
	public static Texture STAR9 = new Texture(Gdx.files.internal("assets/image/stars/star-9.png"));
	public static Texture STAR10 = new Texture(Gdx.files.internal("assets/image/stars/star-10.png"));
	
	
	//	Planets
	public static Texture PLANET1 = new Texture(Gdx.files.internal("assets/image/stars/planet1.png"));
	public static Texture PLANET1_1 = new Texture(Gdx.files.internal("assets/image/stars/planet1-1.png"));
	public static Texture PLANET2 = new Texture(Gdx.files.internal("assets/image/stars/planet2.png"));
	public static Texture PLANET2_1 = new Texture(Gdx.files.internal("assets/image/stars/planet2-1.png"));
	public static Texture PLANET3 = new Texture(Gdx.files.internal("assets/image/stars/planet3.png"));
	public static Texture PLANET3_1 = new Texture(Gdx.files.internal("assets/image/stars/planet3-1.png"));
	public static Texture PLANET4 = new Texture(Gdx.files.internal("assets/image/stars/planet4.png"));
	public static Texture PLANET4_1 = new Texture(Gdx.files.internal("assets/image/stars/planet4-1.png"));
	
	//	Blank texture mainly to stop effects so they are not always on
	public static Texture NULL = new Texture(Gdx.files.internal("assets/image/effects/blank screen effect.png"));
	
	/*
	 * The artwork for this project was created and donated by Frank Porcello. If this project is to generate any sum of currency
	 * some amount greater than 20% of the sum generated from this project must be sent to Frank Porcello for compensation.
	 * Failure to follow this procedure will result in lawsuit.
	 */
}
