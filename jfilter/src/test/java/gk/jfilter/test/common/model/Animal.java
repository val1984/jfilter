package gk.jfilter.test.common.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal {
	List<Animal> children = new ArrayList<Animal>();
	
	public void addChild(Animal child) {
		children.add(child);
	}

	public List<Animal> getChildren() {
		return children;
	}
	
	public abstract String getName();

	public abstract int getLegs();

	public abstract String getSound();

	public abstract String getColor();

	public abstract String type();
}
