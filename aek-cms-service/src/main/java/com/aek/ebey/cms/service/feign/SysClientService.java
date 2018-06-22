package com.aek.ebey.cms.service.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aek.common.core.Result;
import com.aek.ebey.cms.model.query.TenantQuery;
import com.aek.ebey.cms.model.vo.TenantsVo;
import com.aek.ebey.cms.service.feign.vo.TenantVo;


/**
 * 用户远程调用接口value=${feign-sys.serviceId}
 */
@FeignClient(value="${feign-zbzx-sys.serviceId}", fallback = SysClientHystrix.class)
public interface SysClientService {

	/**
	 * 获取监管树
	 * 
	 * @param tenantId
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/zbzxsys/tenant/getmanageTreeForCms")
	Result<List<TenantVo>> getMangeTree(@RequestParam(value = "tenantId", required = true) Long tenantId,
			@RequestHeader("X-AEK56-Token") String token);
	
	/**
	 * 获取监管树help
	 * 
	 * @param tenantId
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/zbzxsys/tenant/getManageTreeForCmsHelp")
	Result<List<TenantVo>> getMangeTreeHelp(@RequestParam(value = "tenantId", required = true) Long tenantId,
			@RequestHeader("X-AEK56-Token") String token);
	/**
	 * 获取医疗机构
	 * 
	 * @param query
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/zbzxsys/tenant/getHospitalForCms")
	Result<List<TenantVo>> getHospital(@RequestParam(value = "manageId", required = true) Long manageId,
			@RequestParam(value = "Keyword", required = false) String Keyword,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestHeader("X-AEK56-Token") String token);
	
	/**
	 * CMS根据医疗机构获取监管机构Ids
	 * 
	 * @param query
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/zbzxsys/tenant/getManageIdsForCms")
	Result<List<Long>> getManageIds(@RequestParam(value = "tenantId", required = true) Long tenantId,
			@RequestHeader("X-AEK56-Token") String token);
	
	/**
	 * 根据机构id集合查询机构名称
	 * @param tenantIds
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/zbzxsys/invoke/tenant/getTenants")
	Result<List<TenantsVo>> getTenants(@RequestParam(value = "tenantIds", required = true)Long[] tenantIds,
			@RequestHeader("X-AEK56-Token") String token);

	/**
	 * 根据tenantId获取所有上级监管机构ID集合
	 *
	 * @param query
	 * @param token
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/zbzxsys/tenant/getManageTenantByTenantId")
	Result<List<Long>> getManageTenantByTenantId(@RequestParam(value = "tenantId", required = true) Long tenantId,
									@RequestHeader("X-AEK56-Token") String token);
}
