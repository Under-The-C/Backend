package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.AddUser;
import LikeLion.UnderTheCBackend.dto.KakaoUserInfoResponse;
import LikeLion.UnderTheCBackend.dto.UpdateUser;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import LikeLion.UnderTheCBackend.service.UserService;
import LikeLion.UnderTheCBackend.utils.KakaoUserInfo;
import io.micrometer.common.util.internal.logging.InternalLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@Slf4j
@RestController
@Tag(name = "user API", description = "유저 API")
@RequestMapping("/api/v1/user")
public class UserController {
    final String imagesPath = "/src/main/resources/images/";
    private final UserRepository userRepository;
    private final UserService userService;
    private final KakaoUserInfo kakaoUserInfo;
    UserController(UserRepository userRepository, UserService userService, KakaoUserInfo kakaoUserInfo) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.kakaoUserInfo = kakaoUserInfo;
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

        Long id = (Long) session.getAttribute("user");

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
        }
    }

    @PostMapping("/add")
    @Operation(summary = "유저 추가", description = "user 테이블에 유저 추가", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료")
    })
    public String addUser(@RequestParam("access_token") String token, @RequestBody AddUser json) {
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        if (kakaoUserInfo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 로그인에 실패하였습니다.");
        }
        String email = userInfo.getKakao_account().getEmail();
        String name = json.getName();
        String phone = json.getPhone();
        String address = json.getAddress();
        String detailAddress = json.getAddress();
        String role = json.getRole();
        String certificate = json.getCertificate();

        /* 이메일로 중복 회원 체크 */
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입 실패. 중복회원입니다.");
        });

        userService.createUser(name, phone, email, address, detailAddress, role, certificate);

        return "success";
    }

    @PatchMapping("/update")
    @Operation(summary = "유저 정보 수정", description = "user 테이블에 지정된 id로 유저 정보 수정", responses = {
            @ApiResponse(responseCode = "200", description = "수정 완료")
    })
    public User updateById(HttpServletRequest request, @RequestBody UpdateUser json) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }

        Long id = (Long) session.getAttribute("user");

        Optional<User> beforeUser = userRepository.findById(id);

        String name = json.getName();
        String phone = json.getPhone();
        String address = json.getAddress();
        String detailAddress = json.getDetailAddress();
        MultipartFile profile = json.getProfile();
        String certificate = json.getCertificate();

        User afterUser = beforeUser.get();

        if(beforeUser.isPresent()) {
            afterUser.setName(name);
            afterUser.setPhone(phone);
            afterUser.setAddress(address);
            afterUser.setDetailAddress(detailAddress);
            afterUser.setCertificate(certificate);

            if (profile != null && !profile.isEmpty()) {
                String filename = profile.getOriginalFilename();
                log.info("reviewImage.getOriginalFilename = {}", filename);

                ClassPathResource classPathResource = new ClassPathResource("images/");
                String absolutePath = System.getProperty("user.dir");
                ;
                String fullPath = classPathResource.getPath() + filename;
                log.info(" absolutePath = {}", absolutePath);
                profile.transferTo(new File(absolutePath + imagesPath + filename));
            }

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
//    public String deleteById(HttpServletRequest request) {
//        // 로그인 구현 후, 로그인 되어 있을 경우, 안 되어 있을 경우 나누어서 구현할 예정
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
//        }
//        Long id = (Long) session.getAttribute("user");
//
//        User user = userRepository.findById(id).orElse(null);
//        if (user != null) {
//            userRepository.deleteById(id);
//            session.invalidate();
//            return "success";
//        } else {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다.");
//        }
//    }
}
