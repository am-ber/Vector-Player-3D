package com.game.launcher.screens.add;

import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.game.launcher.screens.Mp3Screen;

public class Mp3ScreenThread extends Thread {
	
	Mp3Screen screen;
	AudioFormat decodedFormat;
	AudioInputStream din;
	
	public Mp3ScreenThread(Mp3Screen screen, AudioFormat decodedFormat, AudioInputStream din){
		this.screen = screen;
		this.decodedFormat = decodedFormat;
		this.din = din;
	}
	
	@Override
	public void run(){
	}
	
	public void play() {
		try {
			rawplay(decodedFormat, din);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException, LineUnavailableException {
	  byte[] data = new byte[4096];
	  SourceDataLine line = getLine(targetFormat); 
	  
	  if (line != null) {
	    // Start
	    line.start();
	    @SuppressWarnings("unused")
		int nBytesRead = 0, nBytesWritten = 0;
	    while (nBytesRead != -1) {
	        nBytesRead = din.read(data, 0, data.length);
	        if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
	        if (din instanceof javazoom.spi.PropertiesContainer) {
			    @SuppressWarnings("rawtypes")
				Map properties = ((javazoom.spi.PropertiesContainer) din).properties();
			    float[] equalizer = (float[]) properties.get("mp3.equalizer");
			    equalizer[0] = 0.5f;
			    equalizer[31] = 0.25f;
			    for (int i=0; i < equalizer.length; i++) {
			    	screen.vals.add(equalizer[i]);
			    }
			}
	    }
	    // Stop
	    line.flush();
	    line.stop();
	    line.close();
	    din.close();
	  } 
	}

	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

}
