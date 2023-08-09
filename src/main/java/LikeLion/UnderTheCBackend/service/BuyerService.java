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
    public Long createBuyer(String email) {
        Buyer buyer = Buyer.builder()
                .email(email)
                .build();

        buyerRepository.save(buyer);
        log.info("success");
        return buyer.getId();
    }
}
