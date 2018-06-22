package com.aek.ebey.cms.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseController;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.ebey.cms.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.cms.enums.WeiXinServiceMessageTypeEnum;
import com.aek.ebey.cms.core.jackson.annotation.AllowProperty;
import com.aek.ebey.cms.model.CmsContent;
import com.aek.ebey.cms.model.bo.BadAskBo;
import com.aek.ebey.cms.model.bo.ConsultBo;
import com.aek.ebey.cms.model.query.ComplaintQuery;
import com.aek.ebey.cms.model.query.ConsultationQuery;
import com.aek.ebey.cms.model.query.ContentQuery;
import com.aek.ebey.cms.model.query.TenantQuery;
import com.aek.ebey.cms.model.vo.BadAskConsultVO;
import com.aek.ebey.cms.model.vo.ComplaintVO;
import com.aek.ebey.cms.model.vo.ConsultationVO;
import com.aek.ebey.cms.model.vo.ContentDetailVO;
import com.aek.ebey.cms.model.vo.ContentReturnVo;
import com.aek.ebey.cms.model.vo.ContentVO;
import com.aek.ebey.cms.model.vo.ManageTreeVO;
import com.aek.ebey.cms.service.CmsContentService;
import com.aek.ebey.cms.service.feign.AuthClientService;
import com.aek.ebey.cms.service.feign.SysClientService;
import com.aek.ebey.cms.service.feign.request.WeiXinServiceMessageRequest;
import com.aek.ebey.cms.service.feign.vo.TenantVo;
import com.aek.ebey.cms.web.controller.request.BadAskConsultRequest;
import com.aek.ebey.cms.web.controller.request.NewArticleOrNewsOrNoticeRequest;
import com.aek.ebey.cms.web.validator.Add;
import com.aek.ebey.cms.web.validator.Edit;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * <p>
 *   内容控制器
 * </p>
 *
 * @since 2017-10-25
 */
@RestController
@RequestMapping("/cms/cmsContent")
@Api(value = "CmsContentController", description = "内容管理")
public class CmsContentController extends BaseController {
	
	@Autowired
	private CmsContentService cmsContentService;
	
	@Autowired
	private SysClientService sysClientService;
	
	@Autowired
	private AuthClientService authClientService;
	
	@IgnoreProperties(allow = {@AllowProperty(pojo = CmsContent.class, name = {"article","complaint","consultation","news","notice"}) })
	@GetMapping(value="/stats")
	@ApiOperation(value="监管机构、医疗机构统计数目未读提示",httpMethod="GET")
	@ApiResponse(code = 200, message = "OK",response=Result.class)
	public Result<CmsContent> stats(){
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		int tenantType = currentUser.getTenantType();
		if(tenantType ==1){
			return response(cmsContentService.statsForHospital());
		}else if (tenantType ==2) {
			return response(cmsContentService.statsForManage());
		}
		return response(null);
	};
	
	
	@GetMapping(value="/getNoticeList")
	@ApiOperation(value="获取通知列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ContentVO>> getNoticeList(ContentQuery query){
		//通知
		query.setType(0);
		return response(cmsContentService.getContentList(query));
	}
	
	@GetMapping(value="/getNewsList")
	@ApiOperation(value="获取消息列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ContentVO>> getNewsList(ContentQuery query){
		//消息
		query.setType(1);
		return response(cmsContentService.getContentList(query));
	}
	
	@GetMapping(value="/getArticleList")
	@ApiOperation(value="获取文章列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ContentVO>> getArticleList(ContentQuery query){
		//文章
		query.setType(2);
		return response(cmsContentService.getContentList(query));
	}
	
	/********************************小程序******************************************/
	@GetMapping(value="/getNoticeListForMobile")
	@ApiOperation(value="获取通知列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ContentVO>> getNoticeListForMobile(ContentQuery query){
		//通知
		query.setType(0);
		return response(cmsContentService.getContentListForMobile(query));
	}
	
	@GetMapping(value="/getNewsListForMobile")
	@ApiOperation(value="获取消息列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ContentVO>> getNewsListForMobile(ContentQuery query){
		//消息
		query.setType(1);
		return response(cmsContentService.getContentListForMobile(query));
	}
	
	@GetMapping(value="/getArticleListForMobile")
	@ApiOperation(value="获取文章列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ContentVO>> getArticleListForMobile(ContentQuery query){
		//文章
		query.setType(2);
		return response(cmsContentService.getContentListForMobile(query));
	}	
	
