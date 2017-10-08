package com.vexoid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static Music CurrentMusic = null;

//	Music
	public static Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/asdd - main 2.mp3"));
	public static Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/asdd - main.mp3"));
	
	public static Music extraMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/Vocaloid Miku ievan polkka.mp3"));
	public static Music extraMusic2 = Gdx.audio.newMusic(Gdx.files.internal("assets/music/Subboss Theme - Scott Pilgrim vs. The World_ The Game Music.mp3"));
	public static Music extraMusic3 = Gdx.audio.newMusic(Gdx.files.internal("assets/music/asdd - main 8bit.mp3"));
	public static Music extraMusic4 = Gdx.audio.newMusic(Gdx.files.internal("assets/music/nick tune chip tune.mp3"));
	
	public static Music boss1Music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/asdd - boss1.mp3"));
	
	public static Music endMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/music/end_game.mp3"));
	
	
//	Hits
	public static Sound hit1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/hits/hit1.mp3"));
	public static Sound hit2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/hits/hit2.mp3"));
	public static Sound hit3 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/hits/hit3.mp3"));
	public static Sound hit4 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/hits/hit4.mp3"));
	public static Sound hit5 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/hits/hit5.mp3"));
	public static Sound hit6 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/hits/hit6.mp3"));
	

//	Cries
	public static Sound cry1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/cries/cry1.mp3"));
	public static Sound cry2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/cries/cry2.mp3"));
	public static Sound cry3 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/cries/cry3.mp3"));

//	Shots
	public static Sound shot1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/shot1.mp3"));
	public static Sound shot2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/shot2.mp3"));
	public static Sound shot3 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/shot3.mp3"));
	public static Sound shot4 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/shot4.mp3"));
	public static Sound shot5 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/shot5.mp3"));
	public static Sound shot6 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/shot6.mp3"));
	
	public static Sound charge1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/shots/charge1.mp3"));

	public static Sound laserShot1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/laser_shot.mp3"));

//	Other sounds
	public static Sound startSound = Gdx.audio.newSound(Gdx.files.internal("assets/sound/start_game.mp3"));
	
	public static Sound warning1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/warning.mp3"));
	public static Sound warning2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/warning2.mp3"));
	
	public static Sound liveLost = Gdx.audio.newSound(Gdx.files.internal("assets/sound/live_lost.mp3"));

	public static Sound sound1 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/sound1.mp3"));
	public static Sound sound2 = Gdx.audio.newSound(Gdx.files.internal("assets/sound/sound2.mp3"));
	
	
	public static void stopASound(Sound sound){
		sound.stop();
	}	
	public static void playMusic(){
		//determines if the player allows music or not in options
		try{
			if(Options.options.getBoolean("musicAllowed")){
				CurrentMusic.play();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("reverting controls file due to an error in SoundManager playMusic");
			Options.generateDefaults(Options.validateFile("C:\\\\Vexoids\\options.txt"));
		}
	}
	public static void stopMusic(){
		CurrentMusic.stop();
	}
	public static void pauseMusic(){
		CurrentMusic.pause();
	}
	public static float getMusicPosition(){
		return CurrentMusic.getPosition();
	}
	public static void setMusic(Music music, float vol, boolean loop){
		CurrentMusic = music;
		CurrentMusic.setVolume(vol);
		CurrentMusic.setLooping(loop);
		//determines if the player allows music or not in options
		try{
			if(Options.options.getBoolean("musicAllowed")){
				CurrentMusic.play();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("reverting controls file due to an error in SoundManager setMusic");
			Options.generateDefaults(Options.validateFile("C:\\\\Vexoids\\options.txt"));
		}
		
	}
	public static Music getMusic(){
		return CurrentMusic;
	}
}
