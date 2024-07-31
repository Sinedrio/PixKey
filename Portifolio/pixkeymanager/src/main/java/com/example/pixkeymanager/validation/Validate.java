package com.example.pixkeymanager.validation;

import com.example.pixkeymanager.entity.PixKey;

public interface Validate {

    void validatePhoneNumber(String phoneNumber) throws Exception;

    void validateEmail(String email) throws Exception;

    void validateCpf(String cpf) throws Exception;

    void validateCnpj(String cnpj) throws Exception;

    void validateRandomKey(String randomKey) throws Exception;

    void validateAccountDetails(PixKey pixKey) throws Exception;

    boolean isValidCpf(String cpf);

    boolean isValidCnpj(String cnpj);


}
