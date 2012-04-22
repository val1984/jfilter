package gk.jfilter.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import gk.jfilter.JFilter;
import gk.jfilter.test.common.model.Animal;
import gk.jfilter.test.common.model.Cat;
import gk.jfilter.test.common.model.Dog;

import org.junit.BeforeClass;
import org.junit.Test;

public class Random {
	
	final private static List<Animal> animals = new ArrayList<Animal>();
	
	@BeforeClass
	public static void  insertObjects() {
		//add dog family
		Animal dog1 = new Dog("dog1", 4, "black");
		Animal dog2 = new Dog("dog2", 4, "white");
		Animal dog3 = new Dog("dog3", 4, "black");
		
		dog1.addChild(dog2);
		dog1.addChild(dog3);
		
		dog2.setMother(dog1);
		dog3.setMother(dog1);
		
		animals.add(dog1);
		animals.add(dog2);
		animals.add(dog3);
		
		// add cat family
		Animal cat1 = new Cat("cat1", 4, "white");
		Animal cat2 = new Cat("cat2", 4, "white");
		Animal cat3 = new Cat("cat3", 4, "black");
		Animal cat4 = new Cat("cat4", 4, "white");
		
		cat1.addChild(cat2);
		cat1.addChild(cat3);
		cat1.addChild(cat4);
		
		cat2.setMother(cat1);
		cat3.setMother(cat1);
		cat4.setMother(cat1);
	
		animals.add(cat1);
		animals.add(cat2);
		animals.add(cat3);
		animals.add(cat4);
	}

	@Test
	public void testMother() {
		JFilter<Animal> filter = new JFilter<Animal>(animals, Animal.class);
		
		List<Animal> fa = new ArrayList<Animal>(filter.execute("{'mother.color':'?1'}", "black"));
		assertEquals(3,fa.size());
	}
}