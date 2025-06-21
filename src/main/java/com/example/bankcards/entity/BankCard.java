package com.example.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
public class BankCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(
            regexp = "^\\*{4}-\\*{4}-\\*{4}-\\d{4}$",
            message = "Номер карты должен быть в формате XXXX-XXXX-XXXX-XXXX"
    )
    @Column(name = "cardNumber", nullable = false)
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "expiration_date", nullable = false)
    @Future(message = "Срок действия карты должен быть в будущем")
    private LocalDate expirationDate;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(
            name = "balance",
            nullable = false
    )
    private BigDecimal balance;

    @PrePersist
    public void prePersist() {
        status = Status.ACTIVE;
        expirationDate = LocalDate.now().plusYears(5);
        balance = BigDecimal.ZERO;
    }

}
