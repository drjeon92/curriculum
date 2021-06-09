package notice;

import java.util.List;

public interface NoticeService {
	
	void notice_insert(NoticeVO vo); //신규공지글 저장
	void notice_reply_insert(NoticeVO vo); //신규 답글 저장
	List<NoticeVO> notice_list();//공지글목록조회
	NoticeVO notice_view(int id); //공지글 상세조회
	void notice_read(int id); //조회수 증가 처리
	void notice_update(NoticeVO vo); //공지글 수정(변경저장)
	void notice_delete(int id); //공지글 삭제
	//페이징 처리하는 notice_list 추가 예정
	NoticePage notice_list(NoticePage page);
}
