package com.aek.ebey.cms.mapper;

import com.aek.ebey.cms.model.CmsReplyUser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aek.common.core.base.BaseMapper;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author xxx
 * @since 2017-11-03
 */
public interface CmsReplyUserMapper extends BaseMapper<CmsReplyUser> {

	/**
	 * 根据内容id和用户Id查询已读评论
	 * 
	 */
	CmsReplyUser getReadedReplyById(@Param("contentId")Long contentId,@Param("userId")Long userId,@Param("replyId")Long replyId);
	
	Long findByReplyId(@Param("replyId")Long replyId,@Param("userId")Long userId,@Param("contentId")Long contentId);

	void deleteByContentId(@Param("id")Long id);
}