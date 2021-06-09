<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>공지글 수정</h3>
	<!--첨부파일을 전송할때는 반드시 post enctype = multipart/form-data  -->
	<form action="update.no" method="post" enctype="multipart/form-data">
		<table>
			<tr><th class="w-px160">제목</th>
				<td><input type="text" name='title' title='제목' class='chk' value="${vo.title}"></td>
			</tr>
			
			<tr><th class="w-px160">작성자</th>
				<td>${logininfo.name}</td>
			</tr>
			
			<tr><th class="w-px160">내용</th>
				<td><textarea name='content' title='내용' class="chk" >${vo.content }</textarea> </td>
			</tr>
			
			<tr><th class="w-px160">첨부파일</th>
				<td class="left">
					<label>
						<input type="file" name='file' id='attach-file' />
						<img alt="x" src="imgs/select.png" class="file-img" />
					</label>
					<span id='file-name'>${vo.filename }</span>
					<span id='delete-file'><i class="fas fa-times"> </i></span>
				</td>
			</tr>
			<input type="hidden" name='id' value="${vo.id}">
			<input type="hidden" name='filename' value="${vo.filename}">
		</table>
	</form>
	<div class="btnset">					<!-- //boolean -->
		<a class="btn-fill" onclick=" if( emptyCheck() ){ $('[name=filename]').val($('#file-name').text()) ;   $('form').submit(); }" >저장 </a>
		<a class="btn-empty" href="list.no" >취소 </a>
		
	</div>
	<script type="text/javascript" src="js/file_check.js">


	</script>
	<script type="text/javascript">
	if( ${!empty vo.filename}){
		$('#delete-file').css('display' , 'inline');
	}
	if( emptyCheck() ){
		$('[name=filename]').val(
				$('#file-name').text()
				)
	}
		
	</script>
</body>
</html>