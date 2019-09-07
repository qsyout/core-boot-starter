package com.qsyout.core.dao.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.qsyout.core.dao.IbatisDao;
import com.qsyout.core.dao.impl.IbatisDaoImpl;

@Configuration
public class DaoConfig {

	@Bean
	public SqlSessionFactory getSqlSessionFactoryBean(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);

		ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
		factory.setMapperLocations(resolver.getResources("classpath*:/sqlmap/**/*.xml"));

		return factory.getObject();
	}

	@Bean
	public IbatisDao getIbatisDao(SqlSessionFactory sqlSessionFactory) {
		IbatisDaoImpl ibatisDao = new IbatisDaoImpl();
		ibatisDao.setSqlSessionFactory(sqlSessionFactory);
		return ibatisDao;
	}
}
