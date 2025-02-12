package com.pinguela.thegoldenbook.filter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.thegoldenbook.config.ConfigurationParametersManager;
import com.pinguela.thegoldenbook.utils.CookieManager;
import com.pinguela.thegoldenbook.utils.SessionManager;


public class LanguageFilter extends HttpFilter implements Filter {
	
	private static String[] SUPPORTED_LOCALES = ConfigurationParametersManager.getParameterValue("locale.support").split(",");
	private static String LOCALE_DEFAULT = ConfigurationParametersManager.getParameterValue("locale.default");
	
	private static Logger logger = LogManager.getLogger(LanguageFilter.class);
	
    public LanguageFilter() {
        super();
    }
    
	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    String uri = httpRequest.getRequestURI();
    if (uri.endsWith("version.html")) {
        chain.doFilter(request, response);
        return; 
    }

    
    String locale = CookieManager.getValue(httpRequest, "locale");
    
    logger.info("Locale from the cookie: "+locale);
    
    if (locale != null) {
    	
    	boolean flag = false;
    	
    	for(String checkedLocale : SUPPORTED_LOCALES) {
    		if(locale.equalsIgnoreCase(checkedLocale)) {
    			flag = true;
    		}
    	}
    	
    	if(!flag) {
    		locale = LOCALE_DEFAULT;
    	}
    	
    	SessionManager.setAttribute(httpRequest, "locale", Locale.forLanguageTag(locale));
    	
    } else {
        String idiomas = httpRequest.getHeader("Accept-Language");
        List<String> browserIdiomas = com.pinguela.thegoldenbook.utils.LocaleUtils.getSupportedLocales(idiomas);
        
        logger.info("Languages from the browser: "+browserIdiomas);
        
        boolean localeFound = false;
        if (SUPPORTED_LOCALES != null && SUPPORTED_LOCALES.length > 0) {
            for (String browserIdioma : browserIdiomas) {
                for (String supportedLocale : SUPPORTED_LOCALES) {
                    if (supportedLocale.equalsIgnoreCase(browserIdioma)) {
                        localeFound = true;
                        break;
                    }
                }

                if (localeFound) {
                    SessionManager.setAttribute(httpRequest, "locale", new Locale(browserIdioma));
                    break;
                }
            }
        }

        if (!localeFound) {
            SessionManager.setAttribute(httpRequest, "locale", new Locale(LOCALE_DEFAULT));
            logger.info("Default locale: "+new Locale(LOCALE_DEFAULT));
        }
    }

    chain.doFilter(request, response);
}

	
	public void destroy() {
	}


}
