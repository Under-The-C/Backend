package LikeLion.UnderTheCBackend.dto;

import LikeLion.UnderTheCBackend.entity.Buyer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginResponseForBuyer {
    @Schema(description = "성공 여부")
    private String status;
    @Schema(description = "응답 메세지")
    private String message;
    @Schema(description = "구매자 정보")
    private Buyer buyer;
    @Schema(description = "액세스 토큰")
    private String access_token;

    public LoginResponseForBuyer(String status, String message, Buyer buyer, String access_token) {
        this.status = status;
        this.message = message;
        this.buyer = buyer;
        this.access_token = access_token;
    }
}

