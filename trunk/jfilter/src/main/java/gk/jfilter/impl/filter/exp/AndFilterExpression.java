package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;

public class AndFilterExpression extends AbstractFilterExpression {

	AndFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean=bean;
	}

	public boolean eval(Object object) {
		if(object==null) {
			return false;
		}
		
		for (FilterExpression exp : expressions) {
			if (exp.eval(object) == false) {
				return false;
			}
		}
		return true;
	}

}
