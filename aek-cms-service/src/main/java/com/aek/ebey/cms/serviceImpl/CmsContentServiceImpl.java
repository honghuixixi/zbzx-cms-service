package com.aek.ebey.cms.serviceImpl;

import com.aek.ebey.cms.model.CmsContent;
import com.aek.ebey.cms.model.CmsContentUser;
import com.aek.ebey.cms.model.CmsReply;
import com.aek.ebey.cms.model.CmsReplyUser;
import com.aek.ebey.cms.model.bo.Article;
import com.aek.ebey.cms.model.bo.AttachmentsBO;
import com.aek.ebey.cms.model.bo.BadAskBo;
import com.aek.ebey.cms.model.bo.Complaint;
import com.aek.ebey.cms.model.bo.ConsultBo;
import com.aek.ebey.cms.model.bo.Consultation;
import com.aek.ebey.cms.model.bo.News;
import com.aek.ebey.cms.model.bo.Notice;
import com.aek.ebey.cms.model.query.ComplaintQuery;
import com.aek.ebey.cms.model.query.ConsultationQuery;
import com.aek.ebey.cms.model.query.ContentQuery;
import com.aek.ebey.cms.model.vo.BadAskConsultVO;
import com.aek.ebey.cms.model.vo.ComplaintVO;
import com.aek.ebey.cms.model.vo.ConsultationVO;
import com.aek.ebey.cms.model.vo.ContentDetailVO;
import com.aek.ebey.cms.model.vo.ContentReturnVo;
import com.aek.ebey.cms.model.vo.ContentVO;
import com.aek.ebey.cms.model.vo.StatsVO;
import com.aek.ebey.cms.model.vo.TenantsVo;
import com.aek.ebey.cms.enums.WeiXinServiceMessageTypeEnum;
import com.aek.ebey.cms.mapper.CmsContentMapper;
import com.aek.ebey.cms.mapper.CmsContentUserMapper;
import com.aek.ebey.cms.mapper.CmsReplyMapper;
import com.aek.ebey.cms.mapper.CmsReplyUserMapper;
import com.aek.ebey.cms.service.CmsContentService;
import com.aek.ebey.cms.service.CmsContentUserService;
import com.aek.ebey.cms.service.CmsReplyUserService;
import com.aek.ebey.cms.service.feign.AuthClientService;
import com.aek.ebey.cms.service.feign.SysClientService;
import com.aek.ebey.cms.service.feign.request.WeiXinServiceMessageRequest;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;


import com.aek.common.core.BeanMapper;
import com.aek.common.core.Result;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;
import com.aek.common.core.util.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
@Service
@Transactional
public class CmsContentServiceImpl extends BaseServiceImpl<CmsContentMapper, CmsContent> implements CmsContentService {

	private Logger logger = LoggerFactory.getLogger(CmsContentServiceImpl.class);
	
	@Autowired
	private CmsContentMapper cmsContentMapper;
	
	@Autowired
	private SysClientService sysClientService;
	
	@Autowired
	private AuthClientService authClientService;
	
	@Autowired
	private CmsReplyMapper cmsReplyMapper;
	
	@Autowired
	private CmsReplyUserMapper cmsReplyUserMapper;
	
	@Autowired
	private CmsReplyUserService cmsReplyUserService;
	
	@Autowired
	private CmsContentUserMapper cmsContentUserMapper;
	
	@Autowired
	private CmsContentUserService cmsContentUserService;
	
	@Override
	public List<Long> findUserHas(Long tenantId) {
		List<Long> userHas = cmsContentMapper.findUserHas(tenantId);
		return userHas;
	}

	@Override
	public List<CmsContent> findUserHasAll(Long tenantId) {
		return cmsContentMapper.findUserHasAll(tenantId);
	}

