package gk.jfilter.impl.filter.exp;

/**
 * 
 * 
 * @author Kamran Ali Khan (khankamranali@gmail.com)
 * 
 * @param <T>
 */
public interface FilterExpression {

	String getFilterKey();

	Object getBeanPropertyValue(Object object);

	void addExpression(FilterExpression expression);

	boolean eval(Object object);
}
