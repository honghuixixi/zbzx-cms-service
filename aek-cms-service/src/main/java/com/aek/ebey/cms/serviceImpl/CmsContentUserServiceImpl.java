package com.aek.ebey.cms.serviceImpl;

import com.aek.ebey.cms.model.CmsContent;
import com.aek.ebey.cms.model.CmsContentUser;
import com.aek.ebey.cms.model.CmsReply;
import com.aek.ebey.cms.model.bo.Complaint;
import com.aek.ebey.cms.model.query.ContentUserQuery;
import com.aek.ebey.cms.model.vo.CurrentUserVO;
import com.aek.ebey.cms.model.vo.ReplyVO;
import com.aek.ebey.cms.mapper.CmsContentUserMapper;
import com.aek.ebey.cms.mapper.CmsReplyMapper;
import com.aek.ebey.cms.service.CmsContentService;
import com.aek.ebey.cms.service.CmsContentUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aek
 * @since 2017-10-25
 */
@Service
@Transactional
public class CmsContentUserServiceImpl extends BaseServiceImpl<CmsContentUserMapper, CmsContentUser> implements CmsContentUserService {

	private Logger logger = LoggerFactory.getLogger(CmsContentUserServiceImpl.class);
	
	@Autowired
	private CmsContentUserMapper cmsContentUserMapper;
	@Autowired
	private CmsContentService cmsContentService;
	@Autowired
	private CmsReplyMapper cmsReplyMapper;
	
	@Override
	public CurrentUserVO getCurrentUser() {
		CurrentUserVO currentUserVO = new CurrentUserVO();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		currentUserVO.setUserId(currentUser.getId());
		currentUserVO.setName(currentUser.getRealName());
		currentUserVO.setEmail(currentUser.getEmail());
		if(currentUser.getTenantName().equals(currentUser.getDeptName())){
			currentUserVO.setOrg(currentUser.getTenantName());
		}else {
			if(StringUtils.isBlank(currentUser.getDeptName())){
				currentUserVO.setOrg(currentUser.getTenantName());			
			}else {
				currentUserVO.setOrg(currentUser.getDeptName()+" "+currentUser.getTenantName());
			}		
		}			
		currentUserVO.setPhone(currentUser.getMobile());
		currentUserVO.setTenantId(currentUser.getTenantId());
		if(currentUser.getTenantType() == 1){
			currentUserVO.setPart(2);
		}else if (currentUser.getTenantType() == 2) {
			currentUserVO.setPart(1);
		}	
		return currentUserVO;
	}

	@Override
	public CurrentUserVO getUser() {
		CurrentUserVO currentUserVO = new CurrentUserVO();
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		currentUserVO.setUserId(currentUser.getId());
		currentUserVO.setName(currentUser.getRealName());
		currentUserVO.setEmail(currentUser.getEmail());
		currentUserVO.setOrg(currentUser.getTenantName());
		currentUserVO.setPhone(currentUser.getMobile());
		if(currentUser.getTenantType() == 1){
			currentUserVO.setPart(2);
		}else if (currentUser.getTenantType() == 2) {
			currentUserVO.setPart(1);
		}	
		return currentUserVO;
	}

	
}