	@Override
	public CmsContent statsForManage() {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		List<StatsVO> unreads = cmsContentMapper.statsUnread(currentUser.getId(),currentUser.getTenantId());
		List<StatsVO> numsByType = cmsContentMapper.statsNumByType(currentUser.getTenantId());
		
		StatsVO unreadForConsult = cmsContentMapper.statsUnreadForConsult(currentUser.getId());
		StatsVO numsByConsult = cmsContentMapper.statsNumByTypeForConsult();
	
		CmsContent cmsContent = new CmsContent();
		Notice notice = new Notice();
		News news =  new News();
		Article article = new Article();
		Complaint complaint = new Complaint();
		Consultation consultation = new Consultation();
		
		if(numsByConsult != null){
			if(unreadForConsult !=null){
				if(unreadForConsult.getType().intValue() == 4){
					consultation.setHasNews(true);
				}
			}else if (unreadForConsult ==null) {
				consultation.setHasNews(false);
			}	
			consultation.setCount(numsByConsult.getCount());
		}
		if(numsByConsult == null){
			consultation.setCount(0);
			consultation.setHasNews(false);
		}
		cmsContent.setConsultation(consultation);
		
		//处理数目
		if(numsByType != null && numsByType.size() > 0){
			for (StatsVO statsVO : numsByType) {
				if(statsVO.getType().intValue() == 0){
					notice.setCount(statsVO.getCount());
				}
				if(statsVO.getType().intValue() == 1){
					news.setCount(statsVO.getCount());
				}
				if(statsVO.getType().intValue() == 2){
					article.setCount(statsVO.getCount());
				}
				if(statsVO.getType().intValue() == 3){
					complaint.setCount(statsVO.getCount());
				}			
			}
		 }		
		if(notice.getCount()==null){
			notice.setCount(0);
		}
		if(news.getCount()==null){
			news.setCount(0);
		}
		if(article.getCount()==null){
			article.setCount(0);
		}
		if(complaint.getCount()==null){
			complaint.setCount(0);
		}
		
		//处理未读
		if(unreads != null && unreads.size() > 0){
			for (StatsVO statsVO : unreads) {
				if(statsVO.getType().intValue() == 0){
					notice.setHasNews(true);
				}
				if(statsVO.getType().intValue() == 1){
					news.setHasNews(true);
				}
				if(statsVO.getType().intValue() == 2){
					article.setHasNews(true);
				}
				if(statsVO.getType().intValue() == 3){
					complaint.setHasNews(true);
				}
			}
		}
		if(notice.getHasNews() == null){
			notice.setHasNews(false);
		}	
		if(news.getHasNews() == null){
			news.setHasNews(false);
		}
		if(article.getHasNews() == null){
			article.setHasNews(false);
		}
		if(complaint.getHasNews() == null){
			complaint.setHasNews(false);
		}

		cmsContent.setNotice(notice);
		cmsContent.setNews(news);
		cmsContent.setArticle(article);
		cmsContent.setComplaint(complaint);
		
		return cmsContent;
	}
	
	
	@Override
	public CmsContent statsForHospital() {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long userId = currentUser.getId();
		Long tenantId = currentUser.getTenantId();
		List<StatsVO> statsNumByTypeForHospital = cmsContentMapper.statsNumByTypeForHospital(tenantId);		
		StatsVO badUnreadForHospital = cmsContentMapper.statsBadUnreadForHospital(userId, tenantId);		
		List<StatsVO> statsUnreadForHospital = cmsContentMapper.statsUnreadForHospital(userId, tenantId);
		
		StatsVO statsAskUnreadForHospital = cmsContentMapper.statsAskUnreadForHospital(userId);
		StatsVO numsByConsultForHospital = cmsContentMapper.statsNumByTypeForConsult();
		
		CmsContent cmsContent = new CmsContent();
		Notice notice = new Notice();
		News news =  new News();
		Article article = new Article();
		Complaint complaint = new Complaint();
		Consultation consultation = new Consultation();
		
		if(numsByConsultForHospital != null){
			if(statsAskUnreadForHospital !=null){
				if(statsAskUnreadForHospital.getType().intValue() == 4){
					consultation.setHasNews(true);
				}				
			}else if (statsAskUnreadForHospital ==null) {
				consultation.setHasNews(false);
			}
			consultation.setCount(numsByConsultForHospital.getCount());
		}
		if(numsByConsultForHospital == null){
			if(statsAskUnreadForHospital !=null){
				consultation.setCount(0);
				consultation.setHasNews(false);
			}
		}
		cmsContent.setConsultation(consultation);
		
		//处理数目
		if(statsNumByTypeForHospital != null && statsNumByTypeForHospital.size() > 0){
			for (StatsVO statsVO : statsNumByTypeForHospital) {
				if(statsVO.getType().intValue() == 0){
					notice.setCount(statsVO.getCount());
				}
				if(statsVO.getType().intValue() == 1){
					news.setCount(statsVO.getCount());
				}
				if(statsVO.getType().intValue() == 2){
					article.setCount(statsVO.getCount());
				}
				if(statsVO.getType().intValue() == 3){
					complaint.setCount(statsVO.getCount());
				}			
			}
		}
		if(notice.getCount()==null){
			notice.setCount(0);
		}
		if(news.getCount()==null){
			news.setCount(0);
		}
		if(article.getCount()==null){
			article.setCount(0);
		}
		if(complaint.getCount()==null){
			complaint.setCount(0);
		}
		
		//处理未读
		if(statsUnreadForHospital != null && statsUnreadForHospital.size() > 0){
			for (StatsVO statsVO : statsUnreadForHospital) {
				if(statsVO.getType().intValue() == 0){
					notice.setHasNews(true);
				}
				if(statsVO.getType().intValue() == 1){
					news.setHasNews(true);
				}
				if(statsVO.getType().intValue() == 2){
					article.setHasNews(true);
				}
			}
		}
		if(notice.getHasNews() == null){
			notice.setHasNews(false);
		}	
		if(news.getHasNews() == null){
			news.setHasNews(false);
		}
		if(article.getHasNews() == null){
			article.setHasNews(false);
		}
		if(badUnreadForHospital != null){
			if(badUnreadForHospital.getType().intValue() == 3){
			complaint.setHasNews(true);
			}			
		}else if (badUnreadForHospital == null) {
			complaint.setHasNews(false);
		}
			
		cmsContent.setNotice(notice);
		cmsContent.setNews(news);
		cmsContent.setArticle(article);
		cmsContent.setComplaint(complaint);		

		return cmsContent;
	}



	


