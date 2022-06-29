package org.springframework.security.oauth2.core.endpoint;

/**
 * 微信小程序 参数名称
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @see OAuth2ParameterNames 在 OAuth 参数注册表中定义并由授权端点、令牌端点和令牌撤销端点使用的标准和自定义（非标准）参数名称。
 */
public interface OAuth2WeChatMiniProgramParameterNames {

	/**
	 * AppID(小程序ID)
	 */
	String APPID = "appid";

	/**
	 * AppSecret(小程序密钥)
	 */
	String SECRET = "secret";

	/**
	 * @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/wx.login.html">开放接口
	 * - 登录 - wx.login</a>
	 * @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 */
	String JS_CODE = "js_code";

	/**
	 * 用户唯一标识
	 *
	 * @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 */
	String OPENID = "openid";

	/**
	 * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID
	 * 机制说明</a>。
	 */
	String UNIONID = "unionid";

	/**
	 * 会话密钥
	 *
	 * @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 */
	String SESSION_KEY = "sessionKey";

}
