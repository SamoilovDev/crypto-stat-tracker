package com.samoilov.dev.cryptostattracker.entity;

import com.samoilov.dev.cryptostattracker.entity.base.BaseEntity;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency_check_results", indexes = {
        @Index(name = "pk_currency_check_results_check_result_id", columnList = "check_result_id"),
        @Index(name = "idx_currency_check_results_check_time", columnList = "check_time")
})
public class CurrencyCheckResultEntity implements BaseEntity {

    @Id
    @Column(name = "check_result_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "check_result_id_currency_check_results_seq")
    @SequenceGenerator(
            sequenceName = "check_result_id_currency_check_results_seq",
            name = "check_result_id_currency_check_results_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "currency_price", nullable = false, updatable = false)
    private Double currencyPrice;

    @CreationTimestamp
    @Column(name = "check_time", nullable = false, updatable = false)
    private LocalDateTime checkTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "crypto_currency_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_currency_check_results_crypto_currency_id"))
    private CryptoCurrencyEntity cryptoCurrency;

}
