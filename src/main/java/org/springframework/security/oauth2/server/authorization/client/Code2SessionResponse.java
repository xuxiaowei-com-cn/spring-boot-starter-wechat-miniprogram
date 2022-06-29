package org.springframework.security.oauth2.server.authorization.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录凭证校验 返回值
 *
 * @author xuxiaowei
 * @see 0.0.1
 * @see <a href=
 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录凭证校验</a>
 */
@Data
public class Code2SessionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户唯一标识，<a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html">登录
	 * - code2Session</a>
	 */
	private String openid;

	/**
	 * 会话密钥
	 */
	@JsonProperty("session_key")
	private String sessionKey;

	/**
	 * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a href=
	 * "https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID
	 * 机制说明</a>。
	 */
	private String unionid;

	/**
	 * 错误码
	 */
	private String errcode;

	/**
	 * 错误信息
	 */
	private String errmsg;

}
