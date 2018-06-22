package com.aek.ebey.cms.model.vo;

import com.aek.ebey.cms.model.CmsContent;
import com.baomidou.mybatisplus.annotations.TableField;

import io.swagger.annotations.ApiModelProperty;

public class CmsContentVO extends CmsContent{

	@ApiModelProperty(value="是否有新内容")
	@TableField(exist=false)
    private Boolean hasNews;

	public Boolean getHasNews() {
		return hasNews;
	}

	public void setHasNews(Boolean hasNews) {
		this.hasNews = hasNews;
	}
	
}
