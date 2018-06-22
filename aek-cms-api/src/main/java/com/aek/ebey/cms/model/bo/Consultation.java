package com.aek.ebey.cms.model.bo;

import io.swagger.annotations.ApiModelProperty;

public class Consultation {

	@ApiModelProperty(value="统计数量")
	private Integer count;
	
	@ApiModelProperty(value="是否有新内容")
	private Boolean hasNews;
	
	@ApiModelProperty(value="咨询类型[1：采购计划2：验收管理3：使用管理4：巡检质控5：信息化6：其它]")
	private Integer consultationType;

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

	public Integer getConsultationType() {
		return consultationType;
	}

	public void setConsultationType(Integer consultationType) {
		this.consultationType = consultationType;
	}
	
	
}
