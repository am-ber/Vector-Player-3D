package core.UI;

import processing.core.PApplet;
import processing.core.PVector;

public class Slider {
	
	private PApplet applet;
	
	public ButtonStruct sliderButton;
	public PVector position;
	public PVector size;
	public PVector bottomRight;
	
	public String sliderName = "";
	public float beginningValue = 0;
	public float currentValue = 0;
	public float endingValue = 0;
	public boolean fill = false;
	public boolean horizontal = true;
	public boolean clickable = true;
	public int color = 0;
	
	// constructor chain
	public Slider(PApplet applet, String sliderName, PVector position, PVector size, float beginningValue, float endingValue, boolean horizontal) {
		this(applet, sliderName, position, size, beginningValue, endingValue, horizontal, applet.color(0,0,100), false, true);
	}
	public Slider(PApplet applet, String sliderName, PVector position, PVector size, float beginningValue, float endingValue, boolean horizontal, boolean clickable) {
		this(applet, sliderName, position, size, beginningValue, endingValue, horizontal, applet.color(0,0,100), false, clickable);
	}
	public Slider(PApplet applet, String sliderName, PVector position, PVector size, float beginningValue, float endingValue, float startingValue, boolean horizontal, boolean clickable) {
		this(applet, sliderName, position, size, beginningValue, endingValue, startingValue, horizontal, applet.color(0,0,100), false, clickable);
	}
	public Slider(PApplet applet, String sliderName, PVector position, PVector size, float beginningValue, float endingValue, boolean horizontal, int color, boolean fill, boolean clickable) {
		this(applet, sliderName, position, size, beginningValue, endingValue, 0, horizontal, color, fill, clickable);
	}
	public Slider(PApplet applet, String sliderName, PVector position, PVector size, float beginningValue, float endingValue, float startingValue, boolean horizontal, int color, boolean fill, boolean clickable) {
		this.applet = applet;
		this.sliderName = sliderName;
		this.position = position;
		this.size = size;
		this.beginningValue = beginningValue;
		this.endingValue = endingValue;
		this.currentValue = startingValue;
		this.horizontal = horizontal;
		this.color = color;
		this.fill = fill;
		this.clickable = clickable;
		
		bottomRight = new PVector(position.x + size.x, position.y + size.y);
		
		PApplet.println(sliderName + " slider created");
		
		if (horizontal)
			sliderButton = new ButtonStruct(applet, sliderName, buttonPosition(), new PVector(size.y / 2, size.y), color, fill);
		else
			sliderButton = new ButtonStruct(applet, sliderName, buttonPosition(), new PVector(size.x, size.x / 2), color, fill);
	}
	
	// draw method which is called in a loop
	public void draw() {
		if (fill) {
			applet.noStroke();
			applet.fill(color);
		} else {
			applet.noFill();
			applet.stroke(color);
		}
		applet.rect(position.x, position.y, size.x, size.y);
		sliderButton.draw();
	}
	
	// updates the slider value on its own if needed
	public void updateValue(float value) {
		currentValue = value;
		sliderButton.position = buttonPosition();
	}
	
	public boolean clicked(float mouseX, float mouseY) {
		if (mouseX > position.x && mouseY > position.y) {
			if (mouseX < bottomRight.x && mouseY < bottomRight.y) {
				return true;
			}
		}
		return false;
	}
	
	private PVector buttonPosition() {
		float x = 0;
		float y = 0;
		if (horizontal) {
			x = PApplet.map(currentValue, beginningValue, endingValue, position.x + (size.y), bottomRight.x - (size.y));
			y = position.y;
		} else {
			y = PApplet.map(currentValue, beginningValue, endingValue, position.y + (size.x), bottomRight.y - (size.x));
			x = position.x;
		}
		return new PVector(x, y);
	}
}
