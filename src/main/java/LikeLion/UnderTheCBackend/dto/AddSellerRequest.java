package LikeLion.UnderTheCBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSellerRequest {
    private String email;
    private String password;
}
