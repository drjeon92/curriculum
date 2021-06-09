package com.hanul.iot;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import customer.CustomerServiceImpl;
import customer.CustomerVO;

@Controller
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired private CustomerServiceImpl service;
	
	@RequestMapping("list.cu")
	public String list(Model model , HttpSession session) {
		session.setAttribute("category", "cu");
		//0.RequestMapping을 통해서 log찍히는지 먼저확인(Console)
		//0.Method -> Void
		//1.Url매핑을 통해서 어떤 페이지가 접속되는지. 테스트
		//1.Method -> String
		
		//2.Mybatis이용해서 String,int ,Vo 데이터가 정상적으로
		//Select 되는지 확인 for logger 
		
		//3.내가 호출한 페이지에서 제대로 데이터를 받았는지 확인
		logger.info("list.cu");
		List<CustomerVO> list =	service.customer_list();
		model.addAttribute("list", list);
		
		return "customer/list";
		
	}
	@RequestMapping("detail.cu")
	public String detail(Model model , String id) {
		//tile 작업이 되었을때는
		//1번부터 작업
		logger.info(id);
		/* 1번방식
		 * CustomerVO vo = service.customer_detail(id); 
		 * model.addAttribute("vo" , vo ) ;
		 */
		//2번 방식
		CustomerVO vo = service.customer_detail(id);
		logger.info(vo.getId() + "DB에서 받은값");
		model.addAttribute("vo",vo);
		return "customer/detail";
	}
	@RequestMapping("new.cu")
	public String cuNew() {
		return "customer/new"; 
	}
	@RequestMapping("insert.cu")
	public String cuInsert(CustomerVO vo) {
		logger.info(vo.getName());
		vo.setId("zzzzz");
		service.customer_insert(vo);
		//인서트 처리
		return "redirect:list.cu";
	}
	@RequestMapping("modify.cu")
	public String cumodify(String id , Model model) {
		CustomerVO vo = service.customer_detail(id);
		logger.info(vo.getGender());
		model.addAttribute("vo",vo);
		//인서트 처리
		return "customer/modify";
	}
	@RequestMapping("update.cu")
	public String cuUpdate(CustomerVO vo) {
		logger.info(vo.getName());
		service.costomer_update(vo);
		//인서트 처리
		return "redirect:list.cu";
	}
	
}
