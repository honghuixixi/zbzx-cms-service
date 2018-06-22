package com.aek.ebey.cms.service;

import com.aek.ebey.cms.model.CmsContentUser;
import com.aek.ebey.cms.model.query.ContentUserQuery;
import com.aek.ebey.cms.model.vo.CurrentUserVO;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

import com.aek.common.core.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
public interface CmsContentUserService extends BaseService<CmsContentUser> {
		
	CurrentUserVO getCurrentUser();
	
	CurrentUserVO getUser();
	
}
