package com.core;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

public class PlaylistMenu extends PApplet {

	SoundScape scape;
	int padding = 10;
	PVector scrubPos;
	
	public PlaylistMenu(SoundScape parent) {
		this.scape = parent;
	}

	public void settings() {
		size(scape.width - 4, scape.height - 75);
	}
	
	public void setup() {
		background(10);
		getSurface().setFrameRate(10);
		getSurface().setTitle("VP3D Playlist");
		
		scrubPos = new PVector(padding,padding);
	}
	public void draw() {
		update();
		fill(230);
		rect(padding,padding + 2,width - (padding * 2), 5);
		rect(scrubPos.x, scrubPos.y,4,padding);
	}
	private void update() {
		if (millis() > 2000)
			scrubPos = new PVector(map(scape.song.position(),0,scape.song.length(),padding, width - (padding * 2)),padding);
		
	}

	public void mouseWheel(MouseEvent event) {
		float e = event.getCount();
		if (scape.songGain < 0) scape.songGain += map(e, 1, 0, 0, 1);
		if (scape.songGain > -80) scape.songGain += map(e, -1, 0, 0, -1);
		if (scape.songGain > 0) scape.songGain = 0;
		if (scape.songGain < -80) scape.songGain = -80;
	}
	public void keyPressed() {
		if (key == 'p') 
			scape.togglePlaylist();
		if (keyCode == 32)
			scape.toggleSong();
	}
	public void exit() {
		scape.togglePlaylist();
	}
}
