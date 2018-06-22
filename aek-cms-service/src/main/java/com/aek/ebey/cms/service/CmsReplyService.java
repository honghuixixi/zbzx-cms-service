package com.aek.ebey.cms.service;

import com.aek.ebey.cms.model.CmsReply;
import com.aek.ebey.cms.model.query.ReplyQuery;
import com.aek.ebey.cms.model.vo.PageVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.aek.common.core.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2017-10-31
 */
public interface CmsReplyService extends BaseService<CmsReply> {
	
	PageVO<CmsReply> getReplyLists(ReplyQuery query);
	
	Page<CmsReply> getBadAskReplyList(ReplyQuery query);
	
	void addReply(CmsReply reply);
	
	void deletReply(Long contentId,Long id);
	
}
