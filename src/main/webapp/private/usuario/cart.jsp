<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div id="cart-container">
    <div id="lineas-container">
        <c:choose>
            <c:when test="${not empty pedido.lineas}">
                <c:forEach var="linea" items="${pedido.lineas}">
                    <div id="linea-container">
                        <div id="linea-book-image">
                            <img alt="portada del libro" src="${pageContext.request.contextPath}/ImageServlet?action=image-libro&libroId=${linea.libroId}&imageNombre=g1.jpg">
                            <h1><c:out value="${linea.nombreLibro}"/></h1>
                        </div>
                        <div id="linea-data">
                            <input class="spinner" type="number" min="1" name="unidades" value="${linea.unidades}" pedidoId="${pedido.id}" lineaId="${linea.id}" />
                            <form action="${pageContext.request.contextPath}/private/PedidoServlet">
                                <input type="hidden" name="action" value="delete-line"/>
                                <input type="hidden" name="lineaId" value="${linea.id}"/>
                                <input type="hidden" name="pedidoId" value="${pedido.id}"/>
                                <h2><c:out value="${linea.precio}"/>€</h2>
                                <input type="submit" value="Eliminar">
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <h2><fmt:message bundle="${messages}" key="nothingCart"></fmt:message></h2>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="header-pedido-data">
    	<h2>Precio total: <c:out value="${pedido.precio}"/>€</h2>
    	<a href="${pageContext.request.contextPath}/private/PedidoServlet?action=purchase&pedidoId=${pedido.id}">
    		<button>Compra en 1 clic!</button>
    	</a>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
