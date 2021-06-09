<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Insert title here</title>

<style>

.container {

	position: absolute; left:50%;  top:50%;

	width: 400px; height:340px;

	margin-left: -200px; margin-top:-170px;   

}

#login { width:100%; border:1px solid #ccc; }

#userid, #userpw { width:48%; height:25px; 

	padding:5px 10%; margin-bottom:10px  }

.social img { width:254px; height:42px; margin: 5px;}

</style>

</head>

<body>

 

<div class='container'>

	<div style='height:80px'>

		<a href='<c:url value="/"/>'><img src='imgs/hanul.logo.png'/></a>

	</div>

	<div id='login' style="padding:50px 0">

		<form method="post" action=''>

			<input type='text' id='userid' placeholder="아이디" />

			<input type='password' id='userpw' placeholder="비밀번호"

					onkeypress="if( event.keyCode==13 ) go_login()"	 />

			<a onclick='go_login()' class='btn-fill' style='display:block; margin:auto; width:63.5%; height:42px; line-height:42px; box-shadow:none;'>로그인</a>
		</form>
		<hr style='width:69%; margin:25px auto'>
		<a class='social' href='naverlogin'><img src='imgs/naver_login.png' alt='네이버로그인' /></a>
		<a class='social' href='kakaologin'><img src='imgs/kakao_login.png' alt='카카오로그인' /></a>


	</div>

</div>
<script type="text/javascript">
	function go_login() {
		if($('#userid').val()==''){
			alert('아이디를 입력하세요!');
			$('#userid').focus();
		}else if($('#userpw').val()==''){
			alert('비밀번호를 입력하세요!');
			$('#userpw').focus();
		}
		
		$.ajax({
			type: 'post',
			url: 'ailogin',
			data: { id:$('#userid').val(), pw:$('#userpw').val() },
			success: function (response) {
				if(response){
					//목록을 제외한 화면과 회원가입화면은 홈으로 연결하고
					//그외는 해당 화면
					location.href = ( document.referrer.match(/member/g) 
							|| !document.referrer.match(/list/g) )
							? '<c:url value="/"/>' : document.referrer;
				}else{
					alert('아이디나 비밀번호가 틀렸습니다.');
				}	
			},error: function (req , text) {
				alert(text + ':' + req.status );
			}
		});
		
	}
</script>
 



</body>

</html>

 