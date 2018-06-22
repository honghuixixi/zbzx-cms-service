package com.aek.ebey.cms.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * <p>
 * 回复用户关联表
 * </p>
 *
 * @author xxx
 * @since 2017-11-03
 */
@ApiModel
@TableName("cms_reply_user")
public class CmsReplyUser extends BaseModel {

    private static final long serialVersionUID = 1L;

	/**
	 * 回复id
	 */
	@TableField(value="reply_id")
	private Long replyId;
	/**
	 * 用户id
	 */
	@TableField(value="user_id")
	private Long userId;
	/**
	 * 内容id
	 */
	@TableField(value="content_id")
	private Long contentId;
	
	/**
	 * 删除标志
	 */
	@TableField(value="del_flag")
	private Boolean delFlag;

	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

}
