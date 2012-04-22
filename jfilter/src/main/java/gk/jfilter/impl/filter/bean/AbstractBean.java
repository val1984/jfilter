package gk.jfilter.impl.filter.bean;

import gk.jfilter.JFilterException;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBean implements Bean {
	protected Method readMethod;
	protected Class<?> type;
	protected Map<String, Bean> properties = new HashMap<String, Bean>();
	protected Bean parent;

	protected AbstractBean(Class<?> type, Method readMethod, Bean parent) {
		this.type = type;
		this.readMethod = readMethod;
		this.parent = parent;
	}

	protected void populateProperties(Class<?> type) throws IntrospectionException {
		// Method[] methods = type.getDeclaredMethods();
		Method[] methods = type.getMethods();
		for (Method m : methods) {
			if (isSupported(m)) {
				m.setAccessible(true);
				addProperty(m.getName(), m.getReturnType(), m);
			}
		}
	}

	protected void addProperty(String name, Class<?> propertyType, Method readMethod) throws IntrospectionException {
		if (propertyType.isPrimitive() || Comparable.class.isAssignableFrom(propertyType)) {
			properties.put(name, new SimpleBean(propertyType, readMethod, this));
		} else if (propertyType.isArray()) {
			properties.put(name, new ArrayBean(propertyType, readMethod, this));
		} else if (Collection.class.isAssignableFrom(propertyType)) {
			properties.put(name, new CollectionBean(propertyType, readMethod, this));
		} else if (Map.class.isAssignableFrom(propertyType)) {
			properties.put(name, new MapBean(propertyType, readMethod, this));
		} else {
			properties.put(name, new ClassBean(propertyType, readMethod, this));
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
		// first try with getter method
		Bean bean = properties.get(makeGetter(propertyName));
		if (bean == null) {
			// again try direct method
			bean = properties.get(propertyName);
			if (bean == null) {
				throw new JFilterException("Property: " + propertyName + " does not exist in the class: " + type);
			}
		}
		return bean;
	}

	protected String makeGetter(String property) {
		char[] pa = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		sb.append("get");
		pa[0] = Character.toUpperCase(pa[0]);
		sb.append(pa);
		return sb.toString();
	}

	/**
	 * only method supported by JFilter.
	 * 
	 * @return
	 */
	protected boolean isSupported(Method m) {
		if ((m.getReturnType() != Void.TYPE) && (m.getParameterTypes().length == 0)) {
			if (parent != null && ( Class.class.isAssignableFrom(parent.getType()) || parent.getType().isAssignableFrom(type))) {
				return false;
			}
			
			if(parent!=null)
				System.out.println("method:"+m.getName()+"  type:"+type+"  parent type:"+parent.getType());
			else 
				System.out.println("method:"+m.getName()+"  type:"+type+"  parent type: null");
			
			// if any of the parameterized type matching parent bean type return
			// false
			// to avoid recursion else true.
			Type t = m.getGenericReturnType();
			if (t instanceof ParameterizedType) {
				ParameterizedType ptype = (ParameterizedType) t;
				for (Type atype : ptype.getActualTypeArguments()) {
					if (atype instanceof Class && parent != null && parent.getType().isAssignableFrom((Class<?>) atype) == true) {
						return false;
					}
				}

			}
			return true;

		}

		return false;
	}
}
