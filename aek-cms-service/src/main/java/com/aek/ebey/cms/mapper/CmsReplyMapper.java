package com.aek.ebey.cms.mapper;

import com.aek.ebey.cms.model.CmsReply;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author xxx
 * @since 2017-10-31
 */
public interface CmsReplyMapper extends BaseMapper<CmsReply> {

	/**
	 * 根据内容id查回复
	 * @param contentId
	 * @return
	 */
	List<Long> findReplys(Long contentId);
	
	/**
	 * 根据内容id查最近回复
	 * @param contentId
	 * @return
	 */
	CmsReply findLatestOne(Long contentId);
	
	void deleteByContentId(@Param("id")Long id);
}