package com.aek.ebey.cms.model.query;

import com.aek.common.core.base.page.PageHelp;
import com.aek.ebey.cms.model.vo.ConsultationVO;

import io.swagger.annotations.ApiModelProperty;

public class ConsultationQuery extends PageHelp<ConsultationVO> {

	@ApiModelProperty(value="技术咨询类型，取值如下：1：采购计划2：验收管理3：使用管理4：巡检质控5：信息化6：其它")
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
