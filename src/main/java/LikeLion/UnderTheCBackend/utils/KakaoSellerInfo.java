package LikeLion.UnderTheCBackend.utils;

import LikeLion.UnderTheCBackend.dto.KakaoSellerInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class KakaoSellerInfo {
    private final WebClient webClient;
    private static final String SELLER_INFO_URI = "https://kapi.kakao.com/v2/seller/me";

    public KakaoSellerInfoResponse getSellerInfo(String token) {
        String uri = SELLER_INFO_URI;

        Flux<KakaoSellerInfoResponse> response = webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(KakaoSellerInfoResponse.class);

        return response.blockFirst();
    }
}
