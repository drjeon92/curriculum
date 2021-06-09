package com.hanul.iot;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import employee.EmployeeSeviceImpl;
import employee.EmployeeVO;

@Controller
public class EmployeeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private EmployeeSeviceImpl service;

	@RequestMapping("list.hr")
	public String list(Model model, String depts , HttpSession session) {
		session.setAttribute("category", "hr");
		// 부서정보를 select해서 같이 model로 넘기면 됨.
		logger.info("list.cu");
		model.addAttribute("depts", service.employee_department());
		// 부서정보를 전체 조회해서 화면 combobox(option select)에 값을 담아서 보여주기
		// 위한 select

		if (depts == null || depts.equals("all")) {
			model.addAttribute("list", service.employee_list());
			model.addAttribute("dept_id", "all");
		}

		else {
			model.addAttribute("list", service.employee_list(depts));
			model.addAttribute("dept_id", depts);

		}
		//model.addAttribute("dept_id",depts==null || depts.equals("all") ? "all" : depts);
		return "employee/list";

	}

	@RequestMapping("detail.hr")
	public String detail(Model model, int id) {
		// tile 작업이 되었을때는
		// 1번부터 작업
		/*
		 * logger.info(id); 1번방식 CustomerVO vo = service.customer_detail(id);
		 * model.addAttribute("vo" , vo ) ;
		 * 
		 * //2번 방식 CustomerVO vo = service.customer_detail(id); logger.info(vo.getId() +
		 * "DB에서 받은값"); model.addAttribute("vo",vo);
		 */
		model.addAttribute("vo", service.employee_detail(id));
		return "employee/detail";
	}

}
