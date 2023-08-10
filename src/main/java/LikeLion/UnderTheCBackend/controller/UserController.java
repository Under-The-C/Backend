package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.AddUser;
import LikeLion.UnderTheCBackend.dto.UpdateUser;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@Tag(name = "user API", description = "유저 API")
@RequestMapping("/api/vi/user")
public class UserController {
    private final UserRepository userRepository;
    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/id")
    @Operation(summary = "유저 정보 보기", description = "user 테이블에 지정된 이메일로 유저 정보 반환", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public User findByEmail(@RequestParam("email") String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }
    }

    @GetMapping("/id/me")
    @Operation(summary = "로그인 되어 있는 유저 정보 보기", description = "로그인 되어 있는 유저 정보 반환", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public User findMineById(@RequestParam("email") String email) {
        // 로그인 구현 후, 로그인 되어 있는 이메일의 계정 정보 조회할 예정
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            return user.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }
    }

    @PostMapping("/add")
    @Operation(summary = "유저 추가", description = "user 테이블에 지정된 이메일로 유저 추가", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료")
    })
    public User addById(@RequestBody AddUser json, @RequestParam("email") String email, @RequestParam("photo") String photo) {
        // 이메일과 프로필 사진은 카카오에서 받아올 예정
        String name = json.getName();
        String call = json.getCall();
        String nickname = json.getNickname();
        String address = json.getAddress();
        String detailAddress = json.getAddress();
        String role = json.getRole();
        String certificate = json.getCertificate();

        User user = new User();

        if (certificate != null && !certificate.isEmpty()) {
            user.setName(name);
            user.setNickname(nickname);
            user.setCall(call);
            user.setEmail(email);
            user.setAddress(address);
            user.setDetailAddress(detailAddress);
            user.setRole(role);
            user.setPhoto(photo);
            user.setCertificate(certificate);
        }
        else {
            user.setName(name);
            user.setNickname(nickname);
            user.setCall(call);
            user.setEmail(email);
            user.setAddress(address);
            user.setDetailAddress(detailAddress);
            user.setRole(role);
            user.setPhoto(photo);
        }
        userRepository.save(user);
        return user;
    }

    @PatchMapping("/update")
    @Operation(summary = "유저 정보 수정", description = "user 테이블에 지정된 이메일로 유저 정보 수정", responses = {
            @ApiResponse(responseCode = "200", description = "수정 완료")
    })
    public User updateById(@RequestParam("email") String userEmail, @RequestBody UpdateUser json) {
        // 로그인 구현 후, 로그인 되어 있는 이메일의 계정 정보 수정할 예정
        Optional<User> beforeUser = Optional.ofNullable(userRepository.findByEmail(userEmail));

        String name = json.getName();
        String nickname = json.getNickname();
        String call = json.getCall();
        String email = userEmail;
        String address = json.getAddress();
        String detailAddress = json.getDetailAddress();
        String photo = json.getPhoto();
        String certificate = json.getCertificate();

        User afterUser = beforeUser.get();
        afterUser.setName(name);
        afterUser.setNickname(nickname);
        afterUser.setCall(call);
        afterUser.setAddress(address);
        afterUser.setDetailAddress(detailAddress);
        afterUser.setEmail(photo);
        afterUser.setEmail(certificate);
        userRepository.save(afterUser);
        return afterUser;
    }

    @DeleteMapping("/delete")
    @Operation(summary = "유저 삭제", description = "user 테이블에 지정된 이메일로 유저 삭제", responses = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 완료")
    })
    public User deleteById(@RequestParam("email") String email) {
        // 로그인 구현 후, 로그인 되어 있을 경우, 안 되어 있을 경우 나누어서 구현할 예정
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));

        if (user.isPresent()) {
            userRepository.deleteByEmail(email);
            return user.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 계정입니다.");
        }
    }
}
