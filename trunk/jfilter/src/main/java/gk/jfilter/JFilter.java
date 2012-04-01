package gk.jfilter;

import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.bean.QueryBean;
import gk.jfilter.impl.filter.exp.FilterExpression;
import gk.jfilter.impl.filter.exp.FilterParser;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Main class to execute filter/query on different type of collection classes
 * (java.util.Collection, java.util.Map or arrays). New instance of this class
 * to be created for each collection. Then this instance can be used to execute
 * a single json filter or multiple json filters with different parameters.
 * Following are different ways to execute filter.
 * 
 * Collection<Pet> pets = new ArrayList<Pet>(); JFilter jfilter = new
 * JFilter(pets, Pet.class); Collection<Pet> cats =
 * filter.execute("{'type':'CAT'}");
 * 
 * Collection<Pet> pets = new ArrayList<Pet>(); JFilter jfilter = new
 * JFilter(pets, Pet.class); Map<String, ?> parameters = new
 * HashMap<String,String>();
 * 
 * parameters.put("type", "CAT"); Collection<Pet> cats =
 * jfilter.execute("{'type':'?type'}", args);
 * 
 * parameters.put("type", "DOG"); Collection<Pet> dogs =
 * jfilter.execute("{'type':'?type'}", args);
 * 
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 * 
 */
public class JFilter<T> {
	private Bean bean;
	private FilterExpression filterExp;
	private FilterParser filterParser;

	/** Supported collection types */
	private Iterator<T> itr;

	/**
	 * 
	 * @param collection
	 *            collection to be filtered.
	 * @param klass
	 *            collection parameterized class object.
	 */
	public JFilter(Collection<T> collection, Class<T> klass) {
		init(klass);
		itr = collection.iterator();
	}

	/**
	 * 
	 * @param array
	 *            array to be filtered.
	 * @param klass
	 *            array type.
	 */
	public JFilter(T[] array, Class<T> klass) {
		init(klass);
		itr = Arrays.asList(array).iterator();
	}

	/**
	 * 
	 * @param map
	 *            values of Map are filtered.
	 * @param klass
	 *            array type.
	 */
	public JFilter(Map<?, T> map, Class<T> klass) {
		init(klass);
		map.values().iterator();
	}

	/**
	 * 
	 * @param itr
	 *            Iterator of collection to be filtered.
	 * @param klass
	 *            array type.
	 */
	public JFilter(Iterator<T> itr, Class<T> klass) {
		init(klass);
		this.itr=itr;
	}
	
	private void init(Class<T> klass) {
		try {
			this.bean = new QueryBean(klass);
			this.filterParser = new FilterParser(bean);
		} catch (IntrospectionException e) {
			throw new JFilterException("Unable to introspect the bean class.");
		}
	}

	/**
	 * Executes non parameterized filter.
	 * 
	 * @param jsonFilter
	 *            filter in json format.
	 * @return filtered collection.
	 */
	public Collection<T> execute(String jsonFilter) {
		this.filterExp = filterParser.parse(jsonFilter);
		return execute();
	}

	/**
	 * Executes parameterized filter.
	 * 
	 * @param filter
	 *            filter in json format.
	 * @param parameters
	 *            filter parameters are given in key value form where key is
	 *            string given in filter in "?string" format and value is object
	 *            of matching property class of collection bean. $in and $nin
	 *            values are given as List.
	 * @return filtered collection.
	 */
	public Collection<T> execute(String filter, Map<String, ?> parameters) {
		this.filterExp = filterParser.parse(filter, parameters);
		return execute();
	}

	private Collection<T> execute() {
		List<T> list = new ArrayList<T>();

		while (itr.hasNext()) {
			T o = itr.next();
			if (filterExp.eval(o)) {
				list.add(o);
			}
		}
		return list;
	}
}
