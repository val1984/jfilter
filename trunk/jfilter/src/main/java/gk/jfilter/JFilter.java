package gk.jfilter;

import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.bean.QueryBean;
import gk.jfilter.impl.filter.exp.FilterExpression;
import gk.jfilter.impl.filter.exp.FilterParser;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Main class to execute filter/query on different type of collection classes (java.util.Collection, java.util.Map or arrays). New instance of this class to be created for each collection. Then this instance can be used to execute a single json 
 * filter or multiple json filters with different parameters. Following are different ways to execute filter.
 * 
 * Collection<Pet> pets = new ArrayList<Pet>();
 * JFilter jfilter = new JFilter(pets, Pet.class);
 * Collection<Pet> cats = jfilter.execute("{\"type\":\"CAT\"}");
 * 
 * Collection<Pet> pets = new ArrayList<Pet>();
 * JFilter jfilter = new JFilter(pets, Pet.class);
 * Map<String, ?> args = new HashMap<String, String>();
 * args.put("type", "CAT");
 * Collection<Pet> cats = jfilter.execute("{\"type\":\"?type\"}", args);
 * 
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 *
 */
public class JFilter<T> {
	private Bean bean;
	private FilterExpression filterExp;
	private Collection<T> collection;

	/**
	 * 
	 * @param collection collection object to be filtered.
	 * @param klass collection parameterized class object.
	 */
	public JFilter(Collection<T> collection, Class<T> klass) {
		try {
			this.bean = new QueryBean(klass);
			this.collection=collection;
		} catch (IntrospectionException e) {
			throw new JFilterException("Unable to introspect the bean class.");
		}
	}

	/**
	 * Executes the filter.
	 * @param jsonFilter filter in json format.
	 * @return filtered collection.
	 */
	public Collection<T> execute(String jsonFilter) {
		this.filterExp = FilterParser.parse(jsonFilter, bean);
		
		List<T> list = new ArrayList<T>();
		for (T o : collection) {
			if (filterExp.eval(o)) {
				list.add(o);
			}
		}
		return list;
	}
}
