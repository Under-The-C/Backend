package LikeLion.UnderTheCBackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AddUser {
    @Schema(description = "성함")
    private String name;
    @Schema(description = "이메일")
    private String email;
    @Schema(description = "연락처")
    private String phone;
    @Schema(description = "도로명주소")
    private String address;
    @Schema(description = "상세주소")
    private String detailAddress;
    @Schema(description = "유형")
    private String role;
    @Schema(description = "사업자 등록증")
    private String certificate;

    public AddUser(String name, String email, String phone, String address, String detailAddress, String role, String certificate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.role = role;
        this.certificate = certificate;
    }
}
