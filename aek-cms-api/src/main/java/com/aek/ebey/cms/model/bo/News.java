package com.aek.ebey.cms.model.bo;

import io.swagger.annotations.ApiModelProperty;

public class News {

	@ApiModelProperty(value="统计数量")
	private Integer count;
	
	@ApiModelProperty(value="是否有新内容")
	private Boolean hasNews;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Boolean getHasNews() {
		return hasNews;
	}

	public void setHasNews(Boolean hasNews) {
		this.hasNews = hasNews;
	}
}
