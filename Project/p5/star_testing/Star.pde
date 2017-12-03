class Star {
  float x;
  float y;
  float z;
  float size;
  
  Star() {
    x = random(0, width);
    y = random(0, height);
    z = random(0, width);
    size = random(0.5, 7);
  }
  
  void update() {
    
  }
  
  void show() {
    fill(255);
    noStroke();
    ellipse(x, y, size, size);
  }
}