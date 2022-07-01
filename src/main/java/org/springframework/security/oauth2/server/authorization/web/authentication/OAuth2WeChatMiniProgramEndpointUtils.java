package org.springframework.security.oauth2.server.authorization.web.authentication;

/**
 * 微信小程序 OAuth 2.0 协议端点的实用方法
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class OAuth2WeChatMiniProgramEndpointUtils {

	/**
	 * 登录凭证校验。通过 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/wx.login.html">wx.login</a>
	 * 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程。更多使用方法详见 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html">小程序登录</a>
	 * 。
	 */
	public static final String AUTH_CODE2SESSION_URI = "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html";

	/**
	 * 错误代码
	 */
	public static final String ERROR_CODE = "C10000";

}
