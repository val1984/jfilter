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
		} else if (operator == Operator.$sw) {
			if(objectValue instanceof String) {
				String fv = (String) filterValues[0];
				String ov = (String) objectValue;
				return ov.startsWith(fv);
			} else {
				throw new JFilterException("$sw operator is applicable for String data type. Parameter value is : "+filterValues[0]);
			}
		} else if (operator == Operator.$ew) {
			if(objectValue instanceof String) {
				String fv = (String) filterValues[0];
				String ov = (String) objectValue;
				return ov.endsWith(fv);
			} else {
				throw new JFilterException("$ew operator is applicable for String data type. Parameter value is : "+filterValues[0]);
			}
		} else if (operator == Operator.$cts) {
			if(objectValue instanceof String) {
				String fv = (String) filterValues[0];
				String ov = (String) objectValue;
				return ov.contains(fv);
			} else {
				throw new JFilterException("$cts operator is applicable for String data type. Parameter value is : "+filterValues[0]);
			}
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
