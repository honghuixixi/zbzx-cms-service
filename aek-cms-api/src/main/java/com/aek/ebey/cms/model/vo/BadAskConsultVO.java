package com.aek.ebey.cms.model.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class BadAskConsultVO {

	@ApiModelProperty(value="投诉/技术咨询id")
	private Long id;
	@ApiModelProperty(value="标题")
	private String title;
	private String content;
	private String publisher;
	private String org;
	private Date publishTime;
	private String phone;
	private String email;
	private String wx;
	private String productor;
	private String manufacturer;
	private Integer type; 
	private Boolean isNews;
	private Boolean replyLimit;
	private String url;
	private Boolean isDel;
	private String deptName;
	
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
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
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
	public Boolean getIsNews() {
		return isNews;
	}
	public void setIsNews(Boolean isNews) {
		this.isNews = isNews;
	}
	public Boolean getReplyLimit() {
		return replyLimit;
	}
	public void setReplyLimit(Boolean replyLimit) {
		this.replyLimit = replyLimit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getIsDel() {
		return isDel;
	}
	public void setIsDel(Boolean isDel) {
		this.isDel = isDel;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	
}
