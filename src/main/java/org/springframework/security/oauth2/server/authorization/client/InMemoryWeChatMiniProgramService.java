package org.springframework.security.oauth2.server.authorization.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.WeChatMiniProgramAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2WeChatMiniProgramParameterNames;
import org.springframework.security.oauth2.server.authorization.exception.AppidWeChatMiniProgramException;
import org.springframework.security.oauth2.server.authorization.properties.WeChatMiniProgramProperties;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2WeChatMiniProgramEndpointUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序 账户服务接口 基于内存的实现
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class InMemoryWeChatMiniProgramService implements WeChatMiniProgramService {

	private final WeChatMiniProgramProperties weChatMiniProgramProperties;

	public InMemoryWeChatMiniProgramService(WeChatMiniProgramProperties weChatMiniProgramProperties) {
		this.weChatMiniProgramProperties = weChatMiniProgramProperties;
	}

	/**
	 * 认证信息
	 * @param appid AppID(小程序ID)
	 * @param openid 用户唯一标识，<a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 * @param unionid 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID
	 * 机制说明</a>。
	 * @param sessionKey 会话密钥
	 * @param details 登录信息
	 * @return 返回 认证信息
	 */
	@Override
	public AbstractAuthenticationToken authenticationToken(Authentication clientPrincipal,
			Map<String, Object> additionalParameters, Object details, String appid, String code, String openid,
			Object credentials, String unionid, String sessionKey) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(weChatMiniProgramProperties.getDefaultRole());
		authorities.add(authority);
		User user = new User(openid, sessionKey, authorities);

		UsernamePasswordAuthenticationToken principal = UsernamePasswordAuthenticationToken.authenticated(user, null,
				user.getAuthorities());

		WeChatMiniProgramAuthenticationToken authenticationToken = new WeChatMiniProgramAuthenticationToken(authorities,
				clientPrincipal, principal, user, additionalParameters, details, appid, code, openid);

		authenticationToken.setCredentials(credentials);
		authenticationToken.setUnionid(unionid);

		return authenticationToken;
	}

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
	@Override
	public Code2SessionResponse getCode2SessionResponse(String appid, String code, String jsCode2SessionUrl) {
		Map<String, String> uriVariables = new HashMap<>(8);
		uriVariables.put(OAuth2WeChatMiniProgramParameterNames.APPID, appid);

		String secret = getSecretByAppid(appid);

		uriVariables.put(OAuth2WeChatMiniProgramParameterNames.SECRET, secret);
		uriVariables.put(OAuth2WeChatMiniProgramParameterNames.JS_CODE, code);

		RestTemplate restTemplate = new RestTemplate();

		String forObject = restTemplate.getForObject(jsCode2SessionUrl, String.class, uriVariables);

		Code2SessionResponse code2SessionResponse;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			code2SessionResponse = objectMapper.readValue(forObject, Code2SessionResponse.class);
		}
		catch (JsonProcessingException e) {
			OAuth2Error error = new OAuth2Error(OAuth2WeChatMiniProgramEndpointUtils.ERROR_CODE,
					"使用微信小程序授权code：" + code + " 获取Token异常", OAuth2WeChatMiniProgramEndpointUtils.AUTH_CODE2SESSION_URI);
			throw new OAuth2AuthenticationException(error, e);
		}

		String openid = code2SessionResponse.getOpenid();
		if (openid == null) {
			OAuth2Error error = new OAuth2Error(code2SessionResponse.getErrcode(), code2SessionResponse.getErrmsg(),
					OAuth2WeChatMiniProgramEndpointUtils.AUTH_CODE2SESSION_URI);
			throw new OAuth2AuthenticationException(error);
		}

		return code2SessionResponse;
	}

	/**
	 * 根据 appid 获取 微信小程序属性配置
	 * @param appid 小程序ID
	 * @return 返回 微信小程序属性配置
	 */
	@Override
	public WeChatMiniProgramProperties.WeChatMiniProgram getWeChatMiniProgramByAppid(String appid) {
		List<WeChatMiniProgramProperties.WeChatMiniProgram> list = weChatMiniProgramProperties.getList();
		if (list == null) {
			throw new AppidWeChatMiniProgramException("appid 未配置");
		}

		for (WeChatMiniProgramProperties.WeChatMiniProgram weChatMiniProgram : list) {
			if (appid.equals(weChatMiniProgram.getAppid())) {
				return weChatMiniProgram;
			}
		}

		throw new AppidWeChatMiniProgramException("未匹配到 appid");
	}

	/**
	 * 根据 AppID(小程序ID) 查询 AppSecret(小程序密钥)
	 * @param appid AppID(小程序ID)
	 * @return 返回 AppSecret(小程序密钥)
	 */
	public String getSecretByAppid(String appid) {
		Assert.notNull(appid, "appid 不能为 null");
		WeChatMiniProgramProperties.WeChatMiniProgram weChatMiniProgram = getWeChatMiniProgramByAppid(appid);
		return weChatMiniProgram.getSecret();
	}

}
