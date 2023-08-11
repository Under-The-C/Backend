package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.entity.User;
import LikeLion.UnderTheCBackend.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUser(String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        User user = User.builder()
                .name(name)
                .nickname(nickname)
                .call(call)
                .email(email)
                .address(address)
                .detailAddress(detailAddress)
                .role(role)
                .photo(photo)
                .certificate(certificate)
                .build();

        userRepository.save(user);
        log.info("success");
        return user.getId();
    }
}