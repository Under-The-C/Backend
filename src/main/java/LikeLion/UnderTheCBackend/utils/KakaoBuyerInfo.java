package LikeLion.UnderTheCBackend.utils;

import LikeLion.UnderTheCBackend.dto.KakaoBuyerInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
public class KakaoBuyerInfo {
    private final WebClient webClient;
    private static final String BUYER_INFO_URI = "https://kapi.kakao.com/v2/buyer/me";

    public KakaoBuyerInfoResponse getBuyerInfo(String token) {
        String uri = BUYER_INFO_URI;

        Flux<KakaoBuyerInfoResponse> response = webClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToFlux(KakaoBuyerInfoResponse.class);

        return response.blockFirst();
    }
}
