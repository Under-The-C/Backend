package LikeLion.UnderTheCBackend.controller;

import LikeLion.UnderTheCBackend.Entity.Seller;
import LikeLion.UnderTheCBackend.Repository.SellerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@Tag(name = "seller API", description = "판매자 정보 API")
@RequestMapping("/api/vi/seller")
public class SellerController {
    private SellerRepository sellerRepository;

    SellerController(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @GetMapping("/api/vi/seller/id")
    @Operation(summary = "판매자 정보 보기", description = "seller 테이블에 지정된 이메일로 유저 정보 반환", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public Seller findByEmail(@RequestParam("email") String email) {
        Optional<Seller> seller = null;
        seller = sellerRepository.findByEmail(email);

        /* 판매자 정보 반환 */
        if (seller.isPresent()) {
            return seller.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 판매자입니다.");
        }
    }

//    @GetMapping("/id/me")
//    @Operation(summary = "로그인 되어 있는 유저 정보 보기", description = "로그인 되어 있는 유저 정보 반환", responses = {
//            @ApiResponse(responseCode = "200", description = "조회 성공")
//    })
//    public User findMineById(HttpServletRequest request) {
//        /* 로그인 상태 확인 */
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
//        }
//
//        User loginUser = (User) session.getAttribute("loginUser");
//
//        String id = loginUser.getId();
//
//        Optional<User> user = userRepository.findById(id);
//
//        /* 유저 정보 반환 */
//        return user.get();
//    }
//
//    /* 이메일 유효성 검사 메소드 */
//    private void isEmailValidate(String email) {
//        if (!(email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+"))) {
//            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "유효하지 않은 이메일 형식입니다.");
//        }
//    }

    /* 아이디 중복 검사 메소드 */
    private void isUserNotExist(String email) {
        Optional<Seller> existingUser = sellerRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입 실패. 중복회원입니다.");
        }
    }

//    @PostMapping("/add")
//    @Operation(summary = "유저 추가", description = "user 테이블에 지정된 ID로 유저 추가", responses = {
//            @ApiResponse(responseCode = "200", description = "회원가입 완료")
//    })
//    public User addById(@RequestBody UserAdd json) {
//        /* json 데이터로 유저 정보 확인 */
//        String id = json.getId();
//        String password = json.getPassword();
//        String email = json.getEmail();
//
//        /* 아이디 중복 검사 */
//        isUserNotExist(id);
//        /* 이메일 유효성 검사 */
//        isEmailValidate(email);
//
//        /* 아이디가 중복되지 않고 이메일이 유효할 경우, 회원가입 진행 */
//        User user = new User();
//        user.setId(id);
//        user.setPassword(password);
//        user.setEmail(email);
//        userRepository.save(user);
//        return user;
//    }

//    @PatchMapping("/update")
//    @Operation(summary = "유저 정보 수정", description = "user 테이블에 지정된 ID로 유저 정보 수정", responses = {
//            @ApiResponse(responseCode = "200", description = "수정 완료")
//    })
//    public User updateById(HttpServletRequest request, @RequestBody UserUpdate json) {
//        /* 로그인 상태 확인 */
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인 되어 있지 않습니다.");
//        }
//
//        User loginUser = (User) session.getAttribute("loginUser");
//
//        String id = loginUser.getId();
//
//        Optional<User> beforeUser = userRepository.findById(id);
//
//        /* json 데이터로 수정할 유저 정보 확인 */
//        String password = json.getPassword();
//        String email = json.getEmail();
//
//        /* 이메일 유효성 검사 */
//        isEmailValidate(email);
//
//        /* 회원 정보 수정 진행 */
//        User afterUser = beforeUser.get();
//        afterUser.setPassword(password);
//        afterUser.setEmail(email);
//        userRepository.save(afterUser);
//        return afterUser;
//    }
//
//    @DeleteMapping("/api/vi/delete/seller")
//    @Operation(summary = "판매자 삭제", description = "seller 테이블에 지정된 ID로 판매자 삭제", responses = {
//            @ApiResponse(responseCode = "200", description = "회원탈퇴 완료")
//    })
//    public Seller deleteById(HttpServletRequest request, @RequestBody LoginRequest json) {
//        /* 로그인 상태 확인 */
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            /* json 데이터로 아이디와 패스워드 확인 */
//            String id = json.getId();
//            String password = json.getPassword();
//
//            Optional<User> user = userRepository.findById(id);
//
//            /* 유저 테이블 내에 아이디가 존재하고 패스워드가 일치하면 회원탈퇴 진행, 아니면 400 오류 메시지 반환  */
//            if (user.isPresent() && user.get().getPassword().equals(password)) {
//                userRepository.deleteById(id);
//                return user.get();
//            }
//            else {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 또는 패스워드가 틀렸습니다.");
//            }
//        }
//        else {
//            User loginUser = (User) session.getAttribute("loginUser");
//
//            /* json 데이터로 아이디와 패스워드 확인 */
//            String id = json.getId();
//            String password = json.getPassword();
//
//            Optional<User> user = userRepository.findById(id);
//
//            /* 아이디와 패스워드가 일치하면 로그아웃하고 회원탈퇴 진행, 아니면 400 오류 메시지 반환  */
//            if (user.get().getPassword().equals(password)) {
//                session.invalidate();
//                userRepository.deleteById(id);
//                return user.get();
//            } else {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "아이디 또는 패스워드가 틀렸습니다.");
//            }
//        }
//    }
}
