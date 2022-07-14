# 更新日志

---

## RELEASE JDK 17 Build 0.0.1-alpha.2 2022-07-14

### ⭐ New Features | 新功能

- 当授权参数缺省 `appid` 时，可以从请求头中的 `Referer` 中自动截取

### 📔 Documentation | 文档

- 缺省 `appid`

```
/oauth2/token?grant_type=wechat_miniprogram&client_id={CLIENT_ID}&client_secret={CLIENT_SECRET}&code={CODE}
```

---

## RELEASE JDK 17 Build 0.0.1-alpha.1 2022-07-14

### ⭐ New Features | 新功能

- 首次发布项目，支持微信小程序获取 OAuth 2.1 授权信息

### 📔 Documentation | 文档

1. 依赖引入

```xml
<dependencies>
   <!-- OAuth 2.1 依赖 -->
   <!-- https://mvnrepository.com/artifact/io.xuxiaowei.security.next/spring-security-oauth2-authorization-server -->
   <dependency>
      <groupId>io.xuxiaowei.security.next</groupId>
      <artifactId>spring-security-oauth2-authorization-server</artifactId>
      <version>0.3.1</version>
   </dependency>

   <!-- https://mvnrepository.com/artifact/cn.com.xuxiaowei.boot.next/spring-boot-starter-wechat-miniprogram -->
   <dependency>
      <groupId>cn.com.xuxiaowei.boot.next</groupId>
      <artifactId>spring-boot-starter-wechat-miniprogram</artifactId>
      <version>0.0.1-alpha.1</version>
   </dependency>
</dependencies>
```

2. 项目配置

```java
package cloud.xuxiaowei.passport.configuration;

import cloud.xuxiaowei.passport.handler.AccessTokenAuthenticationFailureHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2WeChatMiniProgramAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

/**
 * Spring Security 配置
 * <p>
 * 详细使用说明参见：
 * <p>
 * <a href="https://gitee.com/xuxiaowei-cloud/xuxiaowei-cloud/blob/main/passport/src/main/java/cloud/xuxiaowei/passport/configuration/AuthorizationServerConfiguration.java">Gitee</a>
 * <p>
 * <a href="https://github.com/xuxiaowei-cloud/xuxiaowei-cloud/blob/main/passport/src/main/java/cloud/xuxiaowei/passport/configuration/AuthorizationServerConfiguration.java">Github</a>
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
public class WebSecurityConfigurerAdapterConfiguration {

    @Bean
    @Order(-1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // 此段代码来自：OAuth2AuthorizationServerConfiguration#applyDefaultSecurity(HttpSecurity)
        // @formatter:off
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer<>();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer
                .getEndpointsMatcher();

        http
                .requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer);
        // @formatter:on

        // 自定义客户授权
        authorizationServerConfigurer.tokenEndpoint(tokenEndpointCustomizer -> tokenEndpointCustomizer
                .accessTokenRequestConverter(new DelegatingAuthenticationConverter(Arrays.asList(
                        // 新增：微信 OAuth2 用于验证授权授予的 {@link
                        // OAuth2WeChatMiniProgramAuthenticationToken}
                        new OAuth2WeChatMiniProgramAuthenticationConverter(),
                        // 默认值：OAuth2 授权码认证转换器
                        new OAuth2AuthorizationCodeAuthenticationConverter(),
                        // 默认值：OAuth2 刷新令牌认证转换器
                        new OAuth2RefreshTokenAuthenticationConverter(),
                        // 默认值：OAuth2 客户端凭据身份验证转换器
                        new OAuth2ClientCredentialsAuthenticationConverter())))
                // 用于处理失败的身份验证尝试的策略。
                .errorResponseHandler(new AccessTokenAuthenticationFailureHandlerImpl()));

        // 微信小程序 OAuth2 身份验证提供程序
        new OAuth2WeChatMiniProgramAuthenticationProvider(http);

        return http.build();
    }

}
```

```yaml
# 微信小程序配置
wechat:
  mini:
    program:
      # 默认微信小程序的权限
      default-role: wechat_miniprogram
      # 小程序账户列表
      list:
        - appid: ${wx_miniapp_appid:}
          secret: ${wx_miniapp_secret:}
```

3. 微信小程序可使用下列URL获取授权Token
    - grant_type
        - 必须使用 `wechat_miniprogram`
    - client_id
        - OAuth 2 客户ID
    - client_secret
        - OAuth 2 客户秘钥
    - appid
        - 小程序appid
    - code
        - 微信登录授权码

```
/oauth2/token?grant_type=wechat_miniprogram&client_id={CLIENT_ID}&client_secret={CLIENT_SECRET}&appid={APPID}&code={CODE}
```
