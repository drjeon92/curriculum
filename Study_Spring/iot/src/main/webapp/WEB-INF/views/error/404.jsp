<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
	#error{
		position: absolute; left: 50%; top:50%;
		width: 800px ; margin: 0 auto;
		transform:translate(-50% , -50%);
	}
	footer{
	visibility: hidden;
	}
	header{
	visibility: hidden;
	}
</style>
<div id="error">
	<div class="left" style="height: 80px">
	<a href='<c:url value="/" />'><img alt="x" src="imgs/hanul.logo.png"> </a>
	</div><hr>
	
	<div class="left">

	<h3>요청하신 페이지는 찾을 수 없습니다.</h3>
	<p>방문하시려는 페이지의 주소가 잘못 되었거나</p>
	<p>페이지의 주소가 변경 혹은 삭제되어 요청하신 페이지를 찾을 수 없습니다.</p>
	<p>입력하신 주소가 정확한지 다시 한 번 확인해 주시기 바랍니다.</p>
	<p>잘못된 접근</p>
	</div>
</div>