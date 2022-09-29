package org.springframework.security.oauth2.server.authorization.exception;

import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * 微信小程序 Secret 异常
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class SecretWeChatMiniProgramException extends WeChatMiniProgramException {

	public SecretWeChatMiniProgramException(String errorCode) {
		super(errorCode);
	}

	public SecretWeChatMiniProgramException(OAuth2Error error) {
		super(error);
	}

	public SecretWeChatMiniProgramException(OAuth2Error error, Throwable cause) {
		super(error, cause);
	}

	public SecretWeChatMiniProgramException(OAuth2Error error, String message) {
		super(error, message);
	}

	public SecretWeChatMiniProgramException(OAuth2Error error, String message, Throwable cause) {
		super(error, message, cause);
	}

}
