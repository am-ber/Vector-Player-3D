package com.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SoundScapeTest {

	SoundScape scape;
	//Minim minim;
	//AudioPlayer song;
	String artist = "Konami";
	
	@Before
	public void setUp() throws Exception {
		scape = new SoundScape();
		//minim = new Minim(scape);
		//song = minim.loadFile("res/song.mp3");
		// These would work if we could actually setup a sketch through processing
	}

	@Test
	public void testMetaString() {
		assertEquals(artist,"Konami");
	}

	@Test
	public void testRandomFloatFloat() {
		assertFalse(scape.random(1, 10) > 11);
	}

	@Test
	public void testNoiseFloat() {
		assertTrue(scape.noise(45564657851651f) < 1.1f);
	}

	@After
	public void tearDown() {
		scape = null;
	}
}
