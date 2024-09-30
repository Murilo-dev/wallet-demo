package com.demo.wallet.repository;

import com.demo.wallet.model.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Long> {

    List<WalletHistory> findByWalletId(Long walletId);

    List<WalletHistory> findByUserId(Long userId);

    List<WalletHistory> findByUserIdAndTimestampBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}