package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.entity.Buyer;
import LikeLion.UnderTheCBackend.entity.Seller;
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
    public Long createUser(String userType, String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        User user;

        if ("BUYER".equals(userType)) {
            user = Buyer.builder()
                    .name(name)
                    .nickname(nickname)
                    .call(call)
                    .email(email)
                    .address(address)
                    .detailAddress(detailAddress)
                    .role(role)
                    .photo(photo)
                    .certificate(null)
                    .build();
        } else if ("SELLER".equals(userType)) {
            user = Seller.builder()
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
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        userRepository.save(user);
        log.info("Success");
        return user.getId();
    }
}