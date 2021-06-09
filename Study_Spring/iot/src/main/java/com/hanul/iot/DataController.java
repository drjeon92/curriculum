package com.hanul.iot;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.CommonService;

@Controller
public class DataController {
	@Autowired private CommonService common;
	
	private String key 
		= "FPgj2NXbJw46TcGkmAfZEiYFDbxilys7KLjk3KaB7AfeJE00ZhPNM0M8unwbsI69fSmT8SNfVEimE6ZZ2U14hA%3D%3D"; 	
	
	//약국정보조회 요청
	@ResponseBody @RequestMapping("/data/pharmacy")
//	public Map<String, Object> pharmacy() {
	public Object pharmacy(int pageNo, int rows) {
		StringBuffer url = new StringBuffer("http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList");
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
		url.append("&pageNo=").append(pageNo);
		url.append("&numOfRows=").append(rows);
		return  common.json_requestAPI(url);
	}
	
	
	
	//공공데이터 목록 화면 요청
	@RequestMapping("/list.da")
	public String list(HttpSession session) {
		session.setAttribute("category", "da");
		return "data/list";
	}
}
