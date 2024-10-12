package com.samoilov.dev.cryptostattracker.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crypto_currencies", indexes = {
        @Index(name = "pk_crypto_currencies_currency_id", columnList = "currency_id"),
        @Index(name = "idx_crypto_currencies_currency_code", columnList = "currency_code"),
        @Index(name = "idx_crypto_currencies_last_check_time", columnList = "last_check_time")
})
public class CryptoCurrencyEntity implements Serializable {

    @Id
    @Column(name = "currency_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_id_crypto_currencies_seq")
    @SequenceGenerator(
            sequenceName = "currency_id_crypto_currencies_seq",
            name = "currency_id_crypto_currencies_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "checking_period", nullable = false)
    private Integer checkingPeriod; // in minutes

    @Column(name = "last_check_time")
    private LocalDateTime lastCheckTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "subscriber_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_crypto_currencies_subscriber_id"))
    private UserEntity cryptoCurrencySubscriber;

    @OneToMany(mappedBy = "cryptoCurrency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurrencyCheckResultEntity> checkResults;

}
