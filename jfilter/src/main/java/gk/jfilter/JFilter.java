package gk.jfilter;

import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.bean.QueryBean;
import gk.jfilter.impl.filter.expression.FilterExpression;
import gk.jfilter.impl.filter.parser.FilterParser;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Main class to filter/query collections (java.util.Collection, java.util.Map
 * or arrays). New instance of this class to be created for each collection.
 * Then this instance can be used to execute queries multiple times on the
 * collection. Following are different ways to execute filter.
 * <p>
 * <blockquote>
 * 
 * <pre>
 * Collection&lt;Pet&gt; pets = new ArrayList&lt;Pet&gt;();
 * JFilter jfilter = new JFilter(pets, Pet.class);
 * Collection&lt;Pet&gt; cats = filter.execute(&quot;{'type':'?1'}&quot;, &quot;CAT&quot;);
 * 
 * Collection&lt;Pet&gt; pets = new ArrayList&lt;Pet&gt;();
 * JFilter jfilter = new JFilter(pets, Pet.class);
 * Map&lt;String, ?&gt; parameters = new HashMap&lt;String, String&gt;();
 * 
 * parameters.put(&quot;type&quot;, &quot;CAT&quot;);
 * Collection&lt;Pet&gt; cats = jfilter.execute(&quot;{'type':'?type'}&quot;, parameters);
 * 
 * parameters.put(&quot;type&quot;, &quot;DOG&quot;);
 * Collection&lt;Pet&gt; dogs = jfilter.execute(&quot;{'type':'?type'}&quot;, parameters);
 * 
 * </pre>
 * 
 * <blockquote>
 * </p>
 * 
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 * 
 */
public class JFilter<T> implements Iterable<T> {
	private Bean bean;
	private FilterParser filterParser;

	/** Supported collection types */
	private Iterable<T> iterable;
	private List<T> result;

	/**
	 * 
	 * @param collection
	 *            collection to be filtered.
	 * @param klass
	 *            collection parameterized class object.
	 */
	public JFilter(Iterable<T> iterable, Class<T> klass) {
		init(klass);
		this.iterable = iterable;
	}

	/**
	 * For making clone.
	 * 
	 * @param iterable
	 * @param bean
	 */
	private JFilter(Iterable<T> iterable, Bean bean) {
		this.iterable = iterable;
		this.bean = bean;
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
	 * integer. Parameter values are are picked from corresponding argument
	 * position in the variable arguments.
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
		return execute(filter, getParameterMap(parameters));
	}

	/**
	 * Executes filter with map of parameter values. filter parameter values are
	 * given in key value form where key is string given in filter in "?string"
	 * format and value is a object of same class as of bean property. $in and
	 * $nin values are given as List of objects.
	 * 
	 * @param filter
	 *            Json filter string e.g. "{'id':'?id'}"
	 * @param parameters
	 *            Map of parameter values, e.g. key="id", value=object.
	 * @return filtered collection.
	 */
	public List<T> execute(String filter, Map<String, ?> parameters) {
		FilterExpression filterExp = filterParser.parse(filter, parameters);
		return execute(iterable.iterator(), filterExp);
	}

	/**
	 * Returns first element of the filtered values.
	 * 
	 * @return First element of the filtered values.
	 */
	public T getFirst() {
		checkState();
		if (result.get(0) != null) {
			return null;
		} else {
			return result.get(0);
		}
	}

	private List<T> execute(Iterator<T> itr, FilterExpression filterExp) {
		result = new ArrayList<T>();

		while (itr.hasNext()) {
			T o = itr.next();
			if (filterExp.eval(o)) {
				result.add(o);
			}
		}
		return result;
	}

	/**
	 * Executes filter with map of parameter values. filter parameter values are
	 * given in key value form where key is string given in filter in "?string"
	 * format and value is a object of same class as of bean property. $in and
	 * $nin values are given as List of objects.
	 * 
	 * @param filter
	 *            Json filter string e.g. "{'id':'?id'}"
	 * @param parameters
	 *            Filter parameters values are given in key value form where key
	 *            is string given in filter in "?string" format and value is
	 *            object of bean property class. $in and $nin values are given
	 *            as List of objects.
	 * 
	 * @return filtered collection.
	 */
	public JFilter<T> filter(String filter, Map<String, ?> parameters) {
		FilterExpression filterExp = filterParser.parse(filter, parameters);
		execute(iterable.iterator(), filterExp);
		return new JFilter<T>(result, bean);
	}

	/**
	 * Executes filter with array of parameter values. Parameters are given as
	 * "?1", "?2" etc in the filter, starting from "?1" to "?n" where n is
	 * integer. Parameter values are are picked from corresponding argument
	 * position in the variable arguments.
	 * 
	 * @param filter
	 *            Json filter string e.g. "{'id':'?1'}"
	 * @param parameters
	 *            Filter parameters value objects are given in the same order as
	 *            given in filter in the form of "?1", "?2" ..."?n". The object
	 *            should be of bean property class. $in and $nin values are
	 *            given as List of objects.
	 * 
	 * @return filtered values.
	 */
	public JFilter<T> filter(String filter, Object... parameters) {
		filter(filter, getParameterMap(parameters));
		return this;
	}

	@Override
	public Iterator<T> iterator() {
		checkState();
		return result.iterator();
	}

	public <U extends Collection<T>> U into(U collection) {
		collection.addAll(result);
		return collection;
	}

	private void checkState() {
		if (result == null) {
			throw new IllegalStateException("First call filter method.");
		}
	}

	private Map<String, Object> getParameterMap(Object... parameters) {
		Map<String, Object> map = new HashMap<String, Object>();

		Integer i = 1;
		for (Object parameter : parameters) {
			map.put(i.toString(), parameter);
			++i;
		}
		return map;
	}

	public JFilter<T> map(String select) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	public Number max() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	public Number min() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	public Number sum() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	public Number avg() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	/**
	 * e.g. {'id'}, {'id':'desc'}, {'id', 'skus.price'}
	 * @param property
	 * @return
	 */
	public JFilter<T> sortBy(String property) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
	public Collection out(Collection c) {
		c.addAll(result);
		return c;
	}
	
	public void out(Map<?,?> c) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}
	
}
