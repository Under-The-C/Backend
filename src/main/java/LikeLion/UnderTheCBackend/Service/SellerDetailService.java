package LikeLion.UnderTheCBackend.Service;

import LikeLion.UnderTheCBackend.Entity.Seller;
import LikeLion.UnderTheCBackend.Repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerDetailService implements UserDetailsService {

    private final SellerRepository sellerRepository;

    @Override
    public Seller loadUserByUsername(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
