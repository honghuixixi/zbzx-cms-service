package com.aek.ebey.cms.service;

import com.aek.ebey.cms.model.CmsContent;
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
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

import com.aek.common.core.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
public interface CmsContentService extends BaseService<CmsContent> {
	
	
	List<Long> findUserHas(Long tenantId);

	List<CmsContent> findUserHasAll(Long tenantId);
	
	ContentDetailVO getDetail(Long id);
	 
	ContentReturnVo getContentReturn(Long id);
	
	void deleteById(Long id);
	
	void deleteBadConsultById(Long id);
	
	void newArticle(CmsContent cmsContent);
	
	void newNews(CmsContent cmsContent);
	
	void newNotice(CmsContent cmsContent);
	
	Page<ComplaintVO> getComplaintList(ComplaintQuery query);
	
	Page<ComplaintVO> getComplaintListForManage(ComplaintQuery query);
	
	Page<ConsultationVO> getConsultionListForManage(ConsultationQuery query);
	
	Page<ConsultationVO> getConsultationList(ConsultationQuery query);
	
	Page<ContentVO> getContentList(ContentQuery query);
	
	Page<ContentVO> getContentListForMobile(ContentQuery query);
	
	CmsContent statsForManage();
	
	CmsContent statsForHospital();
	
	void newBadAskConsult(CmsContent cmsContent);
	
	BadAskConsultVO getBadAsk(Long id);
	
	BadAskConsultVO getConsult(Long id);
	
	List<ContentVO> getFrontList();
	
	void edit(CmsContent cmsContent);
}

