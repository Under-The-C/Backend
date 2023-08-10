package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.KakaoBuyerInfoResponse;
import LikeLion.UnderTheCBackend.dto.KakaoSellerInfoResponse;
import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import LikeLion.UnderTheCBackend.service.BuyerService;
import LikeLion.UnderTheCBackend.service.SellerService;
import LikeLion.UnderTheCBackend.utils.KakaoBuyerInfo;
import LikeLion.UnderTheCBackend.utils.KakaoSellerInfo;
import LikeLion.UnderTheCBackend.utils.KakaoTokenJsonData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SignUp API", description = "회원가입 API")
@RequestMapping("/api/v1/signup")
public class SignUpController {
    @GetMapping("")
    @Operation(summary = "회원가입 API", description = "구매자와 판매자 정보를 이용하여 서비스에 회원가입하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    private String SignUpForBuyer(@RequestParam("code") String code, KakaoTokenResponse kakaoTokenResponse) {
//        buyer와 seller를 통합한 user 테이블을 만든 후 밑에 코드 작성할 예정
//        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(kakaoTokenResponse.getAccess_token());
//        log.info("회원 정보 입니다.{}",userInfo);
//
//        userService.createUser(userInfo.getKakao_account().getEmail());
        return "success";
    }
}
