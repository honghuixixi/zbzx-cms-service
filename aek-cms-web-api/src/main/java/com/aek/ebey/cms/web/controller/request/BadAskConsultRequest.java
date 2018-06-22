package com.aek.ebey.cms.web.controller.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.aek.ebey.cms.model.bo.AttachmentsBO;
import com.aek.ebey.cms.web.validator.Add;

import io.swagger.annotations.ApiModelProperty;

public class BadAskConsultRequest {

	@ApiModelProperty(value="申请人邮箱")
	private String email;
	@ApiModelProperty(value="申请人微信号")
	private String wx;
	@ApiModelProperty(value="产品名称")
	@NotNull(groups=Add.class,message="a_003")
	private String productor;
	@ApiModelProperty(value="厂商")
	@NotNull(groups=Add.class,message="a_004")
	private String manufacturer;
	@ApiModelProperty(value="投诉或技术咨询类型")
	@NotNull(groups=Add.class,message="a_005")
	private Integer type;
	@ApiModelProperty(value="标题")
	@NotNull(groups=Add.class,message="a_006")
	private String title;
	@ApiModelProperty(value="内容")
	@NotNull(groups=Add.class,message="a_007")
	private String content;
	@ApiModelProperty(value="附件路径")
	private String url;
	@ApiModelProperty(value="接收附件数据")
	private List<AttachmentsBO> files;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWx() {
		return wx;
	}
	public void setWx(String wx) {
		this.wx = wx;
	}
	public String getProductor() {
		return productor;
	}
	public void setProductor(String productor) {
		this.productor = productor;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<AttachmentsBO> getFiles() {
		return files;
	}
	public void setFiles(List<AttachmentsBO> files) {
		this.files = files;
	}
	
	
}
