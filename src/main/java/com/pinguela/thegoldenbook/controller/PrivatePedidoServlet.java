package com.pinguela.thegoldenbook.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.pinguela.PinguelaException;
import com.pinguela.thegoldenbook.model.ClienteDTO;
import com.pinguela.thegoldenbook.model.LibroDTO;
import com.pinguela.thegoldenbook.model.LineaPedido;
import com.pinguela.thegoldenbook.model.Pedido;
import com.pinguela.thegoldenbook.model.Results;
import com.pinguela.thegoldenbook.service.LibroService;
import com.pinguela.thegoldenbook.service.LineaPedidoService;
import com.pinguela.thegoldenbook.service.MailException;
import com.pinguela.thegoldenbook.service.PedidoCriteria;
import com.pinguela.thegoldenbook.service.PedidoService;
import com.pinguela.thegoldenbook.service.impl.LibroServiceImpl;
import com.pinguela.thegoldenbook.service.impl.LineaPedidoServiceImpl;
import com.pinguela.thegoldenbook.service.impl.PedidoServiceImpl;
import com.pinguela.thegoldenbook.utils.Actions;
import com.pinguela.thegoldenbook.utils.AttributeNames;
import com.pinguela.thegoldenbook.utils.Parameters;
import com.pinguela.thegoldenbook.utils.RouterUtils;
import com.pinguela.thegoldenbook.utils.SessionManager;
import com.pinguela.thegoldenbook.utils.ValidationUtils;
import com.pinguela.thegoldenbook.utils.Views;

/**
 * Servlet implementation class PrivatePedidoServlet
 */
@WebServlet("/private/PedidoServlet")
public class PrivatePedidoServlet extends HttpServlet {
	
	private static final Integer CARRITO_ESTADO = 7;
	private static final Integer EN_PROCESO = 4;
	
	private static Logger logger = LogManager.getLogger(PrivatePedidoServlet.class);
	
	private PedidoService pedidoService = null;
	private LineaPedidoService lineaPedidoService = null;
	private LibroService libroService = null;
       
