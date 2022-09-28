package org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.InMemoryWeChatMiniProgramService;
import org.springframework.security.oauth2.server.authorization.client.WeChatMiniProgramService;
import org.springframework.security.oauth2.server.authorization.properties.WeChatMiniProgramProperties;

import java.util.List;

/**
 * 微信小程序 配置
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class OAuth2WeChatMiniProgramConfiguration {

	private WeChatMiniProgramProperties wechatMiniProgramProperties;

	@Autowired
	public void setWechatMiniProgramProperties(WeChatMiniProgramProperties wechatMiniProgramProperties) {
		this.wechatMiniProgramProperties = wechatMiniProgramProperties;
	}

	@Bean
	@ConditionalOnMissingBean
	public WeChatMiniProgramService weChatMiniProgramService() {
		List<WeChatMiniProgramProperties.WeChatMiniProgram> wechatMiniProgramList = wechatMiniProgramProperties
				.getList();
		String defaultRole = wechatMiniProgramProperties.getDefaultRole();
		return new InMemoryWeChatMiniProgramService(wechatMiniProgramList, defaultRole);
	}

}
