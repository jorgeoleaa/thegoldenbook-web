package com.pinguela.thegoldenbook.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.config.ConfigurationParametersManager;
import com.pinguela.thegoldenbook.model.ClienteDTO;
import com.pinguela.thegoldenbook.service.ClienteService;
import com.pinguela.thegoldenbook.service.ServiceException;
import com.pinguela.thegoldenbook.service.impl.ClienteServiceImpl;
import com.pinguela.thegoldenbook.utils.Actions;
import com.pinguela.thegoldenbook.utils.AttributeNames;
import com.pinguela.thegoldenbook.utils.CookieManager;
import com.pinguela.thegoldenbook.utils.ErrorCodes;
import com.pinguela.thegoldenbook.utils.Errors;
import com.pinguela.thegoldenbook.utils.Parameters;
import com.pinguela.thegoldenbook.utils.RouterUtils;
import com.pinguela.thegoldenbook.utils.SessionManager;
import com.pinguela.thegoldenbook.utils.ValidationUtils;
import com.pinguela.thegoldenbook.utils.Views;


@WebServlet("/public/UsuarioServlet")
public class PublicUsuarioServlet extends HttpServlet {
	
	private static Logger logger = LogManager.getLogger(PublicUsuarioServlet.class);
		
	private ClienteService clienteService = null;
   
    public PublicUsuarioServlet() {
        super();
        clienteService = new ClienteServiceImpl();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter(Parameters.ACTION);
		String targetView = null;
		boolean forwardOrRedirect = false;
		
		Errors errors = new Errors();
		request.setAttribute(com.pinguela.thegoldenbook.utils.AttributeNames.ERRORS, errors);
		
		if(Actions.LOGIN.equalsIgnoreCase(action)) {
			String check = request.getParameter(Parameters.REMEMBER_ME);
			String mail = request.getParameter(Parameters.MAIL);
			String password = request.getParameter(Parameters.PASSWORD);
			
			boolean checkboxIsOn= "on".equalsIgnoreCase(check)? true : false;
			
			try {
				
				if(!errors.hasErrors()) {
					
					ClienteDTO cliente = clienteService.autenticar(mail, password);
					
					if(cliente != null) {
						SessionManager.setAttribute(request, com.pinguela.thegoldenbook.utils.AttributeNames.CLIENTE, cliente);
						
						if(checkboxIsOn == true) {
							CookieManager.setCookie(response, request.getContextPath(), "user", cliente.getEmail(), 30*60);
						}else {
							CookieManager.removeCookie(response, request.getContextPath(), "user");
						} 
						
						targetView = Views.INDEX;
						forwardOrRedirect = true;
					}else {
						logger.warn("Authentication failed: mail = {} ", mail);
						errors.addGlobal(ErrorCodes.AUTHENTICATION_FAILED);
						forwardOrRedirect = true;
						targetView = Views.LOGIN;
					}
				}else {
					forwardOrRedirect = false;
					targetView = Views.LOGIN;
				}
				forwardOrRedirect = true;
			}catch(PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
		}else if(Actions.CHANGE_LOCALE.equalsIgnoreCase(action)) {
			
			String[] newLocaleStr = request.getParameter(Parameters.LOCALE).split("_");
			Locale newLocale = new Locale(ConfigurationParametersManager.getParameterValue("locale.default"));
			
			if(newLocaleStr.length == 1) {
				newLocale = new Locale(newLocaleStr[0]);
			}else if(newLocaleStr.length==2) {
				newLocale = new Locale(newLocaleStr[0], newLocaleStr[1]);
			}
			SessionManager.setAttribute(request, AttributeNames.LOCALE, newLocale);
			
			CookieManager.setCookie(response, request.getContextPath(), AttributeNames.LOCALE, newLocale.getLanguage(), 60*60*1000);
			
			targetView = request.getParameter(Parameters.CALLBACK);
			
			forwardOrRedirect = false;
		
		}else if (Actions.REGISTER.equalsIgnoreCase(action)) {

		    String nombre = request.getParameter(Parameters.NOMBRE);
		    String apellido1 = request.getParameter(Parameters.APELLIDO1);
		    String apellido2 = request.getParameter(Parameters.APELLIDO2);
		    String nickname = request.getParameter(Parameters.NICKNAME);    
		    String email = request.getParameter(Parameters.MAIL);
		    String dniNie = request.getParameter(Parameters.DNI_NIE);
		    String telefono = request.getParameter(Parameters.PHONE);
		    String password = request.getParameter(Parameters.PASSWORD);
		    String confirmPassword = request.getParameter(Parameters.CONFIRM_PASSWORD);

		    if (!password.equals(confirmPassword)) {
		        logger.warn("Registration failed: passwords do not match for email {}", email);
		        errors.addGlobal(ErrorCodes.PASSWORDS_DO_NOT_MATCH);
		    }

		    if (!ValidationUtils.isPasswordValid(password)) {
		        logger.warn("Registration failed: password does not meet security requirements for email {}", email);
		        errors.addGlobal(ErrorCodes.PASSWORD_NOT_SECURE);
		    }

		    if (!errors.hasErrors()) { 
		        ClienteDTO cliente = new ClienteDTO();
		        cliente.setNombre(nombre);
		        cliente.setApellido1(apellido1);
		        cliente.setApellido2(apellido2);
		        cliente.setNickname(nickname);
		        cliente.setDniNie(dniNie);
		        cliente.setEmail(email);
		        cliente.setTelefono(telefono);
		        cliente.setPassword(confirmPassword);

		        try {
		            clienteService.registrar(cliente);
		            targetView = Views.LOGIN; 
		            forwardOrRedirect = true;
		        } catch (PinguelaException pe) {
		            logger.error(pe.getMessage(), pe);
		        } catch (ServiceException e) {
		            logger.error(e.getMessage(), e);
		        }
		    } else {
		        targetView = Views.REGISTER;
		        forwardOrRedirect = true;
		    }
		}

		RouterUtils.route(request, response, forwardOrRedirect, targetView);
		}


	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
