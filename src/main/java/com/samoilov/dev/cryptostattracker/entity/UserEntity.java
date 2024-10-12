package com.samoilov.dev.cryptostattracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "pk_users_user_id", columnList = "user_id"),
        @Index(name = "idx_users_username", columnList = "username")
})
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_users_seq")
    @SequenceGenerator(
            sequenceName = "user_id_users_seq",
            name = "user_id_users_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "encoded_password", nullable = false)
    private String encodedPassword;

    @OneToMany(mappedBy = "cryptoCurrencySubscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CryptoCurrencyEntity> subscribedCurrencies;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.encodedPassword;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
