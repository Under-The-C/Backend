package LikeLion.UnderTheCBackend.config;

import LikeLion.UnderTheCBackend.Repository.RefreshTokenRepository;
import LikeLion.UnderTheCBackend.Service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class OAuth2SecurityConfigForBuyer {

    private final OAuth2BuyerCustomService oAuth2BuyerCustomService;
    private final TokenProviderForBuyer tokenProviderForBuyer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BuyerService buyerService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final OAuth2SuccessHandlerForBuyer oAuth2SuccessHandlerForBuyer;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable());

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(tokenAuthenticationFilterForBuyer(), UsernamePasswordAuthenticationFilter.class);


        http.authorizeRequests()
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/login")
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .baseUri("/oauth2/authorize") // OAuth2 인증 엔드포인트 설정
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository)
                )
                .successHandler(oAuth2SuccessHandlerForBuyer)
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(oAuth2BuyerCustomService)
                )
        );

        http.logout(logout -> logout.logoutSuccessUrl("/login"));


        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")
                )
        );


        return http.build();
    }


    @Bean
    public OAuth2SuccessHandlerForBuyer oAuth2SuccessHandler() {
        return new OAuth2SuccessHandlerForBuyer(tokenProviderForBuyer,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                buyerService
        );
    }

    @Bean
    public TokenAuthenticationFilterForBuyer tokenAuthenticationFilterForBuyer() {
        return new TokenAuthenticationFilterForBuyer(tokenProviderForBuyer);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
