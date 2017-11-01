package com.metaget;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SongTest {
	Song test;
	String title = "Screaming Eagles";
	String artist = "Sabaton";
	String genre = "Power Metal";
	String falseTitle = "Screming Eagles";
	String falseArtist = "Sbaton";
	String falseGenre = "Death Metal";

	@Before
	public void setUp() throws Exception {
		test = new Song("C:\\Users\\User\\Desktop\\Music\\2010 Sabaton Coat Of Arms\\04. Screaming Eagles.mp3");
	}

	@Test
	public void testGetTitle() {
		assertTrue(title.equals(test.getTitle()));
		assertFalse(falseTitle.equals(test.getTitle()));
	}

	@Test
	public void testGetGenre() {
	assertTrue(genre.equals(test.getGenre()));
	assertFalse(falseGenre.equals(test.getGenre()));
	}

	@Test
	public void testGetArtist() {
		assertTrue(artist.equals(test.getArtist()));
		assertFalse(falseArtist.equals(test.getArtist()));
	}

}
