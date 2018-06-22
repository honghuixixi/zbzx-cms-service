package com.aek.ebey.cms.web.controller.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.aek.ebey.cms.model.bo.AttachmentsBO;
import com.aek.ebey.cms.web.validator.Add;
import com.aek.ebey.cms.web.validator.Edit;

import io.swagger.annotations.ApiModelProperty;

public class NewArticleOrNewsOrNoticeRequest {

	@ApiModelProperty(value="内容id")
	@NotNull(groups=Edit.class,message="n_005")
	private Long id;
	
	@ApiModelProperty(value="投诉标题")
	@NotNull(groups={Edit.class,Add.class},message="n_001")
	private String title;
	
	@ApiModelProperty(value="投诉内容")
	@NotNull(groups={Edit.class,Add.class},message="n_002")
	private String content;
	
	@NotEmpty(groups={Edit.class,Add.class},message="n_003")
	@ApiModelProperty(value="机构id集合")
	private Long[] ids;
	
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

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public List<AttachmentsBO> getFiles() {
		return files;
	}

	public void setFiles(List<AttachmentsBO> files) {
		this.files = files;
	}
	
	
}
