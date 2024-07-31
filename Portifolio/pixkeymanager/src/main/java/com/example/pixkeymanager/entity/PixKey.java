package com.example.pixkeymanager.entity;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@RequiredArgsConstructor
@Document(collection = "pixKeys")
public class PixKey {
    @Id
    private String id;
    private String keyType;
    private String keyValue;
    private String accountType;
    private String agencyNumber;
    private String accountNumber;
    private LocalDateTime createdAt;
    private LocalDateTime inactivatedAt;
    private String firstName;
    private String lastName;
}
