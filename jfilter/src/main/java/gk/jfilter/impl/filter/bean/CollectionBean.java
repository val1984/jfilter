package gk.jfilter.impl.filter.bean;

import gk.jfilter.JFilterException;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CollectionBean extends AbstractBean {
	
	public CollectionBean(Class<?> type, Method readMethod) throws IntrospectionException {
		super(type, readMethod);
		
		Type t = readMethod.getGenericReturnType();
		if (t instanceof ParameterizedType) {
			Class<?> actualType = (Class<?>) ((ParameterizedType) t).getActualTypeArguments()[0];
			if (actualType.isPrimitive() || Comparable.class.isAssignableFrom(actualType)) {
				properties.put("get", new SimpleBean(actualType));
			} else {
				populateProperties(actualType);
			}
		} else {
			throw new JFilterException("Generic type information is required for Collection and Map types properties.");
		}
	}
}
