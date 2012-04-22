package gk.jfilter.test.common.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
	List<Animal> children = new ArrayList<Animal>();
	Animal mother = null;
	
	public void addChild(Animal child) {
		children.add(child);
	}

	public List<Animal> getChildren() {
		return children;
	}
	
	public void setMother(Animal mother) {
		this.mother=mother;
	}
	
	public Animal getMother() {
		return mother;
	}
	
	public abstract String getName();

	public abstract int getLegs();

	public abstract String getSound();

	public abstract String getColor();

	public abstract String type();
}
