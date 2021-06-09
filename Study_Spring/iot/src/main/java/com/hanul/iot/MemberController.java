package com.hanul.iot;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
//import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import common.CommonService;
import member.MemberServiceImpl;
import member.MemberVO;

@Controller
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private MemberServiceImpl service;

	private String naver_client_id = "mhh3qIevSb9m2MleiJde";
	private String kakao_client_id = "0bdeb6b5055303d45957fa65d0c1ea68";
	
	//카카오로그인
	@RequestMapping("/kakaologin")
	public String kakaologin(HttpSession session) {
		
		//https://kauth.kakao.com/oauth/authorize?response_type=code
		//&client_id={REST_API_KEY}
		//&redirect_uri={REDIRECT_URI}
		String state = UUID.randomUUID().toString();
		session.setAttribute("state", state);
		
		StringBuffer url 
			= new StringBuffer("https://kauth.kakao.com/oauth/authorize?response_type=code");
		url.append("&client_id=").append(kakao_client_id);
		url.append("&redirect_uri=").append("http://localhost:8989/iot/kakaocallback");
		url.append("&state=").append(state);
		
		return "redirect:" + url.toString();
	}
	
	@RequestMapping("/kakaocallback")
	public String kakaocallback(String state, HttpSession session
								, @RequestParam(required = false) String code
								, @RequestParam(required = false) String error) {
		if( error!=null || !state.equals( (String)session.getAttribute("state")  ) ) return "redirect:/"; 
		
//		curl -v -X POST "https://kauth.kakao.com/oauth/token" \
//		 -d "grant_type=authorization_code" \
//		 -d "client_id={REST_API_KEY}" \
//		 -d "redirect_uri={REDIRECT_URI}" \
//		 -d "code={AUTHORIZATION_CODE}"
		
		StringBuffer url 
			= new StringBuffer("https://kauth.kakao.com/oauth/token?grant_type=authorization_code");
		url.append("&client_id=").append(kakao_client_id);
//		url.append("&redirect_uri=").append("http://localhost:8989/iot/kakaocallback");
		url.append("&code=").append(code);
		
		JSONObject json = new JSONObject( common.requestAPI(url) );
		String token = json.getString("access_token");
		String type = json.getString("token_type");
		
		//사용자 정보 가져오기
//		curl -v -X GET "https://kapi.kakao.com/v2/user/me" \
//		  -H "Authorization: Bearer {ACCESS_TOKEN}"
		url = new StringBuffer("https://kapi.kakao.com/v2/user/me");
		json = new JSONObject( common.requestAPI(url, type + " " + token) );
		
		if( ! json.isEmpty() ) {
			MemberVO vo = new MemberVO();
			vo.setSocial_type("kakao");
			
			vo.setId( json.get("id").toString() );
			
			json = json.getJSONObject("kakao_account");
			vo.setSocial_email( json.getString("email") );
			vo.setGender(  
					json.has("gender") &&  json.getString("gender").equals("male") ? "남" : "여" 
			);
			
			json = json.getJSONObject("profile");
			vo .setName( json.getString("nickname") );
			
			if( service.member_social_id(vo) ) 
				service.member_social_update(vo);
			else 
				service.member_social_insert(vo);
			
			session.setAttribute("logininfo", vo);
			
		}
		
		return "redirect:/";
	}
	
	
	//네이버로그인
	@RequestMapping("/naverlogin")
	public String naverlogin(HttpSession session) {
		
//		https://nid.naver.com/oauth2.0/authorize?response_type=code
//		&client_id=CLIENT_ID
//		&state=STATE_STRING
//		&redirect_uri=CALLBACK_URL
		
		//세션상태 토큰 문자 생성
		String state = UUID.randomUUID().toString();
		session.setAttribute("state", state);
		
		StringBuffer url 
			= new StringBuffer("https://nid.naver.com/oauth2.0/authorize?response_type=code");
		url.append("&client_id=").append(naver_client_id);
		url.append("&state=").append(state);
		url.append("&redirect_uri=").append("http://localhost:8989/iot/navercallback");
		
		return "redirect:" + url.toString();
	}
	
	@RequestMapping("/navercallback")
	public String navercallback(String state, HttpSession session
								, @RequestParam(required=false) String code
								, @RequestParam(required=false) String error) {
		//상태 토큰이 일치하지 않거나 콜백실패시 토큰을 발급받을수 없다
		if( error!=null  || !state.equals( (String)session.getAttribute("state") ) ) return "redirect:/";
		
		//정상적으로 Callback 이 성공했다면 네이버로부터 정보를 얻을수 있다
		
		//접근 토큰 발급을 위한 요청
		//https://nid.naver.com/oauth2.0/token?grant_type=authorization_code
		//&client_id=jyvqXeaVOVmV
		//&client_secret=527300A0_COq1_XV33cf
		//&code=EIc5bFrl4RibFls1
		//&state=9kgsGTfH4j7IyAkg
		StringBuffer url 
			= new StringBuffer("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code");
		url.append("&client_id=").append(naver_client_id);
		url.append("&client_secret=IUP2xdZC42");
		url.append("&code=").append(code);
		url.append("&state=").append(state);
		JSONObject json = new JSONObject( common.requestAPI(url) );
		String access_token = json.getString("access_token");
		String token_type = json.getString("token_type");
		
		
		//사용자 프로필 정보 조회를 위한 요청
		url = new StringBuffer("https://openapi.naver.com/v1/nid/me");
		json = new JSONObject( common.requestAPI(url, token_type + " " + access_token) );
		if( json.getString("resultcode").equals("00") ) {
			//사용자프로필 정보는 response 에 있다
			json = json.getJSONObject("response");
			MemberVO vo = new MemberVO();
			vo.setSocial_type("naver");
			vo.setId( json.getString("id") );
			vo.setGender( json.getString("gender").equals("F") ? "여" : "남");
			vo.setName( json.getString("name") );
			vo.setSocial_email( json.getString("email") );
			
			//네이버로그인이 처음인 경우 회원정보insert
			//               처음이 아닌 경우 회원정보update
			if( service.member_social_id(vo) ) {
				service.member_social_update(vo);
			}else {
				service.member_social_insert(vo);
			}
			
			session.setAttribute("logininfo", vo);
			
		}
		
		return "redirect:/";
	}
	
	
	
	@RequestMapping("/login")
	public String list(Model model, HttpSession session) {
		session.setAttribute("category", "login");
	
		return "member/login";

	}
	//responseBody 어노테이션 
	//ajax나 아니면 내가 결과값만 따로 java형태의 결과값을 받고싶을때.
	//json형태를 바로 받아서 사용할때는 responseBody
	
	@Autowired private CommonService common;
	@ResponseBody @RequestMapping("/ailogin")
	public boolean login(String id ,String pw , HttpSession session) {
		//화면에서 입력한 아이디/비번이 일치하는 회원정보를 조회해온다.
		//Intent나 Bundle 이런 key , value 로 구분해서 사용할수있는 DTO와 비슷한구조의
		//Map
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("pw", pw);
		MemberVO vo = service.member_login(map);
		
		//common.sendEmail(session, vo.getEmail(), vo.getName());
		
		//로그인한 회원정보를 세션에 담아두기.
		session.setAttribute("logininfo", vo);
		
		/*
		 * if(vo==null) { return false; }else { return true; }
		 */
		return vo==null ? false : true;

	}
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		MemberVO vo = (MemberVO)session.getAttribute("logininfo");
		String social = vo.getSocial_type();
