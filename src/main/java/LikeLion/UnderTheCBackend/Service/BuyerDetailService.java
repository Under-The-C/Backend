package LikeLion.UnderTheCBackend.Service;

import LikeLion.UnderTheCBackend.Entity.Buyer;
import LikeLion.UnderTheCBackend.Repository.BuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BuyerDetailService implements UserDetailsService {

    private final BuyerRepository buyerRepository;

    @Override
    public Buyer loadUserByUsername(String email) {
        return buyerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
