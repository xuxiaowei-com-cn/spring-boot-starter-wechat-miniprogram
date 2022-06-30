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
import org.springframework.security.oauth2.core.endpoint.OAuth2WeChatMiniProgramParameterNames;
import org.springframework.security.oauth2.server.authorization.properties.WeChatMiniProgramProperties;
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
public class InMemoryWeChatAppletService implements WeChatMiniProgramService {

	private final List<WeChatMiniProgramProperties.WeChatMiniProgram> wechatMiniProgramList;

	/**
	 * 默认微信小程序的权限
	 * <p>
	 * 若要自定义用户的权限，请开发者自己实现 {@link WeChatMiniProgramService}
	 */
	private final String defaultRole;

	public InMemoryWeChatAppletService(List<WeChatMiniProgramProperties.WeChatMiniProgram> wechatMiniProgramList,
			String defaultRole) {
		this.wechatMiniProgramList = wechatMiniProgramList;
		this.defaultRole = defaultRole;
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
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(defaultRole);
		authorities.add(authority);
		User user = new User(openid, sessionKey, authorities);

		UsernamePasswordAuthenticationToken principal = UsernamePasswordAuthenticationToken.authenticated(user, null,
				user.getAuthorities());

		WeChatMiniProgramAuthenticationToken authenticationToken = new WeChatMiniProgramAuthenticationToken(authorities,
				clientPrincipal, principal, user, additionalParameters, details, appid, code, openid);

		authenticationToken.setCredentials(credentials);
		authenticationToken.setUnionid(unionid);
		authenticationToken.setSessionKey(sessionKey);

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
			throw new RuntimeException(e);
		}

		String openid = code2SessionResponse.getOpenid();
		if (openid == null) {
			throw new RuntimeException(String.format("错误代码：%s，错误消息：%s", code2SessionResponse.getErrcode(),
					code2SessionResponse.getErrmsg()));
		}

		return code2SessionResponse;
	}

	/**
	 * 根据 AppID(小程序ID) 查询 AppSecret(小程序密钥)
	 * @param appid AppID(小程序ID)
	 * @return 返回 AppSecret(小程序密钥)
	 */
	public String getSecretByAppid(String appid) {
		Assert.notNull(appid, "appid 不能为 null");
		for (WeChatMiniProgramProperties.WeChatMiniProgram wechatMiniProgram : wechatMiniProgramList) {
			if (appid.equals(wechatMiniProgram.getAppid())) {
				return wechatMiniProgram.getSecret();
			}
		}
		throw new IllegalArgumentException("未找到 secret");
	}

}
