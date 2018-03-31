package com.pap.nuts.modules.interfaces;

import java.util.List;

public interface DaoService<T>{
	
	public void insert(T value);
	public void update(T value);
	public void delete(T value);
	public void execute(T value);
	public T select(long value);
	public List<T> getAll();
	
	public default void customInsert(String value){
		throw new UnsupportedOperationException("You must implement this method, otherwise can't use it.");
	}
	
}
