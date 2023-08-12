package LikeLion.UnderTheCBackend.dto;

import lombok.Data;
@Data
public class KakaoAccount {
    private Boolean has_email;
    private Boolean email_needs_agreement;
    private Boolean is_email_valid;
    private Boolean is_email_verified;
    private String email;
    private Boolean profile_image_needs_agreement;
    private Profile profile;

    @Data
    public static class Profile {
        private String thumbnail_image_url;
        private String profile_image_url;
        private Boolean is_default_image;
    }
}

