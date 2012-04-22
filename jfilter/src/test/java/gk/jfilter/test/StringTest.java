package gk.jfilter.test;

import static org.junit.Assert.assertEquals;
import gk.jfilter.JFilter;
import gk.jfilter.test.common.model.Animal;
import gk.jfilter.test.common.model.Cat;
import gk.jfilter.test.common.model.Dog;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class StringTest {
	final private static List<String> animals = new ArrayList<String>();
	
	@BeforeClass
	public static void  insertObjects() {				
		animals.add("dog");
		animals.add("cat");
		animals.add("rat");
		animals.add("Lion");
		animals.add("tiger");
		animals.add(" elephant ");
	}

	@Test
	public void testToString() {	
		"a".toUpperCase();
		"".toLowerCase();
		"".length();
		"".isEmpty();
		"".trim();
		JFilter<String> filter = new JFilter<String>(animals, String.class);
		
		List<String> fa = new ArrayList<String>(filter.execute("{'toString':{'$cts':'?1'}}", "a"));
		assertEquals(3,fa.size());
	}
	
	
	@Test
	public void testToUpperLower() {	
		
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		assertEquals(1,filter.execute("{'toString.toUpperCase':'?1'}", "LION").size());
		assertEquals(1,filter.execute("{'toString.toLowerCase':'?1'}", "lion").size());
	}
	
	@Test
	public void testTrim() {	
		
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		assertEquals(1,filter.execute("{'toString.trim':'?1'}", "elephant").size());
	}
	
	@Test
	public void testIsEmpty() {	
		
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		assertEquals(6,filter.execute("{'toString.empty':'?1'}", false).size());
		assertEquals(0,filter.execute("{'toString.empty':'?1'}", true).size());
	}
	
	@Test
	public void testLength() {	
		
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		assertEquals(3,filter.execute("{'toString.length':'?1'}", 3).size());
		assertEquals(1,filter.execute("{'toString.length':'?1'}", 4).size());
	}
}
