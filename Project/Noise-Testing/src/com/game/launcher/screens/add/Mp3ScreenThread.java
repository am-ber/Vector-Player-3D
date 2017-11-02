package com.game.launcher.screens.add;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import com.game.launcher.screens.Mp3Screen;

public class Mp3ScreenThread extends Thread {
	
	private AudioInputStream din = null;
	private int len;
	Mp3Screen screen;
	
	public Mp3ScreenThread(Mp3Screen screen, AudioInputStream din, int len){
		this.screen = screen;
		this.din = din;
		this.len = len;
	}
	
	@Override
	public void run(){
		byte[] b = new byte[len];
		int stream = 0;
		try {
			din.read(b, 0, len);
			
			for(int i=0; i < len; i++){
				screen.vals.add((Integer)((screen.getGame().getHeight()/2)*b[i]));
			}
			
		} catch(NullPointerException e){
			this.stop();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ArrayIndexOutOfBoundsException e){
			this.stop();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
