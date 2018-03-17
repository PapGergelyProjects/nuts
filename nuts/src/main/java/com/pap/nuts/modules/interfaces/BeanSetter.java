package com.pap.nuts.modules.interfaces;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface BeanSetter<T> {
	public T apply(Map<String, Object> resultSet);
}
