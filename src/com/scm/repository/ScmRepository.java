package com.scm.repository;

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

@Repository
public class ScmRepository {
	private static final Log log = LogFactory.getLog(ScmRepository.class);
	
	@Autowired
	@Qualifier("scmSimpleJdbcTemplate")
	SimpleJdbcTemplate scmJdbc;

	@Autowired
	@Qualifier("transactionManager")
	DataSourceTransactionManager txManager;
	
	public void query_test(){
		String sql = "select * from test_table";
		List<Map<String, Object>> result = scmJdbc.queryForList(sql, new Object[]{});
		for (Map<String, Object> map : result){
			log.info(map.get("name") + "\t" + map.get("score"));
		}
	}
}
	