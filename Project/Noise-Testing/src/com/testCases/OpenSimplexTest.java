package com.testCases;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.noise.OpenSimplexNoise;

public class OpenSimplexTest {

	OpenSimplexNoise noise = new OpenSimplexNoise();
	
	@Test
	public void test() {
		
		int count = 0;
		
		while(true){
			double value = 0;
			double input = Math.random();
			double input2 = Math.random();
			
			value = noise.eval(input, input2);
			
			System.out.println("Value : "+value);
			
			assertTrue("Out of bounds. value = "+value,!((value > 1) || (value < -1)));
			
			count ++;
			
			if (count > 10000)
				break;
		}
	}
}
