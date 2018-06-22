package com.aek.ebey.cms.serviceImpl;

import com.aek.ebey.cms.model.CmsReplyUser;
import com.aek.ebey.cms.mapper.CmsReplyUserMapper;
import com.aek.ebey.cms.service.CmsReplyUserService;
import com.aek.common.core.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 回复用户关联表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2017-11-03
 */
@Service
@Transactional
public class CmsReplyUserServiceImpl extends BaseServiceImpl<CmsReplyUserMapper, CmsReplyUser> implements CmsReplyUserService {
	
}
