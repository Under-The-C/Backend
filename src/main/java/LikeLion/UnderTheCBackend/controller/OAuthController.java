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

    private Boolean isUserEmailExist(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }

    @GetMapping("/code/kakao")
    @Operation(summary = "카카오 OAuth API", description = "인가 코드를 이용해 토큰을 받는 API", responses = {
            @ApiResponse(responseCode = "200", description = "OAuth 성공")
    })
    public ResponseEntity<?> Oauth(@RequestParam("code") String code, HttpServletRequest request)  {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code, redirectUrl); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        log.info("회원 정보 입니다.{}",userInfo);

        String email = userInfo.getKakao_account().getEmail();

        HttpHeaders headers = new HttpHeaders();

        if(isUserEmailExist(userInfo.getKakao_account().getEmail())) {
//            headers.setLocation(URI.create(redirectReactUrl + "?email=" + email));
            if (request.getSession(false) != null) {
                headers.setLocation(URI.create(redirectReactUrl));
                return new ResponseEntity<>("이미 로그인 되어 있습니다.", headers, HttpStatus.MOVED_PERMANENTLY);
            }

            Optional<User> result = this.userRepository.findByEmail(email);
            User user = result.orElse(null);

            if (user == null) {
                headers.setLocation(URI.create(redirectReactUrl));
                return new ResponseEntity<>("존재하지 않는 사용자입니다.", headers, HttpStatus.MOVED_PERMANENTLY);
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", user.getId());

            headers.setLocation(URI.create(redirectReactUrl + "?JSESSIONID=" + session.getId()));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        else {
            headers.setLocation(URI.create(redirectReactUrl + "signup-choose-role?email=" + email));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
    }
}