	@Override
	public ContentDetailVO getDetail(Long id) {
		if(id == null){
			throw ExceptionFactory.create("r_001");
		}
		CmsContent contentNotDel = cmsContentMapper.getByIdNotDel(id);
		if(contentNotDel==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long userId = currentUser.getId();
		CmsContent exist = cmsContentMapper.isExist(currentUser.getTenantId(), id);
		if(exist == null){
			throw ExceptionFactory.create("d_003");
		}
		
		ContentDetailVO content=new ContentDetailVO();
		CmsContent cmscontent = cmsContentMapper.selectById(id);
		if(cmscontent != null){
			content.setId(cmscontent.getId());
			content.setTitle(cmscontent.getTitle());
			content.setContent(cmscontent.getContent());
			content.setPublisher(cmscontent.getReleasePersonName());
			content.setOrg(cmscontent.getFromTenantName());
			content.setPublishTime(cmscontent.getUpdateTime());
			content.setUrl(cmscontent.getUrl());
			
			Long id0 = null;
			Long id1 = null;
			Long id2 = null;
			Long noticeLatestId = cmsContentMapper.findNewestId(0,currentUser);
			Long newsLatestId = cmsContentMapper.findNewestId(1,currentUser);
			Long aticleLatestId = cmsContentMapper.findNewestId(2,currentUser);	
			if(noticeLatestId != null){
				id0 = noticeLatestId;
			}
			if(newsLatestId != null){
				id1 = newsLatestId;
			}		
			if(aticleLatestId != null){
				id2 = aticleLatestId;
			}
			if(id0 !=null){
				if(id.longValue() == id0.longValue()){
					content.setIsNews(true);
				}	
			}
			
			if(id1 !=null){
				if(id.longValue() == id1.longValue()){
					content.setIsNews(true);
				}
			}
			
			if(id2 != null){
				if(id.longValue() == id2.longValue()){
					content.setIsNews(true);
				}
			}
			
			if(id0 != null && id1 != null && id2 != null){
				if(id.longValue() != id0.longValue() && id.longValue() != id1.longValue() && id.longValue() != id2.longValue()){
					content.setIsNews(false);
				}
			}
			
			//是否可以编辑、删除标志
			if(currentUser.getId().longValue() == exist.getReleaseBy().longValue()){
				content.setIsEdit(true);
			}else {
				content.setIsEdit(false);
			}
			
			CmsContentUser findByUserIdAndContentid = cmsContentUserMapper.findByUserIdAndContentid(userId, id);
			
			if(findByUserIdAndContentid == null){
				//阅读后更新cms_content_user表		
				CmsContentUser cmsContentUser = new CmsContentUser();
				cmsContentUser.setContentId(id);
				cmsContentUser.setUserId(userId);
				cmsContentUserMapper.insert(cmsContentUser);
			}	
			
			//监管机构用户点详情，文章评论更新cms_reply_user表	
			if(currentUser.getTenantType() ==2){//2=监管机构
				new Thread() {
					@Override
					public void run() {
						List<CmsReplyUser> list = Lists.newArrayList();
						List<Long> replyIds = cmsReplyMapper.findReplys(id);
						if(replyIds != null && replyIds.size() > 0){
							for (Long replyId : replyIds) {
								if(cmsReplyUserMapper.getReadedReplyById(id, userId, replyId) == null){
									CmsReplyUser cmsReplyUser = new CmsReplyUser();
									cmsReplyUser.setReplyId(replyId);
									cmsReplyUser.setContentId(id);
									cmsReplyUser.setUserId(userId);
									list.add(cmsReplyUser);
								}
							}
							cmsReplyUserService.insertBatch(list);
						}
					}
				}.start();			
			}
		}
		
		return content;
	}

	@Override
	public void newArticle(CmsContent cmsContent) {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		int tenantType = currentUser.getTenantType();
		if(tenantType == 1){
			throw ExceptionFactory.create("n_004");
		}
		Date now = new Date();
		cmsContent.setCreateBy(currentUser.getId());
		cmsContent.setReleaseBy(currentUser.getId());
		//新建文章
		cmsContent.setType(2);
		cmsContent.setFromTenantId(currentUser.getTenantId());
		cmsContent.setFromTenantName(currentUser.getTenantName());
		cmsContent.setFromDeptName(currentUser.getDeptName());
		cmsContent.setCreateTime(now);
		cmsContent.setReleasePersonName(currentUser.getRealName());
		cmsContent.setReleaseTime(now);
		cmsContent.setUpdateTime(now);
		cmsContent.setUpdateBy(currentUser.getId());
		this.insert(cmsContent);
		
		CmsContentUser findByUserIdAndContentid = cmsContentUserMapper.findByUserIdAndContentid(currentUser.getId(), cmsContent.getId());	
		if(findByUserIdAndContentid == null){
			//阅读后更新cms_content_user表		
			CmsContentUser cmsContentUser = new CmsContentUser();
			cmsContentUser.setContentId(cmsContent.getId());
			cmsContentUser.setUserId(currentUser.getId());
			cmsContentUserMapper.insert(cmsContentUser);
		}
		
		//发送文章消息给至微信公众号
		WeiXinServiceMessageRequest messageBody = new WeiXinServiceMessageRequest();
		String toTenantIds = cmsContent.getToTenantIdsCopy();
		List<Long> tenantIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(toTenantIds)){
			List<String> tenantIdsStr = Arrays.asList(toTenantIds.split(","));
			for (String tenantId : tenantIdsStr) {
				tenantIds.add(Long.parseLong(tenantId));
			}
		}
		messageBody.setTenantIds(tenantIds);
		messageBody.setType(WeiXinServiceMessageTypeEnum.ARTICLE.getType());
		messageBody.setPublishTenantName(cmsContent.getFromTenantName());
		messageBody.setPublishTime(DateUtil.format(cmsContent.getReleaseTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		Result<List<Map<String, Object>>> result = authClientService.sendWeiXinServiceMessage(messageBody, WebSecurityUtils.getCurrentToken());
		logger.info("发布文章推送消息接口返回结果="+(result!=null?result.getData().toString():null));
	}

	@Override
	public void newNews(CmsContent cmsContent) {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		int tenantType = currentUser.getTenantType();
		if(tenantType == 1){
			throw ExceptionFactory.create("n_004");
		}
		Date now = new Date();
		cmsContent.setCreateBy(currentUser.getId());
		cmsContent.setReleaseBy(currentUser.getId());
		//新建消息
		cmsContent.setType(1);
		cmsContent.setFromTenantId(currentUser.getTenantId());
		cmsContent.setFromTenantName(currentUser.getTenantName());
		cmsContent.setFromDeptName(currentUser.getDeptName());
		cmsContent.setCreateTime(now);
		cmsContent.setReleasePersonName(currentUser.getRealName());
		cmsContent.setReleaseTime(now);
		cmsContent.setUpdateBy(currentUser.getId());
		cmsContent.setUpdateTime(now);
		this.insert(cmsContent);
		
		CmsContentUser findByUserIdAndContentid = cmsContentUserMapper.findByUserIdAndContentid(currentUser.getId(), cmsContent.getId());	
		if(findByUserIdAndContentid == null){
			//阅读后更新cms_content_user表		
			CmsContentUser cmsContentUser = new CmsContentUser();
			cmsContentUser.setContentId(cmsContent.getId());
			cmsContentUser.setUserId(currentUser.getId());
			cmsContentUserMapper.insert(cmsContentUser);
		}
		
		//发送消息给至微信公众号
		WeiXinServiceMessageRequest messageBody = new WeiXinServiceMessageRequest();
		String toTenantIds = cmsContent.getToTenantIdsCopy();
		List<Long> tenantIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(toTenantIds)){
			List<String> tenantIdsStr = Arrays.asList(toTenantIds.split(","));
			for (String tenantId : tenantIdsStr) {
				tenantIds.add(Long.parseLong(tenantId));
			}
		}
		messageBody.setTenantIds(tenantIds);
		messageBody.setType(WeiXinServiceMessageTypeEnum.MESSAGE.getType());
		messageBody.setPublishTenantName(cmsContent.getFromTenantName());
		messageBody.setPublishTime(DateUtil.format(cmsContent.getReleaseTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		Result<List<Map<String, Object>>> result = authClientService.sendWeiXinServiceMessage(messageBody, WebSecurityUtils.getCurrentToken());
		logger.info("发布消息推送消息接口返回结果="+(result!=null?result.getData().toString():null));
	}

	@Override
	public void newNotice(CmsContent cmsContent) {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		int tenantType = currentUser.getTenantType();
		if(tenantType == 1){
			throw ExceptionFactory.create("n_004");
		}
		Date now = new Date();
		cmsContent.setCreateBy(currentUser.getId());
		cmsContent.setReleaseBy(currentUser.getId());
		//新建通知
		cmsContent.setType(0);
		cmsContent.setFromTenantId(currentUser.getTenantId());
		cmsContent.setFromTenantName(currentUser.getTenantName());
		cmsContent.setFromDeptName(currentUser.getDeptName());
		cmsContent.setCreateTime(now);
		cmsContent.setReleasePersonName(currentUser.getRealName());
		cmsContent.setReleaseTime(now);
		cmsContent.setUpdateBy(currentUser.getId());
		cmsContent.setUpdateTime(now);
		this.insert(cmsContent);
		
		CmsContentUser findByUserIdAndContentid = cmsContentUserMapper.findByUserIdAndContentid(currentUser.getId(), cmsContent.getId());	
		if(findByUserIdAndContentid == null){
			//阅读后更新cms_content_user表		
			CmsContentUser cmsContentUser = new CmsContentUser();
			cmsContentUser.setContentId(cmsContent.getId());
			cmsContentUser.setUserId(currentUser.getId());
			cmsContentUserMapper.insert(cmsContentUser);
		}
		
		//发送消息给至微信公众号
		WeiXinServiceMessageRequest messageBody = new WeiXinServiceMessageRequest();
		String toTenantIds = cmsContent.getToTenantIdsCopy();
		List<Long> tenantIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(toTenantIds)){
			List<String> tenantIdsStr = Arrays.asList(toTenantIds.split(","));
			for (String tenantId : tenantIdsStr) {
				tenantIds.add(Long.parseLong(tenantId));
			}
		}
		messageBody.setTenantIds(tenantIds);
		messageBody.setType(WeiXinServiceMessageTypeEnum.NOTICE.getType());
		messageBody.setPublishTenantName(cmsContent.getFromTenantName());
		messageBody.setPublishTime(DateUtil.format(cmsContent.getReleaseTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		Result<List<Map<String, Object>>> result = authClientService.sendWeiXinServiceMessage(messageBody, WebSecurityUtils.getCurrentToken());
		logger.info("发布通知推送消息接口返回结果="+(result!=null?result.getData().toString():null));
	}

	@Override
	public Page<ComplaintVO> getComplaintList(ComplaintQuery query) {
		Page<ComplaintVO> page = query.getPage();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		if(keyword !=null){
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
				query.setKeyword('\\'+keyword);
			}
		}
		List<ComplaintVO> complaintPage = cmsContentMapper.getComplaintPage(page, query, currentUser);
		if(complaintPage != null && complaintPage.size() > 0){
			for (ComplaintVO complaintVO : complaintPage) {
				if(complaintVO.getReply().intValue() == 1){
					Long complaintId = complaintVO.getId();
					CmsReply replyLatest = cmsReplyMapper.findLatestOne(complaintId);
					if(replyLatest != null){
						complaintVO.setReplyContent(replyLatest.getContent());
						complaintVO.setReplyOrg(replyLatest.getOrg());
						complaintVO.setReplyTime(replyLatest.getReplyTime());
					}
				}
			}
		}	
		page.setRecords(complaintPage);
		return page;
	}
	
	@Override
	public Page<ComplaintVO> getComplaintListForManage(ComplaintQuery query) {
		Page<ComplaintVO> page = query.getPage();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		if(keyword !=null){
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
				query.setKeyword('\\'+keyword);
			}
		}
		List<ComplaintVO> complaintPage = cmsContentMapper.getComplaitPageForManage(page, query, currentUser);
		if(complaintPage != null && complaintPage.size() > 0){
			for (ComplaintVO complaintVO : complaintPage) {
				if(complaintVO.getReply().intValue() == 1){
					Long complaintId = complaintVO.getId();
					CmsReply replyLatest = cmsReplyMapper.findLatestOne(complaintId);
					if(replyLatest != null){
						complaintVO.setReplyContent(replyLatest.getContent());
						complaintVO.setReplyOrg(replyLatest.getOrg());
						complaintVO.setReplyTime(replyLatest.getReplyTime());
					}
				}
			}
		}
		page.setRecords(complaintPage);
		return page;
	}


	@Override
	public Page<ConsultationVO> getConsultationList(ConsultationQuery query) {
		Page<ConsultationVO> page = query.getPage();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		if(keyword !=null){
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
				query.setKeyword('\\'+keyword);
			}
		}
		List<ConsultationVO> consultationPage = cmsContentMapper.getConsultationPage(page, query, currentUser);
		if(consultationPage != null && consultationPage.size() > 0){
			for (ConsultationVO consultationVO : consultationPage) {
				if(consultationVO.getReply().intValue() == 1){
					Long consultationId = consultationVO.getId();
					CmsReply replyLatest = cmsReplyMapper.findLatestOne(consultationId);
					if(replyLatest != null){
						consultationVO.setReplyContent(replyLatest.getContent());
						consultationVO.setReplyOrg(replyLatest.getOrg());
						consultationVO.setReplyTime(replyLatest.getReplyTime());
					}
				}
			}
		}
		page.setRecords(consultationPage);
		return page;
	}
	
	@Override
	public Page<ConsultationVO> getConsultionListForManage(ConsultationQuery query) {
		Page<ConsultationVO> page = query.getPage();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		if(keyword !=null){
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
				query.setKeyword('\\'+keyword);
			}
		}
		List<ConsultationVO> consultationPage = cmsContentMapper.getConsultionPageForManage(page, query, currentUser);
		if(consultationPage != null && consultationPage.size() > 0){
			for (ConsultationVO consultationVO : consultationPage) {
				if(consultationVO.getReply().intValue() == 1){
					Long consultationId = consultationVO.getId();
					CmsReply replyLatest = cmsReplyMapper.findLatestOne(consultationId);
					if(replyLatest != null){
						consultationVO.setReplyContent(replyLatest.getContent());
						consultationVO.setReplyOrg(replyLatest.getOrg());
						consultationVO.setReplyTime(replyLatest.getReplyTime());
					}
				}
			}
		}
		page.setRecords(consultationPage);
		return page;
	}

