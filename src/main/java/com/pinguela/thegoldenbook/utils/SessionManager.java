package com.pinguela.thegoldenbook.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {
	
	public static Object getAttribute(HttpServletRequest request, String name) {
		return getSession(request).getAttribute(name);
	}
	
	public static void setAttribute(HttpServletRequest request, String name, Object value){
		getSession(request).setAttribute(name, value);
	}
	
	public static void removeAttribute (HttpServletRequest request, String nombre) {
		getSession(request).removeAttribute(nombre);
	}
	
	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession(); 
	}
}
