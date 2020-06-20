package core.components;

import ddf.minim.AudioMetaData;
import ddf.minim.AudioPlayer;

public class SongStruct {
	
	public AudioPlayer audio;
	public AudioMetaData meta;
	
	public SongStruct(AudioPlayer audio, AudioMetaData meta) {
		this.audio = audio;
		this.meta = meta;
	}
}
