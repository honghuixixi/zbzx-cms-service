package com.aek.ebey.cms.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.base.BaseController;

import io.swagger.annotations.Api;

/**
 * <p>
 * 回复用户关联表  前端控制器
 * </p>
 *
 * @author xxx
 * @since 2017-11-03
 */
@RestController
@RequestMapping("/cms/cmsReplyUser")
@Api(value = "CmsReplyUserController", description = "回复用户管理")
public class CmsReplyUserController extends BaseController {

}
