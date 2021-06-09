<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>AI</title>
</head>
<body>
	<!--Content  -->
	<table>
		<tr><th>고객명</th>
			<th>이메일</th>
		</tr>
		<c:forEach items="${list}" var="vo">
			<tr>
				<th><a href='detail.cu?id=${vo.name}'> ${vo.name} </a></th>
				<th><a href='detail.cu?id=${vo.email}'> ${vo.email}</a></th>
			</tr>
		</c:forEach>
	</table>

</body>
</html>