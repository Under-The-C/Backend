package LikeLion.UnderTheCBackend.Service;

import LikeLion.UnderTheCBackend.Entity.Buyer;
import LikeLion.UnderTheCBackend.Repository.BuyerRepository;
import LikeLion.UnderTheCBackend.dto.AddBuyerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddBuyerRequest dto) {
        return buyerRepository.save(Buyer.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public Buyer findById(Long userId) {
        return buyerRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected buyer"));
    }

    public Buyer findByEmail(String email) {
        return buyerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected buyer"));
    }
}
