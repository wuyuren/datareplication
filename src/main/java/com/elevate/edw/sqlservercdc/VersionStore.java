package com.elevate.edw.sqlservercdc;

public interface VersionStore<T> {
	public void storeVersion(T key, Long version) throws CDTException;
	public Long retrieveVersion(T key) throws CDTException;

}
