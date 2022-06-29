package org.springframework.security.oauth2.server.authorization.authentication;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 微信小程序 OAuth2 身份验证令牌
 *
 * @author xuxiaowei
 * @since 0.0.1
 * @see OAuth2AuthorizationCodeAuthenticationToken 用于 OAuth 2.0 授权代码授予的
 * {@link Authentication} 实现。
 * @see OAuth2RefreshTokenAuthenticationToken 用于 OAuth 2.0 刷新令牌授予的 {@link Authentication}
 * 实现。
 * @see OAuth2ClientCredentialsAuthenticationToken 用于 OAuth 2.0
 * 客户端凭据授予的{@link Authentication} 实现。
 */
public class OAuth2WeChatMiniProgramAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	/**
	 * 授权类型：微信小程序
	 */
	public static final AuthorizationGrantType WECHAT_MINIPROGRAM = new AuthorizationGrantType("wechat_miniprogram");

	/**
	 * AppID(小程序ID)
	 */
	@Getter
	private final String appid;

	/**
	 * @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 */
	@Getter
	private final String code;

	/**
	 * @see OAuth2ParameterNames#SCOPE
	 */
	@Getter
	private final String scope;

	/**
	 * 子类构造函数。
	 * @param clientPrincipal 经过身份验证的客户端主体
	 * @param additionalParameters 附加参数
	 * @param appid AppID(小程序ID)
	 * @param code @see <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 * @param scope {@link OAuth2ParameterNames#SCOPE}
	 */
	public OAuth2WeChatMiniProgramAuthenticationToken(Authentication clientPrincipal,
			Map<String, Object> additionalParameters, String appid, String code, String scope) {
		super(OAuth2WeChatMiniProgramAuthenticationToken.WECHAT_MINIPROGRAM, clientPrincipal, additionalParameters);
		Assert.hasText(code, "appid 不能为空");
		Assert.hasText(code, "code 不能为空");
		this.appid = appid;
		this.code = code;
		this.scope = scope;
	}

}
