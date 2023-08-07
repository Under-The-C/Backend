package LikeLion.UnderTheCBackend.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderReq {
    @NonNull
    private String impUid;

    @NonNull
    private String merchantUid;
}
