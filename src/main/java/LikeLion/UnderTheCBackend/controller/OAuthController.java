package LikeLion.UnderTheCBackend.controller;


import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import LikeLion.UnderTheCBackend.dto.KakaoUserInfoResponse;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import LikeLion.UnderTheCBackend.service.UserService;
import LikeLion.UnderTheCBackend.utils.KakaoTokenJsonData;
import LikeLion.UnderTheCBackend.utils.KakaoUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@Slf4j
@Tag(name = "OAuth API", description = "카카오 OAuth API")
@RequestMapping("/login/oauth2")
public class OAuthController {
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoUserInfo kakaoUserInfo;
    private final UserService userService;
    private final UserRepository userRepository;
    private final String redirectReactUrl;
    private final String redirectUrl;

    public OAuthController(KakaoTokenJsonData kakaoTokenJsonData, KakaoUserInfo kakaoUserInfo, UserService userService, UserRepository userRepository, @Value("${redirect_react_URL}") String redirectReactUrl, @Value("${redirect_URL}") String redirectUrl) {
        this.kakaoTokenJsonData = kakaoTokenJsonData;
        this.kakaoUserInfo = kakaoUserInfo;
        this.userService = userService;
        this.userRepository = userRepository;
        this.redirectReactUrl = redirectReactUrl;
        this.redirectUrl = redirectUrl;
    }

    @GetMapping("/code/kakao")
    @Operation(summary = "카카오 OAuth API", description = "인가 코드를 이용해 토큰을 받는 API", responses = {
            @ApiResponse(responseCode = "200", description = "OAuth 성공")
    })
    public ResponseEntity<?> Oauth(@RequestParam("code") String code)  {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code, redirectUrl); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        log.info("회원 정보 입니다.{}",userInfo);

        String email = userInfo.getKakao_account().getEmail();
        log.info("회원 이메일 입니다.{}",email);

        HttpHeaders headers = new HttpHeaders();

        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            log.info("이미 가입된 회원입니다.");
            headers.setLocation(URI.create(redirectReactUrl + "login-success?access_token=" + kakaoTokenResponse.getAccess_token()));
        }
        else {
            log.info("가입되지 않은 회원입니다.");
            headers.setLocation(URI.create(redirectReactUrl + "signup-choose-role?access_token=" + kakaoTokenResponse.getAccess_token()));
        }

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/kakao-unlink")
    @Operation(summary = "카카오 계정 탈퇴", description = "인가 코드를 이용해 토큰을 받아 탈퇴하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "OAuth 성공")
    })
    public ResponseEntity<?> kakaoUnlink(@RequestParam("code") String code) {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code, redirectUrl); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        String token = kakaoTokenResponse.getAccess_token();

        // 카카오 연동 해제
        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoUserInfo.postUnlink(token);
        String email = kakaoUserInfoResponse.getKakao_account().getEmail();

        User user = userRepository.findByEmail(email).orElse(null);
        HttpHeaders headers = new HttpHeaders();
        if (user != null) {
            /* User 정보 삭제 */
            userService.deleteUser(email);
            headers.setLocation(URI.create(redirectReactUrl + "delete-success"));
        }
        headers.setLocation(URI.create(redirectReactUrl));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
