package com.aek.ebey.cms.model.bo;

import io.swagger.annotations.ApiModelProperty;

public class Complaint {

	@ApiModelProperty(value="统计数量")
	private Integer count;
	
	@ApiModelProperty(value="是否有新内容")
	private Boolean hasNews;
	
	@ApiModelProperty(value="投诉类型[1：售后服务2：质量缺陷3：操作使用4：产品配置5：其它]")
	private Integer complaintType;

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

	public Integer getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(Integer complaintType) {
		this.complaintType = complaintType;
	}
	
	
}
