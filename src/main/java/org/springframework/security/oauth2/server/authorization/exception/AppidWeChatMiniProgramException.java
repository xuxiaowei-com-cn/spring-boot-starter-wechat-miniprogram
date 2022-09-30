package org.springframework.security.oauth2.server.authorization.exception;

import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * 微信小程序 AppID(小程序ID) 异常
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class AppidWeChatMiniProgramException extends WeChatMiniProgramException {

	public AppidWeChatMiniProgramException(String errorCode) {
		super(errorCode);
	}

	public AppidWeChatMiniProgramException(OAuth2Error error) {
		super(error);
	}

	public AppidWeChatMiniProgramException(OAuth2Error error, Throwable cause) {
		super(error, cause);
	}

	public AppidWeChatMiniProgramException(OAuth2Error error, String message) {
		super(error, message);
	}

	public AppidWeChatMiniProgramException(OAuth2Error error, String message, Throwable cause) {
		super(error, message, cause);
	}

}
