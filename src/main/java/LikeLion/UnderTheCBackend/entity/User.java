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
    private String phone;
    @Column(nullable = false, unique = true, updatable = false)
    private String email;
    @Column
    private String address;
    @Column
    private String detailAddress;
    @Column(nullable = false, updatable = false)
    private String role;
    @Column(nullable = true)
    private String profile;
    @Column(nullable = true)
    private String certificate;

    @Builder
    public User(Long id, String name, String nickname, String phone, String email, String address, String detailAddress, String role, String profile, String certificate) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
        this.role = role;
        this.profile = profile;
        this.certificate = certificate;
    }
}
