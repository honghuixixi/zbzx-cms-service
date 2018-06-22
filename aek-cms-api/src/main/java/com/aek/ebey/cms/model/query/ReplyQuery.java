package com.aek.ebey.cms.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.cms.model.CmsReply;

import io.swagger.annotations.ApiModelProperty;

public class ReplyQuery extends PageHelp<CmsReply> {

	@ApiModelProperty(value="文章id")
	private Long id;
	
	@ApiModelProperty(value="当前页码从1开始）")
	private Integer pageNo;
	
	@ApiModelProperty(value="每页记录数")
	private Integer pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
