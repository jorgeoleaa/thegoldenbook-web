package com.pinguela.thegoldenbook.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {

	public static final Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie c: cookies) {
				if(c.getName().equalsIgnoreCase(name)) {
					return c;
				}
			}
		}
		return null;
	}

	public static final String getValue(HttpServletRequest request, String name) {
		Cookie c = getCookie(request, name);
		return c!=null?c.getValue():null;
	}

	public static final void setCookie(HttpServletResponse response, String path, String name, String value, int ttl) {
		Cookie c = new Cookie(name, value);
		c.setMaxAge(ttl);
		c.setPath(path);
		response.addCookie(c);
	}

	public static void removeCookie(HttpServletResponse response, String path, String name) {
		setCookie(response, path, name, null, 0);
	}

}
