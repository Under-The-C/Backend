package LikeLion.UnderTheCBackend.dto;

import LikeLion.UnderTheCBackend.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginResponse {
    @Schema(description = "성공 여부")
    private String status;
    @Schema(description = "응답 메세지")
    private String message;
    @Schema(description = "유저 정보")
    private User user;
    @Schema(description = "액세스 토큰")
    private String access_token;

    public LoginResponse(String status, String message, User user, String access_token) {
        this.status = status;
        this.message = message;
        this.user = user;
        this.access_token = access_token;
    }
}


