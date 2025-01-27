<%@page import="com.pinguela.thegoldenbook.utils.SessionManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.pinguela.thegoldenbook.service.*"%>
<%@ page import="com.pinguela.thegoldenbook.service.impl.*"%>
<%@ page import="com.pinguela.thegoldenbook.model.*"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<%@ include file="/common/header.jsp" %>
	
<style>
#content {
    display: flex;
    gap: 20px;
    padding: 20px;
    background-color: #f4f4f4;
}

#search-menu {
    flex: 1;
    max-width: 300px;
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

#results {
    flex: 2;
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.book-table {
    width: 100%;
    border-collapse: collapse;
    text-align: center;
}

.book-table td {
    padding: 10px;
}

.book-table img {
    width: 100px;
    height: 150px;
    display: block;
    margin: 0 auto;
}

.book-table a {
    display: block;
    margin-top: 10px;
    text-decoration: none;
    color: #333;
}

.book-table a:hover {
    color: #007bff;
    text-decoration: underline;
}
		
	</style>
	
	<%
	String localeName = ((Locale) SessionManager.getAttribute(request, "locale")).getLanguage();
	%>
	<div id="content">
		<div id="search-menu">
			<form action="${pageContext.request.contextPath}/public/LibroServlet" method="post">
				
				<input type="hidden" name="action" value="search-book"/>
				<input type="hidden" name="locale" value="${sessionScope.locale}"/>
				
				<%
					FormatoService formatoService = new FormatoServiceImpl();
					List<Formato> formatos = formatoService.findAll(localeName);
					
					request.setAttribute("formatos", formatos);
				%>
			
				<div id="formatos">
					<h3><fmt:message bundle="${messages}" key="format"/></h3>
					<c:forEach var="formato" items="${formatos}">
						<label><input type="radio" name="formato" value="${formato.id}"/><c:out value="${formato.nombre}"/></label>
						<br>
					</c:forEach>
				</div>
				
				<%
					GeneroLiterarioService generoService = new GeneroLiterarioServiceImpl();
					List<GeneroLiterario> generos = generoService.findAll(localeName);
					
					request.setAttribute("generos", generos);
				%>
				
				<div id="generos">
					<h3><fmt:message bundle="${messages}" key="literaryGenre"/></h3>
					<c:forEach var="genero" items="${generos}">
						<label><input type="radio" name="genero" value="${genero.id}"><c:out value="${genero.nombre}"/></label>
						<br>
					</c:forEach>			
				</div>
				
				<%
					IdiomaService idiomaService = new IdiomaServiceImpl();
					List<Idioma> idiomas = idiomaService.findAll(localeName);
					
					request.setAttribute("idiomas", idiomas);
				
				%>
				
				<div id="idiomas">
					<h3><fmt:message bundle="${messages}" key="language"/></h3>
					<c:forEach var="idioma" items="${idiomas}">
						<label><input type="radio" name="idioma" value="${idioma.id}"><c:out value="${idioma.nombre}"/></label>
						<br>
					</c:forEach>
				</div>
				
				<%
					ClasificacionEdadService edadService = new ClasificacionEdadServiceImpl();
					List<ClasificacionEdad> edades = edadService.findAll(localeName);
					
					request.setAttribute("edades", edades);
				%>
				
				<div id="edades">
					<h3><fmt:message bundle="${messages}" key="age"/></h3>
					<c:forEach var="edad" items="${edades}">
						<label><input type="radio" name="edad" value="${edad.id}"><c:out value="${edad.nombre}"/></label>
						<br>
					</c:forEach>
				</div>
			</form>
		</div>
		<div id="results">
			<div id="pagination">
			</div>
		</div>
	</div>
</body>
</html>