    public PrivatePedidoServlet() {
        super();
        pedidoService = new PedidoServiceImpl();
        lineaPedidoService = new LineaPedidoServiceImpl();
        libroService = new LibroServiceImpl();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter(Parameters.ACTION);
		
		String targetView = null;
		
		boolean forwardOrRedirect = false;
		
		if(Actions.OPEN_CART.equalsIgnoreCase(action)) {
			
			PedidoCriteria criteria = new PedidoCriteria();
			criteria.setClienteId(((ClienteDTO)SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
			criteria.setTipoEstadoPedidoId(CARRITO_ESTADO);
			
			try {
				Results<Pedido> pedidos = pedidoService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
				
				if(!pedidos.getPage().isEmpty()) {
					request.setAttribute(AttributeNames.PEDIDO, pedidos.getPage().get(0));
				}else {
					
					Pedido pedido = new Pedido();
					
					pedido.setFechaRealizacion(new Date());
					pedido.setPrecio(pedidoService.calcularPrecio(pedido));
					pedido.setClienteId(((ClienteDTO) SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
					pedido.setTipoEstadoPedidoId(CARRITO_ESTADO);
					List<LineaPedido> lineas = new ArrayList<LineaPedido>();
					pedido.setLineas(lineas);
					
					pedidoService.create(pedido);
					
					request.setAttribute(AttributeNames.PEDIDO, pedido);
				}
			} catch (PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			} catch (MailException me) {
				logger.error(me.getMessage(), me);
			}
			
			targetView = Views.CART;
			forwardOrRedirect = true;
			
		}else if(Actions.DELETE_LINE.equalsIgnoreCase(action)) {
			
			String lineaIdStr = request.getParameter(Parameters.LINEA_PEDIDO_ID);
			Long lineaId = Long.valueOf(lineaIdStr);
			
			String pedidoIdStr = request.getParameter(Parameters.PEDIDO_ID);
			Long pedidoId = Long.valueOf(pedidoIdStr);
			
			try {
				boolean isDeleted = lineaPedidoService.deleteFromPedido(lineaId, pedidoId);
				
				if(isDeleted) {
					Pedido pedidoActualizado = pedidoService.findBy(pedidoId);
					pedidoService.update(pedidoActualizado);
					request.setAttribute(AttributeNames.PEDIDO, pedidoActualizado);
				}
				
			}catch(PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
			targetView = Views.CART;
			forwardOrRedirect = true;
			
		}else if(Actions.ADD_TO_CART.equalsIgnoreCase(action)) {
		    String libroIdStr = request.getParameter(Parameters.LIBRO_ID);
		    Long libroId = Long.valueOf(libroIdStr);

		    try {
		        LibroDTO libro = libroService.findByLibro(
		            ((Locale) SessionManager.getAttribute(request, AttributeNames.LOCALE)).getLanguage(), libroId
		        );

		        PedidoCriteria criteria = new PedidoCriteria();
		        criteria.setClienteId(((ClienteDTO) SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
		        criteria.setTipoEstadoPedidoId(CARRITO_ESTADO);

		        Results<Pedido> pedidos = pedidoService.findByCriteria(criteria, 1, Integer.MAX_VALUE);

		        Pedido pedido;
		        // Verificar si la lista de pedidos está vacía
		        if (!pedidos.getPage().isEmpty()) {
		            pedido = pedidos.getPage().get(0);
		        } else {
		            // Crear un nuevo pedido si no existe
		            pedido = new Pedido();
		            pedido.setFechaRealizacion(new Date());
		            pedido.setPrecio(pedidoService.calcularPrecio(pedido));
		            pedido.setClienteId(((ClienteDTO) SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
		            pedido.setTipoEstadoPedidoId(CARRITO_ESTADO);
		            pedido.setLineas(new ArrayList<LineaPedido>());
		            pedidoService.create(pedido);
		        }

		        boolean lineaActualizada = false;

		        // Buscar si el libro ya está en el carrito
		        for (LineaPedido linea : pedido.getLineas()) {
		            if (linea.getLibroId() == libroId) {
		                linea.setUnidades(linea.getUnidades() + 1);
		                lineaActualizada = true;
		                break;
		            }
		        }

		        // Si el libro no está en el carrito, añadirlo
		        if (!lineaActualizada) {
		            LineaPedido lp = new LineaPedido();
		            lp.setLibroId(libro.getId());
		            lp.setPrecio(libro.getPrecio());
		            lp.setUnidades(1);
		            lp.setPedidoId(pedido.getId());
		            pedido.getLineas().add(lp);
		        }

		        // Actualizar el precio total del pedido
		        pedido.setPrecio(pedidoService.calcularPrecio(pedido));

		        // Actualizar el pedido en la base de datos
		        boolean isUpdated = pedidoService.update(pedido);

		        if (isUpdated) {
		            request.setAttribute(AttributeNames.PEDIDO, pedido);
		        }

		    } catch (PinguelaException pe) {
		        logger.error(pe.getMessage(), pe);
		    } catch (MailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    targetView = Views.CART;
		    forwardOrRedirect = true;
		}
else if (Actions.UPDATE_FROM_SPINNER.equalsIgnoreCase(action)) {
		    String pedidoIdStr = request.getParameter(Parameters.PEDIDO_ID);
		    Long pedidoId = Long.valueOf(pedidoIdStr);

		    String lineaIdStr = request.getParameter(Parameters.LINEA_PEDIDO_ID);
		    Long lineaId = Long.valueOf(lineaIdStr);

		    String unidadesStr = request.getParameter(Parameters.NEW_UNIDADES);
		    int unidades = Integer.valueOf(unidadesStr);
		    
		    
		    
		    try {
		        Pedido pedido = pedidoService.findBy(pedidoId);

		        if (unidades == 0) {
		            lineaPedidoService.deleteFromPedido(lineaId, pedidoId);
		        } else {
		            for (LineaPedido linea : pedido.getLineas()) {
		                if (linea.getId().equals(lineaId)) {
		                    linea.setUnidades(unidades);
		                }
		            }
		        }

		        pedido.setPrecio(pedidoService.calcularPrecio(pedido));
		        boolean isUpdated = pedidoService.update(pedido);

		        if (isUpdated) {
		            String requestedWith = request.getHeader("X-Requested-With");
		            if ("XMLHttpRequest".equals(requestedWith)) {
		                JSONObject jsonResponse = new JSONObject();
		                jsonResponse.put("success", true);
		                jsonResponse.put("totalPrice", pedido.getPrecio());
		                response.setContentType("application/json");
		                response.getWriter().write(jsonResponse.toString());
		                return;
		            } else {
		                request.setAttribute(AttributeNames.PEDIDO, pedido);
		            }
		        }

		    } catch (PinguelaException pe) {
		        logger.error(pe.getMessage(), pe);
		    }

		    targetView = Views.CART;
		    forwardOrRedirect = true;
		    
		}else if(Actions.OPEN_MY_ORDERS.equalsIgnoreCase(action)) {
			
			try {
				
				PedidoCriteria criteria = new PedidoCriteria();
				criteria.setClienteId(((ClienteDTO) SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
				Results<Pedido> pedidos = pedidoService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
				request.setAttribute(AttributeNames.PEDIDOS, pedidos.getPage());
				
			}catch(PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
			targetView = Views.MY_ORDERS;
			forwardOrRedirect = true;
			
		}else if (Actions.SEARCH_ORDERS.equalsIgnoreCase(action)) {

		    String fechaDesdeStr = request.getParameter(Parameters.FECHA_DESDE);
		    String fechaHastaStr = request.getParameter(Parameters.FECHA_HASTA);
		    
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    
		    PedidoCriteria criteria = new PedidoCriteria();

		    criteria.setFechaHasta(ValidationUtils.parseToDate(fechaHastaStr, formatter));
		    criteria.setFechaDesde(ValidationUtils.parseToDate(fechaDesdeStr, formatter));
		    
		    String estadoIdStr = request.getParameter(Parameters.STATUS);

		    criteria.setTipoEstadoPedidoId(ValidationUtils.parseInteger(estadoIdStr));

		    
		    criteria.setClienteId(((ClienteDTO) SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());  
		    

		    try {
		        Results<Pedido> pedidos = pedidoService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
		        request.setAttribute(AttributeNames.PEDIDOS, pedidos.getPage());
		    } catch (PinguelaException pe) {
		        logger.error(pe.getMessage(), pe);
		    }

		    targetView = Views.MY_ORDERS;
		    forwardOrRedirect = true;
		}else if(Actions.PURCHASE.equalsIgnoreCase(action)) {
			
			String pedidoIdStr = request.getParameter(Parameters.PEDIDO_ID);
			Long pedidoId = Long.valueOf(pedidoIdStr);
			
			try {
				
				Pedido pedido = pedidoService.findBy(pedidoId);
				
				pedido.setTipoEstadoPedidoId(EN_PROCESO);
				pedido.setFechaRealizacion(new Date());
				
				if(pedidoService.update(pedido)) {
					PedidoCriteria criteria = new PedidoCriteria();
					criteria.setClienteId(((ClienteDTO)SessionManager.getAttribute(request, AttributeNames.CLIENTE)).getId());
					Results<Pedido> pedidos = pedidoService.findByCriteria(criteria, 1, Integer.MAX_VALUE);
					request.setAttribute(AttributeNames.PEDIDOS, pedidos.getPage());
				}
				
			}catch(PinguelaException pe) {
				logger.error(pe.getMessage(), pe);
			}
			
			targetView = Views.MY_ORDERS;
			forwardOrRedirect = true;
			
		}

		
		RouterUtils.route(request, response, forwardOrRedirect, targetView);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
