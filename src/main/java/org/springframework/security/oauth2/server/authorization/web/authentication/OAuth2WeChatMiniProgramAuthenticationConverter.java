package org.springframework.security.oauth2.server.authorization.web.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.OAuth2WeChatMiniProgramParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2WeChatMiniProgramAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信 OAuth2 用于验证授权授予的 {@link OAuth2WeChatMiniProgramAuthenticationToken}。
 *
 * @author xuxiaowei
 * @since Joe Grandja
 * @since 0.0.1
 * @see OAuth2AuthorizationCodeAuthenticationConverter 尝试从 {@link HttpServletRequest} 提取
 * OAuth 2.0 授权代码授权的访问令牌请求，然后将其转换为用于验证授权授权的
 * {@link OAuth2AuthorizationCodeAuthenticationToken} 。
 * @see OAuth2RefreshTokenAuthenticationConverter 用于 OAuth 2.0 授权代码授予的Authentication实现。
 * @see OAuth2ClientCredentialsAuthenticationConverter 尝试从 {@link HttpServletRequest} 提取
 * OAuth 2.0 客户端凭据授予的访问令牌请求，然后将其转换为用于验证授权授予的
 * {@link OAuth2ClientCredentialsAuthenticationToken} 。
 */
public class OAuth2WeChatMiniProgramAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {

		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!OAuth2WeChatMiniProgramAuthenticationToken.WECHAT_MINIPROGRAM.getValue().equals(grantType)) {
			return null;
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// code (REQUIRED)
		String code = parameters.getFirst(OAuth2ParameterNames.CODE);
		if (!StringUtils.hasText(code) || parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CODE,
					OAuth2WeChatMiniProgramEndpointUtils.AUTH_CODE2SESSION_URI);
		}

		// appid (REQUIRED)
		String appid = parameters.getFirst(OAuth2WeChatMiniProgramParameterNames.APPID);
		if (!StringUtils.hasText(appid)) {
			// 若参数中缺省 appid，使用 Referer
			Enumeration<String> headers = request.getHeaders(HttpHeaders.REFERER);
			while (headers.hasMoreElements()) {
				String nextElement = headers.nextElement();
				if (StringUtils.hasText(nextElement)) {
					String[] split = nextElement.split("/");
					if (split.length > 3) {
						appid = split[3];
						parameters.add(OAuth2WeChatMiniProgramParameterNames.APPID, appid);
					}
				}
			}
		}
		if (!StringUtils.hasText(appid) || parameters.get(OAuth2WeChatMiniProgramParameterNames.APPID).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST,
					OAuth2WeChatMiniProgramParameterNames.APPID,
					OAuth2WeChatMiniProgramEndpointUtils.AUTH_CODE2SESSION_URI);
		}

		// scope
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);

		Map<String, Object> additionalParameters = new HashMap<>(4);
		parameters.forEach((key, value) -> {
			if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) && !key.equals(OAuth2ParameterNames.CLIENT_ID)
					&& !key.equals(OAuth2ParameterNames.CODE) && !key.equals(OAuth2ParameterNames.REDIRECT_URI)
					&& !key.equals(OAuth2ParameterNames.CLIENT_SECRET)
					&& !key.equals(OAuth2WeChatMiniProgramParameterNames.APPID)
					&& !key.equals(OAuth2ParameterNames.SCOPE)) {
				additionalParameters.put(key, value.get(0));
			}
		});

		return new OAuth2WeChatMiniProgramAuthenticationToken(clientPrincipal, additionalParameters, appid, code,
				scope);
	}

}
