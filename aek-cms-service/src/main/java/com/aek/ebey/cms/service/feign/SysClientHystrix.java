package com.aek.ebey.cms.service.feign;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aek.common.core.Result;
import com.aek.ebey.cms.model.query.TenantQuery;
import com.aek.ebey.cms.model.vo.TenantsVo;
import com.aek.ebey.cms.service.feign.vo.TenantVo;
import com.alibaba.fastjson.JSON;

/**
 * 断路器
 */
@Component
public class SysClientHystrix implements SysClientService{

	private static final Logger logger = LoggerFactory.getLogger(SysClientHystrix.class);
	
	@Override
	public Result<List<TenantVo>> getMangeTree(Long tenantId, String token) {
		logger.debug("tenantId:"+tenantId+"token:"+token);
		return null;
	}

	@Override
	public Result<List<TenantVo>> getHospital(@RequestParam(value = "manageId", required = true) Long manageId,
			@RequestParam(value = "Keyword", required = false) String Keyword,
			@RequestParam(value = "type", required = false) Integer type, String token) {
		logger.debug("token:"+token);
		return null;
	}

	@Override
	public Result<List<Long>> getManageIds(Long tenantId, String token) {
		logger.debug("tenantId:"+tenantId+"token:"+token);
		return null;
	}

	@Override
	public Result<List<TenantsVo>> getTenants(@RequestParam(value = "tenantIds", required = true)Long[] tenantIds, String token) {
		logger.debug("tenantId:"+tenantIds+"token:"+token);
		return null;
	}

	@Override
	public Result<List<TenantVo>> getMangeTreeHelp(Long tenantId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<List<Long>> getManageTenantByTenantId(Long tenantId, String token) {
		return null;
	}
}
