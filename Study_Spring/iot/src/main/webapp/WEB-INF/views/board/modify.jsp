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
<h3>방명록 수정</h3>
<form method="post" action='update.bo' enctype="multipart/form-data">
<input type='hidden' name='id' value='${vo.id}'/>
<table>
<tr><th class='w-px120'>제목</th>
	<td><input type='text' name='title' class='chk' title='제목' value='${vo.title}' /></td>
</tr>
<tr><th>작성자</th>
	<td>${vo.name}</td>
</tr>
<tr><th>내용</th>
	<td><textarea class='chk' title='내용' name='content'>${vo.content}</textarea></td>
</tr>
<tr><th>첨부파일</th>
	<td class='left middle'>
		<label>
			<input type='file' name='file' id='attach-file'/>
			<img src='imgs/select.png' class='file-img'/>
		</label>
		<span id='file-name'>${vo.filename}</span>
		<span id='preview'></span>
		<a id='delete-file'><i class='fas fa-times font-img'></i></a>
	</td>
</tr>
</table>
<input type='hidden' name='attach' />
</form>
<div class='btnSet'>
	<a class='btn-fill' 
	onclick="if( emptyCheck() ){ $('[name=attach]').val( $('#file-name').text() );  $('form').submit() }">저장</a>
	<a class='btn-empty' onclick='history.go(-1)'>취소</a>
</div>


<script type="text/javascript" src='js/file_check.js'></script>
<script type="text/javascript">
$(function(){
	if( ${!empty vo.filename} ){
		$('#delete-file').css('display', 'inline');
		
		//첨부파일이 이미지인경우 미리보기되게
		if( isImage('${vo.filename}') ){
			$('#preview').html( "<img src='${vo.filepath}' class='file-img' />" );
		}
	}
	
});

</script>

</body>
</html>





