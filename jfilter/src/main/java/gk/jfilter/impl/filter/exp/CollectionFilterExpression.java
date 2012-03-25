package gk.jfilter.impl.filter.exp;

import java.util.Collection;

import gk.jfilter.impl.filter.bean.Bean;

public class CollectionFilterExpression extends AbstractFilterExpression {

	CollectionFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean = bean.getProperty(filterKey);
	}

	/**
	 * If any of the collection element matches the filter it is true.
	 */
	public boolean eval(Object object) {
		Collection<?> values = (Collection<?>) object;
		for (Object value : values) {
			if (and(value) == true) {
				return true;
			}
		}
		return false;
	}

	private boolean and(Object object) {
		for (FilterExpression exp : expressions) {
			if (exp.eval(exp.getBeanPropertyValue(object)) == false) {
				return false;
			}
		}
		return true;
	}

	public Object getBeanPropertyValue(Object object) {
		return bean.getValue(object);
	}

}
