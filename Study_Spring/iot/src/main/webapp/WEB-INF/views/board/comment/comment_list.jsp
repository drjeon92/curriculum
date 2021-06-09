<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:forEach items="${list}" var="vo" varStatus="status">
${status.index eq 0 ? '<hr>' : ''}
<div data-id='${vo.id}'>${vo.name} [ ${vo.writedate} ]
	<c:if test="${vo.writer eq logininfo.id}"> 
	<span style='float:right;'>
		<a class='btn-fill-s btn-modify-save'>수정</a>
		<a class='btn-fill-s btn-delete-cancel'>삭제</a>
	</span>
	</c:if>
<%-- 	<div class='original'>${fn: replace( fn:replace(vo.content, crlf, '<br>'), lf, '<br>') }</div> --%>
	<div class='original'>${fn: replace( fn:replace(vo.content, lf, '<br>'), crlf, '<br>') }</div>
	<div class='modify' style='display:none; margin-top:6px;'></div>
</div>
<hr>
</c:forEach>

<script>
$('.btn-modify-save').on('click', function(){
	var $div = $(this).closest('div');
	
	if( $(this).text() == '수정' ){ //수정
	
		$div.children('.modify').css('height', $div.children('.original').height()-6 );
		
		var tag = '<textarea style="width:96%; height:90%; resize:none;">'
				+ $div.children('.original').html().replace(/<br>/g, '\n')
				+ '</textarea>';
		$div.children('.modify').html( tag );
		display( $div, false );
	
	}else{ //저장
		
		$.ajax({
			type: 'post',
			contentType: 'application/json',
			url: 'board/comment/update',
			data : JSON.stringify( { id:$div.data('id') , content: $div.find('textarea').val() } ),
			success: function( response ){
				alert( response );
				comment_list();
				
			},error: function(req, text){
				alert(text+':'+req.status);
			}
		});
	}
});

$('.btn-delete-cancel').on('click', function(){
	var $div = $(this).closest('div');
	if( $(this).text()=='취소' ){ //취소
		display( $div, true );
	}else{ //삭제
		if( confirm('정말 삭제?') ){
			$.ajax({
				url: 'board/comment/delete/' + $div.data('id'),
				success: function(){
					comment_list();
				},error: function(req, text){
					alert(text+':'+req.status);
				}
			});
		}
	}
});

function display( div, mode ){
	//보기상태: 원글 보이게, 수정글 안 보이게, 수정/삭제 버튼
	//수정상태: 원글 안 보이게 수정글 보이게, 저장/취소 버튼
	div.children('.modify').css('display', mode ? 'none' : 'block');
	div.children('.original').css('display', mode ? 'block' : 'none');
	div.find('.btn-modify-save').text(mode ? '수정' : '저장');
	div.find('.btn-delete-cancel').text(mode ? '삭제' : '취소');
}

</script>

<%
// String[] data = {"A", "B", "C", "D"};
// for(String d : data){
// 	System.out.print(d);
// }
// for(int i=0; i<data.length; i++){
// 	System.out.print(data[i]);
// }
%>