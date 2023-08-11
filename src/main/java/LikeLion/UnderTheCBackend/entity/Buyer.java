package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "buyer")
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("BUYER")
public class Buyer extends User {
    @Builder
    public Buyer(Long id, String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        super(id, name, nickname, call, email, address, detailAddress, role, photo, certificate);
    }
}
