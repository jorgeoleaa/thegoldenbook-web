<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.pinguela.thegoldenbook.model.*"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="i18n.Messages" var="messages" />


<!DOCTYPE html>
<html>
<head>
<script async src="https://www.googletagmanager.com/gtag/js?id=G-DR6X67B92N"></script> <script> window.dataLayer = window.dataLayer || []; function gtag(){dataLayer.push(arguments);} gtag('js', new Date()); gtag('config', 'G-DR6X67B92N'); </script>
<meta charset="UTF-8">
<title>The Golden Book</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/styles.css">

<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<link rel="stylesheet" type="text/css"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />

<script
	src="${pageContext.request.contextPath}/js/jquery/jquery-3.7.1.min.js"></script>

<script src="${pageContext.request.contextPath}/js/libro-search.js"></script>

<style type="text/css">
input.form-control.form-control-dark::placeholder {
	color: white;
}

body {
	font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
		"Helvetica Neue", Arial, sans-serif;
}

svg {
	margin-left: 3px;
}

#cart{
	margin-left: 10px;
}

#lupa{
	margin-left: 10px;
}
</style>

</head>
<body>
	<header class="p-3 text-bg-dark">
		<div class="container">
			<div
				class="d-flex flex-wrap align-items-center justify-content-between">
				<a href="${pageContext.request.contextPath}/${Views.INDEX}"
					class="nav-link px-2 text-secondary"> <img
					alt="logo thegoldenbook"
					src="${pageContext.request.contextPath}/images/logo-removebg-preview.png"
					width="85px">
				</a>

				<form
					action="${pageContext.request.contextPath}/public/LibroServlet"
					class="d-flex mx-auto col-lg-6" role="search">

					<input type="hidden" name="action" value="search-book" /> <input
						type="search" class="form-control form-control-dark text-bg-dark"
						placeholder="Search..." aria-label="Search" id="titulo"
						name="titulo">
					
					<button id="lupa" type="button" class="btn btn-outline-light me-2">
					<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32"
						fill="white" class="bi bi-search" viewBox="0 0 16 16">
  					<path
							d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
					</svg>
					</button>
					<button id="cart" type="button" class="btn btn-outline-light me-2"
						onclick="window.location.href='${pageContext.request.contextPath}/private/usuario/cart.jsp';">
						<svg xmlns="http://www.w3.org/2000/svg" width="32" height="32"
							fill="#fff" class="bi bi-cart" viewBox="0 0 16 16">
  					<path
								d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5M3.102 4l1.313 7h8.17l1.313-7zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4m7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4m-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2m7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2" />
					</svg>
					</button>

				</form>

				<c:choose>
					<c:when test="${sessionScope.cliente.id == null}">
						<div class="text-end">
							<button type="button" class="btn btn-outline-light me-2"
								onclick="window.location.href='${pageContext.request.contextPath}/public/usuario/login.jsp';">
								<fmt:message bundle="${messages}" key="login" />
							</button>
							<button type="button" class="btn btn-warning"
								onclick="window.location.href='${pageContext.request.contextPath}/public/usuario/signup.jsp';">
								<fmt:message bundle="${messages}" key="signup" />
							</button>
						</div>
					</c:when>
					<c:otherwise>
						<div class="dropdown text-end">
							<a href="#"
								class="d-block link-body-emphasis text-decoration-none dropdown-toggle"
								data-bs-toggle="dropdown" aria-expanded="false"> <img
								src="https://github.com/mdo.png" alt="mdo" width="32"
								height="32" class="rounded-circle">
							</a>
							<ul class="dropdown-menu text-small">
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/public/settings.jsp"><fmt:message
											bundle="${messages}" key="settings" /></a></li>
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/private/usuario/my-profile.jsp"><fmt:message
											bundle="${messages}" key="profile" /></a></li>
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/private/usuario/my-orders.jsp"><fmt:message
											bundle="${messages}" key="orders" /></a></li>
								<li><hr class="dropdown-divider"></li>
								<li><a class="dropdown-item"
									href="${pageContext.request.contextPath}/private/UsuarioServlet?action=logout"><fmt:message
											bundle="${messages}" key="signout" /></a></li>
							</ul>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</header>