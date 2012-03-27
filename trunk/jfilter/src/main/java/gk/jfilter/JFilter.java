package gk.jfilter;

import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.bean.QueryBean;
import gk.jfilter.impl.filter.exp.FilterExpression;
import gk.jfilter.impl.filter.exp.FilterParser;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JFilter<T> {
	private Bean bean;
	private FilterExpression filterExp;

	public JFilter(String jsonFilter, Class<T> klass) {
		
		try {
			bean = new QueryBean(klass);
			filterExp = FilterParser.parse(jsonFilter, bean);
		} catch (IntrospectionException e) {
			throw new JFilterException("Enable to introspect the bean class.");
		}
	}

	public Collection<T> filter(Collection<T> collection) {
		List<T> list = new ArrayList<T>();
		for (T o : collection) {
			if (filterExp.eval(o)) {
				list.add(o);
			}
		}
		return list;
	}
}
