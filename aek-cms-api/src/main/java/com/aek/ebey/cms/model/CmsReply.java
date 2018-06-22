package com.aek.ebey.cms.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2017-10-31
 */
@ApiModel
@TableName("cms_reply")
public class CmsReply extends BaseModel {

    private static final long serialVersionUID = 1L;
	/**
	 * 内容id
	 */
    @ApiModelProperty(value="内容id")
	@TableField(value="content_id")
	private Long contentId;
	/**
	 * 评论内容
	 */
    @ApiModelProperty(value="评论内容")
	private String content;
	/**
	 * 回复时间
	 */
    @ApiModelProperty(value="回复时间")
	@TableField(value="reply_time")
	private Date replyTime;
	/**
	 * 回复人所在机构名称
	 */
    @ApiModelProperty(value="回复人所在机构名称")
	private String org;
    
	/**
	 * 回复人所在科室名称
	 */
    @ApiModelProperty(value="回复人所在科室名称")
    @TableField(value="dept_name")
	private String deptName;
	/**
	 * 回复人姓名
	 */
    @ApiModelProperty(value="回复人姓名")
	@TableField(value="reply_name")
	private String replyName;
    /**
	 * 删除标志[0=未删除，1=删除]
	 */
    @ApiModelProperty(value="是否删除[0=未删除，1=删除]")
	@TableField(value="del_flag")
	private Boolean delFlag;

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}	
}
