package gk.jfilter.impl.filter.converter;

import org.codehaus.jackson.map.ObjectMapper;

public class JacksonConverterImpl implements Converter {
	final private static ObjectMapper mapper = new ObjectMapper();

	public <T> T convert(Object object, Class<T> targetType) {
		return mapper.convertValue(object, targetType);
	}

}
