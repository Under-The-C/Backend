package LikeLion.UnderTheCBackend.controller;


import LikeLion.UnderTheCBackend.dto.KakaoBuyerInfoResponse;
import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import LikeLion.UnderTheCBackend.repository.BuyerRepository;
import LikeLion.UnderTheCBackend.repository.SellerRepository;
import LikeLion.UnderTheCBackend.utils.KakaoTokenJsonData;
import com.fasterxml.jackson.core.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.springframework.jdbc.datasource.DataSourceUtils.getConnection;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OAuth API", description = "카카오 OAuth API")
@RequestMapping("/login/oauth2")
public class OAuthController {
    private final KakaoTokenJsonData kakaoTokenJsonData;

    @PostMapping("/code/kakao")
    @Operation(summary = "카카오 OAuth API", description = "인가 코드를 이용해 토큰을 받는 API", responses = {
            @ApiResponse(responseCode = "200", description = "OAuth 성공")
    })
    private String Oauth(@RequestParam("code") String code) {
        log.info("인가 코드를 이용하여 토큰을 받습니다.");
        KakaoTokenResponse kakaoTokenResponse = kakaoTokenJsonData.getToken(code); // Kakao OAuth 인가 코드를 토큰으로 교환하는 요청
        log.info("토큰에 대한 정보입니다.{}",kakaoTokenResponse);
        return "redirect:/api/vi/signup";
    }
}
