package LikeLion.UnderTheCBackend.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PaymentRequest {
    @NonNull
    private String imp_uid;

    @NonNull
    private String merchant_uid;

    @NonNull
    private String name;

    @NonNull
    private String callNumber;

    @NonNull
    private String address;

    @NonNull
    private String method;
}
