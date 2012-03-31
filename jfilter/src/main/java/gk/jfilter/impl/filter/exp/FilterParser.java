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
	private Bean bean;

	public FilterParser(Bean bean) {
		this.bean = bean;
	}

	public FilterExpression parse(String jsonFilter) {
		return parse(jsonFilter, null);
	}

	public FilterExpression parse(String jsonFilter, Map<String, ?> args) {
		try {
			Map<String, ?> filterMap = json.toMap(jsonFilter.getBytes());
			String filterFirstKey = (String) filterMap.keySet().toArray()[0];

			FilterExpression exp = null;
			if (filterFirstKey.equals(Operator.$or.toString())) {
				exp = new OrFilterExpression(filterFirstKey, bean);
			} else {
				exp = new AndFilterExpression(filterFirstKey, bean);
			}

			parseMap(filterMap, args, exp, bean);
			return exp;
		} catch (Throwable e) {
			throw new JFilterException("Filter parsing error.", e);
		}
	}

	private FilterExpression parseMap(Map<String, ?> filterMap, Map<String, ?> args, FilterExpression exp, Bean bean) {

		try {
			for (Map.Entry<String, ?> filterMapEntry : filterMap.entrySet()) {
				exp.addExpression(parseKey(filterMapEntry, args, bean));
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
	private FilterExpression parseKey(final Map.Entry<String, ?> filterMapEntry, Map<String, ?> args, Bean bean) {

		String filterKey = filterMapEntry.getKey();

		Object filterValue = filterMapEntry.getValue();

		/** e.g. {a:"v"} */
		if (filterValue instanceof String) {
			Bean filterKeyBean = bean.getProperty(filterKey);
			FilterExpression exp;
			if (filterKeyBean instanceof CollectionBean) {
				exp = new CollectionFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(filterValue, args), Operator.$eq, filterKeyBean));
			} else if (filterKeyBean instanceof ArrayBean) {
				exp = new ArrayFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(filterValue, args), Operator.$eq, filterKeyBean));
			} else {
				exp = new SimpleFilterExpression(filterKey, toArrays(filterValue, args), Operator.$eq, bean);
			}
			return exp;
		}

		/** e.g {a:{$gt:"10"}} , {a:{$in:[1,2,3]}} */
		if ((filterValue instanceof Map) && (containsOperator((Map<String, ?>) filterValue))) {
			Bean filterKeyBean = bean.getProperty(filterKey);

			Map<String, ?> valueMap = (Map<String, ?>) filterValue;
			String s = (String) valueMap.keySet().toArray()[0];
			Operator operator = Operator.operatorOf(s);

			FilterExpression exp;
			if (filterKeyBean instanceof CollectionBean) {
				exp = new CollectionFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(valueMap.get(s), args), operator, filterKeyBean));
			} else if (filterKeyBean instanceof ArrayBean) {
				exp = new ArrayFilterExpression(filterKey, bean);
				exp.addExpression(new SimpleFilterExpression("get", toArrays(valueMap.get(s), args), operator, filterKeyBean));
			} else {
				exp = new SimpleFilterExpression(filterKey, toArrays(valueMap.get(s), args), operator, bean);
			}

			return exp;
		}

		/** e.g. {$and:[{a:"v1"}, {b:"v2}]} */
		if (filterValue instanceof Collection) {
			if (!Operator.isJoin(filterKey)) {
				throw new JFilterException(" $and or $or expected. with collection of expressions: " + filterValue);
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
				parseMap((Map<String, Object>) filterMap, args, exp, bean);
			}
			return exp;
		}

		/** e.g. {a:{b:"v1", c:"v2"}} */
		if (filterValue instanceof Map) {
			Bean filterKeyBean = bean.getProperty(filterKey);
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

			parseMap((Map<String, Object>) filterValue, args, exp, filterKeyBean);
			return exp;
		}

		throw new JFilterException("Unknow filter parsing error.");
	}

	private boolean containsOperator(final Map<String, ?> filterMap) {
		for (String key : filterMap.keySet()) {
			if (Operator.isComparator(key)) {
				return true;
			}
		}
		return false;
	}

	private Object[] toArrays(Object object, Map<String, ?> args) {
		if (object instanceof Collection) {
			return ((Collection<?>) object).toArray();
		} else {
			String s = (String) object;
			if (s.startsWith("?")) {
				Object o = args.get(s.substring(1));
				if (o == null) {
					throw new JFilterException("Filter argument [" + s + "] value not given the the argument Map [" + args + "]");
				}
				if (o instanceof Collection) {
					return ((Collection<?>) o).toArray();
				} else {
					return new Object[] { o };
				}
			} else {
				return new Object[] { s };
			}
		}

	}

}
