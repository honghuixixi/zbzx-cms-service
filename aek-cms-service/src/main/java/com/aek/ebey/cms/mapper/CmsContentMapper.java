package com.aek.ebey.cms.mapper;

import com.aek.ebey.cms.model.CmsContent;
import com.aek.ebey.cms.model.query.ComplaintQuery;
import com.aek.ebey.cms.model.query.ConsultationQuery;
import com.aek.ebey.cms.model.query.ContentQuery;
import com.aek.ebey.cms.model.vo.ComplaintVO;
import com.aek.ebey.cms.model.vo.ConsultationVO;
import com.aek.ebey.cms.model.vo.ContentVO;
import com.aek.ebey.cms.model.vo.StatsVO;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;
import com.aek.common.core.serurity.model.AuthUser;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
public interface CmsContentMapper extends BaseMapper<CmsContent> {

	/**
	 *按内容类型[例如消息、通知、文章...]统计数目
	 *
	 * @param contentIds
	 * @return
	 */
	List<CmsContent> statsCountsByType(@Param("contentIds")List<Long> contentIds);
	
	//test
	List<CmsContent> findBarch();
	
	/**
	 * 监管机构统计未读 
	 */
	List<StatsVO> statsUnread(@Param("userId") Long userId,@Param("tenantId") Long tenantId);
	
	/**
	 * 监管机构统计技术咨询未读 
	 */
	StatsVO statsUnreadForConsult(@Param("userId") Long userId);
	
	/**
	 * 监管机构统计数目
	 */
	List<StatsVO> statsNumByType(@Param("tenantId") Long tenantId);
	
	/**
	 * 监管机构统计技术咨询数目
	 */
	StatsVO statsNumByTypeForConsult();
	
	/**
	 * 医疗机构统计文章，消息，通知未读
	 * @return
	 */
	List<StatsVO>  statsUnreadForHospital(@Param("userId") Long userId,@Param("tenantId") Long tenantId);
	
	/**
	 * 医疗机构统计不良事件未读
	 * @return
	 */
	StatsVO  statsBadUnreadForHospital(@Param("userId") Long userId,@Param("tenantId") Long tenantId);
	
	/**
	 * 医疗机构统计技术咨询未读
	 * @return
	 */
	StatsVO statsAskUnreadForHospital(@Param("userId") Long userId);
	
	/**
	 * 医疗机构按类型统计数目 
	 */
	List<StatsVO> statsNumByTypeForHospital(@Param("tenantId") Long tenantId);
	
	/**
	 * 按机构id查所拥有的内容id集合
	 * 
	 * @param tenantId
	 * @return
	 */
	List<Long> findUserHas(@Param("tenantId") Long tenantId);
	
	/**
	 * 按机构id查所用的内容明细
	 * 
	 * @param tenantId
	 * @return
	 */
	List<CmsContent> findUserHasAll(@Param("tenantId") Long tenantId);
	
	/**
	 * 根据类型查出最新发布的内容
	 * 
	 */
	//CmsContent findNewest(@Param("type") Integer type,@Param("user") AuthUser currentUser);
	
	/**
	 * 根据类型查出最新发布的内容id
	 * @param type
	 * @param currentUser
	 * @return
	 */
	Long findNewestId(@Param("type") Integer type,@Param("user") AuthUser currentUser);
	
	/**
	 * 投诉列表查询
	 * 
	 * @param page
	 * @param query
	 * @return
	 */
	List<ComplaintVO> getComplaintPage(Page<ComplaintVO> page,@Param("q") ComplaintQuery query,@Param("user") AuthUser currentUser);

	/**
	 * 监管机构投诉列表查询
	 * 
	 * @param page
	 * @param query
	 * @return
	 */
	List<ComplaintVO> getComplaitPageForManage(Page<ComplaintVO> page,@Param("q") ComplaintQuery query,@Param("user") AuthUser currentUser);
	
	/**
	 * 监管机构技术咨询列表查询
	 * 
	 * @param page
	 * @param query
	 * @return
	 */
	List<ConsultationVO> getConsultionPageForManage(Page<ConsultationVO> page,@Param("q") ConsultationQuery query,@Param("user") AuthUser currentUser);

	/**
	 * 技术咨询列表查询
	 * 
	 * @param page
	 * @param query
	 * @return
	 */
	List<ConsultationVO> getConsultationPage(Page<ConsultationVO> page,@Param("q") ConsultationQuery query,@Param("user") AuthUser currentUser);


	/**
	 * 消息、文章、通知列表查询
	 * 
	 * @param page
	 * @param query
	 * @return
	 */
	List<ContentVO> getContentPage(Page<ContentVO> page,@Param("q") ContentQuery query,@Param("user") AuthUser currentUser);

	Boolean getIsNews(@Param("id") Long id);
	
	/**
	 * 判断当前登录人是否有权限拥有此内容
	 * @param tenantId
	 * @param id
	 * @return
	 */
	CmsContent isExist(@Param("tenantId") Long tenantId,@Param("id") Long id);
	
	/**
	 * 小程序消息、文章、通知首页查询(针对于最新发布)
	 * @param currentUser
	 * @param type
	 * @return
	 */
	ContentVO getFrontList(@Param("user") AuthUser currentUser,@Param("type") Integer type);
	/**
	 * 小程序投诉首页查询(针对于最新发布)
	 * @param currentUser
	 * @return
	 */
	ContentVO getFrontBadList(@Param("user") AuthUser currentUser);
	/**
	 * 小程序技术咨询首页查询(针对于最新发布)
	 * @param currentUser
	 * @return
	 */
	ContentVO getFrontConsultList(@Param("user") AuthUser currentUser);
	/**
	 * 根据id更新内容
	 */
	void updateContentById(@Param("c")CmsContent cmsContent);
	
	/**
	 * 
	 * @return
	 */
	CmsContent getByIdNotDel(@Param("id")Long id);

}