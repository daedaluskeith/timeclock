package com.karlkeith.paychex.timeclock.services;

import java.util.Set;

public interface CrudService<T, ID> {

	public void delete(T object);

	public void deleteById(ID id);

	public Set<T> findAll();

	public T findById(ID id);

	public T save(T object);
}
