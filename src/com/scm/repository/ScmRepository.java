package com.scm.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
public class ScmRepository extends AbstractRepository {
	private static final Log log = LogFactory.getLog(ScmRepository.class);

	@Autowired
	@Qualifier("scmSimpleJdbcTemplate")
	SimpleJdbcTemplate scmJdbc;

	@Autowired
	@Qualifier("transactionManager")
	DataSourceTransactionManager txManager;

	public List<Map<String, Object>> getAllSubjectList() {
		String sql = "select subject from t_setting_subject";
		return scmJdbc.queryForList(sql, new Object[] {});
	}

	public int addExamRecord(String examName, String examYear,
			String examGrade, String examClass, String examSubject) {
		String sql = "insert into t_exam_record (name, year, grade, class, subject) values(?, ?, ?, ?, ?)";
		int add = scmJdbc.update(sql, new Object[] { examName, examYear,
				examGrade, examClass, examSubject });
		return add;
	}

	public Pager getExamRecordPage(String id, String examYear,
			String examGrade, String examClass, String examSubject,
			String examName, Date startTime, Date endTime, int page, int rows) {
		List<String> params = new ArrayList<String>();
		String sql = "select * from t_exam_record where 1=1";
		String sqlCount = "select count(1) from t_exam_record where 1=1";

		if (id != null && !"".equalsIgnoreCase(id.trim())) {
			sql += " and id = ?";
			sqlCount += " and id = ?";
			params.add(id.trim());
		}
		if (examYear != null && !"".equalsIgnoreCase(examYear.trim())) {
			sql += " and year = ?";
			sqlCount += " and year = ?";
			params.add(examYear);
		}
		if (examGrade != null && !"".equalsIgnoreCase(examGrade.trim())) {
			sql += " and grade = ?";
			sqlCount += " and grade = ?";
			params.add(examGrade);
		}
		if (examClass != null && !"".equalsIgnoreCase(examClass.trim())) {
			sql += " and class = ?";
			sqlCount += " and class = ?";
			params.add(examClass);
		}
		if (examSubject != null && !"".equalsIgnoreCase(examSubject.trim())) {
			sql += " and subject = ?";
			sqlCount += " and subject = ?";
			params.add(examSubject);
		}
		if (examName != null && !"".equalsIgnoreCase(examName.trim())) {
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

	public int deleteExamRecord(String id) {
		String sql = "delete from t_exam_record where id = ?";
		return scmJdbc.update(sql, new Object[] { id });
	}

	public int addExamScore(String examId, String stuName, String stuClass,
			String stuSeat, String subject, String score) {
		String sql = "set names utf8";
		scmJdbc.update(sql, new Object[] {});

		sql = "insert into t_score_detail (name, class, seat, subject, score, examid) values(?, ?, ?, ?, ?, ?)";
		return scmJdbc.update(sql, new Object[] { stuName, stuClass, stuSeat,
				subject, score, examId });
	}

	public int deleteExamScore(String examId) {
		String sql = "delete from t_score_detail where examid = ?";
		return scmJdbc.update(sql, new Object[] { examId });
	}

	public List<Map<String, Object>> getExamSubjectById(String examid) {
		String sql = "select distinct subject as subject from t_score_detail where examid = ?";
		return scmJdbc.queryForList(sql, new Object[] { examid });
	}

	public Pager getExamScoreDetailPage(String examid, int page, int rows) {
		String sql = "select distinct subject as subject from t_score_detail where examid = ?";
		List<Map<String, Object>> subjects = scmJdbc.queryForList(sql, new Object[] { examid });
		if (subjects.size() <= 0) {
			return null;
		}
		sql = "select class, name, seat";
		for (Map<String, Object> map : subjects) {
			sql += ", sum( if(subject = '" + map.get("subject")
					+ "', score, 0)) as '" + map.get("subject") + "'";
		}
		sql += ", sum(score) as all_score from t_score_detail where examid = ? group by class, name, seat order by all_score desc";

		String sqlCount = "select count(1) as count from (select count(1) from t_score_detail where examid = ? group by class,name,seat) as T";
		int total = scmJdbc.queryForInt(sqlCount, new Object[] { examid });
		Pager pager = new Pager();
		pager.setPageNo(page);
		pager.setPageSize(rows);
		pager.setTotal(total);

		if (page > 0 && rows > 0)
			sql += " limit " + (page - 1) * rows + "," + rows;
		// log.info("todo\t:" + sql);
		List<Map<String, Object>> resultList = scmJdbc.queryForList(sql,
				new Object[] { examid });

		int rank = (page - 1) * rows + 1;
		for (Map<String, Object> map : resultList) {
			map.put("rank", rank++);
		}

		pager.setResultList(resultList);

		return pager;
	}

	public int getExamScoreDetail(String examid,
			LinkedHashMap<String, String> propsMap,
			List<LinkedHashMap<String, Object>> dataList) {
		// 第1行，表头，各列顺序
		propsMap.put("class", "班级");
		propsMap.put("name", "姓名");
		propsMap.put("seat", "座位号");
		String sql = "select distinct subject as subject from t_score_detail where examid = ?";
		List<Map<String, Object>> subjects = scmJdbc.queryForList(sql, new Object[] { examid });
		if (subjects.size() <= 0) {
			return -1;
		}
		for (Map<String, Object> map : subjects) {
			propsMap.put((String)map.get("subject"), (String)map.get("subject"));
		}
		propsMap.put("all_score", "总分");
		propsMap.put("rank", "名次");
		
		
		sql = "select class, name, seat";
		for (Map<String, Object> map : subjects) {
			sql += ", sum( if(subject = '" + map.get("subject")
					+ "', score, 0)) as '" + map.get("subject") + "'";
		}
		sql += ", sum(score) as all_score from t_score_detail where examid = ? group by class, name, seat order by all_score desc";
		List<Map<String, Object>> resultList =  scmJdbc.queryForList(sql, new Object[]{ examid });
		int rank = 1;
		for (Map<String, Object> map : resultList) {
			LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
			dataMap.put("class", map.get("class"));
			dataMap.put("name", map.get("name"));
			dataMap.put("seat", map.get("seat"));
			for (Map<String, Object> map1 : subjects) {
				String key = (String)map1.get("subject");
				dataMap.put(key, map.get(key));
			}
			dataMap.put("all_score", map.get("all_score"));
			dataMap.put("rank", rank++);
			dataList.add(dataMap);
		}
		
		return dataList.size();
	}

}
