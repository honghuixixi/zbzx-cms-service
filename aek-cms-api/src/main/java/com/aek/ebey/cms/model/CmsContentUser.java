package com.aek.ebey.cms.model;

import com.aek.common.core.base.BaseModel;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
@ApiModel
@TableName("cms_content_user")
public class CmsContentUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="内容id")
	@TableField(value="content_id")
	private Long contentId;  
	/**
	 * 用户id
	 */
    @ApiModelProperty(value="用户id")
	@TableField(value="user_id")
	private Long userId;
	/**
	 * 是否删除[0=未删除，1=删除]
	 */
    @ApiModelProperty(value="是否删除[0=未删除，1=删除]")
	@TableField(value="del_flag")
	private Boolean delFlag;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(value="创建时间")
	@TableField(value="creat_time")
	private Date creatTime;
	/**
	 * 更新时间
	 */
    @ApiModelProperty(value="更新时间")
	@TableField(value="update_time")
	private Date updateTime;
	/**
	 * 扩展字段
	 */
    @ApiModelProperty(value="扩展字段")
	@TableField(value="custom_data")
	private String customData;
    
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean isDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
