package com.pap.nuts.modules.interfaces;

import java.security.acl.NotOwnerException;

public interface DaoLayerParts<T> {
	
	public void insert(T value);
	public void update(T value);
	public void execute(T value);
	public T select();
	public T getAll();
	
	public default void customInsert(String value){
		throw new UnsupportedOperationException("You must implement this method, otherwise can't use it.");
	}
}
