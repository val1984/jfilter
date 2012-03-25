package gk.jfilter.impl.filter.bean;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;

public class ArrayBean extends AbstractBean {

	public ArrayBean(Class<?> type, Method readMethod) throws IntrospectionException {
		super(type, readMethod);

		if (type.getComponentType().isPrimitive() || Comparable.class.isAssignableFrom(type.getComponentType())) {
			properties.put("get", new SimpleBean(type.getComponentType()));
		} else {
			populateProperties(type.getComponentType());
		}

	}
}
