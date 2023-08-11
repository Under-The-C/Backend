package LikeLion.UnderTheCBackend.service;

import LikeLion.UnderTheCBackend.entity.Seller;
import LikeLion.UnderTheCBackend.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    @Transactional
    public Long createSeller(String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        Seller seller = Seller.builder()
                .name(name)
                .nickname(nickname)
                .call(call)
                .email(email)
                .address(address)
                .address(detailAddress)
                .role(role)
                .photo(photo)
                .certificate(certificate)
                .build();

        sellerRepository.save(seller);
        log.info("success");
        return seller.getId();
    }
}
