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
	public float songLength = 0;
	public boolean isThereSound = false;
	public float intensity = 0;
	
	// private objects and variables
	private PApplet app;
	private Minim minim;
	private AudioPlayer song;
	private AudioInput lineIn;
	private long timeSince = 0;
	private int timePeriod = 1000;
	
	// fft related
	public float sub_range = 0.01f;
	public float low_range = 0.05f;
	public float mid_range = 0.30f;
	public float high_range = 0.38f;
	public float unused_range = 0.26f;
	public float highestIndividual = 0;
	public float highestSub = 0;
	public float highestLow = 0;
	public float highestMid = 0;
	public float highestHigh = 0;
	public float subs = 0;
	public float lows = 0;
	public float mids = 0;
	public float highs = 0;
	
	public MusicHandler(PApplet app) {
		this.app = app;
		minim = new Minim(app);
		lineIn = minim.getLineIn();
		lineIn.mute();
		fft = new FFT(lineIn.bufferSize(), lineIn.sampleRate());
		
		PApplet.println("Band size for fft " + fft.specSize() + "\nsubs: " + fft.specSize() * sub_range
				+ " lows: " + fft.specSize() * low_range + " mids: " + fft.specSize() * mid_range +
				" highs: " + fft.specSize() * high_range + " unused: " + fft.specSize() * unused_range);
	}
	
	// sets the current song
	public void setSong(String file) {
		try {
			song = minim.loadFile(file);
			meta = song.getMetaData();
		} catch (Exception e) {
			PApplet.println("We got a "+e.toString()+" error. So uh, yea.");
		}
		songLength = song.length();
		songPos = 0;
		isThereSound = false;
		fft = new FFT(song.bufferSize(), song.sampleRate());
	}
	
	// called in a loop
	public void update() {
		song.setGain(songGain);
		songPos = song.position();
		
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
		
		if (app.millis() > (timeSince + timePeriod)) {
			highestIndividual = 0;
			highestSub = 0;
			highestLow = 0;
			highestMid = 0;
			highestHigh = 0;
			timeSince = app.millis();
		}
		
		// iterating through each band in the range
		for (int i = 0; i < fft.specSize() * sub_range; i++) {
			subs += fft.getBand(i);
			if (fft.getBand(i) > highestIndividual)
				highestIndividual = fft.getBand(i);
			if (fft.getBand(i) > highestSub)
				highestSub = fft.getBand(i);
		}
		for (int i = (int) (fft.specSize() * sub_range); i < fft.specSize() * low_range; i++) {
			lows += fft.getBand(i);
			if (fft.getBand(i) > highestIndividual)
				highestIndividual = fft.getBand(i);
			if (fft.getBand(i) > highestLow)
				highestLow = fft.getBand(i);
		}
		for (int i = (int) (fft.specSize() * low_range); i < fft.specSize() * mid_range; i++) {
			mids += fft.getBand(i) * 1.5f;
			if (fft.getBand(i) > highestIndividual)
				highestIndividual = fft.getBand(i);
			if (fft.getBand(i) > highestMid)
				highestMid = fft.getBand(i);
		}
		for (int i = (int) (fft.specSize() * mid_range); i < fft.specSize() * high_range; i++) {
			highs += fft.getBand(i) * 5f;
			if (fft.getBand(i) > highestIndividual)
				highestIndividual = fft.getBand(i);
			if (fft.getBand(i) > highestHigh)
				highestHigh = fft.getBand(i);
		}
		intensity = subs + lows + mids + highs;
	}
	
	// returns the full band array
	public float[] getBandArray() {
		return new float[] {subs, lows, mids, highs};
	}
	
	// toggles the song with direct control of enabling and disabling music
	public void toggleSong(boolean enabled) {
		if (!enabled) {
			song.pause();
			songPos = song.position();
			isThereSound = false;
		} else {
			song.play(songPos);
		    isThereSound = true;
		}
	}
	
	// toggles playing the current set song
	public void toggleSong() {
		toggleSong(!song.isPlaying());
	}
}
