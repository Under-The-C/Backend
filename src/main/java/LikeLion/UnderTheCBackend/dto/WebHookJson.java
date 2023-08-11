package LikeLion.UnderTheCBackend.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class WebHookJson extends PaymentRequest {
    private String status;

    public WebHookJson(@NonNull String imp_uid, @NonNull String merchant_uid, String status) {
        super(imp_uid, merchant_uid);
        this.status = status;
    }
}
