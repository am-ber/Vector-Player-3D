package com.gui;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class MainWindowTest {
	MainWindow test;
	String testString;
	
	@Before
	public void setUp() throws Exception {
		test = new MainWindow();
		test.refreshMetadata("F:\\Music\\Persona 5\\Persona 5 Original Soundtrack [2017]\\Disc 1\\29. Beneath the Mask.mp3");
		testString = "Beneath the Mask";
	}

	@Test
	public void testRefreshMetadata() {
		assertTrue(testString.equals(test.lblSongName.getText()));
		testString = "Lyn";
		assertTrue(testString.equals(test.lblArtist.getText()));
		testString = "Game";
		assertTrue(testString.equals(test.lblGenre.getText()));
		
		test.refreshMetadata("F:\\Music\\No Logic.mp3");
		
		testString = "NO DATA FOUND";
		assertTrue(testString.equals(test.lblSongName.getText()));
		assertTrue(testString.equals(test.lblArtist.getText()));
		assertTrue(testString.equals(test.lblGenre.getText()));
		
	}

	@Test
	public void testRefreshSongList() {
		assertSame(3, test.refreshSongList(new File("F:\\Music\\Persona 5\\Persona 5 Original Soundtrack [2017]\\TestDir")));
		assertSame(36, test.refreshSongList(new File("F:\\Music\\Persona 5\\Persona 5 Original Soundtrack [2017]\\Disc 1")));
		assertSame(-1, test.refreshSongList(new File("F:\\NonexistantFolder")));
	}

	@Test
	public void testGetDirectory() {
		//getDirectory needs extensive user input to determine output, and is thus not suited to unit testing.
	}

}
