package com.qsyout.core.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qsyout.core.dao.IbatisDao;
import com.qsyout.core.entity.PageInfo;

@SuppressWarnings({ "rawtypes" })
@Transactional(propagation = Propagation.NESTED)
public class IbatisDaoImpl extends SqlSessionDaoSupport implements IbatisDao {

	@Override
	public boolean insert(String statementName, Object params) {
		return getSqlSession().insert(statementName, params) > 0;
	}

	@Override
	public boolean update(String statementName, Object params) {
		return getSqlSession().update(statementName, params) > 0;
	}

	@Override
	public boolean delete(String statementName, Object params) {
		return getSqlSession().delete(statementName, params) > 0;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public <T> T load(String statementName, Object params) {
		return getSqlSession().selectOne(statementName, params);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List queryForList(String statementName, Object params) {
		return getSqlSession().selectList(statementName, params);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	private String getNameSql(String statementName, Object params) {
		return SqlHelper.getNamespaceSql(this.getSqlSession(), statementName, params);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public PageInfo pageQuery(String statementName, Object params, int pageNumber, int pageSize) {
		String querySql = this.getNameSql(statementName, params);
		String countSql = String.format("select COUNT(1) FROM (%s) as temp", querySql);
		querySql = querySql + " limit " + ((pageNumber - 1) * pageSize < 0 ? 0 : (pageNumber - 1) * pageSize) + ","
				+ pageSize;

		return new PageInfo(getSqlSession().selectOne("PUBLIC.countSql", countSql),
				getSqlSession().selectList("PUBLIC.querySql", querySql));
	}

}
