package com.core;

import processing.core.PVector;

class Particle {
	PVector position;
	PVector velocity;
	PVector acceleration;
	float lifespan;
	SoundScape scape;
	private float vel = 5, accel = 0.07f;

	public Particle(PVector l, SoundScape scape) {
		this.scape = scape;
		velocity = new PVector(scape.random(-vel, vel), scape.random(-vel, vel), scape.random(-vel, vel));
		acceleration = new PVector(((velocity.x > 0) ? -accel : accel),
									((velocity.y > 0) ? -accel : accel),
									((velocity.z > 0) ? -accel : accel));
		position = l.copy();
		lifespan = 255.0f;
	}

	public void run() {
		update();
		display();
	}

	public void update() {
		velocity.add(acceleration);
		position.add(velocity);
		lifespan -= 1;
	}

	public void display() {
		scape.pushMatrix();
		scape.noStroke();
		scape.fill(scape.lows, scape.mids ,scape.highs ,lifespan);
		scape.translate(position.x, position.y, position.z);
		scape.sphere(scape.random(10, (scape.intensity / 6)));
		scape.popMatrix();
	}

	public boolean isDead() {
		if (lifespan < 0.0) {
			return true;
		} else {
			return false;
		}
	}
}