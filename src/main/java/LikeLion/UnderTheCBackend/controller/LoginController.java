package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.KakaoUserInfoResponse;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import LikeLion.UnderTheCBackend.utils.KakaoUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Login/Logout", description = "로그인/로그아웃 API")
public class LoginController {
    private UserRepository userRepository;
    private KakaoUserInfo kakaoUserInfo;

    @Autowired
    LoginController(UserRepository userRepository, KakaoUserInfo kakaoUserInfo) {
        this.userRepository = userRepository;
        this.kakaoUserInfo = kakaoUserInfo;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    public String login(HttpServletRequest request, @RequestParam("access_token") String token) {
        if (request.getSession(false) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 로그인 되어 있습니다.");
        }

        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        if (userInfo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 로그인에 실패하였습니다.");
        }
        String email = userInfo.getKakao_account().getEmail();

        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "등록되지 않은 사용자입니다.");
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user.getId());

        return "success";
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }
        session.invalidate();
        return "success";
    }
}
