package com.hanul.iot;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpSession session) {
		logger.info("Home");
		
		session.removeAttribute("category");
		
		return "home";
	}
	@RequestMapping("/error")
	public String error(HttpServletRequest request , Model model) {
		Throwable exception = (Throwable)request.getAttribute("javax.servlet.error.exception");
		StringBuffer msg = new StringBuffer();
		while (exception != null) {
			msg.append("<p>").append(exception.getMessage())
			.append("</p>");
			exception  = exception.getCause();
			
		}//while
		model.addAttribute("msg" , msg.toString());
		int code = (Integer)request.getAttribute("javax.servlet.error.status_code");
		return "error/" + (code==404 ? "404" : "default") ;
		
	}
	
}
