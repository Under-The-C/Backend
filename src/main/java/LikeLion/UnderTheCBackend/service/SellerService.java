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
    public Long createSeller(String email) {
        Seller seller = Seller.builder()
                .email(email)
                .build();

        sellerRepository.save(seller);
        log.info("Success");
        return seller.getId();
    }
}
