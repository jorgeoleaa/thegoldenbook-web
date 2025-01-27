
package com.pinguela.thegoldenbook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pinguela.thegoldenbook.service.FileService;
import com.pinguela.thegoldenbook.service.impl.FileServiceImpl;
import com.pinguela.thegoldenbook.utils.Actions;
import com.pinguela.thegoldenbook.utils.AttributeNames;
import com.pinguela.thegoldenbook.utils.Parameters;
import com.pinguela.thegoldenbook.utils.SessionManager;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {

	private FileService fileService;

	public ImageServlet() {
		super();
		fileService = new FileServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter(Parameters.ACTION);
		
		if(Actions.IMAGE_LIBRO.equalsIgnoreCase(action)) {

			String libroIdStr = request.getParameter(Parameters.LIBRO_ID);
			String imageName = request.getParameter(Parameters.IMAGE_NAME);
			String localeName = ((Locale) SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage();
			
			if (libroIdStr == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing libroId parameter");
				return;
			}

			Long libroId = Long.parseLong(libroIdStr);
			List<File> images = fileService.getImagesByBookId(localeName, libroId);
			if (images.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "No images found for the given libroId");
				return;
			}

			response.setContentType("image/jpg");
			response.setHeader("Content-Disposition", "inline; filename=hola.png");

			  try (OutputStream out = response.getOutputStream()) {
			        for (File fileImg : images) {
			            if (fileImg.getName().equals(imageName)) {
			                try (FileInputStream fis = new FileInputStream(fileImg)) {
			                    byte[] buffer = new byte[4096];
			                    int bytesRead;
			                    while ((bytesRead = fis.read(buffer)) != -1) {
			                        out.write(buffer, 0, bytesRead);
			                    }
			                    out.flush();
			                }
			            }
			        }
			    }
		}else if(Actions.PROFILE_IMAGE.equalsIgnoreCase(action)) {
			
			String imageName = request.getParameter(Parameters.IMAGE_NAME);
			
			String clienteIdStr = request.getParameter(Parameters.CLIENTE_ID);
			Long clienteId = Long.valueOf(clienteIdStr);
			
			List<File> images = fileService.getProfileImageByClienteId(clienteId);
			
			if(images.isEmpty()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "NO image found for the given clienteId");
			}else {
				
				response.setContentType("image/jpg");
				response.setHeader("Content-Disposition", "inline; filename=hola.png");
				
				try (OutputStream out = response.getOutputStream()) {
			        if (images.get(0).getName().equalsIgnoreCase(imageName)) {
			            try (FileInputStream fis = new FileInputStream(images.get(0))) {
			                byte[] buffer = new byte[4096]; 
			                int bytesRead;
			                while ((bytesRead = fis.read(buffer)) != -1) {
			                    out.write(buffer, 0, bytesRead);
			                }
			                out.flush();
			            }
			        }
			    }
			}
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
