package LikeLion.UnderTheCBackend.controller;


import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import LikeLion.UnderTheCBackend.dto.KakaoUserInfoResponse;
import LikeLion.UnderTheCBackend.utils.KakaoTokenJsonData;
import LikeLion.UnderTheCBackend.utils.KakaoUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "OAuth API", description = "카카오 OAuth API")
@RequestMapping("/login/oauth2")
public class OAuthController {
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoUserInfo kakaoUserInfo;

    public OAuthController(KakaoTokenJsonData kakaoTokenJsonData, KakaoUserInfo kakaoUserInfo) {
        this.kakaoTokenJsonData = kakaoTokenJsonData;
        this.kakaoUserInfo = kakaoUserInfo;
    }

    @PostMapping("/code/kakao")
    @Operation(summary = "카카오 OAuth API", description = "인가 코드를 이용해 토큰을 받는 API", responses = {
            @ApiResponse(responseCode = "200", description = "OAuth 성공")
    })
    private String Oauth(@RequestParam("code") String code) {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
        log.info("회원 정보 입니다.{}",userInfo);
        // 회원이 이미 user 테이블에 있는 회원이면 로그인 진행, 아니면 추가정보 입력 받은 후 회원가입 진행하도록 할 예정 (경우에 따라 리턴 값 수정할 것임)
        return "redirect:/api/vi/user/add";
    }
}
