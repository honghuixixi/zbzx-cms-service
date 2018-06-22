package com.aek.ebey.cms.core.config;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.aek.common.core.util.SecurityUtil;

/**
 * 自定义加密实现类
 *
 * @author  Honghui
 * @date    2017年6月22日
 * @version 1.0
 */
public class CustomPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		return SecurityUtil.encryptDes(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String password = "password=\""+rawPassword+"\"";
		String decryptDes = SecurityUtil.decryptDes(encodedPassword);
		if(password.equals(decryptDes) || rawPassword.equals(decryptDes)){
			return true;
		}
		return false;
	}
}

