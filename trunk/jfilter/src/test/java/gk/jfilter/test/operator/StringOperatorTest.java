package gk.jfilter.test.operator;

import static org.junit.Assert.assertEquals;
import gk.jfilter.JFilter;
import gk.jfilter.test.common.model.Animal;
import gk.jfilter.test.common.model.Cat;
import gk.jfilter.test.common.model.Dog;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class StringOperatorTest {
	final private static List<Animal> animals = new ArrayList<Animal>();
	@BeforeClass
	public static void  insertObjects() {
		animals.add(new Dog("dog1", 4, "black"));
		animals.add(new Cat("cat1", 4, "black"));
		
		animals.add(new Dog("dog2", 4, "white"));
		animals.add(new Cat("cat2", 4, "white"));
	}
	
	@Test
	public void testEQ() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$sw':'?1'}}", "dog"));
		assertEquals(2,fa.size());
	}
	
	@Test
	public void testNE() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$ne':'?1'}}", "dog1"));
		assertEquals(3,fa.size());
	}
	
	@Test
	public void testGT() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$gt':'?1'}}", "dog1"));
		assertEquals(1,fa.size());
	}
	
	@Test
	public void testGE() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$ge':'?1'}}", "dog1"));
		assertEquals(2,fa.size());
	}
	
	@Test
	public void testLT() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$lt':'?1'}}", "dog1"));
		assertEquals(2,fa.size());
	}
	
	@Test
	public void testLE() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$le':'?1'}}", "dog1"));
		assertEquals(3,fa.size());
	}
	
	@Test
	public void testSW() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$sw':'?1'}}", "cat"));
		assertEquals(2,fa.size());
	}
	
	@Test
	public void testEW() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$ew':'?1'}}", "1"));
		assertEquals(2,fa.size());
	}
	
	@Test
	public void testCTS() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'name':{'$cts':'?1'}}", "a"));
		assertEquals(2,fa.size());
	}
	
}

