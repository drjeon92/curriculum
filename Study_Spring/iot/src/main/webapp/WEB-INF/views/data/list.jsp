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
.list-view, .grid-view { color:#3367d6;  padding:0 6px; border:1px solid #b0b0b0; }
.common  a.on { color:#3367d6; }
.common  a.off { color:#b0b0b0; }
ul.pharmacy li div:first-child { height:25px; }
ul.pharmacy li div:last-child { font-size:14px; }
table.tb_list img { width:100px; height:100px; }

ul.animal img { width:100%; height:100%; }
ul.animal li div.li-top { height:65%; }
ul.animal li div.li-top > div { float:left; padding:0; height:100%; }
ul.animal li div.li-top > div:first-child { width:40%; }
ul.animal li > div:nth-child(2) { font-size:14px; }
.spec { width:60% }
.spec div { height:25% !important; padding:0 0 0 10px !important; }
.spec div:nth-child(3) { white-space:nowrap; overflow:hidden; text-overflow:ellipsis; }  

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
			<li class='list-view'><a class='on'><i class="fas fa-list font-img"></i></a></li>
			<li class='grid-view'><a class='off'><i class="fas fa-th font-img"></i></a></li>
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
	else         animal_list( 1 );
});

function resize(){
	$('html, body').css('height', '100%');
	var headerFooter = $('header').outerHeight(true) +  $('footer').outerHeight(true);
	var content = $('#content').outerHeight(true);
	var component = $('h3').outerHeight(true) + $('.dataOption').outerHeight(true)
				+ $('#list-top').outerHeight(true) 
				+ $('.page_list').closest('.btnSet').outerHeight(true) + 20;
	var total = '100%';
	if( content < ( $('table.tb_list').length>0 && $('table.tb_list').outerHeight(true) + component ) )
		total = $('table.tb_list').outerHeight(true) + component + headerFooter;
	else if( content < ( $('ul.grid').length>0 && $('ul.grid').outerHeight(true) + component ) )
		total = $('ul.grid').outerHeight(true) + component + headerFooter;
		
		
	$('html, body').css('height', total);
	resizeBackground();
	moveCenter( $('.loading') );
}

$(window).scroll(function(){
	moveCenter( $('#map') );
	moveCenter( $('.loading') );
});



function pharmacy_list( page ){
	$('.loading').css('display', 'block');
	$.ajax({
		url: 'data/pharmacy',
		data: { pageNo: page, rows: pageList },
		success: function( response ){
			console.log( response )
			
			if( viewType=='list' ) pharmacy_list_view( $(response.item), true );
			else                   pharmacy_grid_view( $(response.item), true );
			/*
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
			*/
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
	moveCenter( $('#map') );
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
	if( $('.pharmacy').length>0 ) pharmacy_list( $(this).data('page') );
	else if( $('.animal').length>0 ) animal_list( $(this).data('page') );
	
}).on('change', '#pageList', function(){  //조회목록수 변경
	pageList = $(this).val();
	if( $('.pharmacy').length>0 ) pharmacy_list( 1 );
	else if( $('.animal').length>0 ) animal_list( 1 );

}).on('click', '.show', function(){ //약국명 클릭
	$('#map, #popup-background').css('display', 'block');
	
	var xy = { lat: Number($(this).data('y')), lng: Number($(this).data('x')) };
	
	 var id = new google.maps.Map(document.getElementById("map"), {
		    center: xy,
		    zoom: 15,
	  });
  
//   new google.maps.Marker({
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
	
}).on('click', '.grid-view', function(){ //리스트뷰 화면 -> 그리드뷰 화면
	if( viewType=='list' ){
		viewType='grid';
		$(this).children('a').removeClass().addClass('on');
		$('.list-view').children('a').removeClass().addClass('off');
		if( $('.pharmacy').length>0 ) pharmacy_grid_view( $('.pharmacy tbody tr'), false );
		else if( $('.animal').length>0 ) animal_grid_view( $('.animal tbody tr') ); 
	}
	
}).on('click', '.list-view', function(){ //그리드뷰 화면 -> 리스트뷰 화면
	if( viewType=='grid' ){
		viewType='list';
		$(this).children('a').removeClass().addClass('on');
		$('.grid-view').children('a').removeClass().addClass('off');
		if( $('.pharmacy').length>0 ) pharmacy_list_view( $('.pharmacy li'), false );
		else if( $('.animal').length>0 ) animal_list_view( $('.animal li'));
	}
	
}).on('change', '#sido', function(){ //시도변경선택
	animal_sigungu(); //시군구 조회
	animal_list( 1 );
	
}).on('change', '#upkind', function(){ //축종변경선택
	animal_kind();  //품종 조회
	animal_list( 1 );
	
}).on('change', '#kind', function(){ //품종변경선택
	animal_list( 1 );
	
}).on('change', '#sigungu', function(){ //시군구변경선택
	animal_shelter(); //보호소 조회
	animal_list( 1 );
	
})
;

function animal_shelter(){ //시군구에 따른 보호소 조회
	$('#shelter').remove();
	if( $('#sigungu').val()==''  ) return; //시군구를 선택하지 않은 경우 보호소 조회하지 않음
	
	$.ajax({
		url: '',
		data: {},
		success: function( response ){
			var tag = '<li><select class="w-px200" id="shelter">'
					+ '<option value="">보호소선택</option>';
			
			$(response.item).each(function(){
				tag += '<option value="'+ this.careRegNo +'">'+ this.careNm +'</option>';
			});
					
			tag += '</select></li>';
			$('#sigungu').closest('li').after( tag );
			
		},error: function(req, text){
			alert(text+':'+req.status);
		}
	});
}

function animal_sigungu(){ //시도에 따른 시군구 조회
	$('#sigungu').remove();
	if( $('#sido').val()=='' ) return; //시도를 선택하지 않은 경우 시군구 조회하지 않음
	
	$.ajax({
		url: 'data/animal/sigungu',
		data: { sido:$('#sido').val() },
		success: function( response ){
			console.log('시군구', response)
			if( response.item ){
				var tag = '<li><select class="w-px120" id="sigungu">'
							+ '<option value="">시군구선택</option>';
				$(response.item).each(function(){
					tag += '<option value="'+ this.orgCd +'">'+ this.orgdownNm +'</option>';
				});
				tag += '</select></li>';
				$('#sido').closest('li').after( tag );
			}
		},error: function(req, text){
			alert(text+':'+req.status);
		}
	});
	
}

function animal_kind(){  //축종에 따른 품종 조회
	$('#kind').remove();
	if( $('#upkind').val()=='' ) return; //축종을 선택하지 않은 경우 품종 조회하지 않음
	
	$.ajax({
		url: 'data/animal/kind',
		data: { upkind:$('#upkind').val() },
		success: function( response ){
			console.log( response )
			var tag = '<li><select class="w-px200" id="kind">'
					+ '<option value="">품종선택</option>';
			$(response.item).each(function(){
				tag += '<option value="'+ this.kindCd +'">'+ this.KNm +'</option>'				
			});
			tag += '</select></li>';
			$('#upkind').closest('li').after( tag );
			
		},error: function(req, text){
			alert(text+':'+req.status);			
		}
	});
}

function animal_list_view( item ){
	var tag = '<table class="tb_list animal">';
	
	if( $('#count').val()==0 ){
		tag += '<thead><tr><td>해당 유기동물이 없습니다.</td></tr></thead>';
		
	}else{
	
		tag += '<tbody>';	
		item.each(function(){
			tag +=	"<tr><td class='w-px100' rowspan='3'><img src='"+ $(this).data('filename') +"'/></td>"
				+	"	<th class='w-px80'>성별</th><td class='w-px60'>"+ $(this).data('sexcd') +"</td>"
				+	"	<th class='w-px40'>나이</th><td class='w-px100'>"+ $(this).data('age') +"</td>"
				+	"	<th class='w-px40'>체중</th><td class='w-px80'>"+ $(this).data('weight') +"</td>"
				+	"	<th class='w-px40'>색상</th><td>"+ $(this).data('colorcd') +"</td>"
				+	"	<th class='w-px60'>접수일자</th><td class='w-px100'>"+ $(this).data('happendt') +"</td>"
				+	"</tr>"
				+	"<tr><th>특징</th>"
				+	"	<td colspan='9' class='left'>"+ $(this).data('specialmark') +"</td>"
				+	"</tr>"
				+	"<tr><th>발견장소</th>"
				+	"	<td colspan='8' class='left'>"+ $(this).data('happenplace') +"</td>"
				+	"	<td>"+ $(this).data('processstate') +"</td>"
				+	"</tr>"
				+	"<tr><td colspan='2'>"+ $(this).data('carenm') +"</td>"
				+	"	<td colspan='7' class='left'>"+ $(this).data('careaddr') +"</td>"
				+	"	<td colspan='2'>"+ $(this).data('caretel') +"</td>"
				+	"</tr>"
		});
		tag += '</tbody>';
	}	
	tag += '</table>';
	
	$('#data-list').html( tag );
	resize();
}
function animal_grid_view( item ){
	var tag = '<ul class="grid animal">';
	
	if( item.length==0 ){
		tag += "<li style='width:100%; height:39px; padding-top:6px;'>해당 유기동물이 없습니다.</li>";
		
	}else{
	
		var animal;
		item.each(function(idx){
			//0,4,8,..
			idx = idx % 4;
			var $td = $(this).children('td');
			if( idx==0 ){
				animal = {};
				animal.filename = $(this).find('img').attr('src');
				animal.sexcd = $td.eq(1).text();
				animal.age = $td.eq(2).text();
				animal.weight = $td.eq(3).text();
				animal.colorcd = $td.eq(4).text();
				animal.happendt = $td.eq(5).text();
			}else if( idx==1 ){
				animal.specialmark = $td.eq(0).text();
			}else if( idx==2 ){
				animal.happenplace = $td.eq(0).text();
				animal.processstate = $td.eq(1).text();
			}else if( idx==3 ){
				animal.carenm = $td.eq(0).text();
				animal.careaddr = $td.eq(1).text();
				animal.caretel = $td.eq(2).text();
				
				tag += '<li '
					+  ' data-filename="'+ animal.filename +'" '
					+  ' data-age="'+ animal.age +'" '
					+  ' data-sexcd="'+ animal.sexcd +'" '
					+  ' data-weight="'+ animal.weight +'" '
					+  ' data-colorcd="'+ animal.colorcd +'" '
					+  ' data-happendt="'+ animal.happendt +'" '
					+  ' data-specialmark="'+ animal.specialmark +'" '
					+  ' data-happenplace="'+ animal.happenplace +'" '
					+  ' data-processstate="'+ animal.processstate +'" '
					+  ' data-carenm="'+ animal.carenm +'" '
					+  ' data-careaddr="'+ animal.careaddr +'" '
					+  ' data-caretel="'+ animal.caretel +'" '
					+  '>' 
				 	+  '	<div class="li-top">'
					+  '	<div><img src="'+ animal.filename +'"/></div>'
					+  '		<div class="spec">'
					+  '			<div><span>'+ animal.age +'</span>'
					+  '				<span style="float:right;">'+ animal.sexcd +'</span>'
					+  '			</div>'
					+  '			<div>'+ animal.weight +'</div>'
					+  '			<div>'+ animal.colorcd +'</div>'
					+  '			<div>'+ animal.processstate +'</div>'
					+  '		</div>'
					+  '	</div>'
					+  '	<div><span>'+ animal.carenm +'</span>'
					+  '		<span style="float:right;">'+ animal.happendt +'</span>'
					+  '	</div>'
					+ '</li>';
			}
		});
	}
	tag += '</ul>';
	if( item.length==0 ) tag += "<input type='hidden' id='count' value='0'/>";
	
	$('#data-list').html( tag );
	resize();
}


//그리드뷰 화면 -> 리스트뷰 
function pharmacy_list_view( item, requestAPI ){
	var tag = '<table class="tb_list pharmacy">'
		+ '<thead><tr><th class="w-px200">약국명</th>'
		+ '<th class="w-px140">전화번호</th><th>주소</th></tr></thead>'
		+ '<tbody>';
		
	if( requestAPI ){  //API요청하는 경우
		item.each(function(){
			tag += '<tr><td><a class="show" data-x='+ this.XPos +' data-y='+ this.YPos +'>'
					+ this.yadmNm +'</a></td>'
				 + '<td>'+ this.telno +'</td>'
				 + '<td class="left">'+ this.addr +'</td></tr>';				
		})	;
		
	}else{ //화면만 바꾸는 형태
			
		item.each(function(){
			var $div = $(this).children('div'), $a = $(this).find('.show');
			tag += '<tr><td><a class="show" data-x='+ $a.data('x') +' data-y='+ $a.data('y') +'>'
					+ $div.eq(0).text() +'</a></td>'
				 + '<td>'+ $div.eq(1).text() +'</td>'
				 + '<td class="left">'+ $div.eq(2).text() +'</td></tr>';				
		})	;
	}
	
	tag += '</tbody></table>';
	$('#data-list').html( tag );
	resize();
}


//리스트뷰 --> 그리드뷰로 바꾸는 형태
function pharmacy_grid_view( item, requestAPI ){
	var tag = '<ul class="grid pharmacy">';
	
	if( requestAPI ){
		item.each(function(idx){
			tag += '<li '+ ( idx<5 ? 'class="first-row"' : '' ) + '>'
				+  '<div><a class="show" data-x='+ this.XPos +' data-y='+ this.YPos +' >'
						  + this.yadmNm +'</a></div>'
				+  '<div>'+ this.telno +'</div>'
				+  '<div>'+ this.addr +'</div>'
				+  '</li>';
		});
		
	}else{
		item.each(function(idx){
			var $td = $(this).children('td'), $a = $(this).find('.show');
			tag += '<li '+ ( idx<5 ? 'class="first-row"' : '' ) + '>'
				+  '<div><a class="show" data-x='+ $a.data('x') +' data-y='+ $a.data('y') +' >'
						  + $td.eq(0).text() +'</a></div>'
				+  '<div>'+ $td.eq(1).text() +'</div>'
				+  '<div>'+ $td.eq(2).text() +'</div>'
				+  '</li>';
		});
	}
	
	tag += '</ul>';
	$('#data-list').html( tag );
	resize();
}


var viewType = 'list';

function animal_sido(){
	$.ajax({
		url: 'data/animal/sido',
		success: function( response ){
			console.log( response )
			var tag = '<ul class="animal-top">'
					+ '<li><select class="w-px120" id="sido">'
					+ '    <option value="">시도선택</option>';

			$(response.item).each(function(){
				tag += '    <option value="'+ this.orgCd+'">'+ this.orgdownNm +'</option>';
			})		
			
			tag += '</select></li></ul>';
			$('#list-top div').prepend( tag );
			animal_type();
			
		},error: function(req, text){
			alert(text+':'+req.status);
		}
	});
}

function animal_type(){ //축종
// 	축종코드
// 	 - 개 : 417000
// 	 - 고양이 : 422400
// 	 - 기타 : 429900
	var tag = '<li><select class="w-px100" id="upkind">'
			+ '<option value="">축종선택</option>'
			+ '<option value="417000">개</option>'
			+ '<option value="422400">고양이</option>'
			+ '<option value="429900">기타</option>'
			+ '</select></li>';
	$('#sido').closest('li').after( tag );
}

function animal_list( page ){ //유기동물조회
	if( $('#sido').length==0 ) animal_sido();
	
	$('.loading').css('display', 'block');
	var animal = new Object();
	animal.pageNo = page;
	animal.rows = pageList;
	animal.viewType = viewType;
	animal.sido = $('#sido').length>0 ? $('#sido').val() : ''; //시도 
	animal.upkind = $('#upkind').length>0 ? $('#upkind').val() : ''; //축종
	animal.kind = $('#kind').length>0 ? $('#kind').val() : ''; //품종
	animal.sigungu = $('#sigungu').length>0 ? $('#sigungu').val() : ''; //시군구
	
	$.ajax({
		url: 'data/animal/list',
		data: JSON.stringify( animal ),
		type: 'post',
		contentType: 'application/json',
		success: function( response ){
			$('#data-list').html( response );
			makePage( $('#count').val(), page );
			resize();
			$('.loading').css('display', 'none');
			
		},error: function(req, text){
			$('.loading').css('display', 'none');
			alert(text+':'+req.status);
		}
	});
}

$(function(){
	$('.dataOption a:eq(1)').trigger('click'); //버튼 클릭 강제 발생시키기
});

</script>











</body>
</html>