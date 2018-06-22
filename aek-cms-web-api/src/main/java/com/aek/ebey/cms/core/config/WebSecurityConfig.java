package com.aek.ebey.cms.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aek.common.core.serurity.JwtAuthenticationEntryPoint;
import com.aek.common.core.serurity.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = false)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private JwtAuthenticationTokenFilter tokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
      return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf()
                .disable()
                
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                
                // don't create session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/sys/excel/export/templt/tenant")
                .permitAll()
                .antMatchers("/sys/excel/export/error/tenant/*")
                .permitAll()
                .antMatchers("/sys/area/**")
                .permitAll()
                .antMatchers("/sys/tenant/all/manageTenant")
                .permitAll()
                .antMatchers("/sys/tenant/createTrialSubTenant")
                .permitAll()
                .antMatchers("/sys/index/sendRstCode")
                .permitAll()
                .antMatchers("/sys/index/sendCode")
                .permitAll()
                .antMatchers("/sys/user/register")
                .permitAll()
                .antMatchers("/sys/user/websiteLogin")
                .permitAll()
                .antMatchers("/sys/user/checkLoginName")
                .permitAll()
                .antMatchers("/sys/user/checkMobile")
                .permitAll()
                .antMatchers("/sys/user/checkTenantName")
                .permitAll()
                .antMatchers("/sys/user/isLoginNameExist")
                .permitAll()
                .antMatchers("/sys/index/resetPassword")
                .permitAll()
                .antMatchers("/sys/user/*/modify/email")  // 邮箱验证
                .permitAll()
                .antMatchers("/sys/tenant/tenantNameCheck")  
                .permitAll()
                .antMatchers("/sys/tenant/mobileCheck")
                .permitAll()
                .antMatchers("/webjars/**")
                .permitAll()
                .antMatchers("/swagger-resources/**")
                .permitAll()
                .antMatchers("/v2/api-docs")
                .permitAll()
                .antMatchers("/images/*.jpg")
                .permitAll()
                .antMatchers("/images/*.png")
                .permitAll()
                .antMatchers("/**/*.js")
                .permitAll()
                .antMatchers("/**/*.css")
                .permitAll()
                .antMatchers("/**/*.woff")
                .permitAll()
                .antMatchers("/**/*.woff2")
                .permitAll()
                .antMatchers("/**/*.jsp")
                .permitAll()
                .antMatchers("/**/*.html")
                .permitAll()
                .antMatchers("/favicon.ico")
                .permitAll()
                .antMatchers("/auth/**")
                .permitAll()
        		.anyRequest()
                .authenticated();
        
        // Custom JWT based security filter
        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}