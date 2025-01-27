package com.pinguela.thegoldenbook.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class URLUtils {
	
	private static final Logger logger = LogManager.getLogger(URLUtils.class);
	
	public static String buildBaseURL (HttpServletRequest request) throws UnsupportedEncodingException{
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(request.getRequestURI().substring(request.getContextPath().length()));
		Map <String, String[]> parametersMap = request.getParameterMap();
		Set<String> parameterNames = parametersMap.keySet();
		
		if(parameterNames.size()>0) urlBuilder.append("?");
		
		List<String> parametersList = new ArrayList<String>();
		for(String parameterName : parameterNames) {
			StringBuilder parameter = new StringBuilder();
			if(!Parameters.CALLBACK.equalsIgnoreCase(parameterName)) {
				parametersList.add(parameter.append(parameterName).append("=").append(request.getParameter(parameterName)).toString());
			}
		}
		
		return urlBuilder.append(String.join("&", parametersList)).toString();
	}

}
