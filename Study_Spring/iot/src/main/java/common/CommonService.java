package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class CommonService {

	public Map<String, Object> json_requestAPI(StringBuffer url) {
		
		JSONObject json = new JSONObject( requestAPI(url) );
		json = json.getJSONObject("response");
		json = json.getJSONObject("body");
		int count = 0;
		if( json.has("totalCount") ) count = json.getInt("totalCount");
		//items: ""
		//items: { "item":[{}, {}, {}, {}, {}, {}, {}, {}, {}, {}] }
		if( json.get("items") instanceof JSONObject ) {
			json = json.getJSONObject("items");
			if( count==1 ) json.put("item", new JSONArray().put(0, json.get("item"))  ); 
		}else
			json.put("item", "");
		
		json.put("count", count);
		return json.toMap();
	}
	
	public String requestAPI( StringBuffer url ) {
		String result = url.toString();
		
	    try {
	        HttpURLConnection con = (HttpURLConnection)new URL(result).openConnection();
	        con.setRequestMethod("GET");
	        int responseCode = con.getResponseCode();
	        BufferedReader br;
	        if(responseCode>=200 && responseCode<=300) { // 정상 호출
	          br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
	        } else {  // 에러 발생
	          br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
	        }
	        String inputLine;
	        StringBuffer res = new StringBuffer();
	        while ((inputLine = br.readLine()) != null) {
	          res.append(inputLine);
	        }
	        br.close();
	        con.disconnect();
	        result = res.toString();
	        
	      } catch (Exception e) {
	        System.out.println(e.getMessage());
	      }
		
		return result;
	}
	
	public String requestAPI( StringBuffer url, String header ) {
		String result = url.toString();
		
		try {
			HttpURLConnection con = (HttpURLConnection)new URL(result).openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if(responseCode>=200 && responseCode<=300) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			} else {  // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
			}
			String inputLine;
			StringBuffer res = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				res.append(inputLine);
			}
			br.close();
			con.disconnect();
			result = res.toString();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}
	
	
	public File fileDownload(String filename , String filepath , 
			HttpSession session , HttpServletResponse response) {
		//response의 경우에는 controller 자체에서 http 응답처리를 한다.
		
		//다운로드할 파일 객체를 생성
		File file = new File(session.getServletContext().getRealPath("resources")
				             + "/" + filepath );
		//content type 지정을 위한 파일의 마임타입
		String mime = session.getServletContext().getMimeType(filename);
		
		response.setContentType(mime);
		try {
			filename = URLEncoder.encode(filename, "utf-8").replaceAll("\\+", "%20");
			
			response.setHeader("content-disposition", "attactment; filename=" + filename);
			
			ServletOutputStream out = response.getOutputStream();
			FileCopyUtils.copy(new FileInputStream(file) ,out);
			out.flush();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return file;
	}
	
	
	public String fileUpload(HttpSession session , MultipartFile file , String category) {
		//서버의 물리적인 위치
		String resources = session.getServletContext().getRealPath("resources");
		//D://StudySpring/....../iot/resources
		String upload = resources + "/upload";
		String folder = upload + "/" + category + "/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		//D://StudyPring/../.... upload/notice/2021/05/26/adsfa.txt 
		File f = new File(folder);
		if(! f.exists() ) f.mkdir();
		
		String uuid = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		try {
			file.transferTo(new File(folder,uuid));
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return folder.substring(resources.length()+1) + "/" + uuid;
	}
	
	public void sendEmail(HttpSession session, String email, String name) {
		// 다른식의 이메일 메소드를 여러가지 사용할수있으니
		// sendSimple(email , name);
		// 기본이메일 sendimple
		// sendAttach파일첨부
		//sendAttach(session, email, name);
		//
		// HTML형태 이메일 전송
		 sendHtml(session, email , name);
	}

	private void sendHtml(HttpSession session, String email, String name) {
		HtmlEmail mail = new HtmlEmail();
		// 메일을 보내기 위한 객체를 만듬
		mail.setHostName("smtp.naver.com");
		mail.setCharset("utf-8");
		mail.setDebug(true);
		// 실제로 smtpHost에 로그인 할수있는 계정
		// youngmoon525@naver.com //
		mail.setAuthentication("개발자 네이버아이디", "비밀번호");
		mail.setSSLOnConnect(true);
		try {
			mail.setFrom("내가 보낼 이메일주소", "한울관리자");
			// 누가 보냈는지 찍히는 부분 //이름
			mail.addTo(email, name);
			mail.setSubject("회원가입축하 메시지-첨부파일이 있습니다.HTML");
			StringBuffer msg = new StringBuffer();
			msg.append("<html>");
			msg.append("<body>");
			msg.append("<a href='http://hanuledu.co.kr'/><img src='http://www.hanuledu.co.kr/data/menu/LOGO_cYFn10oGM70UkjkExmRP1610075399.jpg'/>");
			msg.append("<hr>");
			msg.append("<h2>한울 AI과정 가입을 축하드립니다.</h2>");
			msg.append("<p>회원가입을 축하드립니다.</p>");
			msg.append("<p>첨부 된 파일을 꼭 확인해 주시고</p>");
			msg.append("<p>프로젝트까지 마무리해서</p>");
			msg.append("<p>취업에 꼭 성공하시길 바랍니다.</p>");
			msg.append("</body>");
			msg.append("</html>");
			mail.setHtmlMsg(msg.toString());
			
			EmailAttachment file = new EmailAttachment();
			file.setPath(session.getServletContext().getRealPath("resources")
					+ "/css/common.css");	
			mail.attach(file);
			
			file = new EmailAttachment();
			file.setURL(new URL("http://www.imageclick.com/imgs/main_intro.jpg"));
			mail.attach(file);
			
			mail.send();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	private void sendAttach(HttpSession session, String email, String name) {
		MultiPartEmail mail = new MultiPartEmail();
		// 메일을 보내기 위한 객체를 만듬
		mail.setHostName("smtp.naver.com");
		mail.setCharset("utf-8");
		mail.setDebug(true);
		// 실제로 smtpHost에 로그인 할수있는 계정
		// youngmoon525@naver.com //
		mail.setAuthentication("개발자 네이버아이디", "비밀번호");
		mail.setSSLOnConnect(true);
		try {

			mail.setFrom("내가 보낼 이메일주소", "한울관리자");
			// 누가 보냈는지 찍히는 부분 //이름
			mail.addTo(email, name);
			mail.setSubject("회원가입축하 메시지-첨부파일이 있습니다.");
			mail.setMsg(name + "님 회원가입을 축하합니다. 첨부파일을 확인해주세요");
			
			//파일 첨부 처리
			EmailAttachment file = new EmailAttachment();
			//realPath\ \
			file.setPath("‪D:\\Study_Java\\HelloWorld.java");
			mail.attach(file);
			//java파일을 추가함
			
			file = new EmailAttachment();
			file.setPath(session.getServletContext().getRealPath("resources")
								+ "/images/kakao_login.png");	
			mail.attach(file);
			//이미지 파일도 추가함.
			
			file = new EmailAttachment();
			file.setURL(new URL("http://www.imageclick.com/imgs/main_intro.jpg"));
			mail.attach(file);
			
			
			mail.send();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void sendSimple(String email, String name) {
		MultiPartEmail mail = new MultiPartEmail();
		// 메일을 보내기 위한 객체를 만듬
		mail.setHostName("smtp.naver.com");
		mail.setCharset("utf-8");
		mail.setDebug(true);
		// 실제로 smtpHost에 로그인 할수있는 계정
		// youngmoon525@naver.com //
		mail.setAuthentication("개발자 네이버아이디", "비밀번호");
		mail.setSSLOnConnect(true);
		try {

			mail.setFrom("내가 보낼 이메일주소", "한울관리자");
			// 누가 보냈는지 찍히는 부분 //이름
			mail.addTo(email, name);
			mail.setSubject("회원가입축하 메시지-첨부파일이 있습니다.");
			mail.setMsg(name + "님 회원가입을 축하합니다.");
			mail.send();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendSMS() {
		final String API_KEY = "";
		final String API_SECRET = "";
		final String msg = "문자 메시지 테스트";
		Message coolsms = new Message(API_KEY, API_SECRET);
		//문자를 전송할 객체 생성 
		HashMap<String, String> params = new HashMap<String, String>(){
			{
				put("to","01079972725");
				put("from","01079972725");
				put("type","SMS"); // SMS , LMS , MMS , AT
				put("text" , msg); //보내고싶은 메세지
				put("app_version" , "JAVA SDK v1.2");
			}
		};
		try {
			org.json.simple.JSONObject obj = (org.json.simple.JSONObject) coolsms.send(params);
			System.out.println(obj.toString());
		}catch (CoolsmsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCode());
		}
		
	}
}
