package LikeLion.UnderTheCBackend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column
    private String name;
    @Column
    private String nickname;
    @Column
    private String call;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String address;
    @Column
    private String detailAddress;
    @Column
    private String role;
    @Column
    private String photo;
    @Column
    private String certificate;

    @Builder
    public User(Long id, String name, String nickname, String call, String email, String address, String detailAddress, String role, String photo, String certificate) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.call = call;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
        this.role = role;
        this.photo = photo;
        this.certificate = certificate;
    }
}
