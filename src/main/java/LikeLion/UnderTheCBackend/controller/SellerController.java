package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.KakaoSellerInfoResponse;
import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import LikeLion.UnderTheCBackend.dto.LoginResponseForSeller;
import LikeLion.UnderTheCBackend.entity.Seller;
import LikeLion.UnderTheCBackend.service.SellerService;
import LikeLion.UnderTheCBackend.utils.KakaoSellerInfo;
import LikeLion.UnderTheCBackend.utils.KakaoTokenJsonData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Seller API", description = "판매자 API")
@RequestMapping("/api/v1/seller")
public class SellerController {
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoSellerInfo kakaoSellerInfo;
    private final SellerService sellerService;

    @GetMapping("/oauth")
    @Operation(summary = "카카오 회원가입 API", description = "인가 코드를 이용해 토큰을 받고, 해당 토큰으로 판매자 정보를 조회 + 판매자 정보를 이용하여 서비스에 회원가입하는 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공")
    })
    public String kakaoOauth(@RequestParam("code") String code) {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        KakaoSellerInfoResponse sellerInfo = kakaoSellerInfo.getSellerInfo(kakaoTokenResponse.getAccess_token()); // 토큰을 사용하여 Kakao 서버에서 판매자 정보 조회
        log.info("판매자 정보 입니다.{}",sellerInfo);

        sellerService.createSeller(sellerInfo.getKakao_account().getEmail()); // 조회한 구매자 정보를 이용하여 서비스에 회원가입 처리

        return "success";
    }

    @PostMapping("/login")
    @Operation(summary = "판매자 로그인", description = "판매자 로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    private LoginResponseForSeller login(HttpServletRequest request, HttpServletResponse response, Seller seller, String access_token) {
        /* 로그인 상태 확인 */
        if (request.getSession(false) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 로그인 되어 있습니다.");
        }

        HttpSession session = request.getSession();
        session.setAttribute("login", seller);
        session.setAttribute("access_token", access_token);

        return new LoginResponseForSeller("success", "로그인 성공", seller, access_token);
    }
}
