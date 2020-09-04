package com.xiaojukeji.kafka.manager.web.config;

import net.unicon.cas.client.configuration.CasClientConfigurerAdapter;
import net.unicon.cas.client.configuration.EnableCasClient;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangYanjing
 * @description
 * @date 2020/9/3 14:19
 */
@Configuration
@EnableCasClient
public class CasConfigure extends CasClientConfigurerAdapter {

    @Override
    public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
        super.configureAuthenticationFilter(authenticationFilter);
        authenticationFilter.getInitParameters().put("authenticationRedirectStrategyClass", "com.patterncat.CustomAuthRedirectStrategy");
    }
}
