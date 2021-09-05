package GongMoa.gongmoa.config;

import GongMoa.gongmoa.OAuth2.LoginUserArgumentResolver;
import GongMoa.gongmoa.formatter.ParticipationsFormatter;
import GongMoa.gongmoa.interceptor.SessionUserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionUserInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/jsoup");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new ParticipationsFormatter());
    }
}