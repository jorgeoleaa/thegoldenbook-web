<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp" %>
<div id="search-orders-container">
    <div id="search-parameters-container">
        <form action="${pageContext.request.contextPath}/private/PedidoServlet" method="POST">
            <input type="hidden" name="action" value="searchOrders"/>
            <section class="filter-section">
                <h3>Fechas</h3>
                <label for="from-date">Desde:</label>
                <input type="date" pattern="^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\d{4}$" id="from-date" name="from-date">
                <label for="to-date">Hasta:</label>
                <input type="date" pattern="^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\d{4}$" id="to-date" name="to-date">
            </section>

            <section class="filter-section">
                <h3>Estado del pedido</h3>
                <label><input type="radio" name="status" value="1"> En preparación</label>
                <label><input type="radio" name="status" value="2"> Enviado</label>
                <label><input type="radio" name="status" value="3"> En reparto</label>
                <label><input type="radio" name="status" value="4"> En proceso</label>
                <label><input type="radio" name="status" value="5"> Entregado</label>
                <label><input type="radio" name="status" value="6"> Compra en tienda</label>
            </section>
            <input type="submit" value="Buscar">
        </form>
    </div>

    <div id="orders-container">
        <c:choose>
            <c:when test="${not empty pedidos}">
                <c:forEach var="pedido" items="${pedidos}">
                    <c:if test="${pedido.tipoEstadoPedidoId != 7}">
                        <div id="order-box">
                            <div id="order-data">
                                <h2>Pedido <c:out value="${pedido.id}"/></h2>
                                <h4>Estado: <c:out value="${pedido.tipoEstadoPedidoNombre}"/></h4>
                            </div>
                            <div id="lineas-box">
                                <c:forEach var="linea" items="${pedido.lineas}">
                                    <div id="linea-box">
                                        <img alt="portada del libro" src="${pageContext.request.contextPath}/ImageServlet?action=image-libro&libroId=${linea.libroId}&imageNombre=g1.jpg">
                                        <h2><c:out value="${linea.nombreLibro}"/></h2>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <h2>No has realizado ningun pedido</h2>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
