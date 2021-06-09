/**
 * 
 */
 
//background 크기 조정
function resizeBackground(){
	var width = $(window).width() < $('body').prop('scrollWidth') ?
					$('body').prop('scrollWidth') : $(window).width();
	var height = $(window).height() < $('body').prop('scrollHeight') ?
					$('body').prop('scrollHeight') : $(window).height();
	$('#popup-background').animate( { 'width':width, 'height':height }, 0 );
}

//가운데 위치하게 조정
function moveCenter( tag ){
	var left = ( $(window).width() - tag.width() ) / 2 + $(window).scrollLeft();
	var top =  ( $(window).height() - tag.height() ) / 2 + $(window).scrollTop();
	tag.removeClass('center').css('position', 'absolute');
	tag.animate( { 'left':left, 'top':top }, 0 );
}
 
 
//입력값이 있는지 확인
function emptyCheck(){
	var ok = true;
	$('.chk').each( function(){
		if( $(this).val() == ''){
			alert( $(this).attr('title') + ' 를 입력 하세요.');
			 $(this).focus();
			 ok = false;
			 return ok;
		}
	
	});
	return ok;
}; 



