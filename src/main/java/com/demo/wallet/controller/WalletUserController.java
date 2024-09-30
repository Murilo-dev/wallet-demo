package com.demo.wallet.controller;


import com.demo.wallet.model.WalletUser;
import com.demo.wallet.service.WalletUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class WalletUserController {

    @Autowired
    private WalletUserService walletUserService;

    @PostMapping
    public ResponseEntity<WalletUser> createUser(@RequestBody WalletUser walletUser) {
        return ResponseEntity.ok(walletUserService.createUser(walletUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletUser> getWalletUser(@PathVariable Long id) {
        Optional<WalletUser> user = walletUserService.getWalletUser(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WalletUser> updateUser(@PathVariable Long id, @RequestBody WalletUser walletUserDetails) {
        return ResponseEntity.ok(walletUserService.updateWalletUser(id, walletUserDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        walletUserService.deleteWalletUser(id);
        return ResponseEntity.noContent().build();
    }
}