package com.pinguela.thegoldenbook.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.ClienteDTO;
import com.pinguela.thegoldenbook.model.LibroDTO;
import com.pinguela.thegoldenbook.model.Results;
import com.pinguela.thegoldenbook.model.ValoracionDTO;
import com.pinguela.thegoldenbook.service.LibroService;
import com.pinguela.thegoldenbook.service.ValoracionService;
import com.pinguela.thegoldenbook.service.impl.LibroServiceImpl;
import com.pinguela.thegoldenbook.service.impl.ValoracionServiceImpl;
import com.pinguela.thegoldenbook.utils.Actions;
import com.pinguela.thegoldenbook.utils.AttributeNames;
import com.pinguela.thegoldenbook.utils.Parameters;
import com.pinguela.thegoldenbook.utils.RouterUtils;
import com.pinguela.thegoldenbook.utils.SessionManager;
import com.pinguela.thegoldenbook.utils.Views;

/**
 * Servlet implementation class PrivateValoracionServlet
 */
@WebServlet("/private/ValoracionServlet")
public class PrivateValoracionServlet extends HttpServlet {    
	
	private static Logger logger = LogManager.getLogger();
	
	private ValoracionService valoracionService = null;
	private LibroService libroService = null;

    public PrivateValoracionServlet() {
        super();
        valoracionService = new ValoracionServiceImpl();
        libroService = new LibroServiceImpl();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter(Parameters.ACTION);
		
		String targetView = null;
		
		boolean forwardOrRedirect = false;
		
		if(Actions.CREATE_VALORACION.equalsIgnoreCase(action)) {
			
			String libroIdStr = request.getParameter(Parameters.LIBRO_ID);
			Long libroId = Long.valueOf(libroIdStr);
			
			String asunto = request.getParameter(Parameters.ASUNTO);
			
			String cuerpo = request.getParameter(Parameters.CUERPO);
			
			String puntuacionStr = request.getParameter(Parameters.PUNTUACION);
			Double puntuacion = Double.valueOf(puntuacionStr);
			
			ValoracionDTO valoracion = new ValoracionDTO();
			valoracion.setClienteId(((ClienteDTO)SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
			valoracion.setLibroId(libroId);
			valoracion.setAsunto(asunto);
			valoracion.setCuerpo(cuerpo);
			valoracion.setFechaPublicacion(new Date());
			valoracion.setNumeroEstrellas(puntuacion);
			
			try {
				valoracionService.create(valoracion, ((Locale)SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage());
				
				Results<ValoracionDTO> valoraciones = valoracionService.findByLibro(libroId, 1, Integer.MAX_VALUE);
				request.setAttribute(AttributeNames.VALORACIONES, valoraciones.getPage());
				
				LibroDTO libro = libroService.findByLibro(((Locale)SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage(), libroId);
				request.setAttribute(AttributeNames.LIBRO, libro);
				
			} catch (PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
			targetView = Views.LIBRO_DETAIL;
			forwardOrRedirect = true;
		
		}else if(Actions.DELETE_VALORACION.equalsIgnoreCase(action)) {
			
			String libroIdStr = request.getParameter(Parameters.LIBRO_ID);
			Long libroId = Long.valueOf(libroIdStr);
			
			try {
				if(valoracionService.delete(((ClienteDTO) SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId(), libroId)) {
					LibroDTO libro = libroService.findByLibro(((Locale)SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage(), libroId);
					request.setAttribute(AttributeNames.LIBRO, libro);
					Results<ValoracionDTO> valoraciones = valoracionService.findByLibro(libroId, 1, Integer.MAX_VALUE);
					request.setAttribute(AttributeNames.VALORACIONES, valoraciones.getPage());
				}
			} catch (PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
			targetView = Views.LIBRO_DETAIL;
			forwardOrRedirect = true;
		}
		
		RouterUtils.route(request, response, forwardOrRedirect, targetView);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
