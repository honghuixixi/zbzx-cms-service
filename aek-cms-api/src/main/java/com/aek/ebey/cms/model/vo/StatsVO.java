package com.aek.ebey.cms.model.vo;

import io.swagger.annotations.ApiModelProperty;

public class StatsVO {

	@ApiModelProperty(value="类型")
	private Integer type;
	@ApiModelProperty(value="统计数量")
	private Integer count;
	@ApiModelProperty(value="是否有新内容")
	private boolean hasNews;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public boolean isHasNews() {
		return hasNews;
	}
	public void setHasNews(boolean hasNews) {
		this.hasNews = hasNews;
	}
	
	
}
