<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
	<title>AI</title>
</head>
<body>
<h3>[ ${vo.department_name } 고객정보 ]</h3>
<table>
	<tr><th>성별</th>
		<th>급여</th>
	<tr>
		<td>${vo.department_name }</td>
		<td><fmt:formatNumber value="${vo.salary}"/> </td>
	</tr>
</table>

</body>
</html>