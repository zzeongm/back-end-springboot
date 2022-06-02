package com.mycompany.backend.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.mycompany.backend.security.JwtAuthenticationFilter;

import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    protected void configure(HttpSecurity http) throws Exception{
      log.info("실행");
      //서버 세션 비활성
      http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      //폼 로그인 비활성화
      http.formLogin().disable();
      //사이트간 요청 위조 방지 비활성화
      http.csrf().disable();
      //요청 경로 권한 설정
//      http.authorizeRequests().antMatchers("/board/**")
//          .authenticated().antMatchers("/**").permitAll();
      //CORS 설정(다른 도메인의 JavaScript로 접근을 할 수 있도록 허용)
      http.cors();//@Bean corsConfigurationSource
      //JWT 인증 필터 추가 
      http.addFilterBefore(jwtAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
    }
    
    
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
      JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
      jwtAuthenticationFilter.setRedisTemplate(redisTemplate);
      return jwtAuthenticationFilter;
    }
    
    @Override       
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
      log.info("실행");
      //MPA 폼인증방식에서 사용(JWT 인증방식에서는 사용하지않음) 
     //DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    //db에서 무엇을 가져올것인가 
      //provider.setUserDetailsService(new CustomUserDetailsService());
     //password 인코딩을 어떻게 할 것인가 
     // provider.setPasswordEncoder(passwordEncoder());
     // auth.authenticationProvider(provider);
    }
    @Override
    public void configure(WebSecurity web) throws Exception{
      log.info("실행");
      DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
      defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchyImpl());   
      web.expressionHandler(defaultWebSecurityExpressionHandler);
     //MPA에서 시큐리티를 적용하지않는 경로 설정
//      web.ignoring()
//      .antMatchers("/images/**")
//      .antMatchers("/css/**")
//      .antMatchers("/js/**")
//      .antMatchers("/bootstrap/**")
//      .antMatchers("/jquery/**")
//      .antMatchers("/favicon.ico");
    }
    @Bean    //회원가입할때 사용해야하기때문에 관리 객체로 등록
    public PasswordEncoder passwordEncoder() {
      //암호화 알고리즘 변경x 
   // return new BCryptPasswordEncoder();
      
      //암호화 알고리즘을 변경 {bcypt} {noop} .......
      return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public RoleHierarchyImpl roleHierarchyImpl() {
       log.info("실행");
       RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
       roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
       return roleHierarchyImpl;
    }
    @Bean   //MPA에서는 cors 설정 필요없음 REST API에서만 사용 
    public CorsConfigurationSource corsConfigurationSource() {
      log.info("실행");
        CorsConfiguration configuration = new CorsConfiguration();
        //모든 요청 사이트 허용
        configuration.addAllowedOrigin("*");
        //모든 요청 방식 허용
        configuration.addAllowedMethod("*");
        //모든 요청 헤더 허용
        configuration.addAllowedHeader("*");
        //모든 URL 요청에 대해서 위 내용을 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
