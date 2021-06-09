<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>AI</title>
</head>
<body>
<h3>[ ${vo.name } 고객정보 ]</h3>
<table>
	<tr><th>성별</th>
		<th>이메일</th>
		<th>전화번호</th></tr>
	<tr>
		<td>${vo.gender }</td>
		<td>${vo.email }</td>
		<td>${vo.phone }</td>
	</tr>
</table>
<!--2021-05-13 class css줄라고했는데 아직 이름생각못함  -->
<div class="imsi">
	<a class='btn-fill' href='list.cu'>고객목록</a>
	<a class='btn-fill' href='new.cu'>신규 고객 추가</a>
	<a class='btn-fill' href='modify.cu?id=${vo.id }'>고객 정보 수정</a>
	<a class='btn-fill' onclick='if(confirm("정말삭제?")){href="delete.cu?id=${vo.id }"} '  >고객 정보 삭제</a>
</div>
</body>
</html>