	@Override
	public Page<ContentVO> getContentList(ContentQuery query) {
		Page<ContentVO> page = query.getPage();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		if(keyword !=null){
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
				query.setKeyword('\\'+keyword);
			}
		}
		List<ContentVO> contentPage = cmsContentMapper.getContentPage(page, query, currentUser);
		Integer type = query.getType();
		
		//某种类型最新发布id
		//CmsContent newestOne = cmsContentMapper.findNewest(type,currentUser);
		Long newestOneId = cmsContentMapper.findNewestId(type, currentUser);
		
		//记录最新一条
		Boolean flag=false;
		Integer position = null;
		
		if(contentPage != null && contentPage.size() > 0){
			
			for (ContentVO contentVO : contentPage) {
				if(contentVO.getId().longValue() == newestOneId.longValue()){
					contentVO.setIsNew(true);
					flag=true;
					position=contentPage.indexOf(contentVO);
				}else {
					contentVO.setIsNew(false);
				}
			}
			//匹配到最新发布即插入首位,未匹配则不做处理
			if(flag){
				ContentVO contentVO = contentPage.get(position);
				contentPage.add(0,contentVO);
				contentPage.remove(position+1);
			}
		}
		
		page.setRecords(contentPage);
		return page;
	}
	
	@Override
	public Page<ContentVO> getContentListForMobile(ContentQuery query) {
		Page<ContentVO> page = query.getPage();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String keyword = StringUtils.trimToNull(query.getKeyword());
		if(keyword !=null){
			if (keyword.startsWith("%") || keyword.startsWith("[") || keyword.startsWith("[]") || keyword.startsWith("_")) {
				query.setKeyword('\\'+keyword);
			}
		}
		List<ContentVO> contentPage = cmsContentMapper.getContentPage(page, query, currentUser);			
		page.setRecords(contentPage);
		return page;
	}

	@Override
	public void newBadAskConsult(CmsContent cmsContent) {
		Date now = new Date();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String currentToken = WebSecurityUtils.getCurrentToken();
		int tenantType = currentUser.getTenantType();
		if(tenantType == 2){
			throw ExceptionFactory.create("n_004");
		}
		cmsContent.setUpdateTime(now);
		cmsContent.setUpdateBy(currentUser.getId());
		cmsContent.setReleaseTime(now);
		cmsContent.setCreateTime(now);
		cmsContent.setFromTenantName(currentUser.getTenantName());
		cmsContent.setFromDeptName(currentUser.getDeptName());
		cmsContent.setFromTenantId(currentUser.getTenantId());
		Result<List<Long>> result = sysClientService.getManageIds(currentUser.getTenantId(), currentToken);
		StringBuilder sb =new StringBuilder();
		if(result !=null){
			List<Long> manageIds = result.getData();	
			if(manageIds !=null && manageIds.size() >0){
				for (Long id : manageIds) {
					sb.append(id.toString()+',');
				}
			}
		}
		cmsContent.setToTenantIds(sb.append(currentUser.getTenantId().toString()).toString());
		cmsContent.setCreateBy(currentUser.getId());
		cmsContent.setReleaseBy(currentUser.getId());
		cmsContent.setReleasePersonName(currentUser.getRealName());
		
		cmsContentMapper.insert(cmsContent);	
	}

	@Override
	public BadAskConsultVO getBadAsk(Long id) {
		CmsContent content = cmsContentMapper.getByIdNotDel(id);
		if(content==null){
			throw ExceptionFactory.create("d_003");//内容不存在
		}
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long userId = currentUser.getId();
		CmsContent exist = cmsContentMapper.isExist(currentUser.getTenantId(), id);
		if(exist == null){
			throw ExceptionFactory.create("l_001");
		}
		
		CmsContent cmsContent = cmsContentMapper.selectById(id);
		BadAskConsultVO badAskConsultVO = new BadAskConsultVO();
		badAskConsultVO = BeanMapper.map(cmsContent, BadAskConsultVO.class);
		
		String customData = cmsContent.getCustomData();
		BadAskBo badAskBo = JSON.parseObject(customData, BadAskBo.class);
		
		badAskConsultVO.setWx(badAskBo.getWx());
		badAskConsultVO.setType(badAskBo.getComplaintType());
		
		if(currentUser.getTenantType() == 2){//2=监管机构
			badAskConsultVO.setReplyLimit(true);
		}else if (currentUser.getTenantType() == 1) {//1=医疗机构
			badAskConsultVO.setReplyLimit(false);
		}
		//删除标志
		if(userId.longValue()==cmsContent.getReleaseBy().longValue()){
			badAskConsultVO.setIsDel(true);
		}else {
			badAskConsultVO.setIsDel(false);
		}
		badAskConsultVO.setPublishTime(cmsContent.getUpdateTime());
		badAskConsultVO.setPublisher(cmsContent.getReleasePersonName());
		badAskConsultVO.setProductor(badAskBo.getProductor());
		badAskConsultVO.setPhone(badAskBo.getPhone());
		badAskConsultVO.setOrg(cmsContent.getFromTenantName());
		badAskConsultVO.setDeptName(cmsContent.getFromDeptName());
		badAskConsultVO.setManufacturer(badAskBo.getManufacturer());
		Boolean flag = cmsContentMapper.getIsNews(id);
		badAskConsultVO.setIsNews(flag);
		badAskConsultVO.setEmail(badAskBo.getEmail());
		

		CmsContentUser findByUserIdAndContentid = cmsContentUserMapper.findByUserIdAndContentid(userId, id);	
		if(findByUserIdAndContentid == null){
			//阅读后更新cms_content_user表
			CmsContentUser cmsContentUser = new CmsContentUser();
			cmsContentUser.setContentId(id);
			cmsContentUser.setUserId(userId);
			cmsContentUserMapper.insert(cmsContentUser);
		}
		
		//医疗机构用户点详情，评论更新cms_reply_user表	
		if(currentUser.getTenantType() ==1){//1=医疗机构
			new Thread() {
				@Override
				public void run() {
					List<CmsReplyUser> list = Lists.newArrayList();
					List<Long> replyIds = cmsReplyMapper.findReplys(id);
					if(replyIds != null && replyIds.size() > 0){
						for (Long replyId : replyIds) {
							if(cmsReplyUserMapper.getReadedReplyById(id, userId, replyId) == null){
								CmsReplyUser cmsReplyUser = new CmsReplyUser();
								cmsReplyUser.setReplyId(replyId);
								cmsReplyUser.setContentId(id);
								cmsReplyUser.setUserId(userId);
								list.add(cmsReplyUser);
							}
						}
						cmsReplyUserService.insertBatch(list);
					}
				}
			}.start();			
		}	
		return badAskConsultVO;
	}

	@Override
	public BadAskConsultVO getConsult(Long id) {
		CmsContent content = cmsContentMapper.getByIdNotDel(id);
		if(content==null){
			throw ExceptionFactory.create("d_003");//内容不存在
		}
		CmsContent cmsContent = cmsContentMapper.selectById(id);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long userId = currentUser.getId();
//		if(cmsContent != null){
//			if(cmsContent.getType().intValue() ==3){
//				CmsContent exist = cmsContentMapper.isExist(currentUser.getTenantId(), id);
//				if(exist == null){
//					throw ExceptionFactory.create("l_001");//您没有权限查看此内容
//				}
//			}
//		}
		
		BadAskConsultVO badAskConsultVO = new BadAskConsultVO();
		badAskConsultVO = BeanMapper.map(cmsContent, BadAskConsultVO.class);
		
		String customData = cmsContent.getCustomData();
		ConsultBo consultBo = JSON.parseObject(customData, ConsultBo.class);
		
		badAskConsultVO.setWx(consultBo.getWx());
		badAskConsultVO.setType(consultBo.getConsultationType());
		
		if(currentUser.getTenantType() == 2){
			badAskConsultVO.setReplyLimit(true);
		}else if (currentUser.getTenantType() == 1) {
			badAskConsultVO.setReplyLimit(false);
		}
		badAskConsultVO.setPublishTime(cmsContent.getUpdateTime());
		badAskConsultVO.setPublisher(cmsContent.getReleasePersonName());
		badAskConsultVO.setProductor(consultBo.getProductor());
		badAskConsultVO.setPhone(consultBo.getPhone());
		badAskConsultVO.setOrg(cmsContent.getFromTenantName());
		badAskConsultVO.setDeptName(cmsContent.getFromDeptName());
		badAskConsultVO.setManufacturer(consultBo.getManufacturer());
		Boolean flag = cmsContentMapper.getIsNews(id);
		badAskConsultVO.setIsNews(flag);
		badAskConsultVO.setEmail(consultBo.getEmail());
		//删除标志
		if(userId.longValue()==cmsContent.getReleaseBy().longValue()){
			badAskConsultVO.setIsDel(true);
		}else {
			badAskConsultVO.setIsDel(false);
		}
		
		CmsContentUser findByUserIdAndContentid = cmsContentUserMapper.findByUserIdAndContentid(userId, id);	
		if(findByUserIdAndContentid == null){
			//阅读后更新cms_content_user表
			CmsContentUser cmsContentUser = new CmsContentUser();
			cmsContentUser.setContentId(id);
			cmsContentUser.setUserId(userId);
			cmsContentUserMapper.insert(cmsContentUser);
		}
		//医疗机构用户点详情，评论更新cms_reply_user表	
		if(currentUser.getTenantType() ==1){//1=医疗机构
			new Thread() {
				@Override
				public void run() {
					List<CmsReplyUser> list = Lists.newArrayList();
					List<Long> replyIds = cmsReplyMapper.findReplys(id);
					if(replyIds != null && replyIds.size() > 0){
						for (Long replyId : replyIds) {
							if(cmsReplyUserMapper.getReadedReplyById(id, userId, replyId) == null){
								CmsReplyUser cmsReplyUser = new CmsReplyUser();
								cmsReplyUser.setReplyId(replyId);
								cmsReplyUser.setContentId(id);
								cmsReplyUser.setUserId(userId);
								list.add(cmsReplyUser);
							}
						}
						cmsReplyUserService.insertBatch(list);
					}
				}
			}.start();			
		}
		
		return badAskConsultVO;
	}

	@Override
	public List<ContentVO> getFrontList() {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		List<ContentVO> list = Lists.newArrayList();		
		for (int i = 0; i < 3; i++) {
			ContentVO newestOne = new ContentVO();		
			newestOne = cmsContentMapper.getFrontList(currentUser,i);
			if(newestOne !=null){
				switch (i) {
				case 0:
					newestOne.setTitle("【通知】"+newestOne.getTitle());
					newestOne.setFrontType(newestOne.getFrontType()+1);
					break;
				case 1:
					newestOne.setTitle("【消息】"+newestOne.getTitle());
					newestOne.setFrontType(newestOne.getFrontType()+1);
					break;
				case 2:
					newestOne.setTitle("【文章】"+newestOne.getTitle());
					newestOne.setFrontType(newestOne.getFrontType()+1);
					break;
				}
				list.add(newestOne);
			}
		}
		ContentVO badNewest = new ContentVO();
		badNewest = cmsContentMapper.getFrontBadList(currentUser);
		if(badNewest !=null){
			badNewest.setTitle("【投诉与不良事件】"+badNewest.getTitle());
			badNewest.setFrontType(badNewest.getFrontType()+1);
			list.add(badNewest);
		}
		
		
		ContentVO consultNewest = new ContentVO();
		consultNewest = cmsContentMapper.getFrontConsultList(currentUser);
		if(consultNewest !=null){			
			consultNewest.setTitle("【技术咨询】"+consultNewest.getTitle());
			consultNewest.setFrontType(consultNewest.getFrontType()+1);
			list.add(consultNewest);
		}	
		return list;
	}

	@Override
	public void edit(CmsContent cmsContent) {		
		Long id = cmsContent.getId();
		CmsContent content = cmsContentMapper.selectById(id);
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		if(content==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		Long releaseBy = content.getReleaseBy();
		if(releaseBy !=null && currentUser.getId().longValue() != releaseBy.longValue()){
			throw ExceptionFactory.create("e_002");//编辑权限
		}	
		cmsContent.setUpdateBy(currentUser.getId());
		cmsContent.setUpdateTime(new Date());
		CmsContent contentNotDel = cmsContentMapper.getByIdNotDel(id);
		if(contentNotDel==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		cmsContentMapper.updateContentById(cmsContent);
		//清除该内容与用户绑定关系
		cmsContentUserMapper.delByContentId(id,currentUser.getId());
		
		//发送文章消息给至微信公众号
		WeiXinServiceMessageRequest messageBody = new WeiXinServiceMessageRequest();
		String toTenantIds = cmsContent.getToTenantIdsCopy();
		List<Long> tenantIds = new ArrayList<Long>();
		if(StringUtils.isNotBlank(toTenantIds)){
			List<String> tenantIdsStr = Arrays.asList(toTenantIds.split(","));
			for (String tenantId : tenantIdsStr) {
				tenantIds.add(Long.parseLong(tenantId));
			}
		}
		messageBody.setTenantIds(tenantIds);
		Integer type = content.getType();
		switch (type) {
		case 0:
			messageBody.setType(WeiXinServiceMessageTypeEnum.NOTICE.getType());
			break;
		case 1:
			messageBody.setType(WeiXinServiceMessageTypeEnum.MESSAGE.getType());
			break;
		case 2:
			messageBody.setType(WeiXinServiceMessageTypeEnum.ARTICLE.getType());
			break;
		default:
			break;
		}
		messageBody.setPublishTenantName(content.getFromTenantName());
		messageBody.setPublishTime(DateUtil.format(content.getReleaseTime(), DateUtil.DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));
		Result<List<Map<String, Object>>> result = authClientService.sendWeiXinServiceMessage(messageBody, WebSecurityUtils.getCurrentToken());
		logger.info("发布文章推送消息接口返回结果="+(result!=null?result.getData().toString():null));
	}

	@Override
	public ContentReturnVo getContentReturn(Long id) {
		String currentToken = WebSecurityUtils.getCurrentToken();
		CmsContent content = cmsContentMapper.getByIdNotDel(id);
		if(content==null){
			throw ExceptionFactory.create("d_003");//此内容已删除
		}
		ContentReturnVo contentReturnVo = BeanMapper.map(content, ContentReturnVo.class);
		String toTenantIdsCopy = content.getToTenantIdsCopy();
		if(toTenantIdsCopy != null){
			String[] toIdsStr = toTenantIdsCopy.split(",");
			Long[] toIds = new Long[toIdsStr.length];
			for (int i = 0; i < toIdsStr.length; i++) {
				Long toId = Long.valueOf(toIdsStr[i]);
				toIds[i]=toId;
			}
			Result<List<TenantsVo>> result = sysClientService.getTenants(toIds, currentToken);		
			if(result !=null){
				List<TenantsVo> tenants = result.getData();
				if(tenants !=null && tenants.size()>0){
					contentReturnVo.setTenants(tenants);
				}
			}
		}	
		String url = content.getUrl();
		if(url != null){
			List<AttachmentsBO> files = JSON.parseArray(content.getUrl(), AttachmentsBO.class);
			contentReturnVo.setFiles(files);
		}		
		return contentReturnVo;
	}

	@Override
	public void deleteById(Long id) {
		CmsContent contentNotDel = cmsContentMapper.getByIdNotDel(id);
		if(contentNotDel==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long releaseBy = contentNotDel.getReleaseBy();
		if(releaseBy !=null && currentUser.getId().longValue() != releaseBy.longValue()){
			throw ExceptionFactory.create("d_001");//删除权限
		}
		contentNotDel.setDelFlag(true);
		cmsContentMapper.updateById(contentNotDel);
		cmsContentUserMapper.deleteByContentId(id);
		cmsReplyMapper.deleteByContentId(id);
		cmsReplyUserMapper.deleteByContentId(id);
	}

	@Override
	public void deleteBadConsultById(Long id) {
		//TODO
		CmsContent contentNotDel = cmsContentMapper.getByIdNotDel(id);
		if(contentNotDel==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		Long releaseBy = contentNotDel.getReleaseBy();
		if(releaseBy !=null && currentUser.getId().longValue() != releaseBy.longValue()){
			throw ExceptionFactory.create("d_001");//删除权限
		}
		contentNotDel.setDelFlag(true);
		cmsContentMapper.updateById(contentNotDel);
		cmsContentUserMapper.deleteByContentId(id);
		cmsReplyMapper.deleteByContentId(id);
		cmsReplyUserMapper.deleteByContentId(id);
	}
	
}
