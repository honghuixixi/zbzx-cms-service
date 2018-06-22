package com.aek.ebey.cms.model.vo;

import io.swagger.annotations.ApiModelProperty;

public class ManageTreeVO {

	@ApiModelProperty(value="节点id")
	private Long id;
	@ApiModelProperty(value="父节点id")
	private Long pId;
	@ApiModelProperty(value="节点名称")
	private String name;
	@ApiModelProperty(value="机构等级")
	private Integer parentTenantRank;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getpId() {
		return pId;
	}
	public void setpId(Long pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentTenantRank() {
		return parentTenantRank;
	}
	public void setParentTenantRank(Integer parentTenantRank) {
		this.parentTenantRank = parentTenantRank;
	}
	
	
}
