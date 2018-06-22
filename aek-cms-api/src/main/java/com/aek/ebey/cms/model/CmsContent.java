package com.aek.ebey.cms.model;

import com.aek.common.core.base.BaseModel;
import com.aek.ebey.cms.model.bo.Article;
import com.aek.ebey.cms.model.bo.Complaint;
import com.aek.ebey.cms.model.bo.Consultation;
import com.aek.ebey.cms.model.bo.News;
import com.aek.ebey.cms.model.bo.Notice;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
@ApiModel
@TableName("cms_content")
public class CmsContent extends BaseModel {

    private static final long serialVersionUID = 1L;

	/**
	 * 标题
	 */
    @ApiModelProperty(value="标题")
	private String title;
	/**
	 * 内容
	 */
    @ApiModelProperty(value="内容")
	private String content;
	/**
	 * 内容分类[0=通知，1=消息，2=文章，3=投诉与不良事件，4=技术咨询]
	 */
	@ApiModelProperty(value="内容分类[0=通知，1=消息，2=文章，3=投诉与不良事件，4=技术咨询]")
	@TableField(value="type")
	private Integer type;
	/**
	 * 附件路径
	 */
	@ApiModelProperty(value="附件路径")
	@TableField(value="url")
	private String url;
	/**
	 * 文章创建人
	 */
	@ApiModelProperty(value="文章创建人")
	@TableField(value="create_by")
	private Long createBy;
	
	@ApiModelProperty(value="文章发布人")
	@TableField(value="release_by")
	private Long releaseBy;
	
	@ApiModelProperty(value="文章发布时间")
	@TableField(value="release_time")
	private Date releaseTime;
	
	@ApiModelProperty(value="文章发布人姓名")
	@TableField(value="release_person_name")
	private String releasePersonName;

	/**
	 * 来源机构id
	 */
	@ApiModelProperty(value="来源机构id")
	@TableField(value="from_tenant_id")
	private Long fromTenantId;
	/**
	 * 来源机构名称
	 */
	@ApiModelProperty(value="来源机构名称")
	@TableField(value="from_tenant_name")
	private String fromTenantName;
	/**
	 * 来源部门名称
	 */
	@ApiModelProperty(value="来源部门名称")
	@TableField(value="from_dept_name")
	private String fromDeptName;
	/**
	 * 指定的机构的ids
	 */
	@ApiModelProperty(value="指定的机构的ids")
	@TableField(value="to_tenant_ids")
	private String toTenantIds;
	/**
	 * 指定的机构的ids副本
	 */
	@ApiModelProperty(value="指定的机构的ids副本")
	@TableField(value="to_tenant_ids_copy")
	private String toTenantIdsCopy;
	/**
	 * 文章创建时间
	 */
	@ApiModelProperty(value="文章创建时间")
	@TableField(value="create_time")
	private Date createTime;
	/**
	 * 文章更新人
	 */
	@ApiModelProperty(value="文章更新人")
	@TableField(value="update_by")
	private Long updateBy;
	/**
	 * 文章更新时间
	 */
	@ApiModelProperty(value="文章更新时间")
	@TableField(value="update_time")
	private Date updateTime;
	/**
	 * 文章到期时间
	 */
	@ApiModelProperty(value="文章到期时间")
	@TableField(value="expire_time")
	private Date expireTime;
	/**
	 * 备注信息
	 */
	@ApiModelProperty(value="备注信息")
	private String remarks;
	/**
	 * 启用标记 1 启用 0 停用
	 */
	@ApiModelProperty(value="启用标记 1 启用 0 停用")
	private Boolean enable;
	/**
	 * 删除标记 1 已删除,0 未删除
	 */
	@ApiModelProperty(value="删除标记 1 已删除,0 未删除")
	@TableField(value="del_flag")
	private Boolean delFlag;
	/**
	 * 扩展字段
	 */
	@ApiModelProperty(value="扩展字段")
	@TableField(value="custom_data")
	private String customData;
	
	/**
	 * 统数量计
	 */
	@ApiModelProperty(value="统数量计")
	@TableField(exist=false)
	private Integer count;
	
	/**
	 * 是否阅读
	 */
	@ApiModelProperty(value="是否阅读")
	@TableField(exist=false)
	private Boolean isRead;
	
	/**
	 * 是否最新发布
	 */
	@ApiModelProperty(value="是否最新发布")
	@TableField(exist=false)
	private Boolean isNew;
	
	/**
	 * 发布人
	 */
	@ApiModelProperty(value="发布人")
	@TableField(exist=false)
	private String publisher;
	
	/**
	 * 发布机构
	 */
	@ApiModelProperty(value="发布机构")
	@TableField(exist=false)
	private String org;
	
	/**
	 * 发布时间
	 */
	@ApiModelProperty(value="发布时间")
	@TableField(exist=false)
	private Date publishTime;
	
	/*------------------五类对象-----------------------------------------*/
	@ApiModelProperty(value="文章")
	@TableField(exist=false)
	private Article article;
	
	@ApiModelProperty(value="投诉与不良事件")
	@TableField(exist=false)
	private Complaint complaint;
	
	@ApiModelProperty(value="技术咨询")
	@TableField(exist=false)
	private Consultation consultation;
	
	@ApiModelProperty(value="消息")
	@TableField(exist=false)
	private News news;
	
	@ApiModelProperty(value="通知")
	@TableField(exist=false)
	private Notice notice;
	/*------------------五类对象-----------------------------------------*/
	
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getFromTenantId() {
		return fromTenantId;
	}

	public void setFromTenantId(Long fromTenantId) {
		this.fromTenantId = fromTenantId;
	}

	public String getToTenantIds() {
		return toTenantIds;
	}

	public void setToTenantIds(String toTenantIds) {
		this.toTenantIds = toTenantIds;
	}

	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean isEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean isDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Complaint getComplaint() {
		return complaint;
	}

	public void setComplaint(Complaint complaint) {
		this.complaint = complaint;
	}

	public Consultation getConsultation() {
		return consultation;
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public Long getReleaseBy() {
		return releaseBy;
	}

	public void setReleaseBy(Long releaseBy) {
		this.releaseBy = releaseBy;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
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

	public String getReleasePersonName() {
		return releasePersonName;
	}

	public void setReleasePersonName(String releasePersonName) {
		this.releasePersonName = releasePersonName;
	}

	public String getFromTenantName() {
		return fromTenantName;
	}

	public void setFromTenantName(String fromTenantName) {
		this.fromTenantName = fromTenantName;
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

	public String getFromDeptName() {
		return fromDeptName;
	}

	public void setFromDeptName(String fromDeptName) {
		this.fromDeptName = fromDeptName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToTenantIdsCopy() {
		return toTenantIdsCopy;
	}

	public void setToTenantIdsCopy(String toTenantIdsCopy) {
		this.toTenantIdsCopy = toTenantIdsCopy;
	}
	
	
}
