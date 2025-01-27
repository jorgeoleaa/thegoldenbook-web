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
    <title>Registro - The Golden Book</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register-styles.css"/>
</head>
<body>
    <div class="register-container">
        <div class="form-wrapper">
            <div class="logo-container">
                <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Logo The Golden Book" class="logo">
                <h1>The Golden Book</h1>
            </div>
            <h2><fmt:message bundle="${messages}" key="registerForm"/></h2>
            <p><fmt:message bundle="${messages}" key="introduceFollowingFields"/></p>
            
             <c:forEach var="error" items="${errors.globalErrors}">
				<li><fmt:message key="${error}" bundle="${messages}" /></li>
			</c:forEach>
            
            <form action="${pageContext.request.contextPath}/public/UsuarioServlet" method="post" class="register-form">
                <input type="hidden" name="action" value="register">
                
                <label for="nombre"><fmt:message bundle="${messages}" key="name"/></label>
                <input type="text" id="nombre" name="nombre" required>

                <label for="apellido1"><fmt:message bundle="${messages}" key="firstSurname"/></label>
                <input type="text" id="apellido1" name="apellido1" required>

                <label for="apellido2"><fmt:message bundle="${messages}" key="secondSurname"/></label>
                <input type="text" id="apellido2" name="apellido2">

                <label for="nickname"><fmt:message bundle="${messages}" key="nickname"/></label>
                <input type="text" id="nickname" name="nickname" required>

                <label for="email"><fmt:message bundle="${messages}" key="email"/></label>
                <input type="email" id="email" name="mail" required>

                <label for="dni"><fmt:message bundle="${messages}" key="dniNie"/></label>
                <input type="text" id="dni" name="dniNie" required>

                <label for="telefono"><fmt:message bundle="${messages}" key="phoneNumber"/></label>
                <input type="tel" id="telefono" name="telefono" required>

                <label for="password"><fmt:message bundle="${messages}" key="password"/></label>
                <input type="password" id="password" name="password" required>

                <label for="confirm-password"><fmt:message bundle="${messages}" key="confirmPassword"/></label>
                <input type="password" id="confirm-password" name="confirmPassword" required>

                <button type="submit" class="register-btn"><fmt:message bundle="${messages}" key="confirm"/></button>
            </form>
            <div class="login-link">
                <a href="${pageContext.request.contextPath}/public/usuario/login.jsp"><fmt:message bundle="${messages}" key="alreadyHaveAccount"/></a>
            </div>
        </div>
    </div>
</body>
</html>
