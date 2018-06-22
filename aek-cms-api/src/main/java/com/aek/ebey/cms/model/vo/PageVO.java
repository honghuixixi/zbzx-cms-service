package com.aek.ebey.cms.model.vo;

import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.ApiModelProperty;

public class PageVO<CmsReply> extends Page<CmsReply> {

	private static final long serialVersionUID = 1L;
	/**
	 * 是否可删除
	 */
    @ApiModelProperty(value="是否可删除")
	private Boolean canDel;

	public Boolean getCanDel() {
		return canDel;
	}

	public void setCanDel(Boolean canDel) {
		this.canDel = canDel;
	}
    
}
