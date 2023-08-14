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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public OAuthController(KakaoTokenJsonData kakaoTokenJsonData, KakaoUserInfo kakaoUserInfo, UserService userService, UserRepository userRepository) {
        this.kakaoTokenJsonData = kakaoTokenJsonData;
        this.kakaoUserInfo = kakaoUserInfo;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    private Boolean isUserEmailExist(String email) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(email));
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
    public String Oauth(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        log.info("회원 정보 입니다.{}",userInfo);

        String email = userInfo.getKakao_account().getEmail();
        String profile = userInfo.getKakao_account().getProfile().getProfile_image_url();

        redirectAttributes.addAttribute("email", email);

        if(isUserEmailExist(userInfo.getKakao_account().getEmail())) {
            return "redirect:/api/v1/login";
        }
        else {
            redirectAttributes.addAttribute("profile", profile);
            return "redirect:/api/v1/user/add";
        }
    }
}
