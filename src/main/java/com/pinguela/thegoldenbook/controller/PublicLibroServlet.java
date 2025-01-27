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
import com.pinguela.thegoldenbook.dao.DataException;
import com.pinguela.thegoldenbook.model.LibroDTO;
import com.pinguela.thegoldenbook.model.Results;
import com.pinguela.thegoldenbook.model.ValoracionDTO;
import com.pinguela.thegoldenbook.service.LibroCriteria;
import com.pinguela.thegoldenbook.service.LibroService;
import com.pinguela.thegoldenbook.service.ValoracionService;
import com.pinguela.thegoldenbook.service.impl.LibroServiceImpl;
import com.pinguela.thegoldenbook.service.impl.ValoracionServiceImpl;
import com.pinguela.thegoldenbook.utils.Actions;
import com.pinguela.thegoldenbook.utils.AttributeNames;
import com.pinguela.thegoldenbook.utils.Errors;
import com.pinguela.thegoldenbook.utils.Parameters;
import com.pinguela.thegoldenbook.utils.RouterUtils;
import com.pinguela.thegoldenbook.utils.SessionManager;
import com.pinguela.thegoldenbook.utils.ValidationUtils;
import com.pinguela.thegoldenbook.utils.Views;

/**
 * Servlet implementation class PublicLibroServlet
 */
@WebServlet("/public/LibroServlet")
public class PublicLibroServlet extends HttpServlet {
	
	private static Logger logger = LogManager.getLogger(PublicLibroServlet.class);
	
	private LibroService libroService = null;
	private ValoracionService valoracionService = null;
       
    public PublicLibroServlet() {
        super();
        libroService = new LibroServiceImpl();
        valoracionService = new ValoracionServiceImpl();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter(Parameters.ACTION);
		
		String targetView = null;
		
		boolean forwardOrRedirect = false;
		
		if(Actions.SEARCH_BOOK.equalsIgnoreCase(action)) {
			
			Errors errors = new Errors();
			LibroCriteria criteria = new LibroCriteria();
			
			String localeName = ((Locale)SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage();
			criteria.setLocale(localeName);
			criteria.setFormatoId(ValidationUtils.checkEmptyRadiobutton(request.getParameter(Parameters.FORMATO), errors));
			criteria.setIdiomaId(ValidationUtils.checkEmptyRadiobutton(request.getParameter(Parameters.IDIOMA), errors));
			criteria.setGeneroLiterarioId(ValidationUtils.checkEmptyRadiobutton(request.getParameter(Parameters.GENERO), errors));
			criteria.setClasificacionEdadId(ValidationUtils.checkEmptyRadiobutton(request.getParameter(Parameters.EDAD), errors));
			criteria.setNombre(ValidationUtils.checkEmptyStringInput(request.getParameter(Parameters.TITULO), errors));
			
			try {
				Results<LibroDTO> libros = libroService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
				request.setAttribute(AttributeNames.RESULTADOS, libros.getPage());
				targetView = Views.INDEX;
				forwardOrRedirect = true;
			}catch(PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
		}else if(Actions.LIBRO_DETAIL.equalsIgnoreCase(action)) {
			
			String libroIdStr = request.getParameter(Parameters.LIBRO_ID);
			Long libroId = Long.valueOf(libroIdStr);
			
			try {
				LibroDTO libro = libroService.findByLibro(((Locale)SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage(), libroId);
				Results<ValoracionDTO> valoraciones = valoracionService.findByLibro(libroId, 1, Integer.MAX_VALUE);
				request.setAttribute(AttributeNames.LIBRO, libro);
				request.setAttribute(AttributeNames.VALORACIONES, valoraciones.getPage());
			} catch (DataException e) {
				logger.error(e.getMessage(), e);
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
