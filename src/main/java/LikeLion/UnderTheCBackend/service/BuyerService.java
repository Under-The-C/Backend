package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.entity.Buyer;
import LikeLion.UnderTheCBackend.repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;

    @Transactional
    public Long createBuyer(Long id, String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        Buyer buyer = Buyer.builder()
                .id(id)
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

        buyerRepository.save(buyer);
        log.info("success");
        return buyer.getId();
    }
}
