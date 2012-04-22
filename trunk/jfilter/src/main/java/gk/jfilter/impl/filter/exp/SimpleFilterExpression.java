package gk.jfilter.impl.filter.exp;

import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.comparator.SimpleTypeComparator;

import java.util.Arrays;

public class SimpleFilterExpression extends AbstractFilterExpression {

	final private SimpleTypeComparator comparator = new SimpleTypeComparator();
	private Operator operator;
	/** for $in and $nin array of values otherwise single value. */
	private Comparable<?>[] filterValues;

	SimpleFilterExpression(String filterKey, Object[] filterValues, Operator operator, Bean bean) {
		this.filterKey = filterKey;
		this.operator = operator;
		this.bean = bean;
		setFilterValues(filterValues);
	}

	private void setFilterValues(Object[] filterValues) {
		this.filterValues = new Comparable<?>[filterValues.length];
		int i = 0;
		for (Object object : filterValues) {
			this.filterValues[i] = (Comparable<?>) object;
			++i;
		}
	}

	public boolean eval(Object object) {
		return comparator.compare((Comparable) bean.getValue(object), this.filterValues, this.operator);
	}

	public String toString() {
		return "SimpleFilterExpression {operator=" + operator + ", filterValue=" + Arrays.toString(filterValues) + ", expressions=" + expressions
				+ "}";
	}
}
