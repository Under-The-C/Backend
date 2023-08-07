package LikeLion.UnderTheCBackend.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderReq {
    @NonNull
    private String imp_uid;

    @NonNull
    private String merchant_uid;
}
