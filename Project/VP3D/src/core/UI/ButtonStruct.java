package core.UI;

import core.components.GenericMethod;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class ButtonStruct {
	
	private PApplet applet;
	
	public GenericMethod gm;
	public String buttonName = "";
	public PVector position;
	public PVector size;
	public PVector bottomRight;
	public PShape shape;
	public int color;
	public boolean fill;
	// Text settings
	public boolean drawText = false;
	public String text = "";
	public float textPadding = 0;
	public int textColor = 0;
	public float fontSize = 12;
	
	// constructors
	public ButtonStruct(PApplet applet, String buttonName, PVector position, PVector size) {
		this(applet, buttonName, position, size, applet.color(255));
	}
	public ButtonStruct(PApplet applet, String buttonName, PVector position, PVector size, int color) {
		this(applet, buttonName, position, size, color, true);
	}
	public ButtonStruct(PApplet applet, String buttonName, PVector position, PVector size, int color, boolean fill) {
		this(applet, buttonName, position, size, color, true, null);
	}
	public ButtonStruct(PApplet applet, String buttonName, PVector position, PVector size, int color, boolean fill, GenericMethod gm) {
		this.buttonName = buttonName;
		this.applet = applet;
		this.position = position;
		this.size = size;
		this.color = color;
		this.fill = fill;
		this.gm = gm;
		
		bottomRight = new PVector(position.x + size.x, position.y + size.y);
		PApplet.println(buttonName + " button created");
	}
	
	// Setting font
	public ButtonStruct setFont(String text, float padding, float size, int fillColor) {
		drawText = true;
		
		this.text = text;
		textPadding = padding;
		textColor = fillColor;
		fontSize = size;
		
		return this;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean clicked(float mouseX, float mouseY) {
		if (mouseX > position.x && mouseY > position.y) {
			if (mouseX < bottomRight.x && mouseY < bottomRight.y) {
				PApplet.println(buttonName + " pressed.");
				return true;
			}
		}
		return false;
	}
	
	public void function() {
		PApplet.println("Calling method from " + buttonName);
		gm.call();
	}
	
	public void setFunction(GenericMethod gm) {
		this.gm = gm;
	}
	
	public void draw() {
		if (fill) {
			applet.noStroke();
			applet.fill(color);
		} else {
			applet.noFill();
			applet.strokeWeight(1);
			applet.stroke(color);
		}
		applet.rect(position.x, position.y, size.x, size.y);
		
		if (drawText) {
			applet.noStroke();
			applet.textAlign(PApplet.LEFT);
			applet.fill(textColor);
			applet.textSize(fontSize);
			applet.text(text, position.x + textPadding, position.y + (textPadding / 2) + fontSize);
		}
	}
}
