package com.aek.ebey.cms.model.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class ContentDetailVO {

	@ApiModelProperty(value="[通知、消息、文章]id")
	private Long id;
	@ApiModelProperty(value="标题")
	private String title;
	@ApiModelProperty(value="内容")
	private String content;
	@ApiModelProperty(value="发布人")
	private String publisher;
	@ApiModelProperty(value="发布人所在机构名称")
	private String org;
	@ApiModelProperty(value="发布时间（时间戳）")
	private Date publishTime;
	@ApiModelProperty(value="是否最新发布（true：是；false：否）")
	private Boolean isNews;
	@ApiModelProperty(value="附件url")
	private String url;
	@ApiModelProperty(value="是否可以编辑")
	private Boolean isEdit;
	
	public Boolean getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}
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
	public Boolean getIsNews() {
		return isNews;
	}
	public void setIsNews(Boolean isNews) {
		this.isNews = isNews;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	
	
}
