package com.pinguela.thegoldenbook.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.ClienteDTO;
import com.pinguela.thegoldenbook.service.ClienteService;
import com.pinguela.thegoldenbook.service.FileService;
import com.pinguela.thegoldenbook.service.impl.ClienteServiceImpl;
import com.pinguela.thegoldenbook.service.impl.FileServiceImpl;
import com.pinguela.thegoldenbook.utils.Actions;
import com.pinguela.thegoldenbook.utils.AttributeNames;
import com.pinguela.thegoldenbook.utils.Parameters;
import com.pinguela.thegoldenbook.utils.RouterUtils;
import com.pinguela.thegoldenbook.utils.SessionManager;
import com.pinguela.thegoldenbook.utils.Views;

/**
 * Servlet implementation class PrivateUsuarioServlet
 */
@WebServlet("/private/UsuarioServlet")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
	    maxFileSize = 1024 * 1024 * 10,      // 10 MB
	    maxRequestSize = 1024 * 1024 * 50    // 50 MB
	)
public class PrivateUsuarioServlet extends HttpServlet {
	
	private static Logger logger = LogManager.getLogger(PrivateUsuarioServlet.class);
	
	private FileService fileService = null;
	private ClienteService clienteService = null;
	
    public PrivateUsuarioServlet() {
        super();
        fileService = new FileServiceImpl();
        clienteService = new ClienteServiceImpl();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String targetView = null;
		
		Boolean forwardOrRedirect = false;
		
		String action = request.getParameter(Parameters.ACTION);
		
		if(Actions.LOGOUT.equalsIgnoreCase(action)) {
			SessionManager.removeAttribute(request, AttributeNames.CLIENTE);
			targetView = Views.LOGIN;
			forwardOrRedirect = true;
		
		}else if(Actions.EDIT_PROFILE.equalsIgnoreCase(action)) {
			
			Long clienteId = ((ClienteDTO)SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId();
			
			String nickname = request.getParameter(Parameters.NICKNAME);
			String nombre = request.getParameter(Parameters.NOMBRE);
			String apellido1 = request.getParameter(Parameters.APELLIDO1);
			String apellido2 = request.getParameter(Parameters.APELLIDO2);
			String email = request.getParameter(Parameters.MAIL);
			String dniNie = request.getParameter(Parameters.DNI_NIE);
			String telefono = request.getParameter(Parameters.PHONE);
			String password = request.getParameter(Parameters.PASSWORD);
			
			try {
				
				ClienteDTO clienteActualizado = new ClienteDTO();
				clienteActualizado.setId(clienteId);
				clienteActualizado.setNickname(nickname);
				clienteActualizado.setNombre(nombre);
				clienteActualizado.setApellido1(apellido1);
				clienteActualizado.setApellido2(apellido2);
				clienteActualizado.setEmail(email);
				clienteActualizado.setDniNie(dniNie);
				clienteActualizado.setTelefono(telefono);
				
				 Part partFile = request.getPart("file");
		            if (partFile != null && partFile.getSize() > 0) {
		                byte[] byteImage = readInputStreamToByteArray(partFile.getInputStream());
		                fileService.uploadProfileImage(clienteId, byteImage);
		            }
				
				if(clienteService.update(clienteActualizado)) {
					SessionManager.setAttribute(request, AttributeNames.CLIENTE, clienteActualizado);
				}
				
				if(((ClienteDTO)SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getPassword() != password) {
					clienteService.updatePassword(password, clienteId);
				}
				
			}catch(PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
			targetView = Views.MY_PROFILE;
			forwardOrRedirect = true;
		}
		
		RouterUtils.route(request, response, forwardOrRedirect, targetView);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private byte[] readInputStreamToByteArray(InputStream inputStream) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    byte[] buffer = new byte[4096];
	    int bytesRead;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
	        byteArrayOutputStream.write(buffer, 0, bytesRead);
	    }
	    return byteArrayOutputStream.toByteArray();
	}

}
