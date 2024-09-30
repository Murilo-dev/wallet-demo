package com.demo.wallet.repository;

import com.demo.wallet.model.WalletUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletUserRepository extends JpaRepository<WalletUser, Long> {

}