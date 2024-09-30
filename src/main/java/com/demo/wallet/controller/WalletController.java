package com.demo.wallet.controller;

import com.demo.wallet.model.Wallet;
import com.demo.wallet.model.WalletHistory;
import com.demo.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.createWallet(userId));
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        Optional<Wallet> wallet = walletService.getWalletByUserId(userId);
        return wallet.map(w -> ResponseEntity.ok(w.getBalance()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/deposit/{userId}")
    public ResponseEntity<Wallet> depositAmount(@PathVariable Long userId, @RequestBody Wallet wallet) {
        return ResponseEntity.ok(walletService.deposit(userId, wallet.getBalance()));
    }

    @PostMapping("/withdraw/{userId}")
    public ResponseEntity<Wallet> withdraw(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        try {
            return ResponseEntity.ok(walletService.withdraw(userId, amount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam BigDecimal amount) {
        try {
            walletService.transferFunds(fromUserId, toUserId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<WalletHistory>> getWalletHistoryByUserIdAndDateRange(
            @PathVariable Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<WalletHistory> history = walletService.getWalletHistoryByUserIdAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(history);
    }

}