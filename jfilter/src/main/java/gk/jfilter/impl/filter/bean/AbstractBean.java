package gk.jfilter.impl.filter.bean;

import gk.jfilter.JFilterException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBean implements Bean {
	Method readMethod;
	Class<?> type;
	protected Map<String, Bean> properties = new HashMap<String, Bean>();
	
	protected AbstractBean(Class<?> type, Method readMethod) {
		this.type=type;
		this.readMethod=readMethod;
	}
	
	protected void populateProperties(Class<?> type) throws IntrospectionException {
		PropertyDescriptor pds[] = Introspector.getBeanInfo(type).getPropertyDescriptors();
		
		for (PropertyDescriptor pd : pds) {
			if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
				String name = pd.getName();
				Method readMethod = pd.getReadMethod();
				addProperty(name, readMethod.getReturnType(), readMethod);
			}
		}
	}
		
	protected void addProperty(String name, Class<?> propertyType, Method readMethod) throws IntrospectionException {
		if (propertyType.isPrimitive() || Comparable.class.isAssignableFrom(propertyType)) {
			properties.put(name, new SimpleBean(propertyType,readMethod));
		} else if (propertyType.isArray())  {
			properties.put(name, new ArrayBean(propertyType,readMethod));
		} else if (Collection.class.isAssignableFrom(propertyType))  {
			properties.put(name, new CollectionBean(propertyType,readMethod));
		}  else if (Map.class.isAssignableFrom(propertyType)) {
			properties.put(name, new MapBean(propertyType,readMethod));
		} else {
			properties.put(name, new ClassBean(propertyType, readMethod));
		}
	}
	
	public Object getValue(Object object) {
		try {
			return readMethod.invoke(object);
		} catch (Exception e) {
			throw new JFilterException("Error reading object value.", e);
		}
	}
	
	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public Bean getProperty(String propertyName) {
		Bean bean = properties.get(propertyName);
		if(bean==null) {
			throw new JFilterException("Property: "+propertyName+" does not exist in the class: "+type);
		}
		return properties.get(propertyName);
	}
}
