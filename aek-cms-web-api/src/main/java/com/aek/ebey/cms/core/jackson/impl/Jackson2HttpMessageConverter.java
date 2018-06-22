package com.aek.ebey.cms.core.jackson.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimeZone;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-9-27 下午4:05:46
 */
public class Jackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	private ObjectMapper objectMapper = new ObjectMapper();
	private boolean prefixJson = false;

	/**
	 * <br>
	 * 2013-9-27 下午4:10:28
	 * 
	 * @see org.springframework.http.converter.json.MappingJackson2HttpMessageConverter#writeInternal(Object,
	 *      org.springframework.http.HttpOutputMessage)
	 */
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		// super.writeInternal(object, outputMessage);

		// 判断是否需要重写objectMapper
		ObjectMapper objectMapper = this.objectMapper;// 本地化ObjectMapper，防止方法级别的ObjectMapper改变全局ObjectMapper
		if (ThreadJacksonMixInHolder.isContainsMixIn()) {
			objectMapper = ThreadJacksonMixInHolder.builderMapper();
		}
		JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(outputMessage.getBody(), JsonEncoding.UTF8);

		if (objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
			jsonGenerator.useDefaultPrettyPrinter();
		}
		try {
			objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			objectMapper.setSerializationInclusion(Include.NON_NULL);
			objectMapper.writeValue(jsonGenerator, object);
			objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
			objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	public boolean isPrefixJson() {
		return prefixJson;
	}

	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
	}

}
