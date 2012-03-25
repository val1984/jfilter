package gk.jfilter.impl.filter.converter;

public interface Converter {
	<T> T convert(Object object, Class<T> targetType);
}
