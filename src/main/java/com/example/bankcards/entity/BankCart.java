package com.example.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class BankCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(
            regexp = "\\d{4}-\\d{4}-\\d{4}-\\d{4}",
            message = "Номер карты должен быть в формате XXXX-XXXX-XXXX-XXXX"
    )
    @Column(name = "cardNumber", nullable = false)
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User user;

    @Column(name = "expiration_date", nullable = false)
    @Future(message = "Срок действия карты должен быть в будущем")
    private LocalDate expirationDate;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(
            name = "balance",
            nullable = false,
            columnDefinition = "BIGINT DEFAULT 0"
    )
    private Long balance;


}
