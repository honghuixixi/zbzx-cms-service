package com.aek.ebey.cms.core.config;

import java.io.IOException;

import javax.sql.DataSource;

import com.baomidou.mybatisplus.enums.IdType;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.AutoSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;

@Configuration
public class MybatisConfig {

	@Bean("sqlSessionFactory")
	public MybatisSqlSessionFactoryBean createSqlSessionFactory(DataSource dataSource) throws IOException {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/*.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.aek.*.*.model");
		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbColumnUnderline(true);
		globalConfig.setIdType(IdType.AUTO.getKey());
		globalConfig.setSqlInjector(new AutoSqlInjector());
		sqlSessionFactory.setGlobalConfig(globalConfig);
		sqlSessionFactory.setPlugins(new Interceptor[]{paginationInterceptor()});
		return sqlSessionFactory;
	}

	@Bean
	public MapperScannerConfigurer createMapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("com.aek.*.*.mapper");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		return mapperScannerConfigurer;
	}
	
	@Bean
	public PaginationInterceptor paginationInterceptor(){
		PaginationInterceptor pagination = new PaginationInterceptor();
		pagination.setDialectType("mysql");
		return pagination;
	}
	
}
