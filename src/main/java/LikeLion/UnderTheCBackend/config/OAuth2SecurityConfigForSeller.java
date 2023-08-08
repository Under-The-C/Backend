package LikeLion.UnderTheCBackend.config;

import LikeLion.UnderTheCBackend.Repository.RefreshTokenRepository;
import LikeLion.UnderTheCBackend.Service.SellerService;
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
public class OAuth2SecurityConfigForSeller {

    private final OAuth2SellerCustomService oAuth2SellerCustomService;
    private final TokenProviderForSeller tokenProviderForSeller;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SellerService sellerService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final OAuth2SuccessHandlerForSeller oAuth2SuccessHandlerForSeller;

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

        http.addFilterBefore(tokenAuthenticationFilterForSeller(), UsernamePasswordAuthenticationFilter.class);


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
                .successHandler(oAuth2SuccessHandlerForSeller)
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                        .userService(oAuth2SellerCustomService)
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
    public OAuth2SuccessHandlerForSeller oAuth2SuccessHandler() {
        return new OAuth2SuccessHandlerForSeller(tokenProviderForSeller,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                sellerService
        );
    }

    @Bean
    public TokenAuthenticationFilterForSeller tokenAuthenticationFilterForSeller() {
        return new TokenAuthenticationFilterForSeller(tokenProviderForSeller);
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
