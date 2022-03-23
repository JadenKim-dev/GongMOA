package GongMoa.gongmoa.config;

import GongMoa.gongmoa.OAuth2.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/",
                            "/contests", "/contests/*", "/contests/*/notifications",
                            "/signup", "/login*", "/check-email-token", "/resend-confirm-email",
                            "/docs/**", "/crawling").permitAll()
                    .antMatchers("/profile/**").hasAnyAuthority(Role.USER.getKey(), Role.NOT_VALID.getKey())
                    .anyRequest().hasAuthority(Role.USER.getKey())
                .and()
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .logout()
//                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .formLogin()
                        .loginPage("/login-form")
                        .loginProcessingUrl("/doLogin")
                        .successHandler(new SuccessHandler("/"))
                        .failureHandler(new LoginFailureHandler())
                        .permitAll()
                .and()
                    .oauth2Login()
                        .successHandler(new SuccessHandler("/"))
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
        http.exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/h2-console/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
