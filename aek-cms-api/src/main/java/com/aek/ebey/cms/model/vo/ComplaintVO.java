package com.aek.ebey.cms.model.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class ComplaintVO {

	@ApiModelProperty(value="投诉id")
	private Long id;
	@ApiModelProperty(value="投诉标题")
	private String title;
	@ApiModelProperty(value="投诉类型（取值见参数）")
	private Integer type;
	@ApiModelProperty(value="提交人")
	private String publisher;
	@ApiModelProperty(value="提交人所在机科室名称")
	private String org;
	@ApiModelProperty(value="提交时间（时间戳）")
	private Date publishTime;
	@ApiModelProperty(value="是否阅读")
	private Boolean isRead;
	@ApiModelProperty(value="回复状态（1：已回复；2：待回复）")
	private Integer reply;
	@ApiModelProperty(value="最新回复机构名称")
	private String replyOrg;
	@ApiModelProperty(value="回复时间（时间戳）")
	private Date replyTime;
	@ApiModelProperty(value="回复内容")
	private String replyContent;
	//为小程序
	@ApiModelProperty(value="内容")
	private String content;
	
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public Integer getReply() {
		return reply;
	}
	public void setReply(Integer reply) {
		this.reply = reply;
	}
	public String getReplyOrg() {
		return replyOrg;
	}
	public void setReplyOrg(String replyOrg) {
		this.replyOrg = replyOrg;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
