<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#popup { width:350px; height:350px; border:2px solid #666; display:none; }
.popup { width:100%; height:100%; }
#comment_regist span { width:50%; float:left; }
</style>
</head>
<body>
<h3>방명록 안내</h3>
<table>
<tr><th class='w-px120'>제목</th>
	<td colspan='5' class='left' style="word-break:break-all; ">${vo.title}</td>
</tr>
<tr><th>작성자</th>
	<td>${vo.name}</td>
	<th class='w-px100'>작성일자</th>
	<td class='w-px100'>${vo.writedate}</td>
	<th class='w-px80'>조회수</th>
	<td class='w-px80'>${vo.readcnt}</td>
</tr>
<tr><th>내용</th>
	<td colspan='5' class='left'>${fn: replace(vo.content, crlf, '<br>')}</td>
</tr>
<tr><th>첨부파일</th>
	<td colspan='5' class='left'>${vo.filename}
		<c:if test="${!empty vo.filename}">
		<a id='preview'></a>
		<a href='download.bo?id=${vo.id}'><i class='fas fa-download font-img'></i></a>
		</c:if>
	</td>
</tr>
</table>
<div class='btnSet'>
	<a class='btn-fill' onclick="$('form').submit()">목록으로</a>
	<!-- 로그인한 사용자가 작성한 글에 대해서만 수정/삭제 권한 있음 -->
	<c:if test="${logininfo.id eq vo.writer}">
	<a class='btn-fill' 
		onclick="$('form').attr('action', 'modify.bo');   $('form').submit()">수정</a>
	<a class='btn-fill' 
		onclick="if( confirm('정말 삭제?') ){ href='delete.bo?id=${vo.id}' } ">삭제</a>
	</c:if>
	
</div>
<div id='popup-background'></div>
<div id='popup' class='center'></div>

<form method='post' action='list.bo'>
<input type='hidden' name='id' value='${vo.id}' />
<input type='hidden' name='curPage' value='${page.curPage}' />
<input type='hidden' name='search' value='${page.search}' />
<input type='hidden' name='keyword' value='${page.keyword}' />
<input type='hidden' name='pageList' value='${page.pageList}' />
<input type='hidden' name='viewType' value='${page.viewType}' />
</form>

<div style='margin:0 auto; padding-top:20px; width:500px;' class='comment'>
	<div id='comment_regist'>
		<span class='left'><strong>댓글작성</strong></span>
		<span class='right'><a class='btn-fill-s' onclick="comment_regist()">댓글등록</a></span>
		<textarea id='comment' style='width:96%; height:60px; margin-top:5px; resize:none; '></textarea>
	</div>
	<div id='comment_list' class='left'></div>
</div>


<script type="text/javascript" src="js/file_check.js"></script>
<script type="text/javascript">
function comment_regist(){
	if( ${ empty logininfo } ){
		alert('댓글을 등록하려면 로그인하세요!');
		return ;
	}else if( $.trim($('#comment').val())=='' ){
		alert('댓글을 입력하세요!');
		$('#comment').focus();
		$('#comment').val('');
		return;
	}
	
	$.ajax({
		url: 'board/comment/insert',
		data: { content:$('#comment').val(), pid:${vo.id} },
		success: function( response ){
			if( response ){
				alert('댓글이 저장되었습니다!');
				$('#comment').val('');
				comment_list();
			}else
				alert('댓글 저장 실패 ㅠㅠ');
			
		},error: function(req, text){
			alert(text+':'+req.status);
		}
	});
}


function comment_list(){
	$.ajax({
		url: 'board/comment/${vo.id}',
		success: function( response ){
			$('#comment_list').html( response );
			resize();
		},error: function(req, text){
			alert(text+':'+req.status);
		}
	});
}

function resize(){
	$('html, body').css('height', '100%');
	var headerFooter = $('header').outerHeight(true) + $('footer').outerHeight(true);
	var content = $('#content').outerHeight(true);
	var component = $('h3').outerHeight(true) + $('.btnSet').outerHeight(true)
					+ $('.comment').outerHeight(true)
					+ $('table').outerHeight(true) + 40;
	var height = '100%';
	if( content < component ) height = component + headerFooter;
	$('html, body').css('height', height);
	resizeBackground();
}

$(function(){
	comment_list();  //댓글목록 조회해오기
	
	//첨부된 파일이 있고 그 파일이 이미지파일인 경우 미리보기되게
	if( ${!empty vo.filename} ){
		if( isImage( '${vo.filename}' ) ){
			$('#preview').html( '<img src="${vo.filepath}" id="preview-img" class="file-img"/>' );
		}
	}
// 	resize();
});

$(window).resize(function(){
	resize();
	moveCenter( $('#popup') );
});

$(window).scroll(function(){
	moveCenter( $('#popup') );
});

$(document).on('click', '#preview-img', function(){
	$('#popup, #popup-background').css('display', 'block');
	$('#popup').html( "<img src='${vo.filepath}' class='popup' />" );
	
}).on('click', '#popup-background', function(){
	$('#popup, #popup-background').css('display', 'none');
	
});
</script>
</body>
</html>











