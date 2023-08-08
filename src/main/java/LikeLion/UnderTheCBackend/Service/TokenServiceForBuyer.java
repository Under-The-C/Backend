package LikeLion.UnderTheCBackend.Service;

import LikeLion.UnderTheCBackend.Entity.Buyer;
import LikeLion.UnderTheCBackend.config.TokenProviderForBuyer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenServiceForBuyer {

    private final TokenProviderForBuyer tokenProviderForBuyer;
    private final RefreshTokenService refreshTokenService;
    private final BuyerService buyerService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProviderForBuyer.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        Buyer user = buyerService.findById(userId);

        return tokenProviderForBuyer.generateToken(user, Duration.ofHours(2));
    }
}

