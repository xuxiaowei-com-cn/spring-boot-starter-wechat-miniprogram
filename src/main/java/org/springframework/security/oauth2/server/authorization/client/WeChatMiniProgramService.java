package org.springframework.security.oauth2.server.authorization.client;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * 微信小程序 账户服务接口
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @see RegisteredClientRepository
 * @see InMemoryRegisteredClientRepository
 * @see JdbcRegisteredClientRepository
 */
public interface WeChatMiniProgramService {

	/**
	 * 认证信息
	 * @param clientPrincipal 经过身份验证的客户端主体
	 * @param additionalParameters 附加参数
	 * @param details 登录信息
	 * @param appid AppID(小程序ID)
	 * @param code @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 * @param openid 用户唯一标识，<a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 * @param credentials 证书
	 * @param unionid 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID
	 * 机制说明</a>。
	 * @param sessionKey 会话密钥
	 * @return 返回 认证信息
	 */
	AbstractAuthenticationToken authenticationToken(Authentication clientPrincipal,
			Map<String, Object> additionalParameters, Object details, String appid, String code, String openid,
			Object credentials, String unionid, String sessionKey);

	/**
	 * 根据 AppID(小程序ID)、code、jsCode2SessionUrl 获取Token
	 * @param appid AppID(小程序ID)
	 * @param code <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api/open-api/login/wx.login.html">开放接口
	 * - 登录 - wx.login</a>，<a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 * @param jsCode2SessionUrl <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 * @return 返回 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 */
	Code2SessionResponse getCode2SessionResponse(String appid, String code, String jsCode2SessionUrl);

}
