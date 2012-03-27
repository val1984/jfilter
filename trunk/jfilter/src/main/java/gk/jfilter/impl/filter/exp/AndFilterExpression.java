package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;

public class AndFilterExpression extends AbstractFilterExpression {

	AndFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
	}

	public boolean eval(Object object) {
		for (FilterExpression exp : expressions) {
			if (exp.eval(exp.getBeanPropertyValue(object)) == false) {
				return false;
			}
		}
		return true;
	}

	public Object getBeanPropertyValue(Object object) {
		return object;
	}

}