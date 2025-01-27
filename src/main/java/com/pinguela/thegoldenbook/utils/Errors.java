package com.pinguela.thegoldenbook.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper para los errores de validación o de invocación a negocio.
 */

public class Errors {
	
	private List<String> globalErrors = null;
	private Map<String, List<String>> fieldErrors = null;
	
	public Errors() {
		globalErrors = new ArrayList<String>();
		fieldErrors = new HashMap<String, List<String>>();
	}
	
	public void addGlobal(String error) {
		globalErrors.add(error);
	}
	
	public List<String> getGlobalErrors(){
		return globalErrors;
	}
	
	public void addFieldErrors(String fieldName, String errorCode) {
		List<String> errorsList = fieldErrors.get(fieldName);
		if(errorsList == null) {
			errorsList = new ArrayList<String>();
			fieldErrors.put(fieldName, errorsList);
		}
		errorsList.add(errorCode);
	}
	
	public List<String> getFieldErrors(String fieldName){
		return fieldErrors.get(fieldName);
	}
	
	public boolean hasErrors() {
		return !globalErrors.isEmpty() || !fieldErrors.isEmpty();
	}
	
}
