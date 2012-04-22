package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;

public class OrFilterExpression extends AbstractFilterExpression {

	OrFilterExpression(String filterKey, Bean bean) {
		this.filterKey = filterKey;
		this.bean = bean;
	}

	public boolean eval(Object object) {
		if(object==null) {
			return false;
		}
		
		for (FilterExpression exp : this.expressions) {
			if (exp.eval(object) == true) {
				return true;
			}
		}
		return false;
	}

}
