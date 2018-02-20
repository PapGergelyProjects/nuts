package com.pap.nuts.modules.interfaces;

import java.util.List;

public interface DaoFunctions<T> {
	public T getOne(Long id);
	public List<T> getAll();
	public void update(T bean);
	public void insert(T bean);
	public void delete(T bean);
}
