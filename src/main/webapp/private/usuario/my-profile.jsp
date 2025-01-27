<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/header.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/my-profile.js"></script>

<div id="profile-container">
    <h2>Hola <c:out value="${sessionScope.cliente.nombre}"/>!</h2>
    <div id="profile-image">
        <img alt="imagen de perfil" src="${pageContext.request.contextPath}/ImageServlet?action=profileImage&clienteId=${sessionScope.cliente.id}&imageNombre=g1.jpg">
    </div>
    <form action="${pageContext.request.contextPath}/private/UsuarioServlet" method="post" enctype="multipart/form-data">

        <input type="hidden" name="action" value="edit-profile">

        <input name="file" type="file" id="input-file"/>

        <div id="profile-data-container">
            <div id="profile-data1">
                <p><fmt:message bundle="${messages}" key="nickname"/></p>
                <input type="text" name="nickname" value="${sessionScope.cliente.nickname}" required>

                <p><fmt:message bundle="${messages}" key="name"/><p>
                <input type="text" name="nombre" value="${sessionScope.cliente.nombre}" required>

                <p><fmt:message bundle="${messages}" key="firstSurname"/></p>
                <input type="text" name="apellido1" value="${sessionScope.cliente.apellido1}" required>

                <p><fmt:message bundle="${messages}" key="secondSurname"/></p>
                <input type="text" name="apellido2" value="${sessionScope.cliente.apellido2}" required>
            </div>
            <div id="profile-data2">
                <p><fmt:message bundle="${messages}" key="dniNie"/></p>
                <input type="text" name="dniNie" value="${sessionScope.cliente.dniNie}" required>

                <p><fmt:message bundle="${messages}" key="email"/></p>
                <input type="email" name="mail" value="${sessionScope.cliente.email}" required>

                <p><fmt:message bundle="${messages}" key="phoneNumber"/></p>
                <input type="text" name="telefono" value="${sessionScope.cliente.telefono}" required>

                <p><fmt:message bundle="${messages}" key="password"/></p>
                <input type="password" name="password" value="${sessionScope.cliente.password}" required>
            </div>
        </div>    

        <!-- Botón para guardar cambios -->
        <input id="save-button" type="submit" value="<fmt:message bundle='${messages}' key='saveChanges'/>">
    </form>
    <button id="edit-button"><fmt:message bundle="${messages}" key="edit"/></button>
    <button id="cancel-button"><fmt:message bundle="${messages}" key="cancel"/></button>
</div>

</body>
</html>