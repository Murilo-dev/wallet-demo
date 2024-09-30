package com.demo.wallet.service;

import com.demo.wallet.model.Wallet;
import com.demo.wallet.model.WalletHistory;
import com.demo.wallet.model.WalletUser;
import com.demo.wallet.repository.WalletHistoryRepository;
import com.demo.wallet.repository.WalletRepository;
import com.demo.wallet.repository.WalletUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletUserRepository walletUserRepository;

    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    public Wallet createWallet(Long userId) {
        WalletUser user = walletUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = new Wallet(user);
        wallet.setBalance(BigDecimal.valueOf(0));

        Wallet walletReturn = walletRepository.save(wallet);

        // Log history
        String description = "User " + wallet.getUser().getId() + " created a new account.";
        createWalletHistory(wallet, wallet.getUser(), "account_creation", wallet.getBalance(), description);

        return walletReturn;
    }

    public Optional<Wallet> getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    public Wallet deposit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user"));
        wallet.deposit(amount);

        Wallet walletReturn = walletRepository.save(wallet);

        // Log history
        String description = "User " + wallet.getUser().getId() + " deposited " + amount;
        createWalletHistory(wallet, wallet.getUser(), "deposit", amount, description);

        return walletReturn;
    }

    public Wallet withdraw(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        Wallet walletReturn = walletRepository.save(wallet);

        // Log history
        String description = "User " + wallet.getUser().getId() + " withdrew " + amount;
        createWalletHistory(wallet, wallet.getUser(), "withdraw", amount, description);

        return walletReturn;
    }

    public void transferFunds(Long fromUserId, Long toUserId, BigDecimal amount) {
        // Retrieve wallets of both users
        Wallet fromWallet = walletRepository.findByUserId(fromUserId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found for user ID: " + fromUserId));
        Wallet toWallet = walletRepository.findByUserId(toUserId)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found for user ID: " + toUserId));

        // Check if the source wallet has enough balance
        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in source wallet");
        }

        // Deduct the amount from the sender's wallet
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));

        // Add the amount to the recipient's wallet
        toWallet.setBalance(toWallet.getBalance().add(amount));

        // Save both wallets
        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        // Log history
        String description = "User " + fromUserId + " transferred " + amount + " to user " + toUserId;
        createWalletHistory(fromWallet, fromWallet.getUser(), "transfer", amount, description);
    }

    private void createWalletHistory(Wallet wallet, WalletUser user, String transactionType, BigDecimal amount, String description) {
        WalletHistory history = new WalletHistory(wallet, user, transactionType, amount, description);
        walletHistoryRepository.save(history);
    }

    public List<WalletHistory> getWalletHistoryByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found for user ID: " + userId));

        // Log history
        String description = "User " + userId + " requested historical balance from " + startDate + " to " + endDate;
        createWalletHistory(wallet, wallet.getUser(), "history", BigDecimal.valueOf(0), description);

        return walletHistoryRepository.findByUserIdAndTimestampBetween(userId, startDate, endDate);
    }

}