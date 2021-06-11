package com.hanul.iot;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.CommonService;

@Controller
public class DataController {
	@Autowired private CommonService common;
	
	private String key 
		= "bPOfxz9kDWHNWzWO2Bi8gK9S8%2B7RpsyZd3Zezo%2F97WzQBOtyZytNGRprp7mLuKpTSIRb7MzojJ%2F1GH4oF57c1w%3D%3D"; 	
//	private String key 
//	= "FPgj2NXbJw46TcGkmAfZEiYFDbxilys7KLjk3KaB7AfeJE00ZhPNM0M8unwbsI69fSmT8SNfVEimE6ZZ2U14hA%3D%3D"; 	
	private String animalURL 
		= "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/";
	
	//유기동물 시군구조회 요청
	@ResponseBody @RequestMapping("/data/animal/sigungu")
	public Object animal_sigungu(String sido) {
		StringBuffer url = new StringBuffer( animalURL + "sigungu" );
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
		url.append("&upr_cd=").append(sido);
		return common.json_requestAPI(url);
	}
	
	
	
	//유기동물조회 요청
	//@ResponseBody 
	@RequestMapping("/data/animal/list")
//	public Object animal_list( @RequestBody HashMap<String, Object> map ) {
	public String animal_list( @RequestBody HashMap<String, String> map, Model model ) {
		StringBuffer url = new StringBuffer(animalURL + "abandonmentPublic");
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
		url.append("&pageNo=").append(map.get("pageNo"));
		url.append("&numOfRows=").append(map.get("rows"));
		url.append("&upr_cd=").append(map.get("sido")); //시도
		url.append("&upkind=").append(map.get("upkind"));//축종
		url.append("&kind=").append(map.get("kind"));//품종
		url.append("&org_cd=").append(map.get("sigungu"));//시군구
		
//		return common.json_requestAPI(url);
		model.addAttribute("list", common.json_requestAPI(url) );
		model.addAttribute("viewType", map.get("viewType") );
		return "data/animal/animal_list";
	}
	
	//유기동물 축종에 따른 품종조회 요청
	@ResponseBody @RequestMapping("/data/animal/kind")
	public Object animal_kind( String upkind ) {
		StringBuffer url = new StringBuffer( animalURL + "kind" );
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
		url.append("&up_kind_cd=").append(upkind);
		return common.json_requestAPI(url);
	}
	
	
	
	//유기동물 시도조회 요청
	@ResponseBody @RequestMapping("/data/animal/sido")
	public Object animal_sido() {
		StringBuffer url = new StringBuffer( animalURL + "sido");
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
		url.append("&numOfRows=30");
		return common.json_requestAPI(url);
	}
	
	
	//약국정보조회 요청
	@ResponseBody @RequestMapping("/data/pharmacy")
//	public Map<String, Object> pharmacy() {
	public Object pharmacy(int pageNo, int rows) {
		StringBuffer url = new StringBuffer("http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList");
		url.append("?ServiceKey=").append(key);
		url.append("&_type=json");
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
