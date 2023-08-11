package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("SELLER")
public class Seller extends User {
    @Builder
    public Seller(Long id, String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        super(id, name, nickname, call, email, address, detailAddress, role, photo, certificate);
    }
}
