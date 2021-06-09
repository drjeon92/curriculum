package com.hanul.iot;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import common.CommonService;
import member.MemberVO;
import notice.NoticePage;
import notice.NoticeServiceImpl;
import notice.NoticeVO;

@Controller
public class NoticeController {
	@Autowired private NoticeServiceImpl service;
	@Autowired private NoticePage page;
	@Autowired private CommonService common;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	//controller에서 http 응답처리를 하겠다 .responsebody
	@ResponseBody @RequestMapping("download.no")
	public void download(int id , HttpSession session , HttpServletResponse response) {
		//해당 공지글에 파일이 있다면 서버로부터 다운로드 처리를 한다.
		NoticeVO vo = service.notice_view(id);
		common.fileDownload(vo.getFilename(), vo.getFilepath(), session, response);
		
	}
	
	
	@RequestMapping("list.no")
	public String list(Model model , HttpSession session
			, @RequestParam(defaultValue = "1") int curPage,
			String search , String keyword) {
		session.setAttribute("category", "no");
		
		
		page.setCurPage(curPage);
		page.setSearch(search);
		page.setKeyword(keyword);
		model.addAttribute("page", service.notice_list(page));
		/* 과거 전체리스트 그냥 조회용 (테스트)
		 * List<NoticeVO> list = service.notice_list(); model.addAttribute("list",
		 * list);
		 */
		
		return "notice/list";
	}
	@RequestMapping("view.no")
	public String view(int id , Model model) {
		//조회수 증가처리 추가 예정
		//update notice set readcnt = readcnt +1 where ?
		service.notice_read(id);
		
		
		NoticeVO vo = service.notice_view(id);
		model.addAttribute("vo", vo  );
		model.addAttribute("crlf", "\r\n");
		model.addAttribute("page" , page);
		return "notice/view";
	}
	@RequestMapping("new.no")
	public String newWriter() {
		
		return "notice/new";
	}
	@RequestMapping("modify.no")
	public String modify(int id, Model model) {
	
		model.addAttribute("vo", service.notice_view(id) );
		return "notice/modify";
	}
	@RequestMapping("reply.no")
	public String reply(int id, Model model) {
		
		model.addAttribute("vo", service.notice_view(id) );
		return "notice/reply";
	}
	@RequestMapping("update.no")
	public String update(NoticeVO vo, MultipartFile file, String filename , HttpSession session) {
		NoticeVO notice = service.notice_view(vo.getId());
		//원본 파일 (변경처리 되기전 ) 위치를 알아옴
		String realFile = session.getServletContext().getRealPath("resources") + "/" + notice.getFilepath();
		
		if( !file.isEmpty()) {
			//파일을 첨부한 경우: 원래 없ㄷ었는데 신규 첨부, 원래 있었던거 바꿔서 첨부
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath(  common.fileUpload(session, file, "notice"));
			//원래 첨부된 파일이 있다면 물리적영역에서 파일을 삭제
			if(notice.getFilename() != null) {
				File f = new File(realFile);
				if (f.exists() ) f.delete();
			}
		}else {
			//파일을 첨부하지 않은 경우
			if(filename.isEmpty()) {
				//원래 첨부된 파일을 삭제한 경우
				if(notice.getFilename() != null) {
					File f = new File(realFile);
					if (f.exists()) f.delete();
				}
			}else {
				//원래 첨부된 파일을 손대지 않은경우
				vo.setFilename(notice.getFilename());
				vo.setFilepath(notice.getFilepath());
			}
		}
		
		service.notice_update(vo);
		//화면에서 변경입력한 정보들이 정확히 들어왔는지 확인을 위해서 다시 veiw.no로 간다.
		return "redirect:view.no?id=" + vo.getId();
	}
	@RequestMapping("delete.no")
	public String delete(int id) {
		//------프로젝트 참고
		//실제 데이터는 삭제 하는 경우가 거의 없음
		//기존테이블 -> 이력테이블 insert -> delete
		//기존테이블의 구분자를 둬서 사용자에게 안보이게끔 처리 update 보이는데이터 Y -> 삭제된 데이터 N
		//관리자는 삭제 된 데이터들을 조회해서 볼수 있음.
		//-------------------------delete from table_name where 1=1
		service.notice_delete(id);
		return "redirect:list.no";
	}

	/* reply_insert.no */
	
	@RequestMapping("reply_insert.no")
	public String reply_insert(NoticeVO vo  ,  MultipartFile file , HttpSession session) {
		MemberVO member = (MemberVO)session.getAttribute("logininfo");
		vo.setWriter(member.getId());

		//화면에서 입력한 답글정보를 DB에 저장 후 목록화면으로 연결하겠다.
		
		if(! file.isEmpty() ) {
			logger.info("파일이 있음");
			logger.info(file.getOriginalFilename());
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath( common.fileUpload(session, file, "notice"));
		}else {
			logger.info("파일이 없음");
		}
		logger.info("디버깅 포인트");
		
		service.notice_reply_insert(vo);
		
		return "redirect:list.no";
	}
	
	@RequestMapping("insert.no")
	public String insert(NoticeVO vo  ,  MultipartFile file , HttpSession session) {
		MemberVO member = (MemberVO)session.getAttribute("logininfo");
		vo.setWriter(member.getId());
		//세션에 있는 정보는 만약에 정보가 있다면 언제든 session.getAtteribute를 통해 사용가능
		if(! file.isEmpty() ) {
			logger.info("파일이 있음");
			logger.info(file.getOriginalFilename());
			vo.setFilename(file.getOriginalFilename());
			vo.setFilepath( common.fileUpload(session, file, "notice"));
		}else {
			logger.info("파일이 없음");
		}
		logger.info("디버깅 포인트");
		
		service.notice_insert(vo);
		
		return "redirect:list.no";
	}
}
