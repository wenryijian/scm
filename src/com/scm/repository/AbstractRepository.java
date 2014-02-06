package com.scm.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.scm.util.Pager;

public class AbstractRepository {

	public int addObj(SimpleJdbcTemplate simpleJdbcTemplate, String tableName,
			Map<String, Object> properites) {
		int result = 0;
		if (simpleJdbcTemplate != null && tableName != null
				&& properites != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("insert into ").append(tableName);
			StringBuilder keys = new StringBuilder();
			StringBuilder values = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			Iterator<String> iterator = properites.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();

				if (keys.length() > 0)
					keys.append(",");
				keys.append(key);

				if (values.length() > 0)
					values.append(",");
				values.append("?");

				params.add(properites.get(key));
			}
			sb.append(" (").append(keys.toString()).append(") values (")
					.append(values.toString()).append(")");
			result = simpleJdbcTemplate.update(sb.toString(), params.toArray());
		}
		return result;
	}

	public int updateObj(SimpleJdbcTemplate simpleJdbcTemplate,
			String tableName, Map<String, Object> properites,
			Map<String, Object> conditions) {
		int result = 0;
		if (simpleJdbcTemplate != null && tableName != null
				&& properites != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("update ").append(tableName);
			StringBuilder values = new StringBuilder();
			StringBuilder wherecdns = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			Iterator<String> iterator = properites.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				if (values.length() > 0)
					values.append(",");
				values.append(key).append(" = ?");
				params.add(properites.get(key));
			}

			if (conditions != null) {
				iterator = conditions.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = String
							.valueOf(conditions.get(key) == null ? ""
									: conditions.get(key));
					if (wherecdns.length() > 0)
						wherecdns.append(" and ");

					String[] props = key.split(";");
					String[] vals = val.split(";");
					if (props.length == vals.length) {
						if (props.length > 1)
							wherecdns.append(" ( ");
						for (int i = 0; i < props.length; i++) {
							if (i > 0)
								wherecdns.append(" or ");
							String actKey = props[i];
							String actVal = vals[i];
							String[] keyType = actKey.split(":");
							if (keyType.length == 1) {
								wherecdns.append(actKey).append(" = ?");
								params.add(actVal);
							} else if (keyType.length == 2) {
								wherecdns.append(keyType[0]).append(
										" " + keyType[1] + " ?");
								params.add(actVal);
							}
						}
						if (props.length > 1)
							wherecdns.append(" ) ");
					}
				}
			}

			sb.append(" set ").append(values);
			if (wherecdns.length() > 0)
				sb.append(" where ").append(wherecdns);
			result = simpleJdbcTemplate.update(sb.toString(), params.toArray());
		}
		return result;
	}

	public int delObj(SimpleJdbcTemplate simpleJdbcTemplate, String tableName,
			Map<String, Object> conditions) {
		int result = 0;
		if (simpleJdbcTemplate != null && tableName != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("delete from ").append(tableName);
			StringBuilder wherecdns = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			if (conditions != null) {
				Iterator<String> iterator = conditions.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = String
							.valueOf(conditions.get(key) == null ? ""
									: conditions.get(key));
					if (wherecdns.length() > 0)
						wherecdns.append(" and ");

					String[] props = key.split(";");
					String[] vals = val.split(";");
					if (props.length == vals.length) {
						if (props.length > 1)
							wherecdns.append(" ( ");
						for (int i = 0; i < props.length; i++) {
							if (i > 0)
								wherecdns.append(" or ");
							String actKey = props[i];
							String actVal = vals[i];
							String[] keyType = actKey.split(":");
							if (keyType.length == 1) {
								wherecdns.append(actKey).append(" = ?");
								params.add(actVal);
							} else if (keyType.length == 2) {
								wherecdns.append(keyType[0]).append(
										" " + keyType[1] + " ?");
								params.add(actVal);
							}
						}
						if (props.length > 1)
							wherecdns.append(" ) ");
					}
				}
			}
			if (wherecdns.length() > 0)
				sb.append(" where ").append(wherecdns);
			result = simpleJdbcTemplate.update(sb.toString(), params.toArray());
		}
		return result;
	}

	public List<Map<String, Object>> getList(
			SimpleJdbcTemplate simpleJdbcTemplate, String tableName,
			Set<String> properites, Map<String, Object> conditions,
			String orderBy) {
		return getList(simpleJdbcTemplate, tableName, properites, conditions,
				orderBy, 0, 0);
	}

	public List<Map<String, Object>> getList(
			SimpleJdbcTemplate simpleJdbcTemplate, String tableName,
			Set<String> properites, Map<String, Object> conditions,
			String orderBy, int pageNo, int pageSize) {
		List<Map<String, Object>> resultList = null;
		if (simpleJdbcTemplate != null && tableName != null) {
			StringBuilder sb = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			if (properites != null) {
				Iterator<String> itr = properites.iterator();
				while (itr.hasNext()) {
					if (sb.length() == 0)
						sb.append("select ");
					else
						sb.append(",");
					sb.append(itr.next());
				}
			} else {
				sb.append("select *");
			}
			sb.append(" from ").append(tableName);

			if (conditions != null) {
				StringBuilder wherecdns = new StringBuilder();
				Iterator<String> iterator = conditions.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = String
							.valueOf(conditions.get(key) == null ? ""
									: conditions.get(key));
					if (wherecdns.length() > 0)
						wherecdns.append(" and ");

					String[] props = key.split(";");
					String[] vals = val.split(";");
					if (props.length == vals.length) {
						if (props.length > 1)
							wherecdns.append(" ( ");
						for (int i = 0; i < props.length; i++) {
							if (i > 0)
								wherecdns.append(" or ");
							String actKey = props[i];
							String actVal = vals[i];
							String[] keyType = actKey.split(":");
							if (keyType.length == 1) {
								wherecdns.append(actKey).append(" = ?");
								params.add(actVal);
							} else if (keyType.length == 2) {
								wherecdns.append(keyType[0]).append(
										" " + keyType[1] + " ?");
								params.add(actVal);
							}
						}
						if (props.length > 1)
							wherecdns.append(" ) ");
					}
				}
				if (wherecdns.length() > 0)
					sb.append(" where ").append(wherecdns);
			}

			if (orderBy != null && !"".equalsIgnoreCase(orderBy.trim()))
				sb.append(" order by ").append(orderBy);

			if (pageNo > 0 && pageSize > 0)
				sb.append(" limit " + (pageNo - 1) * pageSize + "," + pageSize);
			resultList = simpleJdbcTemplate.queryForList(sb.toString(),
					params.toArray());
		}
		return resultList;
	}

	public int getCount(SimpleJdbcTemplate simpleJdbcTemplate,
			String tableName, Map<String, Object> conditions) {
		int count = 0;
		if (simpleJdbcTemplate != null && tableName != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from ").append(tableName);
			StringBuilder wherecdns = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			if (conditions != null) {
				Iterator<String> iterator = conditions.keySet().iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String val = String
							.valueOf(conditions.get(key) == null ? ""
									: conditions.get(key));
					if (wherecdns.length() > 0)
						wherecdns.append(" and ");

					String[] props = key.split(";");
					String[] vals = val.split(";");
					if (props.length == vals.length) {
						if (props.length > 1)
							wherecdns.append(" ( ");
						for (int i = 0; i < props.length; i++) {
							if (i > 0)
								wherecdns.append(" or ");
							String actKey = props[i];
							String actVal = vals[i];
							String[] keyType = actKey.split(":");
							if (keyType.length == 1) {
								wherecdns.append(actKey).append(" = ?");
								params.add(actVal);
							} else if (keyType.length == 2) {
								wherecdns.append(keyType[0]).append(
										" " + keyType[1] + " ?");
								params.add(actVal);
							}
						}
						if (props.length > 1)
							wherecdns.append(" ) ");
					}
				}
			}
			if (wherecdns.length() > 0)
				sb.append(" where ").append(wherecdns);
			count = simpleJdbcTemplate.queryForInt(sb.toString(),
					params.toArray());
		}
		return count;
	}

    public Pager getPager(SimpleJdbcTemplate simpleJdbcTemplate, String tableName, Set<String> properites, Map<String, Object> conditions, String orderBy, int pageNo, int pageSize) {
    	Pager pager = new Pager();
    	pager.setPageNo(pageNo);
    	pager.setPageSize(pageSize);
    	if (simpleJdbcTemplate != null && tableName != null) {
    	    int total = getCount(simpleJdbcTemplate, tableName, conditions);
    	    pager.setTotal(total);
    	    List<Map<String, Object>> resultList = getList(simpleJdbcTemplate, tableName, properites, conditions, orderBy, pageNo, pageSize);
    	    pager.setResultList(resultList);
    	}
    	return pager;
    }
    
    public TransactionStatus getTransactionStatus(DataSourceTransactionManager transactionManager) {
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();	    
    	def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
    	return transactionManager.getTransaction(def);
    }
    
    public void rollbackTransaction(DataSourceTransactionManager transactionManager, TransactionStatus status) {
    	transactionManager.rollback(status);
    }
    
    public void commitTransaction(DataSourceTransactionManager transactionManager, TransactionStatus status) {
    	transactionManager.commit(status);
    }
}
