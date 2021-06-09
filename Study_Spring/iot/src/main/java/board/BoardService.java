package board;

import java.util.List;

public interface BoardService {
	//CRUD: Create, Read, Update, Delete
	int board_insert(BoardVO vo); //글쓰기
	BoardPage board_list(BoardPage page); //페이지조회
	BoardVO board_detail(int id); //상세조회
	int board_read(int id); //조회수 증가처리
	int board_update(BoardVO vo); //수정
	int board_delete(int id); //삭제

	int board_comment_insert(BoardCommentVO vo); //댓글 저장
	int board_comment_update(BoardCommentVO vo);//댓글수정
	int board_comment_delete(int id) ;//댓글 삭제
	List<BoardCommentVO> board_comment_list(int pid); //댓글조회 
}
