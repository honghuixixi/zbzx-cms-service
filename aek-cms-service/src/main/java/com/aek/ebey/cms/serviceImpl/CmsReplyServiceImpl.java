package com.aek.ebey.cms.serviceImpl;

import com.aek.ebey.cms.model.CmsContent;
import com.aek.ebey.cms.model.CmsReply;
import com.aek.ebey.cms.model.CmsReplyUser;
import com.aek.ebey.cms.model.query.ReplyQuery;
import com.aek.ebey.cms.model.vo.PageVO;
import com.aek.ebey.cms.mapper.CmsContentMapper;
import com.aek.ebey.cms.mapper.CmsReplyMapper;
import com.aek.ebey.cms.mapper.CmsReplyUserMapper;
import com.aek.ebey.cms.service.CmsContentService;
import com.aek.ebey.cms.service.CmsReplyService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.aek.common.core.base.BaseServiceImpl;
import com.aek.common.core.exception.ExceptionFactory;
import com.aek.common.core.serurity.WebSecurityUtils;
import com.aek.common.core.serurity.model.AuthUser;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxx
 * @since 2017-10-31
 */
@Service
@Transactional
public class CmsReplyServiceImpl extends BaseServiceImpl<CmsReplyMapper, CmsReply> implements CmsReplyService {

	@Autowired
	CmsContentService cmsContentService;
	
	@Autowired
	CmsReplyUserMapper cmsReplyUserMapper;
	
	@Autowired
	CmsContentMapper cmsContentMapper;
	
	@Override
	public PageVO<CmsReply> getReplyLists(ReplyQuery query) {
		
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		CmsContent exist = cmsContentMapper.isExist(currentUser.getTenantId(), query.getId());
		if(exist == null){
			throw ExceptionFactory.create("l_001");
		}
		
		Page<CmsReply> page = query.getPage();
		if(query.getPageSize() == null){
			page.setSize(8);
		}else {
			page.setSize(query.getPageSize());
		}
		
		page.setCurrent(query.getPageNo());
		PageVO<CmsReply> pageVO = new PageVO<CmsReply>();
		Wrapper<CmsReply> wrapper = new EntityWrapper<CmsReply>();
		
		if(query.getId() != null){
			wrapper.eq("content_id", query.getId());
		}
		wrapper.eq("del_flag", false);
		wrapper.orderBy("reply_time", false);
		Page<CmsReply> replyPage = this.selectPage(page, wrapper);
		List<CmsReply> records = replyPage.getRecords();
		if(records != null && records.size() > 0){
			for (CmsReply record : records) {
				record.setOrg(record.getDeptName()+" "+record.getOrg());
			}
		}
		//处理文章是否可以删除
		CmsContent cmsContent = cmsContentService.selectById(query.getId());
		Long userId = currentUser.getId();
		if(cmsContent != null){
			if(cmsContent.getCreateBy().longValue()==userId.longValue() && cmsContent.getReleaseBy().longValue()==userId.longValue()){
				pageVO.setCanDel(true);
			}else{
				pageVO.setCanDel(false);
			}
		}
		//构造输出pageVO
		pageVO.setTotal(page.getTotal());
		pageVO.setSize(page.getSize());
		pageVO.setRecords(page.getRecords());	
		return pageVO;
	}
	
	@Override
	public Page<CmsReply> getBadAskReplyList(ReplyQuery query) {
		CmsContent cmsContent = cmsContentMapper.selectById(query.getId());
		if(cmsContent !=null){
			Integer type = cmsContent.getType();
			if(type.intValue() == 3){
				AuthUser currentUser = WebSecurityUtils.getCurrentUser();
				CmsContent exist = cmsContentMapper.isExist(currentUser.getTenantId(), query.getId());
				if(exist == null){
					throw ExceptionFactory.create("l_001");
				}
				
			}
		}
		
		Page<CmsReply> page = query.getPage();
		if(query.getPageSize() == null){
			page.setSize(8);
		}else {
			page.setSize(query.getPageSize());
		}
		page.setCurrent(query.getPageNo());
		Wrapper<CmsReply> wrapper = new EntityWrapper<CmsReply>();
		
		if(query.getId() != null){
			wrapper.eq("content_id", query.getId());
		}
		wrapper.eq("del_flag", false);
		wrapper.orderBy("reply_time", false);		
		return this.selectPage(page, wrapper);
	}

	@Override
	public void addReply(CmsReply reply) {
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		String name = currentUser.getRealName();
		String tenantName = currentUser.getTenantName();
		String deptName = currentUser.getDeptName();
		
		Date now = new Date();
		reply.setReplyTime(now);
		reply.setReplyName(name);
		reply.setOrg(tenantName);
		reply.setDeptName(deptName);
		CmsContent contentNotDel = cmsContentMapper.getByIdNotDel(reply.getContentId());
		//CmsContent exist = cmsContentMapper.isExist(currentUser.getTenantId(),reply.getContentId());
		if(contentNotDel==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		this.insert(reply);
	}

	@Override
	public void deletReply(Long contentId, Long id) {
		CmsContent contentNotDel = cmsContentMapper.getByIdNotDel(contentId);
		if(contentNotDel==null){
			throw ExceptionFactory.create("d_003");//内容不存在提示
		}
		if(contentId == null){
			throw ExceptionFactory.create("r_004");
		}	
		if(id == null){
			throw ExceptionFactory.create("r_005");
		}
		AuthUser currentUser = WebSecurityUtils.getCurrentUser();
		CmsContent cmsContent = cmsContentService.selectById(contentId);
		if(cmsContent != null){
			Long createBy = cmsContent.getCreateBy();
			Long currentUserId = currentUser.getId();
			if(createBy.longValue() != currentUserId.longValue()){
				throw ExceptionFactory.create("r_003");
			}
			CmsReply reply = new CmsReply();
			reply.setId(id);
			reply.setDelFlag(true);
			this.updateById(reply);
			
			//删除cms_reply_user表关联数据
			CmsReplyUser cmsReplyUser = new CmsReplyUser();
			Long findByReplyId = cmsReplyUserMapper.findByReplyId(id,currentUserId,contentId);
			cmsReplyUser.setId(findByReplyId);
			cmsReplyUser.setDelFlag(true);
			cmsReplyUserMapper.updateById(cmsReplyUser);
			
		}else if (cmsContent == null) {
			this.deleteById(id);
		}
		
	}


	
}
