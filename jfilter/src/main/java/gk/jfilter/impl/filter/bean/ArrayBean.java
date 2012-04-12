package gk.jfilter.impl.filter.bean;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;

public class ArrayBean extends AbstractBean {

	public ArrayBean(Class<?> type, Method readMethod, Bean parent) throws IntrospectionException {
		super(type, readMethod, parent);
		populateProperties(this.type);
		
		if (type.getComponentType().isPrimitive() || Comparable.class.isAssignableFrom(type.getComponentType())) {
			properties.put("get", new SimpleBean(type.getComponentType(), this));
		} else {
			populateProperties(type.getComponentType());
		}
	}
}
