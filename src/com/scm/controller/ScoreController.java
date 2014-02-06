package com.scm.controller;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.scm.repository.ScmRepository;
import com.scm.util.ExcelTool;
import com.scm.util.Pager;

@Controller
public class ScoreController {
	private static final Log log = LogFactory.getLog(LoginController.class);

	private static final boolean String = false;

	@Autowired
	ScmRepository scmRepository;

	@Autowired
	@Qualifier("transactionManager")
	DataSourceTransactionManager scmTm;

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
				throw new Exception("没有填写考试名称");
			}
			if (examYear == null || "".equalsIgnoreCase(examYear.trim())) {
				throw new Exception("没有选择该次考试的年度");
			}
			if (examGrade == null || "".equalsIgnoreCase(examGrade.trim())) {
				throw new Exception("没有选择该次考试的年级");
			}
			if (examClass == null || "".equalsIgnoreCase(examClass.trim())) {
				throw new Exception("没有选择该次考试的班级");
			}
			if (examSubject == null || "".equalsIgnoreCase(examSubject.trim())) {
				throw new Exception("没有选择该次考试的科目");
			}

			int add = scmRepository.addExamRecord(examName, examYear,
					examGrade, examClass, examSubject);
			if (add > 0) {
				result.put("success", true);
			} else {
				result.put("errmsg", "保存记录失败");
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
		if (pager.getResultList().size() == 0) {
			log.error(request.getContextPath() + ":\t get no record by id ["
					+ recordId + "]");
			return;
		}

		Map<String, String> record = (Map<String, String>) pager.getResultList().get(0);
		String examInfo = record.get("name");
		String year = record.get("year");
		String grade = record.get("grade");
		String examClass = record.get("class");
		String subjectList = record.get("subject");

		String fileName = year + grade;
		if (!examClass.trim().equalsIgnoreCase("all")) {
			fileName += examClass;
		}
		fileName += examInfo + ".xls";

		LinkedHashMap<String, String> propsMap = new LinkedHashMap<String, String>();
		propsMap.put("class", "班级");
		propsMap.put("name", "姓名");
		propsMap.put("number", "座号");

		String[] subjects = subjectList.split(",");
		for (String subject : subjects) {
			propsMap.put(subject, subject); // eg: 化学: 化学
		}

		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		response.setCharacterEncoding("GBK");
		ExcelTool.exportExcel(request, response, fileName, propsMap, dataList);
	}

	@RequestMapping(value = "/deleteExamRecord")
	public void deleteExamRecord(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();

		String recordId = request.getParameter("id");
		if (recordId == null || "".equalsIgnoreCase(recordId.trim())) {
			result.put("errmsg", "没有记录id");
		} else {
			scmRepository.deleteExamRecord(recordId);
			result.put("success", true);
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.write(new JSONObject(result).toString());
		pw.flush();
		pw.close();
	}

	@RequestMapping(value = "/uploadExamScore")
	public void uploadExamScore(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String examId = request.getParameter("id");

		// 设置上下方文
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		// 检查form是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request) && examId != null
				&& !"".equalsIgnoreCase(examId.trim())) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					TransactionStatus txstatus = scmRepository
							.getTransactionStatus(scmTm);
					scmRepository.deleteExamScore(examId);

					try {
						InputStream is = file.getInputStream();
						HSSFWorkbook book = new HSSFWorkbook(is);
						HSSFSheet sheet = book.getSheetAt(0); // 第一个表格

						int rowNum = 0, colNum = 0;
						Row row = null;
						Cell cell = null;
						List<String> subjects = new ArrayList<String>();
						for (Iterator<Row> rowIt = sheet.rowIterator(); rowIt
								.hasNext();) {
							row = rowIt.next();
							// class, name, seat, <subjects>
							// 前3列固定为学生信息，第4列开始，为各个科目
							colNum = 0;
							String stuClass = null, stuName = null, stuSeat = null;
							for (Iterator<Cell> cellIt = row.cellIterator(); cellIt
									.hasNext();) {
								cell = cellIt.next();
								cell.setCellType(Cell.CELL_TYPE_STRING);
								if (rowNum == 0) {
									// 第1行，表头
									if (colNum >= 3) {
										subjects.add(cell.getStringCellValue());
										// log.info("header: " +
										// cell.getStringCellValue());
									}
								} else {
									switch (colNum) {
									case 0:
										stuClass = cell.getStringCellValue();
										break;
									case 1:
										stuName = cell.getStringCellValue();
										break;
									case 2:
										stuSeat = cell.getStringCellValue();
										break;
									default:
										if (stuClass == null
												|| "".equalsIgnoreCase(stuClass
														.trim())) {
											throw new Exception("没有班级， rowNum["
													+ rowNum + "], colNum["
													+ colNum + "]");
										}
										if (stuName == null
												|| "".equalsIgnoreCase(stuName
														.trim())) {
											throw new Exception("没有姓名， rowNum["
													+ rowNum + "], colNum["
													+ colNum + "]");
										}
										if (stuSeat == null
												|| "".equalsIgnoreCase(stuSeat
														.trim())) {
											throw new Exception(
													"没有座位号， rowNum[" + rowNum
															+ "], colNum["
															+ colNum + "]");
										}
										String subject = subjects
												.get(colNum - 3);
										if (subject == null
												|| "".equalsIgnoreCase(subject
														.trim())) {
											throw new Exception(
													"获取科目名称失败， rowNum["
															+ rowNum
															+ "], colNum["
															+ colNum + "]");
										}
										String score = cell
												.getStringCellValue();
										if (score == null
												|| "".equalsIgnoreCase(score
														.trim())) {
											throw new Exception(
													"没有学生成绩， rowNum[" + rowNum
															+ "], colNum["
															+ colNum
															+ "]， subject["
															+ subject + "]");
										}
										// log.info("score: " + examId + "\t" +
										// stuName + "\t" + stuClass + "\t" +
										// stuSeat + "\t" + subject + "\t" +
										// score);
										scmRepository.addExamScore(examId,
												stuName, stuClass, stuSeat,
												subject, score);
										break;
									}
								}
								colNum++;
							}
							rowNum++;
						}
						scmTm.commit(txstatus);
						result.put("success", true);
					} catch (Exception ex) {
						ex.printStackTrace();
						scmTm.rollback(txstatus);
						result.put("errmsg", "读取cxcel表格出错: " + ex.getMessage());
					}
				}
				// 只分析第一个文件
				break;
			}
		} else {
			result.put("errmsg", "缺少字段");
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.write(new JSONObject(result).toString());
		pw.flush();
		pw.close();
	}

	@RequestMapping(value = "/openExamScore")
	public String getExamScore(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String examid = request.getParameter("examid");
		request.setAttribute("EXAMID", examid.trim());

		List<Map<String, Object>> subjectList = scmRepository
				.getExamSubjectById(examid);
		request.setAttribute("CURRENT_EXAM_SUBJECTS", subjectList);
		
		return "/exam_score";
	}

	@RequestMapping(value = "/getExamScoreDetailPage")
	public void getExamScoreDetailPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String examid = request.getParameter("examid");

		String strPage = request.getParameter("page");
		String strRows = request.getParameter("rows");
		int page = 1, rows = 20;
		if (strPage != null && !"".equalsIgnoreCase(strPage.trim())) {
			page = Integer.valueOf(strPage.trim());
		}
		if (strRows != null && !"".equalsIgnoreCase(strRows.trim())) {
			rows = Integer.valueOf(strRows.trim());
		}

		Map<String, Object> result = new HashMap<String, Object>();
		if (examid == null || "".equalsIgnoreCase(examid.trim())) {
			result.put("errmsg", "没有examid");
		} else {
			Pager pager = scmRepository.getExamScoreDetailPage(examid, page,
					rows);
			if (pager != null) {
				result.put("total", pager.getTotal());
				result.put("rows", pager.getResultList());
			}else{
				result.put("errmsg", "查询无结果");
			}
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter pw = response.getWriter();
		pw.write(new JSONObject(result).toString());
		pw.flush();
		pw.close();
	}
	
	@RequestMapping(value = "/downCurrentSortedScore")
	public void downCurrentSortedScore(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String examid = request.getParameter("examid");
		if (examid == null || "".equalsIgnoreCase(examid.trim())){
			log.error(request.getContextPath() + ":\t get no examid!");
			return;
		}
		
		// 考试信息
		Pager pager = scmRepository.getExamRecordPage(examid,
				null/* examYear */, null/* examGrade */, null/* examClass */,
				null/* examSubject */, null/* examName */, null/* startTime */,
				null/* endTime */, 1/* page */, 1/* rows */);
		if (pager.getResultList().size() == 0) {
			log.error(request.getContextPath() + ":\t get no exam record by examid ["+ examid + "]");
			return;
		}
		Map<String, String> record = (Map<String, String>) pager.getResultList().get(0);
		String examInfo = record.get("name");
		String year = record.get("year");
		String grade = record.get("grade");
		String examClass = record.get("class");

		String fileName = year + grade;
		if (!examClass.trim().equalsIgnoreCase("all")) {
			fileName += examClass;
		}
		fileName += examInfo + ".xls";
		
		// 表头
		LinkedHashMap<String, String> propsMap = new LinkedHashMap<String, String>();

		// 数据行
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
		
		int r = scmRepository.getExamScoreDetail(examid, propsMap, dataList);
		if (r > 0){
			response.setCharacterEncoding("GBK");
			ExcelTool.exportExcel(request, response, fileName, propsMap, dataList);
		}
	}
}