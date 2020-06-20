package core.components;

import java.text.DecimalFormat;

import processing.core.PApplet;

public class Helper {
	
	// Used to linearly interpolate between an array of floats
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
	
	// converts millis to readable hh/mm/ss string
	public static String printNiceMillis(int millis) {
		float seconds = (float) Math.floor((millis / 1000) % 60);
		float minutes = (float) Math.floor((millis / (1000 * 60)) % 60);
		float hours = (float) Math.floor((millis / (1000 * 60 * 60)) % 24);
		String sender = "";
		
		if (hours > 0)
			sender += hours + ":";
		sender += (minutes < 10 ? "0" + (int) (minutes) : (int) (minutes)) + ":" +
				(seconds < 10 ? "0" + (int) (seconds) : (int) (seconds));
		
		return sender;
	}
	
	public static String roundDecimalsToString(float number) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(number);
	}
}
