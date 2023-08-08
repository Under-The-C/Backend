package LikeLion.UnderTheCBackend.Service;

import LikeLion.UnderTheCBackend.Entity.Seller;
import LikeLion.UnderTheCBackend.config.TokenProviderForSeller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenServiceForSeller {

    private final TokenProviderForSeller tokenProviderForSeller;
    private final RefreshTokenService refreshTokenService;
    private final SellerService sellerService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProviderForSeller.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        Seller user = sellerService.findById(userId);

        return tokenProviderForSeller.generateToken(user, Duration.ofHours(2));
    }
}
