package LikeLion.UnderTheCBackend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v1/api/**"
    };

    @Bean
    protected SecurityFilterChain myConfig(HttpSecurity http) throws Exception {
        /* 허용 페이지 등록 */
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(WHITE_LIST).permitAll());
        /* 로그인 페이지 설정 */
        http
                .formLogin(form -> form
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login_proc")
                        .successHandler(
                                new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        System.out.println("authentication : " + authentication.getName());
                                        response.sendRedirect("/"); // 인증이 성공한 후에는 무조건 root로 이동시킴
                                    }
                                }
                        )
                        .failureHandler(
                                new AuthenticationFailureHandler() {
                                    @Override
                                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                        System.out.println("exception : " + exception.getMessage());
                                        response.sendRedirect("/login");
                                    }
                                }
                        )
                        .permitAll()

                );
        return http.build();
    }
}