package gk.jfilter;

import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.bean.QueryBean;
import gk.jfilter.impl.filter.expression.FilterExpression;
import gk.jfilter.impl.filter.parser.FilterParser;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Main class to filter/query collections (java.util.Collection, java.util.Map or arrays).
 * New instance of this class to be created for each collection. Then this instance can be used to execute
 * queries multiple times on the collection.
 * Following are different ways to execute filter.
 * <p><blockquote><pre>
 * Collection<Pet> pets = new ArrayList<Pet>();
 * JFilter jfilter = new JFilter(pets, Pet.class);
 * Collection<Pet> cats = filter.execute("{'type':'?1'}", "CAT");
 * 
 * Collection<Pet> pets = new ArrayList<Pet>();
 * JFilter jfilter = new JFilter(pets, Pet.class);
 * Map<String, ?> parameters = new HashMap<String,String>();
 * 
 * parameters.put("type", "CAT"); 
 * Collection<Pet> cats = jfilter.execute("{'type':'?type'}", parameters);
 * 
 * parameters.put("type", "DOG"); 
 * Collection<Pet> dogs = jfilter.execute("{'type':'?type'}", parameters);
 * 
 * </pre><blockquote></p>
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 * 
 */
public class JFilter<T> {
	private Bean bean;
	private FilterExpression filterExp;
	private FilterParser filterParser;

	/** Supported collection types */
	private Collection<T> collection;

	/**
	 * 
	 * @param collection
	 *            collection to be filtered.
	 * @param klass
	 *            collection parameterized class object.
	 */
	public JFilter(Collection<T> collection, Class<T> klass) {
		init(klass);
		this.collection=collection;
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
		this.collection=Arrays.asList(array);
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
	 * Executes filter with array of parameter values. Parameters are given as
	 * "?1", "?2" etc in the filter, starting from "?1" to "?n" where n is
	 * integer. Parameter values are are picked from corresponding argument position in
	 * the variable arguments.
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
	public List<T> execute(String filter, Object... parameters) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer i = 1;
		for (Object parameter : parameters) {
			map.put(i.toString(), parameter);
			++i;
		}
		return execute(filter, map);
	}
	
	/**
	 * Executes filter with map of parameter values. filter parameter values are
	 * given in key value form where key is string given in filter in "?string"
	 * format and value is a object of same class as of bean property. $in and
	 * $nin values are given as List of objects.
	 * 
	 * @param filter
	 *            filter in json format.
	 * @param parameters
	 *            Map of parameter values.
	 * @return filtered collection.
	 */
	public List<T> execute(String filter, Map<String, ?> parameters) {
		this.filterExp = filterParser.parse(filter, parameters);
		return execute();
	}

	private List<T> execute() {
		List<T> list = new ArrayList<T>();
		Iterator<T> itr = collection.iterator();
		
		while (itr.hasNext()) {
			T o = itr.next();
			if (filterExp.eval(o)) {
				list.add(o);
			}
		}
		return list;
	}
}
