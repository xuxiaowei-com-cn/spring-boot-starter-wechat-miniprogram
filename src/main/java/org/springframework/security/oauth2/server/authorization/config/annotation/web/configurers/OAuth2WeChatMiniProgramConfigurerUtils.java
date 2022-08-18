package org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryWeChatMiniProgramService;
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
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2WeChatMiniProgramConfigurerUtils {

	public static OAuth2AuthorizationService getAuthorizationService(HttpSecurity httpSecurity) {
		return OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
	}

	public static OAuth2TokenGenerator<? extends OAuth2Token> getTokenGenerator(HttpSecurity httpSecurity) {
		return OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
	}

	public static WeChatMiniProgramService getWeChatMiniProgramService(HttpSecurity httpSecurity) {
		WeChatMiniProgramService wechatMiniProgramService = httpSecurity
				.getSharedObject(WeChatMiniProgramService.class);
		if (wechatMiniProgramService == null) {
			wechatMiniProgramService = OAuth2ConfigurerUtils.getOptionalBean(httpSecurity,
					WeChatMiniProgramService.class);
			if (wechatMiniProgramService == null) {
				wechatMiniProgramService = new InMemoryWeChatMiniProgramService(Collections.emptyList(),
						"wechat_miniprogram");
			}
		}
		return wechatMiniProgramService;
	}

}
