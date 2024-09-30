package com.demo.wallet.service;

import com.demo.wallet.model.WalletUser;
import com.demo.wallet.repository.WalletUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletUserService {

    @Autowired
    private WalletUserRepository walletUserRepository;

    public WalletUser createUser(WalletUser walletUser) {
        return walletUserRepository.save(walletUser);
    }

    public Optional<WalletUser> getWalletUser(Long id) {
        return walletUserRepository.findById(id);
    }

    public WalletUser updateWalletUser(Long id, WalletUser walletUserDetails) {
        return walletUserRepository.findById(id).map(walletUser -> {
            walletUser.setName(walletUserDetails.getName());
            return walletUserRepository.save(walletUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteWalletUser(Long id) {
        walletUserRepository.deleteById(id);
    }
}
