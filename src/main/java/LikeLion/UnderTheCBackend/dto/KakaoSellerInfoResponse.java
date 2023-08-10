package LikeLion.UnderTheCBackend.dto;

import lombok.Data;

@Data
public class KakaoSellerInfoResponse {
    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;
}
