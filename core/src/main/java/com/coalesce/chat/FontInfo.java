package com.coalesce.chat;

import java.util.HashMap;
import java.util.Map;

public class FontInfo {

	private static Map<Character, Integer> charSizes;

	public static int getCharSize(char c){
		return getCharSize(c, false);
	}

	public static int getCharSize(char c, boolean bold){

		if (charSizes == null){
			initSizes();
		}

		int size = charSizes.get(c);
		if (bold && c != ' '){
			size += 1;
		}

		return size;
	}

	private static void initSizes(){
		charSizes = new HashMap<>();
		charSizes.put('A', 5);
		charSizes.put('a', 5);
		charSizes.put('B', 5);
		charSizes.put('b', 5);
		charSizes.put('C', 5);
		charSizes.put('c', 5);
		charSizes.put('D', 5);
		charSizes.put('d', 5);
		charSizes.put('E', 5);
		charSizes.put('e', 5);
		charSizes.put('F', 5);
		charSizes.put('f', 4);
		charSizes.put('G', 5);
		charSizes.put('g', 5);
		charSizes.put('H', 5);
		charSizes.put('h', 5);
		charSizes.put('I', 3);
		charSizes.put('i', 1);
		charSizes.put('J', 5);
		charSizes.put('j', 5);
		charSizes.put('K', 5);
		charSizes.put('k', 4);
		charSizes.put('L', 5);
		charSizes.put('l', 1);
		charSizes.put('M', 5);
		charSizes.put('m', 5);
		charSizes.put('N', 5);
		charSizes.put('n', 5);
		charSizes.put('O', 5);
		charSizes.put('o', 5);
		charSizes.put('P', 5);
		charSizes.put('p', 5);
		charSizes.put('Q', 5);
		charSizes.put('q', 5);
		charSizes.put('R', 5);
		charSizes.put('r', 5);
		charSizes.put('S', 5);
		charSizes.put('s', 5);
		charSizes.put('T', 5);
		charSizes.put('t', 4);
		charSizes.put('U', 5);
		charSizes.put('u', 5);
		charSizes.put('V', 5);
		charSizes.put('v', 5);
		charSizes.put('W', 5);
		charSizes.put('w', 5);
		charSizes.put('X', 5);
		charSizes.put('x', 5);
		charSizes.put('Y', 5);
		charSizes.put('y', 5);
		charSizes.put('Z', 5);
		charSizes.put('z', 5);
		charSizes.put('1', 5);
		charSizes.put('2', 5);
		charSizes.put('3', 5);
		charSizes.put('4', 5);
		charSizes.put('5', 5);
		charSizes.put('6', 5);
		charSizes.put('7', 5);
		charSizes.put('8', 5);
		charSizes.put('9', 5);
		charSizes.put('0', 5);
		charSizes.put('!', 1);
		charSizes.put('@', 6);
		charSizes.put('#', 5);
		charSizes.put('$', 5);
		charSizes.put('%', 5);
		charSizes.put('^', 5);
		charSizes.put('&', 5);
		charSizes.put('*', 5);
		charSizes.put('(', 4);
		charSizes.put(')', 4);
		charSizes.put('-', 5);
		charSizes.put('_', 5);
		charSizes.put('+', 5);
		charSizes.put('=', 5);
		charSizes.put('{', 4);
		charSizes.put('}', 4);
		charSizes.put('[', 3);
		charSizes.put(']', 3);
		charSizes.put(':', 1);
		charSizes.put(';', 1);
		charSizes.put('"', 3);
		charSizes.put('\'', 1);
		charSizes.put('<', 4);
		charSizes.put('>', 4);
		charSizes.put('?', 5);
		charSizes.put('/', 5);
		charSizes.put('\\', 5);
		charSizes.put('|', 1);
		charSizes.put('~', 5);
		charSizes.put('`', 2);
		charSizes.put('.', 1);
		charSizes.put(',', 1);
		charSizes.put(' ', 3);
		charSizes.put('a', 4);
	}

}
