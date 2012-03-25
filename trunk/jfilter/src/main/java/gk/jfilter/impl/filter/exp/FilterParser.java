package gk.jfilter.impl.filter.exp;

import gk.jfilter.JFilterException;
import gk.jfilter.impl.filter.bean.ArrayBean;
import gk.jfilter.impl.filter.bean.Bean;
import gk.jfilter.impl.filter.bean.ClassBean;
import gk.jfilter.impl.filter.bean.CollectionBean;
import gk.jfilter.impl.filter.bean.MapBean;
import gk.jfilter.impl.filter.json.JacksonJsonImpl;
import gk.jfilter.impl.filter.json.Json;

import java.util.Collection;
import java.util.Map;

public class FilterParser {
	private static Json json = new JacksonJsonImpl();

	public static FilterExpression parse(String jsonFilter, Bean bean) {
		try {
			Map<String, ?> filterMap = json.toMap(jsonFilter.getBytes());
			String filterFirstKey = (String) filterMap.keySet().toArray()[0];

			FilterExpression exp = null;
			if (filterFirstKey.equals(Operator.$or.toString())) {
				exp = new OrFilterExpression(filterFirstKey, bean);
			} else {
				exp = new AndFilterExpression(filterFirstKey, bean);
			}

			parseMap(filterMap, exp, bean);
			return exp;
		} catch (Throwable e) {
			throw new JFilterException("Filter parsing error.", e);
		}
	}

	static FilterExpression parseMap(Map<String, ?> filterMap, FilterExpression exp, Bean bean) {

		try {
			for (Map.Entry<String, ?> filterMapEntry : filterMap.entrySet()) {
				exp.addExpression(parseKey(filterMapEntry, bean));
			}
		} catch (Throwable e) {
			throw new JFilterException("Filter parsing error.", e);
		}

		return exp;
	}

	/**
	 * Replaces leaf key values FilterKeyValue object.
	 * 
	 * @param filterMapEntry
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static FilterExpression parseKey(final Map.Entry<String, ?> filterMapEntry, Bean bean) {

		String filterKey = filterMapEntry.getKey();
		Bean filterKeyBean = bean.getProperty(filterKey);
		Object filterValue = filterMapEntry.getValue();

		/** e.g. {a:"v"} */
		if (filterValue instanceof String) {
			FilterExpression exp;
			if (filterKeyBean instanceof CollectionBean) {
				exp = new CollectionFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(filterValue), Operator.$eq, filterKeyBean));
			} else if (filterKeyBean instanceof ArrayBean) {
				exp = new ArrayFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(filterValue), Operator.$eq, filterKeyBean));
			} else {
				exp = new SimpleFilterExpression(filterKey, toArrays(filterValue), Operator.$eq, bean);
			}
			return exp;
		}

		/** e.g {a:{$gt:"10"}} , {a:{$in:[1,2,3]}} */
		if ((filterValue instanceof Map) && (containsOperator((Map<String, ?>) filterValue))) {

			Map<String, ?> valueMap = (Map<String, ?>) filterValue;
			String s = (String) valueMap.keySet().toArray()[0];
			Operator operator = Operator.operatorOf(s);

			FilterExpression exp;
			if (filterKeyBean instanceof CollectionBean) {
				exp = new CollectionFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(valueMap.get(s)), operator, filterKeyBean));
			} else if (filterKeyBean instanceof ArrayBean) {
				exp = new ArrayFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(valueMap.get(s)), operator, filterKeyBean));
			} else {
				exp = new SimpleFilterExpression(filterKey, toArrays(valueMap.get(s)), operator, bean);
			}
			
			return exp;
		}

		/** e.g. {$and:[{a:"v1"}, {b:"v2}]} */
		if (filterValue instanceof Collection) {
			if (!Operator.isJoin(filterKey)) {
				throw new JFilterException(" $and or $or expected. with collection of expressions: "
						+ filterValue);
			}

			FilterExpression exp;
			Operator operator = Operator.operatorOf(filterKey);
			if (operator == Operator.$and) {
				exp = new AndFilterExpression(filterKey, bean);
			} else if (operator == Operator.$or) {
				exp = new OrFilterExpression(filterKey, bean);
			} else {
				throw new JFilterException(" Join operator not supported: " + operator);
			}

			for (Object filterMap : (Collection<?>) filterValue) {
				parseMap((Map<String, Object>) filterMap, exp, bean);
			}
			return exp;
		}

		/** e.g. {a:{b:"v1", c:"v2"}} */
		if (filterValue instanceof Map) {
			FilterExpression exp;
			if (filterKeyBean instanceof CollectionBean) {
				/** for properties which returns Collection objects */
				exp = new CollectionFilterExpression(filterKey, bean);
			} else if (filterKeyBean instanceof ArrayBean) {
				/** for properties which returns Array objects */
				exp = new ArrayFilterExpression(filterKey, bean);
			} else if (filterKeyBean instanceof MapBean) {
				/** for properties which returns Map objects */
				exp = new MapFilterExpression(filterKey, bean);
			} else if (filterKeyBean instanceof ClassBean) {
				exp = new ClassFilterExpression(filterKey, bean);
			} else {
				throw new JFilterException("Unknow filter parsing error.");
			}

			parseMap((Map<String, Object>) filterValue, exp, filterKeyBean);
			return exp;
		}

		throw new JFilterException("Unknow filter parsing error.");
	}

	private static boolean containsOperator(final Map<String, ?> filterMap) {
		for (String key : filterMap.keySet()) {
			if (Operator.isComparator(key)) {
				return true;
			}
		}
		return false;
	}
	
	private static Object[]  toArrays(Object object) {
		if(object instanceof Collection) {
			return ((Collection<?>) object).toArray();
		}
		
		return new Object[] { object };
	}
}
