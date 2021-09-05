package GongMoa.gongmoa.config;

import GongMoa.gongmoa.OAuth2.CustomOAuth2UserService;
import GongMoa.gongmoa.OAuth2.LoginFailureHandler;
import GongMoa.gongmoa.OAuth2.Role;
import GongMoa.gongmoa.OAuth2.SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/",
                            "/contests", "/contests/*", "/contests/*/notifications",
                            "/signup", "/login*",
                            "/docs/**", "/jsoup").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable()
                    .headers().frameOptions().disable()
                .and()
                    .logout()
//                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .successHandler(new SuccessHandler("/"))
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

        http.formLogin()
                .loginPage("/login_form")
                .loginProcessingUrl("/doLogin")
                .successHandler(new SuccessHandler("/"))
                .failureHandler(new LoginFailureHandler())
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/h2-console/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
