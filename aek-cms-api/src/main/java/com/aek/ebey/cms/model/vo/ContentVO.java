package com.aek.ebey.cms.model.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class ContentVO {

	@ApiModelProperty(value="[通知、消息、文章]id")
	private Long id;
	@ApiModelProperty(value="标题")
	private String title;
	@ApiModelProperty(value="发布人")
	private String publisher;
	@ApiModelProperty(value="发布人所在机构名称")
	private String org;
	@ApiModelProperty(value="发布时间（时间戳）")
	private Date publishTime;
	@ApiModelProperty(value="是否阅读")
	private Boolean isRead;
	@ApiModelProperty(value="是否最新发布（true：是；false：否）")
	private Boolean isNew;
	
	//为小程序增加属性
	@ApiModelProperty(value="内容")
	private String content;
	@ApiModelProperty(value="类型")
	private Integer frontType;
	@ApiModelProperty(value="标识类型")
	private Integer type;
	@ApiModelProperty(value="回复状态（1：已回复；2：待回复）")
	private Integer reply;
	
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
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getReply() {
		return reply;
	}
	public void setReply(Integer reply) {
		this.reply = reply;
	}
	public Integer getFrontType() {
		return frontType;
	}
	public void setFrontType(Integer frontType) {
		this.frontType = frontType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	

	
}
