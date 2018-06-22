package com.aek.ebey.cms.core.plugins;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.util.ReflectHelper;

/**
 * MyBatis上下文插件：主要用于拦截tenantId及数据范围
 *
 * @author Honghui
 * @date 2017年6月27日
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes" })
@Intercepts( {
	@Signature(type = Executor.class, method = "query", args =
		{MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
	@Signature(type = Executor.class, method = "query", args =
		{MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,CacheKey.class, BoundSql.class}), } 
)
public class AekMyBatisContextPlugin implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AekMyBatisContextPlugin.class);

	//需要进行tenantId处理的字符串正则表达式
	private static String tenantPattern = "^.*tenantId.*$";
	
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECT_FACTORY = new DefaultReflectorFactory();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameter = args[1];
		RowBounds rowBounds = (RowBounds) args[2];
		ResultHandler resultHandler = (ResultHandler) args[3];
		Executor executor = (Executor) invocation.getTarget();
		CacheKey cacheKey;
		BoundSql boundSql;
		// 由于逻辑关系，只会进入一次
		if (args.length == 4) {
			// 4 个参数时
			boundSql = ms.getBoundSql(parameter);
			cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
		} else {
			// 6 个参数时
			cacheKey = (CacheKey) args[4];
			boundSql = (BoundSql) args[5];
		}
		// TODO 自己要进行的各种处理
		LOGGER.info("被[AekMyBatisContextPlugin]拦截的SQL = " + boundSql.getSql());
		LOGGER.info("被[AekMyBatisContextPlugin]拦截的SQL的参数 = " + parameter);
		AuthUser user = WebSecurityUtils.getCurrentUser();
		//String uri = SpringWebUtil.getRequest().getRequestURI();
		LOGGER.info("当前登录用户=" + user);
		String msId = ms.getId().toLowerCase();
		LOGGER.info("当前SQL ID=" + msId);
		//如果执行sql的id与tenant相匹配则进行租户id条件过滤
		if (msId.matches(tenantPattern)) {
			LOGGER.info("当前登录用户机构ID=" + user.getTenantId());
			//return handleStatementHandler(invocation,user.getTenantId());
			String sql = boundSql.getSql();
			if(StringUtils.isNotBlank(sql) && sql.toLowerCase().contains("where")){
				sql = sql.toLowerCase();
				String[] sqls = sql.split("where");
				String newSql = "";
				if(StringUtils.isNotBlank(sqls[1]) && !(sqls[1].toLowerCase().trim().startsWith("order by") || sqls[1].toLowerCase().trim().startsWith("group by"))){
					if(sql.contains("sys_tenant")){
						newSql = sqls[0] + " where " +" id=" + user.getTenantId() +" and "+ sqls[1];
					}else{
						newSql = sqls[0] + " where " +" tenant_id=" + user.getTenantId() +" and "+ sqls[1];
					}
				}else{
					if(sql.contains("sys_tenant")){
						newSql = sqls[0] + " where " +" id=" + user.getTenantId();
					}else{
						newSql = sqls[0] + " where " +" tenant_id=" + user.getTenantId();
					}
				}
				ReflectHelper.setFieldValue(boundSql, "sql", newSql); 
			}
		}
		// 注：下面的方法可以根据自己的逻辑调用多次
		return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
	}

	@SuppressWarnings("unused")
	private Object handleStatementHandler(Invocation invocation,Long tenantId) throws InvocationTargetException, IllegalAccessException{
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECT_FACTORY);
        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
        //分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object,DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,DEFAULT_REFLECT_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object,DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,DEFAULT_REFLECT_FACTORY);
        }
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        String sql = boundSql.getSql();
        //重写sql
        String newSql = sql + " and tenant_id=" + tenantId;
        metaStatementHandler.setValue("delegate.boundSql.sql", newSql);
		return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	public String getTenantPattern() {
		return tenantPattern;
	}

	@SuppressWarnings("static-access")
	public void setTenantPattern(String tenantPattern) {
		this.tenantPattern = tenantPattern;
	}

}
