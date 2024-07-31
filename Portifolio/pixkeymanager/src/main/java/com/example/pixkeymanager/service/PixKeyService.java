package com.example.pixkeymanager.service;

import com.example.pixkeymanager.entity.PixKey;
import com.example.pixkeymanager.repository.PixKeyRepository;
import com.example.pixkeymanager.validation.PixKeyValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class PixKeyService {

    @Autowired
    private final PixKeyValidate pixKeyValidate;

    @Autowired
    private final PixKeyRepository pixKeyRepository;


    public PixKey createPixKey(PixKey pixKey) throws Exception {
        if (pixKey.getKeyType().equalsIgnoreCase("aleatoria")) {
            log.info("Chave aleatória gerada");
            pixKey.setKeyValue(generateRandomKey());
        }

        validatePixKey(pixKey);

        if (pixKeyRepository.existsByKeyValue(pixKey.getKeyValue())) {
            log.info("Chave já existe");
            throw new Exception("Chave já existe");

        }

        List<PixKey> keysByAccount = pixKeyRepository.findByAccountNumber(pixKey.getAccountNumber());
        int limit = pixKey.getAccountType().equalsIgnoreCase("juridica") ? 20 : 5;

        if (keysByAccount.size() >= limit) {
            log.info("Limite de chaves por conta excedido");
            throw new Exception("Limite de chaves por conta excedido");
        }
        pixKey.setCreatedAt(LocalDateTime.now());

        return pixKeyRepository.save(pixKey);
    }

    public PixKey updatePixKey(String id, PixKey updatedPixKey) throws Exception {
        Optional<PixKey> optionalPixKey = pixKeyRepository.findById(id);
        if (optionalPixKey.isEmpty()) {
            log.info("Chave pix não encontrada");
            throw new Exception("Chave Pix não encontrada.");
        }

        PixKey existingPixKey = optionalPixKey.get();

        if (existingPixKey.getInactivatedAt() != null) {
            log.info("Não é permitido alterar chaves inativadas");
            throw new Exception("Não é permitido alterar chaves inativadas.");
        }

        // Validar os campos alterados
        if (!updatedPixKey.getAccountType().matches("corrente|poupança")) {
            log.info("Tipo de conta inválido");
            throw new Exception("Tipo de conta inválido.");
        }
        if (!updatedPixKey.getAgencyNumber().matches("\\d{1,4}")) {
            log.info("Número da agência inválido.");
            throw new Exception("Número da agência inválido.");
        }
        if (!updatedPixKey.getAccountNumber().matches("\\d{1,8}")) {
            log.info("Número da conta inválido.");
            throw new Exception("Número da conta inválido.");
        }
        if (updatedPixKey.getFirstName().isEmpty() || updatedPixKey.getFirstName().length() > 30) {
            log.info("Nome do correntista inválido.");
            throw new Exception("Nome do correntista inválido.");
        }
        if (updatedPixKey.getLastName().length() > 45) {
            log.info("Sobrenome do correntista inválido.");
            throw new Exception("Sobrenome do correntista inválido.");
        }
        existingPixKey.setAccountType(updatedPixKey.getAccountType());
        existingPixKey.setAgencyNumber(updatedPixKey.getAgencyNumber());
        existingPixKey.setAccountNumber(updatedPixKey.getAccountNumber());
        existingPixKey.setFirstName(updatedPixKey.getFirstName());
        existingPixKey.setLastName(updatedPixKey.getLastName());
        existingPixKey.setCreatedAt(LocalDateTime.now());

        return pixKeyRepository.save(existingPixKey);
    }

    public Optional<PixKey> findById(String id) {
        return pixKeyRepository.findById(id);
    }

    public List<PixKey> findByKeyType(String keyType) {
        return pixKeyRepository.findByKeyType(keyType);
    }

    public List<PixKey> findByAgencyNumberAndAccountNumber(String agencyNumber, String accountNumber) {
        return pixKeyRepository.findByAgencyNumberAndAccountNumber(agencyNumber, accountNumber);
    }
    public PixKey inactivatePixKey(String id) throws Exception {
        Optional<PixKey> optionalPixKey = pixKeyRepository.findById(id);
        if (optionalPixKey.isEmpty()) {
            log.info("Chave Pix não encontrada.");
            throw new Exception("Chave Pix não encontrada.");
        }

        PixKey pixKey = optionalPixKey.get();
        if (pixKey.getInactivatedAt() != null) {
            log.info("A chave Pix ja foi deletada.");
            throw new Exception("A chave Pix ja foi deletada.");
        }

        pixKey.setInactivatedAt(LocalDateTime.now());
        return pixKeyRepository.save(pixKey);
    }

    private void validatePixKey(PixKey pixKey) throws Exception {
        String keyType = pixKey.getKeyType().toLowerCase();

        try {
            switch (keyType) {
                case "celular":
                    pixKeyValidate.validatePhoneNumber(pixKey.getKeyValue());
                    break;
                case "email":
                    pixKeyValidate.validateEmail(pixKey.getKeyValue());
                    break;
                case "cpf":
                    pixKeyValidate.validateCpf(pixKey.getKeyValue());
                    break;
                case "cnpj":
                    pixKeyValidate.validateCnpj(pixKey.getKeyValue());
                    break;
                case "aleatoria":
                    pixKeyValidate.validateRandomKey(pixKey.getKeyValue());
                    break;
                default:
                    throw new Exception("Tipo de chave inválido");
            }
            // Validar os detalhes da conta após validar a chave PIX
            pixKeyValidate.validateAccountDetails(pixKey);
        } catch (Exception e) {
            // Propagar a exceção
            throw new Exception("Erro na validação da chave PIX: " + e.getMessage(), e);
        }
    }

    private String generateRandomKey() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (uuid.length() > 36) {
            uuid = uuid.substring(0, 36);
        }
        return uuid;
    }
}
