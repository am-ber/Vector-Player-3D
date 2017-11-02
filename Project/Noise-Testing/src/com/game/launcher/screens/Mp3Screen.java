package com.game.launcher.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.game.engine.Game;
import com.game.launcher.screens.add.Mp3ScreenThread;

public class Mp3Screen extends Screen{

	// Obj
	private String filename = "E:/Audio/Music/my music/Techno/Genre/DnB/DJB---Its-Confusing.mp3";
	private File file;
	private AudioInputStream in;
	private AudioInputStream din = null;
	private AudioFormat baseFormat;
	AudioFormat decodedFormat;
	Mp3ScreenThread thread;
	
	// Vals
	private int columns = 0, rows = 0, scale=10;
	private int[] noiseGrid;
	public static ArrayList<Integer> vals;
	
	@Override
	public void create(ScreenManager screenManager) {
		this.screenManager = screenManager;
		
		columns = screenManager.game().getWidth() / scale;
		rows = screenManager.game().getHeight() / scale;
		noiseGrid = new int[columns];
		
		vals = new ArrayList<Integer>();
		
		try {
			file = new File(filename);
			
			in= AudioSystem.getAudioInputStream(file);
			
			baseFormat = in.getFormat();
			
			decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			
		} catch(IOException e){
			e.printStackTrace();
		} catch(UnsupportedAudioFileException e){
			e.printStackTrace();
		}
		
		len = (int)(baseFormat.getSampleRate() / Game.frameRate);
		
		thread = new Mp3ScreenThread(this, din, columns);
		
		System.out.println("length of buffer: "+len);
	}
	
	private int len, index = 0;
	
	@Override
	public void update() {
		thread.run();
		
		for(int i=0; i < columns-1; i++) {
			noiseGrid[i] = noiseGrid[i+1];
		}
		noiseGrid[columns-1] = vals.get(index);
		System.out.println("val is: "+vals.get(index));
		index ++;
	}
	
	public Game getGame(){
		return screenManager.game;
	}

	@Override
	public void draw(Graphics2D g) {
		Color greyish = new Color(25,25,25);
		g.setColor(greyish);
		g.fillRect(0, 0, screenManager.game().getWidth(), screenManager.game().getHeight());	// Sets background
		g.setColor(Color.darkGray);
		g.setStroke(new BasicStroke(0.1f));
		//g.translate(0, screenManager.game().getHeight()/2);
		//g.rotate(Math.PI/4, screenManager.game().getWidth() / 2, screenManager.game().getHeight() / 2);
		
		for(int j=0; j < rows; j++){
			for(int i=0; i < columns; i++){
				g.drawRect(i*scale, j*scale, scale, scale);
				//g.drawLine(i*scale, j*scale, j*scale, i*scale);
			}
		}
		
		g.setColor(Color.white);
		g.setStroke(new BasicStroke(1f));
		
		for(int i=0; i < columns-1; i++){
			g.drawLine(i*scale, noiseGrid[i], (i*scale)+scale, noiseGrid[i+1]);
		}
	}

}
