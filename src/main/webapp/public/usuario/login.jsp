<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 

    <fmt:setLocale value="${sessionScope.locale}" />
	<fmt:setBundle basename="i18n.Messages" var="messages"/>
 
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión - The Golden Book</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login-styles.css"/>
</head>
<body>
    <div class="login-container">
        <div class="login-form-container">
            <div class="logo-container">
          	<a href="${pageContext.request.contextPath}/${Views.INDEX}">
				<img src="${pageContext.request.contextPath}/images/logo.jpg" alt="logo thegoldenbook" class="logo"/>
			</a>
                <h1>The Golden Book</h1>
            </div>
            <h2><fmt:message bundle="${messages}" key="welcomeMessage"/></h2>
            <p><fmt:message bundle="${messages}" key="introduceFollowingFields"/></p>
            
            <c:forEach var="error" items="${errors.globalErrors}">
				<li><fmt:message key="${error}" bundle="${messages}" /></li>
			</c:forEach>
            
            <form action="${pageContext.request.contextPath}/public/UsuarioServlet" method="post" class="login-form">
            
            	<input type="hidden" name="action" value="login">
            
                <label for="email"><fmt:message bundle="${messages}" key="email"/></label>
                
                <c:choose>
                	<c:when test="${cookie.user.value != null}">
                		<input type="email" id="mail" name="mail" value="${cookie.user.value}" required>		
                	</c:when>
                	<c:otherwise>
                		<input type="email" id="mail" name="mail" required>
                	</c:otherwise>
                </c:choose>
                
                <label for="password"><fmt:message bundle="${messages}" key="password"/></label>
                <input type="password" id="password" name="password" required>
                
                <label for="remember-user">
    				<fmt:message bundle="${messages}" key="rememberUser"/>
   					<input type="checkbox" name="rememberMe" checked="checked">
				</label>
            
                <div class="forgot-password">
                    <a href="#"><fmt:message bundle="${messages}" key="forgottenPassword"/></a>
                </div>
                
                <button type="submit" class="login-btn"><fmt:message bundle="${messages}" key="login"/></button>
                
                <button type="button" class="google-btn">
                    <img src="ruta/icono-google.png" alt="Google Icono"> <fmt:message bundle="${messages}" key="signInGoogle"/>
                </button>
            </form>
            <div class="register-link">
                <a href="${pageContext.request.contextPath}/public/usuario/singup.jsp"><fmt:message bundle="${messages}" key="notAccount"/></a>
            </div>
        </div>
        <div class="image-container">
            <img src="${pageContext.request.contextPath}/images/thegoldenbook-login.png" alt="The Golden Book Decorativo" class="right-image">
        </div>
    </div>
</body>
</html>
