<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#map { width:800px; height:600px; border:2px solid #666; display:none; }
</style>
</head>
<body>
<h3>공공데이터</h3>

<div class='btnSet dataOption'>
	<a class='btn-fill'>약국조회</a>
	<a class='btn-empty'>유기동물조회</a>
</div>

<div id='list-top'>
	<div>
		<ul class='common'>
			<li><select class='w-px90' id='pageList'>
				<option value='10'>10개씩</option>
				<option value='15'>15개씩</option>
				<option value='20'>20개씩</option>
				<option value='25'>25개씩</option>
				<option value='30'>30개씩</option>
				</select>
			</li>
		</ul>
	</div>
</div>

<div id='data-list'></div>

<div class='btnSet'>
	<div class='page_list'></div>
</div>
<div class='loading center'><img src='imgs/loading.gif'/></div>
<div id='popup-background'></div>
<div id='map' class='center'></div>

<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCsrerDHJrp9Wu09Ij7MUELxCTPiYfxfBI"></script>
<script type="text/javascript">
$('.dataOption a').click(function(){
	$('.dataOption a').removeClass();
	$(this).addClass('btn-fill');
	var idx = $(this).index();
	$('.dataOption a:not(:eq('+ idx +'))').addClass('btn-empty');
	
	if( idx==0 ) pharmacy_list( 1 );
	else         animal_list();
});

function resize(){
	$('html, body').css('height', '100%');
	var headerFooter = $('header').outerHeight(true) +  $('footer').outerHeight(true);
	var content = $('#content').outerHeight(true);
	var component = $('h3').outerHeight(true) + $('.dataOption').outerHeight(true)
				+ $('#list-top').outerHeight(true) 
				+ $('.page_list').closest('.btnSet').outerHeight(true) + 20;
	var total = '100%';
	if( content < $('table.tb_list').outerHeight(true) + component )
		total = $('table.tb_list').outerHeight(true) + component + headerFooter;
	$('html, body').css('height', total);
}

function pharmacy_list( page ){
	$('.loading').css('display', 'block');
	$.ajax({
		url: 'data/pharmacy',
		data: { pageNo: page, rows: pageList },
		success: function( response ){
			console.log( response )
			
			var tag = '<table class="tb_list pharmacy">'
				+ '<thead><tr><th class="w-px200">약국명</th>'
				+ '<th class="w-px140">전화번호</th><th>주소</th></tr></thead>'
				+ '<tbody>';
				
			$(response.item).each(function(){
				tag += '<tr><td><a class="show" data-x='+ this.XPos +' data-y='+ this.YPos +'>'+ this.yadmNm +'</a></td>'
					 + '<td>'+ this.telno +'</td>'
					 + '<td class="left">'+ this.addr +'</td></tr>';				
			})	;
			
			tag += '</tbody></table>';
			$('#data-list').html( tag );
			makePage( response.count, page );
			resize();
			
			$('.loading').css('display', 'none');		
		},error: function(req,text){
			$('.loading').css('display', 'none');		
			alert(text+':'+req.status)
		}
	});
}

$(window).resize(function(){
	resize();
});

var pageList = 10, blockPage = 10;
//페이지정보 만들기
function makePage( totalList, curPage ){
	var totalPage = Math.ceil( totalList / pageList );
	var totalBlock = Math.ceil( totalPage / blockPage );
	var curBlock = Math.ceil( curPage / blockPage );
	var endPage = curBlock * blockPage;
	var beginPage = endPage - ( blockPage - 1 );
	if( endPage > totalPage ) endPage = totalPage;
	
	var tag = '';
	
	if( curBlock > 1 ){
		tag += '<a class="page_first" data-page=1 ><i class="fas fa-angle-double-left"></i></a>'
			+  '<a class="page_prev" data-page='+ (beginPage-blockPage) +'><i class="fas fa-angle-left"></i></a>';
	}
	
	for(var no=beginPage; no<=endPage; no++){
		if( no == curPage ) tag += '<span class="page_on">'+ no +'</span>';
		else                tag += '<a class="page_off" data-page='+ no +'>'+ no +'</a>';
	}
	
	if( curBlock < totalBlock ){
		tag += '<a class="page_next" data-page='+ (endPage+1) +' ><i class="fas fa-angle-right"></i></a>'
			+  '<a class="page_last" data-page='+ totalPage +'><i class="fas fa-angle-double-right"></i></a>';
	}
	
	$('.page_list').html( tag );
}

$(document).on('click', '.page_list a', function(){ //페이지클릭
	pharmacy_list( $(this).data('page') );
	
}).on('change', '#pageList', function(){  //조회목록수 변경
	pageList = $(this).val();
	pharmacy_list( 1 );

}).on('click', '.show', function(){ //약국명 클릭
	$('#map, #popup-background').css('display', 'block');
	
	var xy = { lat: Number($(this).data('y')), lng: Number($(this).data('x')) };
	
	 var id = new google.maps.Map(document.getElementById("map"), {
		    center: xy,
		    zoom: 15,
	  });
	  
// 	  new google.maps.Marker({
// 		 map: id,    position: xy,   title: $(this).text()
// 	  });
	
	var info = new google.maps.InfoWindow();
	info.setOptions({
		content: '<div class="marker">'+ $(this).text() +'</div>'
	});
	info.open(id, new google.maps.Marker({
		map: id, position: xy
	}));
	
}).on('click', '#popup-background', function(){
	$('#map, #popup-background').css('display', 'none');
	
})
;

function animal_list(){
}

$(function(){
	$('.dataOption a:eq(0)').trigger('click'); //버튼 클릭 강제 발생시키기
});

</script>











</body>
</html>