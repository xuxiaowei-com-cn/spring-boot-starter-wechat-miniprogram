package org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryWeChatAppletService;
import org.springframework.security.oauth2.server.authorization.client.WeChatMiniProgramService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Collections;

/**
 * 微信小程序 OAuth 2.0 配置器的实用方法。
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @see OAuth2ConfigurerUtils
 */
public class OAuth2WeChatMiniProgramConfigurerUtils {

	public static <B extends HttpSecurityBuilder<B>> OAuth2AuthorizationService getAuthorizationService(B builder) {
		return OAuth2ConfigurerUtils.getAuthorizationService(builder);
	}

	public static <B extends HttpSecurityBuilder<B>> OAuth2TokenGenerator<? extends OAuth2Token> getTokenGenerator(
			B builder) {
		return OAuth2ConfigurerUtils.getTokenGenerator(builder);
	}

	public static <B extends HttpSecurityBuilder<B>> WeChatMiniProgramService getWeChatMiniProgramService(B builder) {
		WeChatMiniProgramService wechatMiniProgramService = builder.getSharedObject(WeChatMiniProgramService.class);
		if (wechatMiniProgramService == null) {
			wechatMiniProgramService = OAuth2ConfigurerUtils.getOptionalBean(builder, WeChatMiniProgramService.class);
			if (wechatMiniProgramService == null) {
				wechatMiniProgramService = new InMemoryWeChatAppletService(Collections.emptyList(),
						"wechat_miniprogram");
			}
		}
		return wechatMiniProgramService;
	}

}
