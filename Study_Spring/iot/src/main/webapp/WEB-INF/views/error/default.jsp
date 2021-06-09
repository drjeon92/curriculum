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

	<h3>내부적인 오류가 발생했습니다.</h3>
	<p>빠른 시일내에 복구시키도록 하겠습니다.</p>
	<p>관련 문의사항은 개발자 또는 고객센터로 알려주시면 친절히 안내 하겠습니다.</p>
	${msg}
	</div>
</div>