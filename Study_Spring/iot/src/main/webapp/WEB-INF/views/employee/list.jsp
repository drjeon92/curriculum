<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>AI</title>
</head>
<body>
<h3>사원 목록</h3>
<div id="list-top">
	<form method="post" action='list.hr'>
		<ul><li><span style="margin-right: 10px">부서명 </span></li>
		<li>
			<select name="depts" onchange="$('form').submit()">
				<option ${dept_id eq 'all' ? 'selected' : ''} value="all">전체</option>
				<c:forEach items="${depts}" var="vo">
					<option
						${!empty dept_id and dept_id ne 'all' and dept_id eq vo.department_id ? 'selected' : ''}
						value="${vo.department_id}">${vo.department_name} </option>
				</c:forEach>
			</select>
		</li>
		</ul>
	</form>
</div>
	<!--Content  -->
	<div class='tb_wrap'>
	<table>
		<tr><th>사번</th>
			<th>성명</th>
			<th>부서명</th>
			<th>업무명</th>
			<th>급여</th>
		</tr>
		<c:forEach items="${list}" var="vo">
			<tr>
				<th><a href='detail.hr?id=${vo.employee_id}'> ${vo.employee_id} </a></th>
				<th><a href='detail.hr?id=${vo.employee_id}'>${vo.first_name} ${vo.last_name}</a></th>
				<th><a href='detail.hr?id=${vo.employee_id}'>${vo.department_name}</a></th>
				<th><a href='detail.hr?id=${vo.employee_id}'>${vo.job_title}</a></th>
				<th><a href='detail.hr?id=${vo.employee_id}'>${vo.salary}</a></th>
			</tr>
		</c:forEach>
	</table>
	</div>
<script type="text/javascript">
$(function(){
	$('.tb_wrap').css('height', $('#content').height() 
					- $('h3').outerHeight(true) - $('#list-top').outerHeight(true) - 20 );
	
});

$(window).resize(function(){
	$('.tb_wrap').css('height', $('#content').height() 
					- $('h3').outerHeight(true) - $('#list-top').outerHeight(true) - 20 );
});

</script>

</body>
</html>







