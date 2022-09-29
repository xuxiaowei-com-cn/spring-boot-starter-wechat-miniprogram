package org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.InMemoryWeChatMiniProgramService;
import org.springframework.security.oauth2.server.authorization.client.WeChatMiniProgramService;
import org.springframework.security.oauth2.server.authorization.properties.WeChatMiniProgramProperties;

/**
 * 微信小程序 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class OAuth2WeChatMiniProgramConfiguration {

	private WeChatMiniProgramProperties weChatMiniProgramProperties;

	@Autowired
	public void setWeChatMiniProgramProperties(WeChatMiniProgramProperties weChatMiniProgramProperties) {
		this.weChatMiniProgramProperties = weChatMiniProgramProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public WeChatMiniProgramService weChatMiniProgramService() {
		return new InMemoryWeChatMiniProgramService(weChatMiniProgramProperties);
	}

}
