package gk.jfilter.impl.filter.bean;

public interface Bean {
	Class<?> getType();

	Bean getProperty(String propertyName);

	Object getValue(Object object);

}
