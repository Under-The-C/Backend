package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.dto.AddUser;
import LikeLion.UnderTheCBackend.dto.KakaoUserInfoResponse;
import LikeLion.UnderTheCBackend.dto.UpdateUser;
import LikeLion.UnderTheCBackend.entity.Role;
import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import LikeLion.UnderTheCBackend.service.UserService;
import LikeLion.UnderTheCBackend.utils.KakaoUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static LikeLion.UnderTheCBackend.entity.Role.BUYER;
import static LikeLion.UnderTheCBackend.entity.Role.SELLER;


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

    private boolean mkdir(String path) {
        String absolutePath = System.getProperty("user.dir");
        File Folder = new File(absolutePath + path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try {
                return Folder.mkdirs(); //폴더 생성합니다.
            }
            catch(Exception e){
                e.getStackTrace();
                return false;
            }
        } else {
            return true;
        }
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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 추가", description = "user 테이블에 유저 추가", responses = {
            @ApiResponse(responseCode = "200", description = "회원가입 완료")
    })
    public String addUser(@RequestParam("access_token") String token, @ModelAttribute AddUser json) throws IOException {
        KakaoUserInfoResponse userInfo = kakaoUserInfo.getUserInfo(token);
        if (userInfo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 로그인에 실패하였습니다.");
        }
        String email = userInfo.getKakao_account().getEmail();
        String name = json.getName();
        String phone = json.getPhone();
        String address = json.getAddress();
        String detailAddress = json.getDetailAddress();
        if (name == null || phone == null || address == null || detailAddress == null) {
            log.info("name = {}", name);
            log.info("phone = {}", phone);
            log.info("address = {}", address);
            log.info("detailAddress = {}", detailAddress);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력되지 않은 정보가 있습니다.");
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy_MM_dd");

        Role role;
        if (json.getRole().equalsIgnoreCase("BUYER")) {
            role = BUYER;
        }
        else if (json.getRole().equalsIgnoreCase("SELLER")) {
            role = SELLER;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 유형입니다.");
        }
        MultipartFile certificate = json.getCertificate();
        String filename = null;
        if (certificate != null) {
            String randomStr = formatter.format(date) + UUID.randomUUID();
            filename = randomStr + certificate.getOriginalFilename();
            log.info("reviewImage.getOriginalFilename = {}", filename);

            String absolutePath = System.getProperty("user.dir");
            log.info(" absolutePath = {}", absolutePath);
            if (mkdir(imagesPath)) {
                log.info("폴더가 생성되었습니다.");
            }
            else {
                log.info("이미 폴더가 생성되어 있습니다.");
            }
            certificate.transferTo(new File(absolutePath + imagesPath + filename));
        }

        /* 이메일로 중복 회원 체크 */
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입 실패. 중복회원입니다.");
        });

        userService.createUser(name, phone, email, address, detailAddress, role, filename);

        return "success";
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 정보 수정", description = "user 테이블에 지정된 id로 유저 정보 수정", responses = {
            @ApiResponse(responseCode = "200", description = "수정 완료")
    })
    public User updateById(HttpServletRequest request, @ModelAttribute UpdateUser json) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
        }

        Long id = (Long) session.getAttribute("user");

        Optional<User> beforeUser = userRepository.findById(id);

        if (beforeUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
        }

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy_MM_dd");

        String name = json.getName();
        String phone = json.getPhone();
        String address = json.getAddress();
        String detailAddress = json.getDetailAddress();
        MultipartFile profile = json.getProfile();
        MultipartFile certificate = json.getCertificate();

        User afterUser = beforeUser.get();
        String absolutePath = System.getProperty("user.dir");

        if(beforeUser.isPresent()) {
            if (name != null)
                afterUser.setName(name);
            if (phone != null)
                afterUser.setPhone(phone);
            if (address != null)
                afterUser.setAddress(address);
            if (detailAddress != null)
                afterUser.setDetailAddress(detailAddress);

            if (certificate != null) {
                String randomStr = formatter.format(date) + UUID.randomUUID();
                String filename1 = randomStr + certificate.getOriginalFilename();
                log.info("reviewImage.getOriginalFilename = {}", filename1);

                log.info(" absolutePath = {}", absolutePath);

                // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
                if (mkdir(imagesPath)) {
                    log.info("폴더가 생성되었습니다.");
                }
                else {
                    log.info("이미 폴더가 생성되어 있습니다.");
                }
                profile.transferTo(new File(absolutePath + imagesPath + filename1));
                afterUser.setCertificate(filename1);
            }

            if (profile != null) {
                String randomStr = formatter.format(date) + UUID.randomUUID();
                String filename2 = randomStr + profile.getOriginalFilename();
                log.info("reviewImage.getOriginalFilename = {}", filename2);

                if (mkdir(imagesPath)) {
                    log.info("폴더가 생성되었습니다.");
                }
                else {
                    log.info("이미 폴더가 생성되어 있습니다.");
                }

                profile.transferTo(new File(absolutePath + imagesPath + filename2));
                afterUser.setProfile(filename2);
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
