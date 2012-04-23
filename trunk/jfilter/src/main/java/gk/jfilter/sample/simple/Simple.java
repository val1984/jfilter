package gk.jfilter.sample.simple;

import gk.jfilter.JFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Simple sample program to demostrate how to use JFilter.
 * 
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 * 
 */
public class Simple {

	public static final List<String> animals = Arrays.asList("dog", "cat", "rat", "Lion", "tiger", " elephant ");

	public static void main(String[] args) {
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		/** filter has trim method of String class */
		System.out.println(filter.execute("{'trim':'?1'}", "elephant"));
		/** filter has empty property ( isEmpty method) of String class */
		System.out.println(filter.execute("{'empty':'?1'}", true));
		/** filter has length method of String class */
		System.out.println(filter.execute("{'length':'?1'}", 3));
		/** filter has toUpperCase method of String class */
		System.out.println(filter.execute("{'toUpperCase':'?1'}", "LION"));
		/** filter has toLowerCase method of String class */
		System.out.println(filter.execute("{'toLowerCase':'?1'}", "lion"));
		/** filter has class property (getClass method) of String super class Object which has name property */
		System.out.println(filter.execute("{'class.name':'?1'}", "java.lang.String"));
	}
}
