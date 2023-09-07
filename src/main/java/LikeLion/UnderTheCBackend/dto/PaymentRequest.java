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

    private String name;

    private String callNumber;

    private String address;

    private String method;
}
