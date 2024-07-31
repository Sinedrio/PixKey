package com.example.pixkeymanager.validation;

import com.example.pixkeymanager.entity.PixKey;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@SpringBootApplication
public class PixKeyValidate implements  Validate{


    @Override
    public void validatePhoneNumber(String phoneNumber) throws Exception {
        // +[codigo][ddd][numero]
        String regex = "\\+\\d{1,2}\\d{2,3}\\d{9}";
        if (!Pattern.matches(regex, phoneNumber)) {
            throw new Exception("Número de celular inválido");
        }
    }

    @Override
    public void validateEmail(String email) throws Exception {
        String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,7}$";
        if (!Pattern.matches(regex, email) || email.length() > 77) {
            throw new Exception("Email inválido");
        }
    }

    @Override
    public void validateCpf(String cpf) throws Exception {
        String regex = "^[0-9]{11}$";
        if (!Pattern.matches(regex, cpf) || !isValidCpf(cpf)) {
            throw new Exception("CPF inválido");
        }
    }

    @Override
    public void validateCnpj(String cnpj) throws Exception {
        String regex = "^[0-9]{14}$";
        if (!Pattern.matches(regex, cnpj) || !isValidCnpj(cnpj)) {
            throw new Exception("CNPJ inválido");
        }
    }

    @Override
    public void validateRandomKey(String randomKey) throws Exception {
        if (randomKey.length() > 36) {
            throw new Exception("Chave aleatória inválida");
        }
    }

    @Override
    public void validateAccountDetails(PixKey pixKey) throws Exception {

        List<Supplier<Optional<Exception>>> validators = List.of(
                () -> pixKey.getAccountType() == null ||
                        (!pixKey.getAccountType().equalsIgnoreCase("corrente") && !pixKey.getAccountType().equalsIgnoreCase("poupança"))
                        ? Optional.of(new Exception("Tipo de conta inválido"))
                        : Optional.empty(),
                () -> pixKey.getAgencyNumber() == null ||
                        !pixKey.getAgencyNumber().matches("[0-9]{1,4}")
                        ? Optional.of(new Exception("Número de agência inválido"))
                        : Optional.empty(),
                () -> pixKey.getAccountNumber() == null ||
                        !pixKey.getAccountNumber().matches("[0-9]{1,8}")
                        ? Optional.of(new Exception("Número de conta inválido"))
                        : Optional.empty(),
                () -> pixKey.getFirstName() == null ||
                        pixKey.getFirstName().isEmpty()
                        ? Optional.of(new Exception("Nome do correntista obrigatório"))
                        : Optional.empty(),
                () -> pixKey.getLastName() == null ||
                        pixKey.getLastName().isEmpty()
                        ? Optional.of(new Exception("Sobrenome do correntista obrigatório"))
                        : Optional.empty()
        );

        for (Supplier<Optional<Exception>> validator : validators) {
            Optional<Exception> validationResult = validator.get();
            if (validationResult.isPresent()) {
                throw validationResult.get();
            }
        }
    }

    @Override
    public boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) {
            firstDigit = 0;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) {
            secondDigit = 0;
        }

        return cpf.charAt(9) - '0' == firstDigit && cpf.charAt(10) - '0' == secondDigit;
    }

    @Override
    public boolean isValidCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            return false;
        }

        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (cnpj.charAt(i) - '0') * weight1[i];
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) {
            firstDigit = 0;
        }

        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += (cnpj.charAt(i) - '0') * weight2[i];
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) {
            secondDigit = 0;
        }

        return cnpj.charAt(12) - '0' == firstDigit && cnpj.charAt(13) - '0' == secondDigit;
    }
}