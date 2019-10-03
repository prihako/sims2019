package com.balicamp.service;

import java.util.List;

import com.balicamp.model.mx.ParameterMx;

public interface ParameterMxManager extends IManager {

	public List<ParameterMx> findAll(String description, int first, int max);

	public int getRowCount(String description);

	public void updateValues(List<ParameterMx> entites);
}
