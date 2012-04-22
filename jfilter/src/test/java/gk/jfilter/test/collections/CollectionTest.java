package gk.jfilter.test.collections;

import static org.junit.Assert.assertEquals;
import gk.jfilter.JFilter;
import gk.jfilter.test.common.model.Animal;
import gk.jfilter.test.common.model.Cat;
import gk.jfilter.test.common.model.Dog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CollectionTest {
	final private static List<Animal> animals = new ArrayList<Animal>();
	
	@BeforeClass
	public static void  insertObjects() {
		//add dog family
		Animal dog1 = new Dog("dog1", 4, "black");
		Animal dog2 = new Dog("dog2", 4, "white");
		Animal dog3 = new Dog("dog3", 4, "black");
		
		dog1.addChild(dog2);
		dog1.addChild(dog3);
		
		animals.add(dog1);
		animals.add(dog2);
		animals.add(dog3);
		
		// add cat family
		Animal cat1 = new Cat("cat1", 4, "black");
		Animal cat2 = new Cat("cat2", 4, "white");
		Animal cat3 = new Cat("cat3", 4, "black");
		Animal cat4 = new Cat("cat4", 4, "white");
		
		cat1.addChild(cat2);
		cat1.addChild(cat3);
		cat1.addChild(cat4);
	
		animals.add(cat1);
		animals.add(cat2);
		animals.add(cat3);
		animals.add(cat4);
	}

	@Test
	public void testLegs() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'legs':'?1'}", 4));
		assertEquals(7,fa.size());
	}
	
	@Test
	public void testCountCats() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("type", "cat");
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'type':'?type'}", parameters));
		assertEquals(4,fa.size());
	}
	
	@Test
	public void testCountDogs() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("type", "dog");
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'type':'?type'}", parameters));
		assertEquals(3,fa.size());
	}
	
	@Test
	public void testChildrenType() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("type", "cat");

		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'children.type':'?type'}", parameters));
		assertEquals(1,fa.size());
	}
	
	@Test
	public void testGetClass() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("class", "gk.jfilter.test.common.model.Cat");
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'class.name':'?class'}", parameters));
		assertEquals(4,fa.size());
	}
	
	@Ignore
	public void testToString() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("class", "gk.jfilter.test.common.model.Cat");
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'class.name.toString':'?class'}", parameters));
		assertEquals(4,fa.size());
	}

}
