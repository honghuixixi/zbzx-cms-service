package com.aek.ebey.cms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.ebey.cms.core.jackson.annotation.AllowProperty;
import com.aek.ebey.cms.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.cms.model.CmsReply;
import com.aek.ebey.cms.model.query.ReplyQuery;
import com.aek.ebey.cms.model.vo.PageVO;
import com.aek.ebey.cms.service.CmsReplyService;
import com.aek.ebey.cms.web.controller.request.ReplyRequest;
import com.aek.ebey.cms.web.validator.Add;
import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 *   前端控制器
 * </p>
 *
 * @author xxx
 * @since 2017-10-31
 */
@RestController
@RequestMapping("/cms/cmsReply")
@Api(value = "CmsReplyController", description = "回复管理")
public class CmsReplyController extends BaseController {
	@Autowired
	CmsReplyService cmsReplyService;
	
	
	@GetMapping(value="/getArticleReplyList")
	@IgnoreProperties(allow = {@AllowProperty(pojo = CmsReply.class, name = {"id","content","replyName","org","replyTime"}) })
	@ApiOperation(value = "文章回复分页列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=true),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=true),
		@ApiImplicitParam(name="id",value="文章id",paramType="query",required=true)})
	@ApiResponse(code = 0, message = "Ok",response=Result.class)
	public Result<PageVO<CmsReply>> getList(ReplyQuery query){
		return response(cmsReplyService.getReplyLists(query));
	}
	
	@GetMapping(value="/getBadAskReplyList")
	@IgnoreProperties(allow = {@AllowProperty(pojo = CmsReply.class, name = {"id","content","replyName","org","replyTime"}) })
	@ApiOperation(value = "不良事件或技术咨询回复分页列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=true),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=true),
		@ApiImplicitParam(name="id",value="不良事件或技术咨询id",paramType="query",required=true)})
	@ApiResponse(code = 0, message = "Ok",response=Result.class)
	public Result<Page<CmsReply>> getLists(ReplyQuery query){
		return response(cmsReplyService.getBadAskReplyList(query));
	}
	
	@PostMapping(value="/addArticleReply")
	@ApiOperation(value="提交文章评论",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="content",value="文章回复内容",paramType="request",required=true),
		@ApiImplicitParam(name="id",value="文章id",paramType="request",required=true)})
	@ApiResponse(code = 0, message = "Ok",response=Result.class)
	public Result<Object> addReply(@Validated(value=Add.class)@RequestBody ReplyRequest request){
		CmsReply reply = new CmsReply();
		reply.setContentId(request.getId());
//		if(request.getContent().length() > 300){
//			throw ExceptionFactory.create("r_006");
//		}
		reply.setContent(request.getContent());
		cmsReplyService.addReply(reply);
		return response();
	}
	
	@PostMapping(value="/addBadAskReply")
	@ApiOperation(value="提交技术咨询与不良事件评论",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="content",value="回复内容",paramType="request",required=true),
		@ApiImplicitParam(name="id",value="技术咨询或不良事件id",paramType="request",required=true)})
	@ApiResponse(code = 0, message = "Ok",response=Result.class)
	public Result<Object> addReplys(@Validated(value=Add.class)@RequestBody ReplyRequest request){
		CmsReply reply = new CmsReply();
		reply.setContentId(request.getId());
//		if(request.getContent().length() > 1000){
//			throw ExceptionFactory.create("r_007");
//		}
		reply.setContent(request.getContent());
		cmsReplyService.addReply(reply);
		return response();
	}
	
	@GetMapping(value="/deletReply")
	@ApiOperation(value="删除文章评论",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="articleId",value="文章id",paramType="body",required=true),
		@ApiImplicitParam(name="commentId",value="评论id",paramType="body",required=true)})
	@ApiResponse(code = 0, message = "Ok",response=Result.class)
	public Result<Object> deletReply(@RequestParam(value="articleId",required=false)Long contentId,
			@RequestParam(value="commentId",required=false)Long id){
		cmsReplyService.deletReply(contentId, id);
		return response();
	}
}
