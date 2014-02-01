package com.scm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.repository.ScmRepository;
import com.scm.util.Pager;

@Controller
public class ScoreController {
	private static final Log log = LogFactory.getLog(LoginController.class);
	
	@Autowired
	ScmRepository scmRepository;

	
	@RequestMapping(value = "/getExamRecordPage")
	public void getExamRecordPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String examYear = request.getParameter("ss_year");	// ss = student score
		String examGrade = request.getParameter("ss_grade");
		String examClass = request.getParameter("ss_class");
		String examSubject = request.getParameter("ss_subject");
		String examName = request.getParameter("ss_name");
		String begin = request.getParameter("ss_begin");
		String end = request.getParameter("ss_end");
		String strPage = request.getParameter("page");
		String strRows = request.getParameter("rows");

		int page = 1, rows = 20;
		if (strPage != null && !"".equalsIgnoreCase(strPage.trim())){
			page = Integer.valueOf(strPage.trim());
		}
		if (strRows != null && !"".equalsIgnoreCase(strRows.trim())){
			rows = Integer.valueOf(strRows.trim());
		}
		Date startTime = null,endTime = null;
		if (begin != null && !"".equalsIgnoreCase(begin.trim())){
			try {
				startTime = new SimpleDateFormat("yyyy-MM-dd").parse(begin.trim());
			} catch (ParseException e) {
				startTime = null;
				e.printStackTrace();
			}
		}
		if (end != null && !"".equalsIgnoreCase(end.trim())){
			try {
				endTime = new SimpleDateFormat("yyyy-MM-dd").parse(end.trim());
			} catch (ParseException e) {
				endTime = null;
				e.printStackTrace();
			}
		}
		
		Pager pager = scmRepository.getExamRecordPage(examYear, examGrade, examClass, examSubject, examName, startTime, endTime, page, rows);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", pager.getTotal());
		result.put("rows", pager.getResultList());
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.write(new JSONObject(result).toString());
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value = "/newExamRecord")
	public void newExamRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String examName = request.getParameter("exam_name");
		String examYear = request.getParameter("exam_year");
		String examGrade = request.getParameter("exam_grade");
		String examClass = request.getParameter("exam_class");
		String examSubject = request.getParameter("exam_subject");
		
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			if (examName == null || "".equalsIgnoreCase(examName.trim())){
				throw new Exception("没有填写考试名称");
			}
			if (examYear == null || "".equalsIgnoreCase(examYear.trim())){
				throw new Exception("没有选择该次考试的年度");
			}
			if (examGrade == null || "".equalsIgnoreCase(examGrade.trim())){
				throw new Exception("没有选择该次考试的年级");
			}
			if (examClass == null || "".equalsIgnoreCase(examClass.trim())){
				throw new Exception("没有选择该次考试的班级");
			}
			if (examSubject == null || "".equalsIgnoreCase(examSubject.trim())){
				throw new Exception("没有选择该次考试的科目");
			}
			
			scmRepository.addExamRecord(examName, examYear, examGrade, examClass, examSubject);
			
			result.put("success", true);
		}catch(Exception ex){
			result.put("errmsg", ex.getMessage());
		}
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.write(new JSONObject(result).toString());
		pw.flush();
		pw.close();
	}
	
}