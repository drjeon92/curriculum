$('#attach-file').on('change', function(){
	var attached = this.files[0];
	if( attached ){
		$('#file-name').text(attached.name);
		$('#delete-file').css('display' , 'inline-block');
		
		//미리보기 태그가 있는 경우만
		if( $('#preview').length>0 ){
			if( isImage(attached.name) ){
				$('#preview').html( '<img src="" id="preview-img" />' );
				
				var reader = new FileReader();
				reader.onload = function(e){
				 	$('#preview-img').attr('src', e.target.result);
				}
				reader.readAsDataURL( attached );
			}else{
				$('#preview').html('');
			}
		}
	}
});


$('#delete-file').on('click', function(){
		$('#file-name').text('');
		$('#attach-file').val('');
		$('#delete-file').css('display' , 'none');
		//미리보기 태그가 있는 경우는 미리보기한 이미지태그 없애기
		if( $('#preview').length>0 ) $('#preview').html('');
		
});


//선택한 파일이 이미지파일인지 판단하는 함수
function isImage( filename ){
	//abc.txt, abc.png, abc.JPG
	var ext = filename.substring( filename.lastIndexOf('.')+1 ).toLowerCase() ;
	var imgs = [ 'jpg',  'jpeg',  'gif',  'bmp',  'png' ];
	if( imgs.indexOf(ext) > -1 ) return true;
	else                         return false;

}















