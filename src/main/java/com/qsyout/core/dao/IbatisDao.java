package com.qsyout.core.dao;

import java.util.List;

import com.qsyout.core.entity.PageInfo;

public interface IbatisDao {

	public boolean insert(String statementName, Object params);

	public boolean update(String statementName, Object params);

	public boolean delete(String statementName, Object params);

	public <T> T load(String statementName, Object params);

	@SuppressWarnings("rawtypes")
	public List queryForList(String statementName, Object params);

	public PageInfo pageQuery(String statementName,Object params,int pageNumber,int pageSize);
}
