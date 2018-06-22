package com.aek.ebey.cms.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.cms.model.vo.ContentVO;

import io.swagger.annotations.ApiModelProperty;

public class ContentQuery extends PageHelp<ContentVO> {

	@ApiModelProperty(value="搜索关键字")
	private String keyword;
	
	@ApiModelProperty(value="搜索类型")
	private Integer type;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
