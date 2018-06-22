package com.aek.ebey.cms.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.ebey.cms.model.vo.CurrentUserVO;
import com.aek.ebey.cms.service.CmsContentUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 *   前端控制器
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
@RestController
@RequestMapping("/cms/cmsContentUser")
@Api(value = "CmsContentUserController", description = "内容用户管理")
public class CmsContentUserController extends BaseController {

	@Autowired
	CmsContentUserService cmsContentUserService;
	
	@GetMapping(value="/getCurrentUser")
	@ApiOperation(value="新建投诉与技术咨询获取用户信息",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<CurrentUserVO> getCurrentUser(){
		return response(cmsContentUserService.getCurrentUser());
	}
	
	@GetMapping(value="/getUser")
	@ApiOperation(value="新建文章通知消息获取用户信息",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<CurrentUserVO> getUser(){
		return response(cmsContentUserService.getUser());
	}
}
