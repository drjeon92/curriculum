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
<h3>방명록 목록</h3>

<div id='list-top'>
<form method="post" action="list.bo">
<div>
	<ul>
		<li><select name="search" class="w-px90">
			<option value="all"  ${page.search eq 'all' ? 'selected' : ''}>전체</option>
			<option value="title" ${page.search eq 'title' ? 'selected' : ''}>제목</option>
			<option value="content" ${page.search eq 'content' ? 'selected' : ''}>내용</option>
			<option value="writer" ${page.search eq 'writer' ? 'selected' : ''}>작성자</option>
			</select>
		</li>
		<li><input type='text' name='keyword' value='${page.keyword}' class='w-px300'/></li>
		<li><a class='btn-fill' onclick="$('form').submit()">검색</a></li>
	</ul>
	
	<ul>
		<li><select name="pageList" class='w-px90' 
					onchange="$('[name=curPage]').val(1); $('form').submit()">
			<option value='10' ${page.pageList eq 10 ? 'selected' : '' }>10개씩</option>
			<option value='15' ${page.pageList eq 15 ? 'selected' : '' }>15개씩</option>
			<option value='20' ${page.pageList eq 20 ? 'selected' : '' }>20개씩</option>
			<option value='25' ${page.pageList eq 25 ? 'selected' : '' }>25개씩</option>
			<option value='30' ${page.pageList eq 30 ? 'selected' : '' }>30개씩</option>
			</select>
		</li>
		<li><select name='viewType' class='w-px110' onchange="$('form').submit()">
			<option value='list' ${page.viewType eq 'list' ? 'selected' : ''}>리스트형태</option>
			<option value='grid' ${page.viewType eq 'grid' ? 'selected' : ''}>그리드형태</option>
			</select>
		</li>
		<!-- 로그인 된 경우만 글쓰기 가능 -->
		<c:if test="${!empty logininfo }">
		<li><a class='btn-fill' href='new.bo'>글쓰기</a></li>
		</c:if>
	</ul>
</div>
<input type='hidden' name='curPage' value="1"/>
<input type='hidden' name='id' />
</form>
</div>

<div class='tb_wrap'>
<c:if test="${page.viewType eq 'list' }">
<table class='tb_list'>
<tr><th class='w-px70'>번호</th><th>제목</th>
	<th class='w-px100'>작성자</th>
	<th class='w-px100'>작성일자</th>
	<th class='w-px60'>첨부파일</th>
</tr>
<c:forEach items="${page.list}" var="vo">
<tr><td>${vo.no}</td>
	<td class='left'><a onclick="go_detail(${vo.id})">${vo.title}</a></td>
	<td>${vo.name}</td>
	<td>${vo.writedate}</td>
	<td>${empty vo.filename ? '' : '<img src="imgs/attach.png" class="file-img" />'}</td>
</tr>
</c:forEach>
</table>
</c:if>

<c:if test="${page.viewType eq 'grid'}">
<ul class='grid'>
	<c:forEach items="${page.list}" var="vo">
	<li><div><a onclick="go_detail(${vo.id})">${vo.title}</a></div>
		<div>${vo.name}</div>
		<div>${vo.writedate}<span style="float:right;">${empty vo.filename ? '' : '<img src="imgs/attach.png" class="file-img" />' }</span></div>
	</li>
	</c:forEach>
</ul>
</c:if>

</div>

<div class='btnSet'>
	<jsp:include page="/WEB-INF/views/include/page.jsp"/>
</div>
<script type="text/javascript">
function go_detail(id){
	$('[name=id]').val(id);
	$('form').attr('action', 'detail.bo');
	$('form').submit();
}
function resize(){
	/*
	테이블 자체에 스크롤을 주는 경우
	$('.tb_wrap').css('height', $('#content').height() - $('h3').outerHeight(true)
				- $('#list-top').outerHeight(true) 
				- $('.page_list').closest('.btnSet').outerHeight(true) - 20);
	*/
	//브라우저에 스크롤을 주는 경우
	$('html, body').css('height', '100%');
	var headerFooter = $('header').outerHeight(true) + $('footer').outerHeight(true);
	var content = $('#content').outerHeight(true);
	var component = $('h3').outerHeight(true) + $('#list-top').outerHeight(true)
					+ $('.btnSet').outerHeight(true) + 40;
	
	var total = '100%';
	if( $('table.tb_list').length>0 && content < $('table.tb_list').outerHeight(true) + component  ){
		total = $('table.tb_list').outerHeight(true) + component + headerFooter;
		console.log('table')
	}else if( $('ul.grid').length>0 && content < $('ul.grid').outerHeight(true) + component ){
		console.log('grid')
		total = $('ul.grid').outerHeight(true) + component + headerFooter;
	}
	$('html, body').css('height', total);
}
$(function(){
	if( $('ul.grid').length>0 ){
		$('ul.grid').css('height', 
					Math.ceil($('.grid li').length/5) * $('.grid li').outerHeight(true)	- 20 );	
	}
	
	resize();
});
$(window).resize(function(){
	resize();
});
</script>
</body>
</html>






