package core.components;

import processing.core.PApplet;

public class Helper {
	
	public static float[] lerpFloatArray(float[] input, float[] target, float time) {
		if (input.length == target.length) {
			float[] sender = new float[input.length];
			for (int i = 0; i < sender.length; i++) {
				sender[i] = PApplet.lerp(input[i], target[i], time);
			}
			return sender;
		}
		return null;
	}
}
