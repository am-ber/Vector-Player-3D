package com.core;

import java.util.ArrayList;

import processing.core.PVector;

public class ParticleSystem {
	
	/* The reason I added Javadoc to this is cause its a system
	 * that handles multiple objects on its own from the main class.
	 */

	ArrayList<Particle> particles;
	PVector origin;
	SoundScape scape;
	
	int capacity = 1;

	public ParticleSystem(PVector position, SoundScape scape, int capacity) {
		this.scape = scape;
		origin = position.copy();
		particles = new ArrayList<Particle>();
		
		this.capacity = capacity;
		
		for (int i=0; i < (int)scape.random(25); i++) {
			particles.add(new Particle(origin, scape));
		}
	}
	/** <b>changePos()</b></br>
	 * Randomly moves itself to a new place on the grid.</br>
	 * Repopulates the particles to draw.
	 */
	public void changePos() {
		origin = new PVector(scape.random(0,scape.width), scape.random(0, scape.height));
		for (int i=0; i < (int)scape.random(capacity); i++) {
			particles.add(new Particle(origin, scape));
		}
	}
	/** <b>run()</b></br>
	 * draws the particles and runs smol physics
	 */
	public void run() {
		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle p = particles.get(i);
			p.run();
			if (p.isDead()) {
				particles.remove(i);
			}
		}
	}
	
}
