package gk.jfilter.impl.filter.bean;

import gk.jfilter.JFilterException;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;

public class SimpleBean extends AbstractBean {

	public SimpleBean(Class<?> type, Method readMethod) throws IntrospectionException {
		super(type, readMethod);
	}
	
	public SimpleBean(Class<?> type) throws IntrospectionException {
		super(type, null);
	}
	
	public Object getValue(Object object) {
		try {
			if(readMethod!=null) {
				return readMethod.invoke(object);
			}
		} catch (Exception e) {
			throw new JFilterException("Error reading object value.", e);
		}
		
		return object;
	}
}
