package LikeLion.UnderTheCBackend.utils;

import LikeLion.UnderTheCBackend.dto.KakaoTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class KakaoTokenJsonData {
    public KakaoTokenJsonData(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String REDIRECT_URI = "https://localhost/login/oauth2/code/kakao";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ID = "be84e5c954c05f4d77886292167f2621";

    public KakaoTokenResponse getToken(String code, String redirectUrl) {
        String uri = TOKEN_URI + "?grant_type=" + GRANT_TYPE + "&client_id=" + CLIENT_ID + "&redirect_uri=" + redirectUrl + "&code=" + code;
        System.out.println(uri);

        Flux<KakaoTokenResponse> response = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(KakaoTokenResponse.class);

        return response.blockFirst();
    }
}
