package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

    LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    @Operation(summary = "로그인", description = "로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    public String login(HttpServletRequest request, @RequestParam("email") String email) {
        if (request.getSession(false) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 로그인 되어 있습니다.");
        }

        Optional<User> result = Optional.ofNullable(this.userRepository.findByEmail(email));
        User user = result.orElse(null);

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
        User user = (User) session.getAttribute("user");
        session.invalidate();
        return "success";
    }
}
