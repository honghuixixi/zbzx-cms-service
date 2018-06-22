package com.aek.ebey.cms.web.controller.request;

import javax.validation.constraints.NotNull;

import com.aek.ebey.cms.web.validator.Add;

import io.swagger.annotations.ApiModelProperty;

public class ReplyRequest {

	@ApiModelProperty(value="内容id")
	@NotNull(groups=Add.class,message="r_001")
	private Long id;
	
	@ApiModelProperty(value="回复内容")
	@NotNull(groups=Add.class,message="r_002")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
