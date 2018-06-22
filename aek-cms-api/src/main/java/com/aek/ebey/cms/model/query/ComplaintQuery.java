package com.aek.ebey.cms.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.cms.model.vo.ComplaintVO;

import io.swagger.annotations.ApiModelProperty;

public class ComplaintQuery extends PageHelp<ComplaintVO> {

	@ApiModelProperty(value="投诉类型，取值如下：1：售后服务2：质量缺陷3：操作使用4：产品配置5：其它")
	private Integer type;
	
	@ApiModelProperty(value="关键字")
	private String keyword;
	
	@ApiModelProperty(value="排序方式[0：默认1：已回复2：待回复]")	
	private Integer order;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
}
