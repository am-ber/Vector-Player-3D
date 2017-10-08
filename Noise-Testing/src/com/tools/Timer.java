package com.tools;

public class Timer {
	long startTime = 0;
	long stopTime = 0;
	long timeTaken = 0;
	
	/**
	 * <i><strong>Start Timing in Milliseconds</strong></i><br><br>
	 * Use this to save the current system millisecond to use for later.<br><br>
	 * @author Zach V.
	 */
	public void startTimingM(){
		startTime = System.currentTimeMillis();
	}
	/**
	 * <i><strong>Get Time difference in Milliseconds</strong></i><br><br>
	 * Hopefully you remembered to start the timer cause this will just report 0.
	 * Use this to get the difference between the time you started the timer and ended it.<br><br>
	 * @author Zach V.
	 */
	public long stopTimeM(){
		if(startTime != 0){
			stopTime = System.currentTimeMillis();
			timeTaken = stopTime - startTime;
		}
		startTime = 0;
		return timeTaken;
	}
	/**
	 * <i><strong>Start Timing in Nano Seconds</strong></i><br><br>
	 * Use this to save the current system nano time to use for later.<br><br>
	 * @author Zach V.
	 */
	public void startTimingN(){
		startTime = System.nanoTime();
	}
	/**
	 * <i><strong>Get Time difference in Nano seconds</strong></i><br><br>
	 * Hopefully you remembered to start the timer cause this will just report 0.
	 * Use this to get the difference between the time you started the timer and ended it.<br><br>
	 * @author Zach V.
	 */
	public long stopTimeN(){
		if(startTime != 0){
			stopTime = System.nanoTime();
			timeTaken = stopTime - startTime;
		}
		startTime = 0;
		return timeTaken;
	}
}
