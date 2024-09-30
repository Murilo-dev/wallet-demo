package com.demo.wallet.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class WalletHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private WalletUser user;
    // private WalletUser userReceiver;

    private String transactionType;  // deposit, withdraw, transfer, balance
    private BigDecimal amount;
    private String description;
    private LocalDateTime timestamp;

    public WalletHistory() {}

    public WalletHistory(Wallet wallet, WalletUser user, String transactionType, BigDecimal amount, String description) {
        this.wallet = wallet;
        this.user = user;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public Wallet getWallet() { return wallet; }

    public WalletUser getUser() { return user; }

    public WalletUser getUserReceiver() { return user; }

    public String getTransactionType() { return transactionType; }

    public BigDecimal getAmount() { return amount; }

    public String getDescription() { return description; }

    public LocalDateTime getTimestamp() { return timestamp; }
}