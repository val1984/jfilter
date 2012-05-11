package gk.jfilter.impl.mr.r;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

public class Reducer {

	public static <T extends Comparable<T>> T min(Iterable<T> iterable) {
		Iterator<T> itr = iterable.iterator();
		T min = itr.next();

		while (itr.hasNext()) {
			T next = itr.next();
			if (next.compareTo(min) < 0)
				min = next;
		}
		return min;
	}

	public static <T extends Comparable<T>> T max(Iterable<T> iterable) {
		Iterator<T> itr = iterable.iterator();
		T max = itr.next();

		while (itr.hasNext()) {
			T next = itr.next();
			if (next.compareTo(max) > 0)
				max = next;
		}
		return max;
	}

	public static <T extends Number> T sum(Iterable<T> iterable) {
		Number sum = 0;
		for (T n : iterable) {
			if (n instanceof Integer) {
				sum = (Integer) sum + (Integer) n;
			} else if (n instanceof Long) {
				sum = (Long) sum + (Long) n;
			} else if (n instanceof Long) {
				sum = (Long) sum + (Long) n;
			} else if (n instanceof Float) {
				sum = (Float) sum + (Float) n;
			} else if (n instanceof Double) {
				sum = (Double) sum + (Double) n;
			} else if (n instanceof BigInteger) {
				sum = ((BigInteger) sum).add((BigInteger) n);
			} else if (n instanceof BigDecimal) {
				sum = ((BigDecimal) sum).add((BigDecimal) n);
			}
		}

		return (T) sum;
	}
	
	public static <T> int count(Iterable<T> iterable) {
		int count=0;
		for(T t: iterable) {
			count++;
		}
		return count;
	}
}
