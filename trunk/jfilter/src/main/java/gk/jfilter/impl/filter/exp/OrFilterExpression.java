package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;

public class OrFilterExpression extends AbstractFilterExpression {

	OrFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean = bean;
	}

	public boolean eval(Object object) {
		for (FilterExpression exp : this.expressions) {
			if (exp.eval(exp.getBeanPropertyValue(object)) == true) {
				return true;
			}
		}
		return false;
	}

	public Object getBeanPropertyValue(Object object) {
		return object;
	}

}
