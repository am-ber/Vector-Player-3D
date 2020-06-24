package core;

import core.components.SongStruct;
import ddf.minim.AudioInput;
import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

public class MusicHandler {
	
	// public access objects and variables
	public FFT fft;
	public AudioMetaData currentSongMeta;
	public float songGain = 0;
	public int songPos = 0;
	public float songLength = 0;
	public boolean isThereSound = false;
	public boolean songPlaying = false;
	public float intensity = 0;
	
	// private objects and variables
	private PApplet app;
	private Minim minim;
	private SongStruct currentSong;
	private AudioInput lineIn;
	private long timeSince = 0;
	private int timePeriod = 1000;
	private float currentSongGain = 1.0f;
	
	// fft related
	public float sub_range = 0.01f;				// These ranges are tuned
	public float low_range = 0.05f;				// Try not to change these
	public float mid_range = 0.30f;				// As they need to add up to 1.0
	public float high_range = 0.38f;			// It is extremely picky about
	public float unused_range = 0.26f;			// Which ranges have what
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
	
	// loads a song to the current playlist
	public void addSongToPlaylist(String file) {
		
	}
	
	// sets the current song
	public void setSong(String file) {
		try {
			AudioPlayer temp = minim.loadFile(file);
			currentSong = new SongStruct(temp, temp.getMetaData());
			currentSongMeta = currentSong.meta;
		} catch (Exception e) {
			PApplet.println("We got a "+e.toString()+" error. So uh, yea.");
		}
		songLength = currentSong.audio.length();
		songPos = 0;
		isThereSound = false;
		fft = new FFT(currentSong.audio.bufferSize(), currentSong.audio.sampleRate());
	}
	
	// called in a loop
	public void update() {
		songPos = currentSong.audio.position();
		
		// check which method of audio to forward to fft
		if (currentSong.audio.isPlaying())
			fft.forward(currentSong.audio.mix);
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
	
	// sets the current song volume between 0 and 1
	public boolean setSongGain(float volume) {
		if (volume <= 1.0f & volume >= -0.05f) {
			currentSongGain = volume;
			currentSong.audio.setGain(volume <= 0 ? -80 : PApplet.map(volume, 0, 1, -40, 0));
			return true;
		}
		return false;
	}
	
	public float getSongGain() {
		return currentSongGain;
	}
	
	// adjusts volume
	public void toggleVolume() {
		toggleVolume(!currentSong.audio.isMuted());
	}
	
	// adjusts volume
	public void toggleVolume(boolean enable) {
		if (enable)
			currentSong.audio.unmute();
		else
			currentSong.audio.mute();
	}
	
	// toggles the song with direct control of enabling and disabling music
	public void toggleSong(boolean enabled) {
		if (!enabled) {
			currentSong.audio.pause();
			songPos = currentSong.audio.position();
			isThereSound = false;
			songPlaying = false;
		} else {
			currentSong.audio.play(songPos);
		    isThereSound = true;
		    songPlaying = true;
		}
	}
	
	// toggles playing the current set song
	public void toggleSong() {
		toggleSong(!currentSong.audio.isPlaying());
	}
}
