package com.demo.wallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Wallet {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private WalletUser user;

    private BigDecimal balance;

    public Wallet() {
        this.user = user;
        this.balance = balance;
    }

    public Wallet(WalletUser user) {
        this.user = user;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WalletUser getUser() {
        return user;
    }

    public void setUser(WalletUser user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        if (balance == null){
            return new BigDecimal(0);
        }
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}