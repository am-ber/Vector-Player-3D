package com.metaget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.wav.WavTag;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Song {
	
	private String title;
	private String artist;
	private String genre;
	private String album;
	
	public static boolean wav = true;
	
	public Song(String filePath) {
		if(wav)
			try {
				AudioFile f = AudioFileIO.read(new File(filePath));
				WavTag tag = (WavTag) f.getTag();
	
				title = tag.getFirst(FieldKey.TITLE);
				artist = tag.getFirst(FieldKey.ARTIST);
				genre = tag.getFirst(FieldKey.GENRE);
				album = tag.getFirst(FieldKey.ALBUM);
				        
			} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
					| InvalidAudioFrameException e1) {
				e1.printStackTrace();
			}
		else
			try {
				InputStream song = new FileInputStream(new File(filePath));
				ContentHandler handler = new DefaultHandler();
				Metadata metadata = new Metadata();
				Parser parser = new Mp3Parser();
				ParseContext parseCtx = new ParseContext();
				parser.parse(song, handler, metadata, parseCtx);
				song.close();
				
				title = metadata.get("title");
				artist =  metadata.get("xmpDM:artist");
				genre = metadata.get("xmpDM:genre");
			
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (TikaException e) {
				e.printStackTrace();
			}	
	}
	
	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
}
