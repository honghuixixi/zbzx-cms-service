package com.aek.ebey.cms.model.vo;

import io.swagger.annotations.ApiModelProperty;

public class TenantsVo {

	@ApiModelProperty(value="机构id")
	private Long id;
	@ApiModelProperty(value="机构名称")
	private String name;
	@ApiModelProperty(value="机构类型")
	private Integer type;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
