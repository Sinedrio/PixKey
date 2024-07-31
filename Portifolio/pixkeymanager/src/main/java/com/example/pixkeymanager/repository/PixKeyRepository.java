package com.example.pixkeymanager.repository;

import com.example.pixkeymanager.entity.PixKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PixKeyRepository extends MongoRepository<PixKey, String> {
    boolean existsByKeyValue(String keyValue);
    List<PixKey> findByAccountNumber(String accountNumber);
    List<PixKey> findByKeyType(String keyType);
    List<PixKey> findByAgencyNumberAndAccountNumber(String agencyNumber, String accountNumber);
}

