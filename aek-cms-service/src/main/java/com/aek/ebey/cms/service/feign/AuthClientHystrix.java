package com.aek.ebey.cms.service.feign;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.aek.common.core.Result;
import com.aek.ebey.cms.service.feign.request.WeiXinServiceMessageRequest;

/**
 * 断路器
 *	
 * @author HongHui
 * @date   2017年12月13日
 */
@Component
public class AuthClientHystrix implements AuthClientService{

	private static final Logger logger = LogManager.getLogger(AuthClientHystrix.class);

	@Override
	public Result<List<Map<String,Object>>> sendWeiXinServiceMessage(WeiXinServiceMessageRequest request,
			String token) {
		logger.error("Auth Server is not connected!");
		logger.info("token = " + token);
		logger.info("tenantIds = " + request != null ? request.getTenantIds() : null);
		logger.info("type = " + request != null ? request.getType() : null);
		return null;
	}
	
	
}
