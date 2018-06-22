package com.aek.ebey.cms.model.vo;

import java.util.List;

import com.aek.ebey.cms.model.bo.AttachmentsBO;
import io.swagger.annotations.ApiModelProperty;

public class ContentReturnVo {

	@ApiModelProperty(value="内容id")
	private Long id;
	
	@ApiModelProperty(value="投诉标题")
	private String title;
	
	@ApiModelProperty(value="投诉内容")
	private String content;
	
	@ApiModelProperty(value="机构集合")
	private List<TenantsVo> tenants;
	
	@ApiModelProperty(value="接收附件数据")
	private List<AttachmentsBO> files;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<TenantsVo> getTenants() {
		return tenants;
	}

	public void setTenants(List<TenantsVo> tenants) {
		this.tenants = tenants;
	}

	public List<AttachmentsBO> getFiles() {
		return files;
	}

	public void setFiles(List<AttachmentsBO> files) {
		this.files = files;
	}
	
	
}
