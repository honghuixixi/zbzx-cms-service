package com.aek.ebey.cms.core.jackson.impl;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Jackson2JsonView extends MappingJackson2JsonView{
	
	ObjectMapper cleanObjectMapper = new ObjectMapper();
	
	@Override
	protected Object filterModel(Map<String, Object> model) {
		ObjectMapper objectMapper = null;// 本地化ObjectMapper，防止方法级别的ObjectMapper改变全局ObjectMapper  
        if (ThreadJacksonMixInHolder.isContainsMixIn()) {  
            objectMapper = ThreadJacksonMixInHolder.builderMapper();  
        }  
        if(objectMapper != null){
        	setObjectMapper(objectMapper);
        }else
        	setObjectMapper(cleanObjectMapper);  //正常的objectMapper
        
        return super.filterModel(model);
	}
	
	
}
