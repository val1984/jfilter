package gk.jfilter.impl.filter.exp;

import java.util.Map;

import gk.jfilter.impl.filter.bean.Bean;

public class MapFilterExpression extends AbstractFilterExpression {
	
	MapFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean=bean.getProperty(filterKey);
	}

	@Override
	public Object getBeanPropertyValue(Object object) {
		return bean.getValue(object);
	}

	/**
	 * If any of the collection element matches the filter it is true.
	 */
	@Override
	public boolean eval(Object object) {
		Map<?,?> values = (Map<?,?>) object;
		for (Object value : values.entrySet()) {
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

}
