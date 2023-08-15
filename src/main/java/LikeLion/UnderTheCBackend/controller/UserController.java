package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.AddUser;
import LikeLion.UnderTheCBackend.dto.UpdateUser;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import LikeLion.UnderTheCBackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@Tag(name = "user API", description = "유저 API")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    @Operation(summary = "유저 정보 보기", description = "user 테이블에 지정된 id로 유저 정보 반환", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public User findById(@RequestParam("id") Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
        }
    }

    @GetMapping("/me")
    @Operation(summary = "로그인 되어 있는 유저 정보 보기", description = "로그인 되어 있는 유저 정보 반환", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public User findMineById(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }

        User loginUser = (User) session.getAttribute("loginUser");

        Long id = loginUser.getId();

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
        }
    }

    private void isUserNotExist(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입 실패. 중복회원입니다.");
        }
    }

    @PostMapping("/add")
    @Operation(summary = "유저 추가", description = "user 테이블에 유저 추가", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료")
    })
    public String addUser(@RequestBody AddUser json) {
        String name = json.getName();
        String email = json.getEmail();
        String phone = json.getPhone();
        String address = json.getAddress();
        String detailAddress = json.getAddress();
        String role = json.getRole();
        String certificate = json.getCertificate();

        isUserNotExist(email);

        userService.createUser(name, phone, email, address, detailAddress, role, certificate);

        return "success";
    }

    @PatchMapping("/update")
    @Operation(summary = "유저 정보 수정", description = "user 테이블에 지정된 id로 유저 정보 수정", responses = {
            @ApiResponse(responseCode = "200", description = "수정 완료")
    })
    public User updateById(HttpServletRequest request, @RequestBody UpdateUser json) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }

        User loginUser = (User) session.getAttribute("loginUser");

        Long id = loginUser.getId();

        Optional<User> beforeUser = userRepository.findById(id);

        String name = json.getName();
        String phone = json.getPhone();
        String address = json.getAddress();
        String detailAddress = json.getDetailAddress();
        String profile = json.getProfile();
        String certificate = json.getCertificate();

        User afterUser = beforeUser.get();

        if(beforeUser.isPresent()) {
            afterUser.setName(name);
            afterUser.setPhone(phone);
            afterUser.setAddress(address);
            afterUser.setDetailAddress(detailAddress);
            afterUser.setProfile(profile);
            afterUser.setCertificate(certificate);

            userRepository.save(afterUser);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
        }
        return afterUser;
    }

//    @DeleteMapping("/delete")
//    @Operation(summary = "유저 삭제", description = "user 테이블에 지정된 이메일로 유저 삭제", responses = {
//            @ApiResponse(responseCode = "200", description = "회원탈퇴 완료")
//    })
//    public User deleteByEmail(@RequestParam("email") String email) {
//        // 로그인 구현 후, 로그인 되어 있을 경우, 안 되어 있을 경우 나누어서 구현할 예정
//        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
//
//        if (user.isPresent()) {
//            userRepository.deleteByEmail(email);
//            return user.get();
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
//        }
//    }
}
