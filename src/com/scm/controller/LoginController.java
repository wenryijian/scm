package com.scm.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.repository.ScmRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


@Controller
public class LoginController {
	private static final Log log = LogFactory.getLog(LoginController.class);
	
	@Autowired
	ScmRepository scmRepository;
	
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String webRoot = "http://" + req.getHeader("host") + req.getContextPath(); 
		req.setAttribute("ROOT", webRoot);

		int current_year = Calendar.getInstance().get(Calendar.YEAR);
		req.setAttribute("CURR_YEAR", current_year);
		
		// 科目列表
		List<Map<String, Object>> subjectList = scmRepository.getAllSubjectList();
		req.setAttribute("ALL_SUBJECT", subjectList);
		return "/index";
	}
	
}
