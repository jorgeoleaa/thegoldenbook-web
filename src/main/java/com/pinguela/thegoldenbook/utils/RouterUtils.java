package com.pinguela.thegoldenbook.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RouterUtils {
	
	private static Logger logger = LogManager.getLogger(RouterUtils.class);
	
	public static final void route (
			HttpServletRequest request, HttpServletResponse response, boolean forwardOrRedirect, String targetView)
			throws IOException, ServletException{
		
		if(forwardOrRedirect) {
			logger.info("Forwarding to "+targetView+"...");
			request.getRequestDispatcher(targetView).forward(request, response);
		}else {
			logger.info("Redirecting to "+targetView+"...");
			response.sendRedirect(request.getContextPath()+targetView);
		}
	}
}
