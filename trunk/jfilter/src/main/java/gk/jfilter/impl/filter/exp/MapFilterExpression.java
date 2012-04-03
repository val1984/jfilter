package gk.jfilter.impl.filter.exp;

import java.util.Map;

import gk.jfilter.impl.filter.bean.Bean;

public class MapFilterExpression extends AbstractFilterExpression {
	
	MapFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean=bean;
	}

	/**
	 * If any of the collection element matches the filter it is true.
	 */
	@Override
	public boolean eval(Object object) {
		Map<?,?> values = (Map<?,?>) bean.getValue(object);
		for (Object value : values.entrySet()) {
			if (and(value) == true) {
				return true;
			}
		}
		return false;
	}

	private boolean and(Object object) {
		for (FilterExpression exp : expressions) {
			if (exp.eval(object) == false) {
				return false;
			}
		}
		return true;
	}

}
