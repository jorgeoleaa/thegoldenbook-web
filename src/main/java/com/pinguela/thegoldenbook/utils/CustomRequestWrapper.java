package com.pinguela.thegoldenbook.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet implementation class CustomRequestWrapper
 */
public class CustomRequestWrapper extends HttpServletRequestWrapper {
       
	private Map<String, Object> customAttributes = new HashMap<String, Object>();
	
    public CustomRequestWrapper(HttpServletRequest request) {
    	super(request);
    }

    @Override
    public Object getAttribute(String name) {
    	
    	if(customAttributes.containsKey(name)) {
    		return customAttributes.get(name);
    	}
    	
    	return super.getAttribute(name);
    }
    
    @Override
    public void setAttribute(String name, Object value) {
    	customAttributes.put(name, value);
    }

}
