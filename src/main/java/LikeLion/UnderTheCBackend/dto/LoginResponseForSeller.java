package LikeLion.UnderTheCBackend.dto;

import LikeLion.UnderTheCBackend.entity.Buyer;
import LikeLion.UnderTheCBackend.entity.Seller;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginResponseForSeller {
    @Schema(description = "성공 여부")
    private String status;
    @Schema(description = "응답 메세지")
    private String message;
    @Schema(description = "판매자 정보")
    private Seller seller;
    @Schema(description = "액세스 토큰")
    private String access_token;

    public LoginResponseForSeller(String status, String message, Seller seller, String access_token) {
        this.status = status;
        this.message = message;
        this.seller = seller;
        this.access_token = access_token;
    }
}
