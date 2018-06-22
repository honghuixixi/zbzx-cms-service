package com.aek.ebey.cms.model.vo;

import io.swagger.annotations.ApiModelProperty;

public class CurrentUserVO {

    @ApiModelProperty(value="用户ID")
    private Long userId;    
	@ApiModelProperty(value="姓名")
	private String name;	
	@ApiModelProperty(value="手机号码")
	private String phone;
	@ApiModelProperty(value="所在机构及科室名称")
	private String org;
	@ApiModelProperty(value="邮箱")
	private String email;
	@ApiModelProperty(value="机构类型，取值如下：1：监管机构2：医疗机构")
	private Integer part;
	@ApiModelProperty(value="机构id")
	private Long tenantId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getPart() {
		return part;
	}
	public void setPart(Integer part) {
		this.part = part;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
	
}
