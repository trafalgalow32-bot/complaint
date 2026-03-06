package com.complaint.config;

import com.complaint.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        HttpSessionRequestCache requestCache =
                new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
// 페이지 이동하는데 로그인해야 이용할 수 있는 경우 로그인 페이지로 이동한다.
// 로그인 성공하면 이전에 방문했던 페이지로 이동할 수 있는 기능을 제공한다.


        http.requestCache(rq -> rq.requestCache(requestCache))
                .authorizeHttpRequests(
                ar -> ar
                        .requestMatchers("/", "/image/**","/css/**","/js/**")
                        .permitAll() // 매처에 작성된 주소에 대해 모두 허용
                        .requestMatchers("/error", "/signUp").permitAll() // /error 주소 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자페이지는 권한이 ADMIN만 허용
                        .anyRequest().authenticated() // 나머지 모든 주소요청은 로그인해야한다.
        )
        .formLogin (
            form -> form
                    .loginPage("/signIn") // 커스텀 로그인 페이지 주소
                    .defaultSuccessUrl("/") // 로그인 성공하면 갈 곳
                    .usernameParameter("userId") // 로그인할 때 계정명 input태그의 name
                    .failureUrl("/signIn/error") // 로그인 실패시 어디로?
                    .permitAll() // 로그인과 관련된 주소들을 허용
        )
        .logout(
            out -> out
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                    .logoutSuccessUrl("/") // 로그아웃 성공 하면 어디로?
                    .invalidateHttpSession(true) // 로그아웃되면 모든 세션 제거
        );

// .logoutUrl("/logout") post 요청일 경우 실행됨. get은 안됨
    // 즉, a태그 통해서 요청은 get이라 안됨

        // http.formLogin( form -> form.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
