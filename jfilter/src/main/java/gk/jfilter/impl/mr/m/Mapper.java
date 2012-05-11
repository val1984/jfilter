package gk.jfilter.impl.mr.m;

import gk.jfilter.impl.filter.bean.Bean;

import java.util.Arrays;
import java.util.Collection;

public class Mapper {
	private Bean bean;
	private Mapper mapper;

	private Mapper(Bean bean, Mapper mapper) {
		this.bean = bean;
		this.mapper = mapper;
	}

	private Mapper(Bean bean) {
		this.bean = bean;
		this.mapper = null;
	}

	public <O, T> void map(O object, Collection<T> list) {
		if (object == null) {
			return;
		}

		Object po = bean.getValue(object);

		if (po instanceof Collection) {
			mapCollection((Collection<?>) po, list);
		} else if (po.getClass().isArray()) {
			mapArray((Object[]) po, list);
		} else {
			mapOne(po, list);
		}
	}
	
	private <O, T> void mapCollection(Collection<O> c, Collection<T> list) {
		for (O o : c) {
			mapOne(o, list);
		}
	}
	
	private <O, T> void mapArray(O[] os, Collection<T> list) {
		for (O o : os) {
			mapOne(o, list);
		}
	}
	
	private <O, T> void mapOne(O o, Collection<T> list) {
		if (mapper != null) {
			mapper.map(o, list);
		} else {
			list.add((T) o);
		}
	}

	public Class<?> getType() {
		if (mapper != null) {
			return mapper.getType();
		} else {
			return bean.getType();
		}
	}

	public static Mapper parse(Bean bean, String property) {
		String[] properties = property.split("\\.");
		return parseDots(bean, properties);
	}

	private static Mapper parseDots(Bean bean, String[] properties) {
		String nextProperty = properties[0];
		Bean nextBean = bean.getProperty(nextProperty);

		Mapper mapper;
		if (properties.length > 1) {
			// recursion
			mapper = new Mapper(nextBean, parseDots(nextBean, Arrays.copyOfRange(properties, 1, properties.length)));
		} else {
			mapper = new Mapper(nextBean);
		}
		return mapper;
	}

}
