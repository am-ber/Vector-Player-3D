package core;

import ddf.minim.AudioInput;
import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

public class MusicHandler {
	
	// public access objects and variables
	public FFT fft;
	public AudioMetaData meta;
	public float songGain = 0;
	public int songPos = 0;
	public boolean isThereSound = false;
	public float intensity = 0;
	
	// private class only objects
	private Minim minim;
	private AudioPlayer song;
	private AudioInput lineIn;
	
	// fft
	public float sub_range = 0.1f;
	public float low_range = 0.1f;
	public float mid_range = 0.4f;
	public float high_range = 0.4f;
	public float subs = 0;
	public float lows = 0;
	public float mids = 0;
	public float highs = 0;
	
	public MusicHandler(PApplet app) {
		minim = new Minim(app);
		lineIn = minim.getLineIn();
		lineIn.mute();
		fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
	}
	
	// sets the current song
	public void setSong(String file) {
		try {
			song = minim.loadFile(file);
			meta = song.getMetaData();
		} catch (Exception e) {
			PApplet.println("We got a "+e.toString()+" error. So uh, yea.");
		}
		songPos = 0;
		isThereSound = false;
		fft = new FFT(song.bufferSize(), song.sampleRate());
	}
	
	// called in a loop
	public void update() {
		song.setGain(songGain);
		
		// check which method of audio to forward to fft
		if (song.isPlaying())
			fft.forward(song.mix);
		if (lineIn.isMonitoring())
			fft.forward(lineIn.mix);
		
		// setting back to 0 for next calculation
		subs = 0;
		lows = 0;
		mids = 0;
		highs = 0;
		
		// iterating through each band in the range
		for (int i = 0; i < fft.specSize() * sub_range; i++)
			subs += fft.getBand(i)*1.15;
		for (int i = (int) (fft.specSize() * sub_range); i < fft.specSize() * low_range; i++)
			lows += fft.getBand(i)*1.15;
		for (int i = (int) (fft.specSize() * low_range); i < fft.specSize() * mid_range; i++)
			mids += fft.getBand(i);
		for (int i = (int) (fft.specSize() * mid_range); i < fft.specSize() * high_range; i++)
			highs += fft.getBand(i);
		intensity = subs + lows + mids + highs;
	}
	
	// returns the full band array
	public float[] getBandArray() {
		return new float[] {subs, lows, mids, highs};
	}
	
	// toggles playing the current set song
	public void toggleSong() {
		if (song.isPlaying()) {
			song.pause();
			songPos = song.position();
			isThereSound = false;
		} else {
		    song.play(songPos);
		    isThereSound = true;
		}
	}
}
