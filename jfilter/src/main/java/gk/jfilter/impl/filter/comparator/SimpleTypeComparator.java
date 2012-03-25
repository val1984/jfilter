package gk.jfilter.impl.filter.comparator;

import gk.jfilter.JFilterException;
import gk.jfilter.impl.filter.exp.Operator;

public class SimpleTypeComparator {
	/**
	 * Compares the object value with the filter value.
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean compare(Comparable objectValue, Comparable[] filterValues, Operator operator) {
		if (operator == Operator.$eq) {
			return (objectValue.compareTo(filterValues[0]) == 0);
		} else if (operator == Operator.$ne) {
			return (objectValue.compareTo(filterValues[0]) != 0);
		} else if (operator == Operator.$gt) {
			return (objectValue.compareTo(filterValues[0]) > 0);
		} else if (operator == Operator.$ge) {
			return (objectValue.compareTo(filterValues[0]) >= 0);
		} else if (operator == Operator.$lt) {
			return (objectValue.compareTo(filterValues[0]) < 0);
		} else if (operator == Operator.$le) {
			return (objectValue.compareTo(filterValues[0]) <= 0);
		} else if (operator == Operator.$in) {
			for (Comparable filterValue : filterValues) {
				if (objectValue.compareTo(filterValue) == 0) {
					return true;
				}
			}
			return false;
		} else if (operator == Operator.$nin) {
			for (Comparable filterValue : filterValues) {
				if (objectValue.compareTo(filterValue) == 0) {
					return false;
				}
			}
			return true;
		} else {
			throw new JFilterException("Operator : " + operator + " not supported.");
		}
	}
}
