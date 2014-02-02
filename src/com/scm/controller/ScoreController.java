package com.scm.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.scm.util.ExcelTool;
import com.scm.util.Pager;

@Controller
public class ScoreController {
	private static final Log log = LogFactory.getLog(LoginController.class);

	private static final boolean String = false;

	@Autowired
	ScmRepository scmRepository;

	@RequestMapping(value = "/getExamRecordPage")
	public void getExamRecordPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String examYear = request.getParameter("ss_year"); // ss = student score
		String examGrade = request.getParameter("ss_grade");
		String examClass = request.getParameter("ss_class");
		String examSubject = request.getParameter("ss_subject");
		String examName = request.getParameter("ss_name");
		String begin = request.getParameter("ss_begin");
		String end = request.getParameter("ss_end");
		String strPage = request.getParameter("page");
		String strRows = request.getParameter("rows");

		int page = 1, rows = 20;
		if (strPage != null && !"".equalsIgnoreCase(strPage.trim())) {
			page = Integer.valueOf(strPage.trim());
		}
		if (strRows != null && !"".equalsIgnoreCase(strRows.trim())) {
			rows = Integer.valueOf(strRows.trim());
		}
		Date startTime = null, endTime = null;
		if (begin != null && !"".equalsIgnoreCase(begin.trim())) {
			try {
				startTime = new SimpleDateFormat("yyyy-MM-dd").parse(begin
						.trim());
			} catch (ParseException e) {
				startTime = null;
				e.printStackTrace();
			}
		}
		if (end != null && !"".equalsIgnoreCase(end.trim())) {
			try {
				endTime = new SimpleDateFormat("yyyy-MM-dd").parse(end.trim());
			} catch (ParseException e) {
				endTime = null;
				e.printStackTrace();
			}
		}

		Pager pager = scmRepository.getExamRecordPage(null/* id */, examYear,
				examGrade, examClass, examSubject, examName, startTime,
				endTime, page, rows);
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
	public void newExamRecord(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String examName = request.getParameter("exam_name");
		String examYear = request.getParameter("exam_year");
		String examGrade = request.getParameter("exam_grade");
		String examClass = request.getParameter("exam_class");
		String examSubject = request.getParameter("exam_subject");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (examName == null || "".equalsIgnoreCase(examName.trim())) {
				throw new Exception("û����д��������");
			}
			if (examYear == null || "".equalsIgnoreCase(examYear.trim())) {
				throw new Exception("û��ѡ��ôο��Ե����");
			}
			if (examGrade == null || "".equalsIgnoreCase(examGrade.trim())) {
				throw new Exception("û��ѡ��ôο��Ե��꼶");
			}
			if (examClass == null || "".equalsIgnoreCase(examClass.trim())) {
				throw new Exception("û��ѡ��ôο��Եİ༶");
			}
			if (examSubject == null || "".equalsIgnoreCase(examSubject.trim())) {
				throw new Exception("û��ѡ��ôο��ԵĿ�Ŀ");
			}

			int add = scmRepository.addExamRecord(examName, examYear,
					examGrade, examClass, examSubject);
			if (add > 0) {
				result.put("success", true);
			} else {
				result.put("errmsg", "�����¼ʧ��");
			}
		} catch (Exception ex) {
			result.put("errmsg", ex.getMessage());
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.write(new JSONObject(result).toString());
		pw.flush();
		pw.close();
	}

	@RequestMapping(value = "/downloadScoreTemplate")
	public void downloadScoreTemplate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String recordId = request.getParameter("id");
		if (recordId == null || "".equalsIgnoreCase(recordId.trim())) {
			log.error(request.getContextPath() + ":\t get no recordid!");
			return;
		}
		Pager pager = scmRepository.getExamRecordPage(recordId,
				null/* examYear */, null/* examGrade */, null/* examClass */,
				null/* examSubject */, null/* examName */, null/* startTime */,
				null/* endTime */, 1/* page */, 1/* rows */);
		if (pager.getResultList().size() == 0){
			log.error(request.getContextPath() + ":\t get no record by id [" + recordId + "]");
			return;
		}
		
		Map<String, String> record = (Map<String, String>) pager.getResultList().get(0);
		String examInfo = record.get("name");
		String year = record.get("year");
		String grade = record.get("grade");
		String examClass = record.get("class");
		String subjectList = record.get("subject");
		
		String fileName = year + grade;
		if (!examClass.trim().equalsIgnoreCase("all")){
			fileName += examClass;
		}
		fileName += examInfo + ".xls";
		
		Map<String, String> propsMap = new LinkedHashMap<String, String>();
		propsMap.put("class", "�༶");
		propsMap.put("name", "����");
		propsMap.put("number", "����");
		
		String[] subjects = subjectList.split(",");
		for (String subject : subjects){
			propsMap.put(subject, subject);		//eg: ��ѧ: ��ѧ
		}
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		response.setCharacterEncoding("GBK");
		ExcelTool.exportExcel(request, response, fileName, propsMap, dataList);
	}
}