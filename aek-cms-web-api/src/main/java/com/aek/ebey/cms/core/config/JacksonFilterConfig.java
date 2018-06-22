package com.aek.ebey.cms.core.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.aek.ebey.cms.core.jackson.impl.Jackson2HttpMessageConverter;

/**
 * json过滤控制器
 * 
 * @author Mr.han
 *
 */
@Configuration
public class JacksonFilterConfig extends WebMvcConfigurerAdapter {
	
	/**
	 * 注入Jackson2HttpMessageConverter控制器
	 * 
	 * @return
	 */
	private MappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new Jackson2HttpMessageConverter();
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		converter.setSupportedMediaTypes(supportedMediaTypes);
		return converter;
	}

	/**
	 * 添加控制器
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		converters.add(jacksonHttpMessageConverter());
	}
}
