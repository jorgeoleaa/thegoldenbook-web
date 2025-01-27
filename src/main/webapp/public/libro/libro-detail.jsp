<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/header.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/libro-detail.js"></script>
	<div id="detail-container">
		<div id="book-images-container">
			<div id="main-image-container">
				<img alt="portada del libro ${libro.nombre}" src="${pageContext.request.contextPath}/ImageServlet?action=image-libro&libroId=${libro.id}&imageNombre=g1.jpg">
			</div>
			<div id="small-images-container">
				<div id="small-image-container">
					<img alt="portada del libro ${libro.nombre}" src="${pageContext.request.contextPath}/ImageServlet?action=image-libro&libroId=${libro.id}&imageNombre=g1.jpg">
				</div>
				<div id="small-image-container">
					<img src="${pageContext.request.contextPath}/ImageServlet?action=image-libro&libroId=${libro.id}&imageNombre=g2.jpg">
				</div>
				<div id="small-image-container">
					<img src="${pageContext.request.contextPath}/ImageServlet?action=image-libro&libroId=${libro.id}&imageNombre=g3.jpg">
				</div>
			</div>
		</div>
		<div id="book-data-container">
			<h1><c:out value="${libro.nombre}"/></h1>
			<h3><c:out value="${libro.autores[0].nombre}"/><c:out value="${libro.autores[0].apellido1}"/></h3>
			<h4><c:out value="${libro.isbn}"/></h4>
			<p>
				<c:forEach var="tematica" items="${libro.tematicas}">
					<c:out value="${tematica.nombre}"></c:out> 
				</c:forEach>
			</p>
			<p><fmt:message bundle="${messages}" key="synopsis"/> <c:out value="${libro.nombre}"/></p>
			<textarea rows="3" cols="30" readonly>
				<c:out value="${libro.sinopsis}"/>
			</textarea>
		</div>
		<div id="book-data-container">
			<h2><c:out value="${libro.precio}"/></h2>
			<form action="${pageContext.request.contextPath}/private/PedidoServlet" method="post">
				<input type="hidden" name="action" value="addToCart"/>
				<input type="hidden" name="libroId" value="${libro.id}"/>
				<button id="addToCart" type="submit"><fmt:message bundle="${messages}" key="addToCart"/></button>
			</form>
			
			<div id="formato-div">
				<label><c:out value="${libro.formatoNombre}"/></label>
			</div>
			<div id="envio-text">
				<label>
					<img alt="imagen carrito" src="${pageContext.request.contextPath}/images/cart.png">
					Recíbelo mañana gratis!
				</label>
			</div>
		</div>
	</div>
	<div id="valoraciones-container">
		<c:forEach var="valoracion" items="${valoraciones}">
			<div id="valoracion-container">
				<form action="${pageContext.request.contextPath}/private/ValoracionServlet" method="post">
					
					<input type="hidden" name="action" value="delete-valoracion"/>
					
					<input type="hidden" name="libroId" value="${valoracion.libroId}"/>
					
					<h4><c:out value="${valoracion.nickname}"/></h4>
					<p><c:out value="${valoracion.asunto}"/></p>
					<p><c:out value="${valoracion.cuerpo}"/></p>
					<c:if test="${valoracion.clienteId == sessionScope.cliente.id}">
						<button><fmt:message bundle="${messages}" key="edit"/></button>
						<input type="submit" value="<fmt:message bundle="${messages}" key="delete"/>"/>
					</c:if>
				</form>
			</div>
		</c:forEach>
	</div>
	
	<c:if test="${sessionScope.cliente.id != null}">
		<button id="openCreateValoracion"><fmt:message bundle="${messages}" key="addReview"/></button>
	</c:if>
	
	
	<dialog id="createValoracion">
		<form action="${pageContext.request.contextPath}/private/ValoracionServlet" method="post">
		
			<input type="hidden" name="action" value="create-valoracion"/>
			<input type="hidden" name="libroId" value="${libro.id}"/>
			<input type="number" name="puntuacion" min="1" max="5"/>
			<p><fmt:message bundle="${messages}" key="subject"/></p>
			<input name="asunto" type="text">
			<p><fmt:message bundle="${messages}" key="body"/></p>
			<textarea name="cuerpo" rows="5" cols="54"></textarea>
			<input type="submit" value="Guardar">
		</form>
		<button id="cancelButton"><fmt:message bundle="${messages}" key="cancel"/></button>
	</dialog>
</body>
</html>