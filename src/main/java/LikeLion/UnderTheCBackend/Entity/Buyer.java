package LikeLion.UnderTheCBackend.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "buyer")
@Getter
@Setter
public class Buyer implements UserDetails {
    @Id
    @Column(updatable = false)
    private Long id;

    @Column
    private String password;

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

    @Column(unique = true)
    private String nickname;

    @Builder
    public Buyer(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public Buyer update(String nickname) {
        this.nickname = nickname;

        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("buyer"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
