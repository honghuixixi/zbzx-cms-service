package com.aek.ebey.cms.mapper;

import com.aek.ebey.cms.model.CmsContentUser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
public interface CmsContentUserMapper extends BaseMapper<CmsContentUser> {

	/**
	 * 根据用户id按内容类型统计未读数目
	 * 
	 * @param userId
	 */
//	List<CmsContentUser> statsCount(@Param("userId")Long userId);
	
	/**
	 * 查出最新发布的内容用户id
	 * 
	 * @param userId
	 */
//	Long findMaxOne();
	
	/**
	 * 根据内容id查内容用户
	 * 
	 * @param id
	 * @return
	 */
//	CmsContentUser findByContentId(@Param("id")Long id);
	
	CmsContentUser findByUserIdAndContentid(@Param("userId")Long userId,@Param("contentId")Long contentId);

	void delByContentId(@Param("id")Long id,@Param("userId")Long userId);
	
	void deleteByContentId(@Param("id")Long id);
}