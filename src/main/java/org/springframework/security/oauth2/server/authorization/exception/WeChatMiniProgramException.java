package org.springframework.security.oauth2.server.authorization.exception;

/**
 * 微信小程序父异常
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class WeChatMiniProgramException extends RuntimeException {

	public WeChatMiniProgramException(String message) {
		super(message);
	}

	public WeChatMiniProgramException(String message, Throwable cause) {
		super(message, cause);
	}

}
