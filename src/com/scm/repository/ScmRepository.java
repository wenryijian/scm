package com.scm.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

import com.scm.util.Pager;

@Repository
public class ScmRepository {
	private static final Log log = LogFactory.getLog(ScmRepository.class);
	
	@Autowired
	@Qualifier("scmSimpleJdbcTemplate")
	SimpleJdbcTemplate scmJdbc;

	@Autowired
	@Qualifier("transactionManager")
	DataSourceTransactionManager txManager;
	
	
	public List<Map<String, Object>> getAllSubjectList() {
		String sql = "select subject from t_setting_subject";
		return scmJdbc.queryForList(sql, new Object[]{});
	}
	
	public int addExamRecord(String examName, String examYear, String examGrade, String examClass, String examSubject) {
		String sql = "insert into t_exam_record (name, year, grade, class, subject) values(?, ?, ?, ?, ?)";
		int add = scmJdbc.update(sql, new Object[]{examName, examYear, examGrade, examClass, examSubject});
		return add;
	}
	
	public Pager getExamRecordPage(String id, String examYear, String examGrade,
			String examClass, String examSubject, String examName,
			Date startTime, Date endTime, int page, int rows) {
		List<String> params = new ArrayList<String>();
		String sql = "select * from t_exam_record where 1=1";
		String sqlCount = "select count(1) from t_exam_record where 1=1";
		
		if (id != null && !"".equalsIgnoreCase(id.trim())){
			sql += " and id = ?";
			sqlCount += " and id = ?";
			params.add(id.trim());
		}
		if (examYear != null && !"".equalsIgnoreCase(examYear.trim())){
			sql += " and year = ?";
			sqlCount += " and year = ?";
			params.add(examYear);
		}
		if (examGrade != null && !"".equalsIgnoreCase(examGrade.trim())){
			sql += " and grade = ?";
			sqlCount += " and grade = ?";
			params.add(examGrade);
		}
		if (examClass != null && !"".equalsIgnoreCase(examClass.trim())){
			sql += " and class = ?";
			sqlCount += " and class = ?";
			params.add(examClass);
		}
		if (examSubject != null && !"".equalsIgnoreCase(examSubject.trim())){
			sql += " and subject = ?";
			sqlCount += " and subject = ?";
			params.add(examSubject);
		}
		if (examName != null && !"".equalsIgnoreCase(examName.trim())){
			sql += " and name like ?";
			sqlCount += " and name like ?";
			params.add("%" + examName + "%");
		}
		
		int total = scmJdbc.queryForInt(sqlCount, params.toArray());
		Pager pager = new Pager();
		pager.setPageNo(page);
		pager.setPageSize(rows);
		pager.setTotal(total);
		
		sql += " limit " + (page - 1) * rows + "," + rows;
		List resultList = scmJdbc.queryForList(sql, params.toArray());
		pager.setResultList(resultList);

		return pager;
	}
}
	