//		((MemberVO)session.getAttribute("logininfo")).getSocial_type();
		
		session.removeAttribute("logininfo");
		
		if( social != null && social.equals("kakao") ) {
			
		//curl -v -X GET "https://kauth.kakao.com/oauth/logout
		//?client_id={YOUR_REST_API_KEY}
		//&logout_redirect_uri={YOUR_LOGOUT_REDIRECT_URI}"
			
			StringBuffer url = new StringBuffer("https://kauth.kakao.com/oauth/logout");
			url.append("?client_id=").append( kakao_client_id );
			url.append("&logout_redirect_uri=").append("http://localhost:8989/iot");
			
			return "redirect:" + url.toString();
			
		}else {
			return "redirect:/";
		}
		
	}
	
	@RequestMapping("/member")
	public String member(HttpSession session) {
		session.setAttribute("category", "join");
		
		return "member/join";
	}
	@ResponseBody @RequestMapping("/id_check")
	public boolean id_check(String id) {
		
		return service.member_id_check(id);
	}
	@ResponseBody @RequestMapping(value = "/join" , produces = "text/html; charset=utf-8")
	public String join(MemberVO vo , HttpSession session , HttpServletRequest request) {
		//화면에서 입력한 회원정보를 DB에 저장한 후 홈으로 연결
		

		StringBuffer msg = new StringBuffer("<script>");
		if(service.member_join(vo)) {
			//회원가입이 정상적으로 된 경우에는 email을 보내준다.
			/* common.sendEmail(vo.getEmail(), vo.getName()); */
			msg.append("alert('회원가입을 축하드립니다. '); location='" +
							request.getContextPath() + "'; ");
														
		}else {
			msg.append("alert('회원가입이 실패했습니다. ');  history.go(-1)");
			
		}
		
		msg.append("</script>");
		return msg.toString();
	}
	

}
