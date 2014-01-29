package com.scm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;


@Controller
public class LoginController {
	private static final Logger log = Logger.getLogger(LoginController.class.getName());
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String webRoot = "http://" + req.getHeader("host") + req.getContextPath(); 
		req.setAttribute("ROOT", webRoot);

		return "index";
	}
	 
}