	//为消息平台小程序提供首页列表
	@GetMapping(value="/getFrontList")
	@ApiOperation(value="消息平台小程序提供首页列表",httpMethod="GET")
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<List<ContentVO>> getFrontList(){
		return response(cmsContentService.getFrontList());
	}	
	/*******************************end*********************************************/
	
	@GetMapping(value="/getComplaintList")
	@ApiOperation(value="获取投诉与不良事件列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="type",value="投诉类型",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ComplaintVO>> getList(ComplaintQuery query){
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		int tenantType = currentUser.getTenantType();
		if(tenantType ==1){
			return response(cmsContentService.getComplaintList(query));
		}else if (tenantType ==2) {
			return response(cmsContentService.getComplaintListForManage(query));
		}
		return response(null);
	}
		
	@GetMapping(value="/getConsultationList")
	@ApiOperation(value="获取获取技术咨询列表",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="pageNo",value="起始页 [默认1]",paramType="query",required=false),
		@ApiImplicitParam(name="pageSize",value="分页大小[默认10]",paramType="query",required=false),
		@ApiImplicitParam(name="type",value="技术咨询类型",paramType="query",required=false),
		@ApiImplicitParam(name="keyword",value="关键字",paramType="query",required=false)})
	@ApiResponse(code =200, message = "OK",response=Result.class)
	public Result<Page<ConsultationVO>> getListForMange(ConsultationQuery query){
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		int tenantType = currentUser.getTenantType();
		if(tenantType ==1){
			return response(cmsContentService.getConsultationList(query));
		}else if (tenantType ==2) {
			return response(cmsContentService.getConsultionListForManage(query));
		}
		return response(null);
		
	}
		
	@GetMapping(value="/getDetail")
	@ApiOperation(value = "消息、通知、文章内容详情",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<ContentDetailVO> getDetail(@RequestParam(required=true) Long id){
		return response(cmsContentService.getDetail(id));
	}
	
	@GetMapping(value="/getContentReturn")
	@ApiOperation(value = "编辑消息、通知、文章内容返回详情",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<ContentReturnVo> getContentReturn(@RequestParam(required=true) Long id){
		return response(cmsContentService.getContentReturn(id));
	}
	
	@GetMapping(value="/deleteById")
	@ApiOperation(value = "删除消息、通知、文章",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> deleteById(@RequestParam(required=true) Long id){
		cmsContentService.deleteById(id);
		return response();
	}
	
	@PostMapping(value="/newArticle")
	@ApiOperation(value = "发布文章",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="title",value="投诉标题",paramType="request",required=true),
		@ApiImplicitParam(name="content",value="投诉内容",paramType="request",required=true),
		@ApiImplicitParam(name="ids",value="机构id集合",paramType="request",required=true)})
	@ApiResponse(code = 200, message = "OK",response=Result.class)
	public Result<Object> newArticle(@Validated(value=Add.class)@RequestBody NewArticleOrNewsOrNoticeRequest request){
		CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		//处理附件
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}
		Long tenantId = currentUser.getTenantId();

		//获取所有上级监管机构ID
		Result<List<Long>> result = sysClientService.getManageTenantByTenantId(tenantId,WebSecurityUtils.getCurrentToken());
		List<Long> manageTenantIds = new ArrayList<>();
		if (null != result) {
			manageTenantIds = result.getData();
		}

		//处理内容分发的机构id集合
		Long[] ids = request.getIds();
		if(ids !=null && ids.length>0){
			StringBuilder sb = new StringBuilder();
			for (Long id : ids) {
				sb.append(id.toString()+",");
			}
			if (null != manageTenantIds && manageTenantIds.size() > 0) {
				for(Long manageTenantId : manageTenantIds) {
					sb.append(manageTenantId.toString() + ",");
				}
			}
			cmsContent.setToTenantIdsCopy(sb.toString().substring(0, sb.length()-1));
			sb.append(tenantId);
			cmsContent.setToTenantIds(sb.toString());
		}					
		cmsContentService.newArticle(cmsContent);	
		return response();
	}
	
	@PostMapping(value="/editArticle")
	@ApiOperation(value = "修改文章",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="title",value="投诉标题",paramType="request",required=true),
		@ApiImplicitParam(name="content",value="投诉内容",paramType="request",required=true),
		@ApiImplicitParam(name="ids",value="机构id集合",paramType="request",required=true)})
	@ApiResponse(code = 200, message = "OK",response=Result.class)
	public Result<Object> editArticle(@Validated(value=Edit.class)@RequestBody NewArticleOrNewsOrNoticeRequest request){
		CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		//处理附件
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}
		Long tenantId = currentUser.getTenantId();

		//获取所有上级监管机构ID
		Result<List<Long>> result = sysClientService.getManageTenantByTenantId(tenantId,WebSecurityUtils.getCurrentToken());
		List<Long> manageTenantIds = new ArrayList<>();
		if (null != result) {
			manageTenantIds = result.getData();
		}

		//处理内容分发的机构id集合
		Long[] ids = request.getIds();
		if(ids !=null && ids.length>0){
			StringBuilder sb = new StringBuilder();
			for (Long id : ids) {
				sb.append(id.toString()+",");
			}
			if (null != manageTenantIds && manageTenantIds.size() > 0) {
				for(Long manageTenantId : manageTenantIds) {
					sb.append(manageTenantId.toString() + ",");
				}
			}
			cmsContent.setToTenantIdsCopy(sb.toString().substring(0, sb.length()-1));
			sb.append(tenantId);
			cmsContent.setToTenantIds(sb.toString());
		}	
		cmsContentService.edit(cmsContent);	
		return response();
	}
	
	@PostMapping(value="/newNotice")
	@ApiOperation(value = "发布通知",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="title",value="投诉标题",paramType="request",required=true),
		@ApiImplicitParam(name="content",value="投诉内容",paramType="request",required=true),
		@ApiImplicitParam(name="ids",value="机构id集合",paramType="request",required=true)})
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> newNotice(@Validated(value=Add.class)@RequestBody NewArticleOrNewsOrNoticeRequest request){
		CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		//处理附件
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}
		Long tenantId = currentUser.getTenantId();

		//获取所有上级监管机构ID
		Result<List<Long>> result = sysClientService.getManageTenantByTenantId(tenantId,WebSecurityUtils.getCurrentToken());
		List<Long> manageTenantIds = new ArrayList<>();
		if (null != result) {
			manageTenantIds = result.getData();
		}

		//处理内容分发的机构id集合
		Long[] ids = request.getIds();
		if(ids !=null && ids.length>0){
			StringBuilder sb = new StringBuilder();
			for (Long id : ids) {
				sb.append(id.toString()+",");
			}
			if (null != manageTenantIds && manageTenantIds.size() > 0) {
				for(Long manageTenantId : manageTenantIds) {
					sb.append(manageTenantId.toString() + ",");
				}
			}
			cmsContent.setToTenantIdsCopy(sb.toString().substring(0, sb.length()-1));
			sb.append(tenantId);
			cmsContent.setToTenantIds(sb.toString());
		}		
		cmsContentService.newNotice(cmsContent);	
		return response();
	}
	
	@PostMapping(value="/editNotice")
	@ApiOperation(value = "修改通知",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="title",value="投诉标题",paramType="request",required=true),
		@ApiImplicitParam(name="content",value="投诉内容",paramType="request",required=true),
		@ApiImplicitParam(name="ids",value="机构id集合",paramType="request",required=true)})
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> editNotice(@Validated(value=Edit.class)@RequestBody NewArticleOrNewsOrNoticeRequest request){
		CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		//处理附件
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}
		Long tenantId = currentUser.getTenantId();

		//获取所有上级监管机构ID
		Result<List<Long>> result = sysClientService.getManageTenantByTenantId(tenantId,WebSecurityUtils.getCurrentToken());
		List<Long> manageTenantIds = new ArrayList<>();
		if (null != result) {
			manageTenantIds = result.getData();
		}

		//处理内容分发的机构id集合
		Long[] ids = request.getIds();
		if(ids !=null && ids.length>0){
			StringBuilder sb = new StringBuilder();
			for (Long id : ids) {
				sb.append(id.toString()+",");
			}
			if (null != manageTenantIds && manageTenantIds.size() > 0) {
				for(Long manageTenantId : manageTenantIds){
					sb.append(manageTenantId.toString() + ",");
				}
			}
			cmsContent.setToTenantIdsCopy(sb.toString().substring(0, sb.length()-1));
			sb.append(tenantId);
			cmsContent.setToTenantIds(sb.toString());
		}
		cmsContentService.edit(cmsContent);
		return response();
	}
	
	@PostMapping(value="/newNews")
	@ApiOperation(value = "发布消息",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="title",value="投诉标题",paramType="request",required=true),
		@ApiImplicitParam(name="content",value="投诉内容",paramType="request",required=true),
		@ApiImplicitParam(name="ids",value="机构id集合",paramType="request",required=true)})
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> newNews(@Validated(value=Add.class)@RequestBody NewArticleOrNewsOrNoticeRequest request){
		CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		//处理附件
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}
		
		Long tenantId = currentUser.getTenantId();

		//获取所有上级监管机构ID
		Result<List<Long>> result = sysClientService.getManageTenantByTenantId(tenantId,WebSecurityUtils.getCurrentToken());
		List<Long> manageTenantIds = new ArrayList<>();
		if (null != result) {
			manageTenantIds = result.getData();
		}

		//处理内容分发的机构id集合
		Long[] ids = request.getIds();
		if(ids !=null && ids.length>0){
			StringBuilder sb = new StringBuilder();
			for (Long id : ids) {
				sb.append(id.toString()+",");
			}
			if (null != manageTenantIds && manageTenantIds.size() > 0) {
				for(Long manageTenantId : manageTenantIds) {
					sb.append(manageTenantId.toString() + ",");
				}
			}
			cmsContent.setToTenantIdsCopy(sb.toString().substring(0, sb.length()-1));
			sb.append(tenantId);
			cmsContent.setToTenantIds(sb.toString());
		}		
		cmsContentService.newNews(cmsContent);				
		return response();
	}
	
	@PostMapping(value="/editNews")
	@ApiOperation(value = "修改消息",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="title",value="投诉标题",paramType="request",required=true),
		@ApiImplicitParam(name="content",value="投诉内容",paramType="request",required=true),
		@ApiImplicitParam(name="ids",value="机构id集合",paramType="request",required=true)})
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> editNews(@Validated(value=Edit.class)@RequestBody NewArticleOrNewsOrNoticeRequest request){
		CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		//处理附件
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}
		
		Long tenantId = currentUser.getTenantId();

		//获取所有上级监管机构ID
		Result<List<Long>> result = sysClientService.getManageTenantByTenantId(tenantId,WebSecurityUtils.getCurrentToken());
		List<Long> manageTenantIds = new ArrayList<>();
		if (null != result) {
			manageTenantIds = result.getData();
		}

		//处理内容分发的机构id集合
		Long[] ids = request.getIds();
		if(ids !=null && ids.length>0){
			StringBuilder sb = new StringBuilder();
			for (Long id : ids) {
				sb.append(id.toString()+",");
			}
			if (null != manageTenantIds && manageTenantIds.size() > 0) {
				for(Long manageTenantId : manageTenantIds) {
					sb.append(manageTenantId.toString() + ",");
				}
			}
			cmsContent.setToTenantIdsCopy(sb.toString().substring(0, sb.length()-1));
			sb.append(tenantId);
			cmsContent.setToTenantIds(sb.toString());
		}
		cmsContentService.edit(cmsContent);			
		return response();
	}
	
	@PostMapping(value="/newBadAsk")
	@ApiOperation(value = "提交投诉与不良事件",httpMethod="POST",produces = "application/json")
	@ApiImplicitParams({@ApiImplicitParam(name="email",value="申请人邮箱",paramType="query",required=true),
		@ApiImplicitParam(name="wx",value="申请人微信号",paramType="query",required=true),
		@ApiImplicitParam(name="manufacturer",value="厂商",paramType="query",required=true),
		@ApiImplicitParam(name="type",value="投诉或技术咨询类型",paramType="query",required=true),
		@ApiImplicitParam(name="title",value="标题",paramType="query",required=true),
		@ApiImplicitParam(name="content",value="内容",paramType="query",required=true),
		@ApiImplicitParam(name="url",value="附件路径",paramType="query",required=true),
		@ApiImplicitParam(name="productor",value="产品名称",paramType="query",required=true)})
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> newBadAsk(@Validated(value=Add.class)@RequestBody BadAskConsultRequest request){
		//CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		CmsContent cmsContent=new CmsContent();
		//处理自定义数据
		BadAskBo badAskBo = new BadAskBo();
		badAskBo.setEmail(request.getEmail());
		badAskBo.setWx(request.getWx());
		badAskBo.setManufacturer(request.getManufacturer());
		badAskBo.setProductor(request.getProductor());
		badAskBo.setComplaintType(request.getType());
		badAskBo.setPhone(WebSecurityUtils.getCurrentUser().getMobile());
		String jsonString = JSON.toJSONString(badAskBo);
		cmsContent.setCustomData(jsonString);
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}	
		cmsContent.setTitle(request.getTitle());
		cmsContent.setContent(request.getContent());
		cmsContent.setType(3);
		cmsContentService.newBadAskConsult(cmsContent);
		return response();
	}
	
	@PostMapping(value="/newConsult")
	@ApiOperation(value = "提交技术咨询",httpMethod="POST",produces = "application/json")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	@ApiImplicitParams({@ApiImplicitParam(name="email",value="申请人邮箱",paramType="query",required=true),
		@ApiImplicitParam(name="wx",value="申请人微信号",paramType="query",required=true),
		@ApiImplicitParam(name="manufacturer",value="厂商",paramType="query",required=true),
		@ApiImplicitParam(name="type",value="投诉或技术咨询类型",paramType="query",required=true),
		@ApiImplicitParam(name="title",value="标题",paramType="query",required=true),
		@ApiImplicitParam(name="content",value="内容",paramType="query",required=true),
		@ApiImplicitParam(name="url",value="附件路径",paramType="query",required=true),
		@ApiImplicitParam(name="productor",value="产品名称",paramType="query",required=true)})
	public Result<Object> newConsult(@Validated(value=Add.class)@RequestBody BadAskConsultRequest request){
		//CmsContent cmsContent = BeanMapper.map(request, CmsContent.class);
		CmsContent cmsContent=new CmsContent();
		//处理自定义数据
		ConsultBo consultBo = new ConsultBo();
		consultBo.setEmail(request.getEmail());
		consultBo.setWx(request.getWx());
		consultBo.setManufacturer(request.getManufacturer());
		consultBo.setProductor(request.getProductor());
		consultBo.setConsultationType(request.getType());
		consultBo.setPhone(WebSecurityUtils.getCurrentUser().getMobile());
		String jsonString = JSON.toJSONString(consultBo);
		cmsContent.setCustomData(jsonString);
		if(request.getFiles() != null && request.getFiles().size()>0){
			String url = JSON.toJSONString(request.getFiles());
			cmsContent.setUrl(url);
		}	
		cmsContent.setTitle(request.getTitle());
		cmsContent.setContent(request.getContent());
		cmsContent.setType(4);
		cmsContentService.newBadAskConsult(cmsContent);
		return response();
	}
	
	@GetMapping(value="/deleteBadConsultById")
	@ApiOperation(value = "删除技术咨询、不良事件",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<Object> deleteBadConsultById(@RequestParam(required=true) Long id){
		cmsContentService.deleteBadConsultById(id);
		return response();
	}
	
	@GetMapping(value="/getBadAsk")
	@ApiOperation(value = "获取投诉与不良事件详情",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<BadAskConsultVO> getBadAsk(@RequestParam(value="id")Long id){
		return response(cmsContentService.getBadAsk(id));
	}
	
	@GetMapping(value="/getConsult")
	@ApiOperation(value = "获取技术咨询详情",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<BadAskConsultVO> getConsult(@RequestParam(value="id")Long id){
		return response(cmsContentService.getConsult(id));
	}
	
	@GetMapping(value="/getManageTree")
	@IgnoreProperties(allow = {@AllowProperty(pojo = TenantVo.class, name = {"id","name"}) })
	@ApiOperation(value = "获取监管树",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<List<ManageTreeVO>> getManageTree(){
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String currentToken = WebSecurityUtils.getCurrentToken();
		Long tenantId = currentUser.getTenantId();
		String tenantName = currentUser.getTenantName();
		Result<List<TenantVo>> result = sysClientService.getMangeTree(tenantId, currentToken);
		List<ManageTreeVO> manageTreeList = Lists.newArrayList();
		//构造父节点
		ManageTreeVO manageTreeVOParent = new ManageTreeVO();
		manageTreeVOParent.setpId(0L);
		manageTreeVOParent.setId(tenantId);
		manageTreeVOParent.setName(tenantName);
		manageTreeList.add(manageTreeVOParent);
		//构造子节点
		if(result != null){
			List<TenantVo> mangeTree = result.getData();
			if(mangeTree != null && mangeTree.size() >0){
				if(manageTreeList != null && manageTreeList.size() > 0){
					for (TenantVo tenantVo : mangeTree) {
						ManageTreeVO manageTreeVO = new ManageTreeVO();
						manageTreeVO.setId(tenantVo.getId());
						manageTreeVO.setName(tenantVo.getName());
						manageTreeVO.setpId(tenantVo.getManageTenantId());
						manageTreeList.add(manageTreeVO);
					}	
				}
			}
		}				
		return response(manageTreeList);
	}
	
	@GetMapping(value="/getManageTreeHelp")
	@IgnoreProperties(allow = {@AllowProperty(pojo = TenantVo.class, name = {"id","name"}) })
	@ApiOperation(value = "获取监管树",httpMethod="GET")
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<List<ManageTreeVO>> getManageTreeHelp(){
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String currentToken = WebSecurityUtils.getCurrentToken();
		Long tenantId = currentUser.getTenantId();
		String tenantName = currentUser.getTenantName();
		Result<List<TenantVo>> result = sysClientService.getMangeTreeHelp(tenantId, currentToken);
		List<ManageTreeVO> manageTreeList = Lists.newArrayList();
		//构造节点
		if(result != null){
			List<TenantVo> mangeTree = result.getData();
			if(mangeTree != null && mangeTree.size() >0){
				for (TenantVo tenantVo : mangeTree) {
					if(tenantVo.getId().longValue()==tenantId.longValue()){
						ManageTreeVO manageTreeVO = new ManageTreeVO();
						manageTreeVO.setId(tenantVo.getId());
						manageTreeVO.setpId(0L);
						manageTreeVO.setParentTenantRank(tenantVo.getParentTenantRank());
						manageTreeVO.setName(tenantVo.getName());
						manageTreeList.add(manageTreeVO);
					}else {
						ManageTreeVO manageTreeVO = new ManageTreeVO();
						manageTreeVO.setId(tenantVo.getId());
						manageTreeVO.setName(tenantVo.getName());
						manageTreeVO.setpId(tenantVo.getManageTenantId());
						manageTreeVO.setParentTenantRank(tenantVo.getParentTenantRank());
						manageTreeList.add(manageTreeVO);
					}
					
				}	
			}
		}				
		return response(manageTreeList);
	}
	
	@GetMapping(value="/getHospital")
	@IgnoreProperties(allow = {@AllowProperty(pojo = TenantVo.class, name = {"id","name"}) })
	@ApiOperation(value = "获取医疗机构",httpMethod="GET")
	@ApiImplicitParams({@ApiImplicitParam(name="manageId",value="上级行政机构id",paramType="query",required=true),
		@ApiImplicitParam(name="Keyword",value="搜索关键字",paramType="query",required=false),
		@ApiImplicitParam(name="type",value="医疗机构类型（CMS），取值如下：1：医疗机构2：医基层疗卫生机构部3：疾病预防控制中心",paramType="query",required=false)})
	@ApiResponse(code = 0, message = "OK",response=Result.class)
	public Result<List<TenantVo>> getHospital(TenantQuery query){
		Result<List<TenantVo>> hospital = sysClientService.getHospital(query.getManageId(), query.getKeyword(), query.getType(), WebSecurityUtils.getCurrentToken());
		List<TenantVo> list = Lists.newArrayList();
		if(hospital != null){
			List<TenantVo> data = hospital.getData();
			if(data != null && data.size() > 0){
				list.addAll(data);
			}	
		}
		return  response(list);
	}
	
	@PostMapping(value="/sendWeiXinMessageTest")
	public void sendWeiXinMessageTest(){
		WeiXinServiceMessageRequest messageBody = new WeiXinServiceMessageRequest();
		String toTenantIds = "1,2,3";
		List<Long> tenantIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(toTenantIds)){
			List<String> tenantIdsStr = Arrays.asList(toTenantIds.split(","));
			for (String tenantId : tenantIdsStr) {
				tenantIds.add(Long.parseLong(tenantId));
			}
		}
		messageBody.setTenantIds(tenantIds);
		messageBody.setType(WeiXinServiceMessageTypeEnum.NOTICE.getType());
		Result<List<Map<String,Object>>> result = authClientService.sendWeiXinServiceMessage(messageBody, WebSecurityUtils.getCurrentToken());
		
		logger.info("发布通知推送消息接口返回结果="+(result!=null?result.getData().toString():null));
	}
}
