package com.tools;

import java.util.Random;

public class RandomGen {
	Random random = new Random();
	/**
	 * <i><strong>Next Random Character</strong></i><br><br>
	 * Use this to generate a random Character
	 * set of any set length.<br><br>
	 * @param length <br>
	 * The length of the Character set you want generated.
	 * @return String
	 * @author Zach V.
	 */
	public String nextCharSet(long length){
		String sender = "";
		for(long i=0; i < length; i++){
			sender += nextChar();
		}
		return sender;
	}
	public String nextString(long length){
		String sender = "";
		for(int i=0; i < length; i++){
			long number = random.nextInt(2);
			if(number == 1)
				sender += nextStringSymbol();
			else
				sender += nextChar();
		}
		return sender;
	}
	/**
	 * <i><strong>Next Random String Symbol</strong></i><br><br>
	 * Use this to generate a random symbol on your keyboard.<br><br>
	 * @return char
	 * @author Zach V.
	 */
	public String nextStringSymbol(){
		String sender = "";
		int number = random.nextInt(31);
		switch(number){
			case 0: sender = "/";
			case 1: sender = "\\";
			case 2: sender = "\'";
			case 3: sender = "\"";
			case 4: sender = "$";
			case 5: sender = "!";
			case 6: sender = "@";
			case 7: sender = "#";
			case 8: sender = "%";
			case 9: sender = "^";
			case 10: sender = "&";
			case 11: sender = "*";
			case 12: sender = "(";
			case 13: sender = ")";
			case 14: sender = "-";
			case 15: sender = "_";
			case 16: sender = "+";
			case 17: sender = "=";
			case 18: sender = "[";
			case 19: sender = "]";
			case 20: sender = "{";
			case 21: sender = "}";
			case 22: sender = "|";
			case 23: sender = "<";
			case 24: sender = ">";
			case 25: sender = ",";
			case 26: sender = ".";
			case 27: sender = "?";
			case 28: sender = ":";
			case 29: sender = ";";
			case 30: sender = " ";
		}
		return sender;
	}
	/**
	 * <i><strong>Next Random Character</strong></i><br><br>
	 * Use this to generate a random character
	 * between A and Z upper and lower case.<br><br>
	 * @return char
	 * @author Zach V.
	 */
	public char nextChar(){
		char sender = 'a';
		int number = random.nextInt(52);
		switch(number){
			case 0: sender = 'a'; break;
			case 1: sender = 'b'; break;
			case 2: sender = 'c'; break;
			case 3: sender = 'd'; break;
			case 4: sender = 'e'; break;
			case 5: sender = 'f'; break;
			case 6: sender = 'g'; break;
			case 7: sender = 'h'; break;
			case 8: sender = 'i'; break;
			case 9: sender = 'j'; break;
			case 10: sender = 'k'; break;
			case 11: sender = 'l'; break;
			case 12: sender = 'm'; break;
			case 13: sender = 'n'; break;
			case 14: sender = 'o'; break;
			case 15: sender = 'p'; break;
			case 16: sender = 'q'; break;
			case 17: sender = 'r'; break;
			case 18: sender = 's'; break;
			case 19: sender = 't'; break;
			case 20: sender = 'u'; break;
			case 21: sender = 'v'; break;
			case 22: sender = 'w'; break;
			case 23: sender = 'x'; break;
			case 24: sender = 'y'; break;
			case 25: sender = 'z'; break;
			case 26: sender = 'A'; break;
			case 27: sender = 'B'; break;
			case 28: sender = 'C'; break;
			case 29: sender = 'D'; break;
			case 30: sender = 'E'; break;
			case 31: sender = 'F'; break;
			case 32: sender = 'G'; break;
			case 33: sender = 'H'; break;
			case 34: sender = 'I'; break;
			case 35: sender = 'J'; break;
			case 36: sender = 'K'; break;
			case 37: sender = 'L'; break;
			case 38: sender = 'M'; break;
			case 39: sender = 'N'; break;
			case 40: sender = 'O'; break;
			case 41: sender = 'P'; break;
			case 42: sender = 'Q'; break;
			case 43: sender = 'R'; break;
			case 44: sender = 'S'; break;
			case 45: sender = 'T'; break;
			case 46: sender = 'U'; break;
			case 47: sender = 'V'; break;
			case 48: sender = 'W'; break;
			case 49: sender = 'X'; break;
			case 50: sender = 'Y'; break;
			case 51: sender = 'Z'; break;
		}
		return sender;
	}
}
