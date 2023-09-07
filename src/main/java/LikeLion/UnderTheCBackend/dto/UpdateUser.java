package LikeLion.UnderTheCBackend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateUser {
    @Schema(description = "성함")
    private String name;
    @Schema(description = "연락처")
    private String phone;
    @Schema(description = "도로명주소")
    private String address;
    @Schema(description = "상세주소")
    private String detailAddress;
    @Schema(description = "사진")
    private MultipartFile profile;
    @Schema(description = "사업자 등록증")
    private MultipartFile certificate;
    public UpdateUser(String name, String phone, String address, String detailAddress, MultipartFile profile, MultipartFile certificate) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.profile = profile;
        this.certificate = certificate;
    }
}