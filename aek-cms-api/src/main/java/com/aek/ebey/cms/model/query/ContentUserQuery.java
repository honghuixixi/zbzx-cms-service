package com.aek.ebey.cms.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.cms.model.CmsContentUser;

import io.swagger.annotations.ApiModelProperty;

public class ContentUserQuery extends PageHelp<CmsContentUser>{

	@ApiModelProperty(value="内容类型/投诉类型/技术咨询类型")
	private Integer type;
	
	@ApiModelProperty(value="关键字")
	private String keyword;
	
	@ApiModelProperty(value="排序方式[0：默认1：已回复2：待回复]")
	private Integer order;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeywords(String keyword) {
		this.keyword = keyword;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
}
