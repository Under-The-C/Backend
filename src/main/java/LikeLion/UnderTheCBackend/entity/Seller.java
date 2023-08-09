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
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private String call;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String address;

    @Column
    private byte[] photo;

    @Column
    private byte[] certificate;

    @Builder
    public Seller(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
