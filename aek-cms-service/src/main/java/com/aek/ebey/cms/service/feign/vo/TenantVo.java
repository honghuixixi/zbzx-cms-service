package com.aek.ebey.cms.service.feign.vo;

import io.swagger.annotations.ApiModelProperty;

public class TenantVo {

	@ApiModelProperty(value="机构id")
	private Long id;
	@ApiModelProperty(value="机构名称")
	private String name;
	//@ApiModelProperty(value="上级行政Id")
	private Long manageTenantId;
	@ApiModelProperty(value="机构等级")
	private Integer parentTenantRank;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getManageTenantId() {
		return manageTenantId;
	}
	public void setManageTenantId(Long manageTenantId) {
		this.manageTenantId = manageTenantId;
	}
	public Integer getParentTenantRank() {
		return parentTenantRank;
	}
	public void setParentTenantRank(Integer parentTenantRank) {
		this.parentTenantRank = parentTenantRank;
	}

	
	
}
