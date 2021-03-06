package sunnn.filehub.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import sunnn.filehub.util.FileHubProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public SunRealm shiroRealm() {
        return new SunRealm();
    }

    @Bean
    @DependsOn("fileHubProperties")
    public SimpleCookie rememberMeCookie(){
        SimpleCookie cookie = new SimpleCookie("verify");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(FileHubProperties.loginTimeout);
        return cookie;
    }

    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie cookie){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(cookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("QVN1bm5uJ3MgU3Vuc2l0ZQ=="));
        return cookieRememberMeManager;
    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(SunRealm realm, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 顺序拦截
        filterChainDefinitionMap.put("/verify", "anon");
        filterChainDefinitionMap.put("/try", "anon");

        filterChainDefinitionMap.put("/share", "user");
        filterChainDefinitionMap.put("/s/**", "anon");
        filterChainDefinitionMap.put("/share/**", "anon");

        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");

        filterChainDefinitionMap.put("/**", "user");

        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }
}

class SunRealm extends AuthorizingRealm {

    @Override
    public String getName() {
        return "SunRealm";
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String passCode = FileHubProperties.verifyCode;
        return new SimpleAuthenticationInfo(
                "", // 个人系统验证不需要用户名
                passCode,
                getName());
    }
}
