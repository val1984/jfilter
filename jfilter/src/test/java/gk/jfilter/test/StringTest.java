package gk.jfilter.test;

import static org.junit.Assert.assertEquals;
import gk.jfilter.JFilter;

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
		JFilter<String> filter = new JFilter<String>(animals, String.class);
		
		List<String> fa = filter.execute("{'toString':{'$cts':'?1'}}", "a");
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

		assertEquals(1,filter.execute("{'trim':'?1'}", "elephant").size());
	}
	
	@Test
	public void testIsEmpty() {	
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		assertEquals(6,filter.execute("{'empty':'?1'}", false).size());
		assertEquals(0,filter.execute("{'empty':'?1'}", true).size());
	}
	
	@Test
	public void testLength() {	
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		assertEquals(3,filter.execute("{'length':'?1'}", 3).size());
		assertEquals(1,filter.execute("{'length':'?1'}", 4).size());
	}
}
