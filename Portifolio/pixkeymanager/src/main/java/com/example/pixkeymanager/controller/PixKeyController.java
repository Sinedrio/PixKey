package com.example.pixkeymanager.controller;

import com.example.pixkeymanager.entity.PixKey;
import com.example.pixkeymanager.service.PixKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pix-keys-v1")
public class PixKeyController {

    @Autowired
    private PixKeyService pixKeyService;

    @PostMapping
    public ResponseEntity<?> createPixKey(@RequestBody PixKey pixKey) {
        try {
            PixKey createdKey = pixKeyService.createPixKey(pixKey);
            return new ResponseEntity<>(createdKey, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePixKey(@PathVariable String id, @RequestBody PixKey updatedPixKey) {
        try {
            PixKey pixKey = pixKeyService.updatePixKey(id, updatedPixKey);
            return ResponseEntity.ok(pixKey);
        } catch (Exception e) {
            return ResponseEntity.status(422).body(e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> detelePixKey(@PathVariable String id) {
        try {
            PixKey inactivatedPixKey = pixKeyService.inactivatePixKey(id);
            return ResponseEntity.ok(inactivatedPixKey);
        } catch (Exception e) {
            return ResponseEntity.status(422).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixKey> getById(@PathVariable String id) {
        Optional<PixKey> pixKey = pixKeyService.findById(id);
        return pixKey.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/keyType/{keyType}")
    public ResponseEntity<List<PixKey>> getByKeyType(@PathVariable String keyType) {
        List<PixKey> pixKeys = pixKeyService.findByKeyType(keyType);
        if (pixKeys.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(pixKeys);
    }

    @GetMapping("/accountNumber")
    public ResponseEntity<List<PixKey>> getByAgencyAndAccount(@RequestParam String agencyNumber, @RequestParam String accountNumber) {
        List<PixKey> pixKeys = pixKeyService.findByAgencyNumberAndAccountNumber(agencyNumber, accountNumber);
        if (pixKeys.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(pixKeys);
    }
}
