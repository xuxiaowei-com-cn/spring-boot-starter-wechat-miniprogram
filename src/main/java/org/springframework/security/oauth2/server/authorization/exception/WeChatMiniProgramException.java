package org.springframework.security.oauth2.server.authorization.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * 微信小程序父异常
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class WeChatMiniProgramException extends OAuth2AuthenticationException {

	public WeChatMiniProgramException(String errorCode) {
		super(errorCode);
	}

	public WeChatMiniProgramException(OAuth2Error error) {
		super(error);
	}

	public WeChatMiniProgramException(OAuth2Error error, Throwable cause) {
		super(error, cause);
	}

	public WeChatMiniProgramException(OAuth2Error error, String message) {
		super(error, message);
	}

	public WeChatMiniProgramException(OAuth2Error error, String message, Throwable cause) {
		super(error, message, cause);
	}

}
