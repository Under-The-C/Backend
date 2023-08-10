package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.KakaoBuyerInfoResponse;
import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import LikeLion.UnderTheCBackend.dto.LoginResponseForBuyer;
import LikeLion.UnderTheCBackend.entity.Buyer;
import LikeLion.UnderTheCBackend.repository.BuyerRepository;
import LikeLion.UnderTheCBackend.service.BuyerService;
import LikeLion.UnderTheCBackend.utils.KakaoBuyerInfo;
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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Buyer API", description = "구매자 API")
@RequestMapping("/api/v1/buyer")
public class BuyerController {
    private final KakaoTokenJsonData kakaoTokenJsonData;
    private final KakaoBuyerInfo kakaoBuyerInfo;
    private final BuyerService buyerService;

}
