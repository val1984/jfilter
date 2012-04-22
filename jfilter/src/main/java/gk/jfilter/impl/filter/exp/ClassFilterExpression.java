package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;

public class ClassFilterExpression extends AbstractFilterExpression {

	ClassFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean = bean;
	}

	public boolean eval(Object object) {
		if(object==null) {
			return false;
		}
		
		Object o = bean.getValue(object);
		for (FilterExpression exp : expressions) {
			if (exp.eval(o) == false) {
				return false;
			}
		}
		return true;
	}

}
