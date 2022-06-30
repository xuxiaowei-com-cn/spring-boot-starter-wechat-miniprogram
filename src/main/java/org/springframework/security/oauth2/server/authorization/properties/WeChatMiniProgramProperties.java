package org.springframework.security.oauth2.server.authorization.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.server.authorization.client.WeChatMiniProgramService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 微信小程序属性配置类
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
@Component
@ConfigurationProperties("wechat.mini.program")
public class WeChatMiniProgramProperties {

	/**
	 * 微信小程序属性配置列表
	 */
	private List<WeChatMiniProgram> list;

	/**
	 * 默认微信小程序的权限
	 * <p>
	 * 若要自定义用户的权限，请开发者自己实现 {@link WeChatMiniProgramService}
	 */
	private String defaultRole;

	/**
	 * 微信小程序属性配置类
	 *
	 * @author xuxiaowei
	 * @since 0.0.1
	 */
	@Data
	public static class WeChatMiniProgram {

		/**
		 * AppID(小程序ID)
		 */
		private String appid;

		/**
		 * AppSecret(小程序密钥)
		 */
		private String secret;

	}

}
