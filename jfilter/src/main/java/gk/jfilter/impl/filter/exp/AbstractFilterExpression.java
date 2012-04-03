package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * A Filter can have expressions and an expression can have filters.
 * 
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 * 
 * @param <T>
 */
public abstract class AbstractFilterExpression implements FilterExpression {

	protected String filterKey;
	protected Bean bean;
	protected final List<FilterExpression> expressions = new ArrayList<FilterExpression>(5);
	
	@Override
	public String getFilterKey() {
		return filterKey;
	}
	
	@Override
	public Bean getBean() {
		return bean;
	}

	public void addExpression(FilterExpression expression) {
		expressions.add(expression);
	}
}
