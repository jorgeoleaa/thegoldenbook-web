<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

	<%@ include file="/common/header.jsp" %>

	<nav class="py-2 bg-body-tertiary border-bottom">
		<div class="container d-flex flex-wrap">
			<ul class="nav me-auto">

				<c:url var="spanish" value="/public/UsuarioServlet">
					<c:param name="action" value="change-locale" />
					<c:param name="locale" value="es" />
					<c:param name="callback" value="${requestScope.callback}" />
				</c:url>

				<li class="nav-item"><a href="${spanish}"
					class="nav-link link-body-emphasis px-2 active"><fmt:message
							bundle="${messages}" key="spanish" /></a></li>

				<c:url var="english" value="/public/UsuarioServlet">
					<c:param name="action" value="change-locale" />
					<c:param name="locale" value="en" />
					<c:param name="callback" value="${requestScope.callback}" />
				</c:url>

				<li class="nav-item"><a href="${english}"
					class="nav-link link-body-emphasis px-2"><fmt:message
							bundle="${messages}" key="english" /></a></li>

				<c:url var="italian" value="/public/UsuarioServlet">
					<c:param name="action" value="change-locale" />
					<c:param name="locale" value="it" />
					<c:param name="callback" value="${requestScope.callback}" />
				</c:url>

				<li class="nav-item"><a href="${italian}"
					class="nav-link link-body-emphasis px-2"><fmt:message
							bundle="${messages}" key="italian" /></a></li>
			</ul>
		</div>
	</nav>

</body>
</html>