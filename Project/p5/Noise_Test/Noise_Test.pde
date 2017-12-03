import ddf.minim.*;
import ddf.minim.analysis.*;

// Drawing vars
int cols, rows;
int scl = 15;
int w = 1200;
int h = 900;

// Noise vars
float accel = 0;
float[][] terrain;

// Audio imports
Minim minim;
AudioPlayer song;
FFT fft;

// Audio vars
float lows = 0;
float mids = 0;
float highs = 0;

float oldLow = lows;
float oldMid = mids;
float oldHigh = highs;

float decreaseRate = 25;

void setup() {
  size(600, 600,  P3D);
  cols = w / scl;
  rows = h/ scl;
  terrain = new float[cols][rows];
  
  // Audio initializing
  minim = new Minim(this);
  song = minim.loadFile("song.mp3");  // Obviously this will be the selected song
  fft = new FFT(song.bufferSize(), song.sampleRate());
  
  // Plays the song
  //song.play(0);
}

void draw() {
  background(0);
  stroke(255);
  noFill();
  
  // Forwards the song on draw() for each "frame" of the song
  fft.forward(song.mix);
  
  // Setting vars
  oldLow = lows;
  oldMid = mids;
  oldHigh = highs;
  lows = 0;
  mids = 0;
  highs = 0;
  
  // Getting the camera correct
  translate(width / 2, height / 2);
  rotateX(PI/3);
  translate(-w/2, -h/2);
  
  // Populate noise
  float xoff = accel;
  for (int y = 0; y < rows; y++) {
    float yoff = 0;
    for (int x = 0; x < cols; x++) {
      terrain[x][y] = map(noise(xoff,yoff), 0, 1, -75, 75);
      yoff += 0.22;
    }
    xoff += 0.22;
  }
  
  accel -= 0.03;
  
  // Acctually draw it
  for (int y = 0; y < rows-1; y++) {
    beginShape(TRIANGLE_STRIP);
    for (int x = 0; x < cols; x++) {
      vertex(x*scl, y*scl, terrain[x][y]);
      vertex(x*scl, (y+1)*scl, terrain[x][y+1]);
    }
    endShape();
  }
}