package com.hanul.iot;

import java.io.File;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import board.BoardCommentVO;
import board.BoardPage;
import board.BoardServiceImpl;
import board.BoardVO;
import common.CommonService;
import member.MemberVO;

@Controller
public class BoardController {
	@Autowired private BoardServiceImpl service;
	@Autowired private BoardPage page;
	@Autowired private CommonService common;
	
	//방명록 신규 글 저장처리 요청
	@RequestMapping("/insert.bo")
	public String insert(BoardVO vo, HttpSession session, MultipartFile file) {
//		MemberVO user = (MemberVO)session.getAttribute("logininfo");
//		vo.setWriter( user.getId() ); 
		vo.setWriter( ((MemberVO)session.getAttribute("logininfo")).getId() );
		
		//첨부파일이 있는 경우
		if( ! file.isEmpty() ) {
			vo.setFilename( file.getOriginalFilename() );
			vo.setFilepath( common.fileUpload(session, file, "board") );
		}
		
		
		//화면에서 입력한 정보를 DB에 저장한 후 목록화면으로 연결
		service.board_insert(vo);
		
		return "redirect:list.bo";
	}
	
	
	//방명록 글쓰기화면 요청
	@RequestMapping("/new.bo")
	public String board() {
		return "board/new";
	}
	
	//첨부파일 다운로드 요청
	@RequestMapping("/download.bo")
	public void download(int id, HttpSession session, HttpServletResponse response) {
		BoardVO vo = service.board_detail(id);
		common.fileDownload( vo.getFilename(), vo.getFilepath(), session, response);
	}
	
	//방명록 수정저장처리 요청
	@RequestMapping("/update.bo")
	public String update(BoardVO vo, Model model
						, MultipartFile file, String attach, HttpSession session) {
		//DB에 관리된 파일관련정보를 조회해 둔다
		BoardVO board = service.board_detail(vo.getId());
		//업로드되어 있는 파일의 정보 조회
		String uuid = session.getServletContext().getRealPath("resources") 
						+ "/" + board.getFilepath(); 
		
		//첨부파일 처리
		
		//파일첨부하는 경우: 원래 없었는데 새로이 첨부, 원래 있었는데 바꿔서 첨부
		if( !file.isEmpty() ) {
			vo.setFilename( file.getOriginalFilename() );
			vo.setFilepath( common.fileUpload(session, file, "board"));
			
			//원래 첨부되어 있던 파일이 있다면 물리적 영역에서 삭제
			if( board.getFilename() != null ) {
				File f = new File( uuid );
				if( f.exists() ) f.delete();
			}
		}else {
		//파일을 첨부하지 않는 경우
		//: 원래 없었고 첨부도 하지 않은 경우, 원래 있었는데 삭제한 경우
		//  , 원래 있었고 해당 파일을 그대로 사용하는 경우 
			if( attach.isEmpty() ) {
				//원래 첨부되어 있던 파일이 있다면 물리적 영역에서 삭제
				if( board.getFilename() != null ) {
					File f = new File( uuid );
					if( f.exists() ) f.delete();
				}
				
			}else {
				vo.setFilename( board.getFilename() );
				vo.setFilepath( board.getFilepath());
			}
		}
		
		
		//화면에서 변경입력한 정보를 DB에 변경저장한 후 상세화면으로 연결
		service.board_update(vo);
		
//		return "redirect:detail.bo?id="+ vo.getId();
		
		model.addAttribute("id", vo.getId());
		model.addAttribute("url", "detail.bo");
		return "board/redirect";
	}
	
	
	//방명록 글 삭제처리 요청
	@RequestMapping("/delete.bo")
	public String delete(int id, HttpSession session, Model model) {
		//첨부파일이 있었다면 물리적영역에서 해당 파일 삭제
		BoardVO vo = service.board_detail(id);
		if( vo.getFilename() != null ) {
			File f = new File( session.getServletContext().getRealPath("resources")
						+ "/" + vo.getFilepath() );
			if( f.exists() ) f.delete();
		}
		
		//해당 글을 DB에서 삭제한 후 목록화면으로 연결
		service.board_delete(id);
		
		model.addAttribute("url", "list.bo");
		model.addAttribute("page", page);
		return "board/redirect";
	}
	
	
	//방명록 댓글 삭제처리 요청
	@ResponseBody @RequestMapping("/board/comment/delete/{id}")
	public void comment_delete(@PathVariable int id) {
		service.board_comment_delete(id);
	}
	
	
	//방명록 댓글 변경저장처리 요청
	@ResponseBody @RequestMapping(value="/board/comment/update"
								, produces = "application/text; charset=utf-8")
	public String comment_update(@RequestBody BoardCommentVO vo, HttpServletResponse response) {
		
// 서블릿으로 응답객체인 화면을 만드는 형태에서
//		try {
//			response.setContentType("text/html; charset=utf-8");
//			PrintWriter out = response.getWriter();
//			out.print("<html><h3>한글</h3>");
//			out.print("</html>");
//		}catch(Exception e) {}
		
		return service.board_comment_update(vo) == 1 ? "댓글 저장 성공^^" : "댓글 저장 실패ㅠㅠ";
	}
	
	
	
	//방명록 댓글 목록 요청
	@RequestMapping("/board/comment/{pid}")
	public String comment_list( @PathVariable int pid, Model model ) {
		//방명록 글에 대한 댓글 목록을 DB에서 조회
		model.addAttribute("list", service.board_comment_list(pid));
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("lf", "\n");
		return "board/comment/comment_list";
	}
	
	//방명록 댓글 저장처리 요청
	@ResponseBody @RequestMapping("/board/comment/insert")
	public boolean comment_regist(BoardCommentVO vo, HttpSession session) {
		MemberVO user = (MemberVO)session.getAttribute("logininfo");
		vo.setWriter( user.getId() );
		//화면에서 입력한 댓글 정보를 DB에 저장
		return service.board_comment_insert(vo) == 1 ? true : false;
	}
	
	
	
	//방명록 수정화면 요청
	@RequestMapping("/modify.bo")
	public String modify(int id, Model model) {
		//해당 글의 정보를 DB에서 조회해와 수정화면에 출력
		model.addAttribute("vo", service.board_detail(id) );
		return "board/modify";
	}
	
	//방명록 상세화면 요청
	@RequestMapping("/detail.bo")
	public String detail(int id, Model model) {
		service.board_read(id); //조회수 증가 처리
		//선택한 글의 정보를 DB에서 조회해와 상세화면에 출력
		model.addAttribute("vo", service.board_detail(id));
		model.addAttribute("crlf", "\r\n");  //줄바꿈
		model.addAttribute("page", page);
		return "board/detail";
	}
	
	
	//방명록 목록화면 요청
	@RequestMapping("/list.bo")
	public String list(HttpSession session
						, String search, String keyword
						, @RequestParam(defaultValue = "list") String viewType
						, @RequestParam(defaultValue = "10") int pageList
						, @RequestParam(defaultValue = "1") int curPage
						, Model model ) {
		session.setAttribute("category", "bo");
		page.setCurPage(curPage);
		page.setKeyword(keyword);
		page.setSearch(search);
		page.setPageList(pageList);
		page.setViewType(viewType);
		
		//DB에서 방명록 정보를 조회해와 목록화면에 출력
		model.addAttribute("page", service.board_list(page));
		return "board/list";
	}
	
}







