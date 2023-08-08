package LikeLion.UnderTheCBackend.Service;

import LikeLion.UnderTheCBackend.Entity.Seller;
import LikeLion.UnderTheCBackend.Repository.SellerRepository;
import LikeLion.UnderTheCBackend.dto.AddBuyerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddBuyerRequest dto) {
        return sellerRepository.save(Seller.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public Seller findById(Long userId) {
        return sellerRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected seller"));
    }

    public Seller findByEmail(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected seller"));
    }
}
