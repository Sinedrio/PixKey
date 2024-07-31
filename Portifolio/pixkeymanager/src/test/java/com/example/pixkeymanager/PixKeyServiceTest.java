package com.example.pixkeymanager;

import com.example.pixkeymanager.entity.PixKey;
import com.example.pixkeymanager.repository.PixKeyRepository;
import com.example.pixkeymanager.service.PixKeyService;
import com.example.pixkeymanager.validation.PixKeyValidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PixKeyServiceTest {

    @InjectMocks
    private PixKeyService pixKeyService;

    @Mock
    private PixKeyRepository pixKeyRepository;

    @Mock
    private PixKeyValidate pixKeyValidate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePixKey() throws Exception {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("email");
        pixKey.setKeyValue("test@example.com");
        pixKey.setAccountType("corrente");
        pixKey.setAgencyNumber("1234");
        pixKey.setAccountNumber("12345678");
        pixKey.setFirstName("John");
        pixKey.setLastName("Doe");

        when(pixKeyRepository.existsByKeyValue(anyString())).thenReturn(false);
        when(pixKeyRepository.findByAccountNumber(anyString())).thenReturn(List.of());
        when(pixKeyRepository.save(any(PixKey.class))).thenReturn(pixKey);

        PixKey createdPixKey = pixKeyService.createPixKey(pixKey);
        assertNotNull(createdPixKey);
        assertEquals("test@example.com", createdPixKey.getKeyValue());

        verify(pixKeyRepository, times(1)).save(any(PixKey.class));
    }

    @Test
    void testCreatePixKey_KeyAlreadyExists() {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("email");
        pixKey.setKeyValue("test@example.com");

        when(pixKeyRepository.existsByKeyValue(anyString())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> pixKeyService.createPixKey(pixKey));
        assertEquals("Chave já existe", exception.getMessage());

        verify(pixKeyRepository, times(0)).save(any(PixKey.class));
    }

    @Test
    void testCreatePixKey_LimitExceeded() {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("email");
        pixKey.setKeyValue("test@example.com");
        pixKey.setAccountType("fisica");
        pixKey.setAccountNumber("12345678");

        when(pixKeyRepository.existsByKeyValue(anyString())).thenReturn(false);
        when(pixKeyRepository.findByAccountNumber(anyString())).thenReturn(List.of(new PixKey(), new PixKey(), new PixKey(), new PixKey(), new PixKey()));

        Exception exception = assertThrows(Exception.class, () -> pixKeyService.createPixKey(pixKey));
        assertEquals("Limite de chaves por conta excedido", exception.getMessage());

        verify(pixKeyRepository, times(0)).save(any(PixKey.class));
    }

    @Test
    void testUpdatePixKey() throws Exception {
        PixKey existingPixKey = new PixKey();
        existingPixKey.setId("1");
        existingPixKey.setAccountType("corrente");
        existingPixKey.setAgencyNumber("1234");
        existingPixKey.setAccountNumber("12345678");
        existingPixKey.setFirstName("John");
        existingPixKey.setLastName("Doe");

        PixKey updatedPixKey = new PixKey();
        updatedPixKey.setAccountType("poupança");
        updatedPixKey.setAgencyNumber("4321");
        updatedPixKey.setAccountNumber("87654321");
        updatedPixKey.setFirstName("Jane");
        updatedPixKey.setLastName("Smith");

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.of(existingPixKey));
        when(pixKeyRepository.save(any(PixKey.class))).thenReturn(updatedPixKey);

        PixKey result = pixKeyService.updatePixKey("1", updatedPixKey);
        assertNotNull(result);
        assertEquals("poupança", result.getAccountType());
        assertEquals("4321", result.getAgencyNumber());
        assertEquals("87654321", result.getAccountNumber());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());

        verify(pixKeyRepository, times(1)).save(any(PixKey.class));
    }

    @Test
    void testUpdatePixKey_NotFound() {
        PixKey updatedPixKey = new PixKey();
        updatedPixKey.setAccountType("poupança");
        updatedPixKey.setAgencyNumber("4321");
        updatedPixKey.setAccountNumber("87654321");
        updatedPixKey.setFirstName("Jane");
        updatedPixKey.setLastName("Smith");

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> pixKeyService.updatePixKey("1", updatedPixKey));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());

        verify(pixKeyRepository, times(0)).save(any(PixKey.class));
    }

    @Test
    void testUpdatePixKey_Inactivated() {
        PixKey existingPixKey = new PixKey();
        existingPixKey.setId("1");
        existingPixKey.setInactivatedAt(LocalDateTime.now());

        PixKey updatedPixKey = new PixKey();
        updatedPixKey.setAccountType("poupança");
        updatedPixKey.setAgencyNumber("4321");
        updatedPixKey.setAccountNumber("87654321");
        updatedPixKey.setFirstName("Jane");
        updatedPixKey.setLastName("Smith");

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.of(existingPixKey));

        Exception exception = assertThrows(Exception.class, () -> pixKeyService.updatePixKey("1", updatedPixKey));
        assertEquals("Não é permitido alterar chaves inativadas.", exception.getMessage());

        verify(pixKeyRepository, times(0)).save(any(PixKey.class));
    }

    @Test
    void testInactivatePixKey() throws Exception {
        PixKey pixKey = new PixKey();
        pixKey.setId("1");
        pixKey.setInactivatedAt(null);

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.of(pixKey));
        when(pixKeyRepository.save(any(PixKey.class))).thenReturn(pixKey);

        PixKey result = pixKeyService.inactivatePixKey("1");
        assertNotNull(result);
        assertNotNull(result.getInactivatedAt());

        verify(pixKeyRepository, times(1)).save(any(PixKey.class));
    }

    @Test
    void testInactivatePixKey_NotFound() {
        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> pixKeyService.inactivatePixKey("1"));
        assertEquals("Chave Pix não encontrada.", exception.getMessage());

        verify(pixKeyRepository, times(0)).save(any(PixKey.class));
    }

    @Test
    void testInactivatePixKey_AlreadyInactivated() {
        PixKey pixKey = new PixKey();
        pixKey.setId("1");
        pixKey.setInactivatedAt(LocalDateTime.now());

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.of(pixKey));

        Exception exception = assertThrows(Exception.class, () -> pixKeyService.inactivatePixKey("1"));
        assertEquals("A chave Pix ja foi deletada.", exception.getMessage());

        verify(pixKeyRepository, times(0)).save(any(PixKey.class));
    }

    @Test
    void testFindById() {
        PixKey pixKey = new PixKey();
        pixKey.setId("1");

        when(pixKeyRepository.findById(anyString())).thenReturn(Optional.of(pixKey));

        Optional<PixKey> result = pixKeyService.findById("1");
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());

        verify(pixKeyRepository, times(1)).findById(anyString());
    }

    @Test
    void testFindByKeyType() {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("email");

        when(pixKeyRepository.findByKeyType(anyString())).thenReturn(List.of(pixKey));

        List<PixKey> result = pixKeyService.findByKeyType("email");
        assertFalse(result.isEmpty());
        assertEquals("email", result.get(0).getKeyType());

        verify(pixKeyRepository, times(1)).findByKeyType(anyString());
    }

    @Test
    void testFindByAgencyNumberAndAccountNumber() {
        PixKey pixKey = new PixKey();
        pixKey.setAgencyNumber("1234");
        pixKey.setAccountNumber("12345678");

        when(pixKeyRepository.findByAgencyNumberAndAccountNumber(anyString(), anyString())).thenReturn(List.of(pixKey));

        List<PixKey> result = pixKeyService.findByAgencyNumberAndAccountNumber("1234", "12345678");
        assertFalse(result.isEmpty());
        assertEquals("1234", result.get(0).getAgencyNumber());
        assertEquals("12345678", result.get(0).getAccountNumber());

        verify(pixKeyRepository, times(1)).findByAgencyNumberAndAccountNumber(anyString(), anyString());
    }

    @Test
    public void testCreatePixKey_InvalidKeyType() {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("invalidType");

        Exception exception = assertThrows(Exception.class, () -> {
            pixKeyService.createPixKey(pixKey);
        });

        assertEquals("Erro na validação da chave PIX: Tipo de chave inválido", exception.getMessage());
    }

    @Test
    public void testCreatePixKey_InvalidCpf() throws Exception {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("cpf");
        pixKey.setKeyValue("123"); // CPF inválido
        pixKey.setAccountType("corrente");
        pixKey.setAgencyNumber("1234");
        pixKey.setAccountNumber("12345678");
        pixKey.setFirstName("John");
        pixKey.setLastName("Doe");

        doThrow(new Exception("CPF inválido")).when(pixKeyValidate).validateCpf(anyString());

        Exception exception = assertThrows(Exception.class, () -> {
            pixKeyService.createPixKey(pixKey);
        });

        assertTrue(exception.getMessage().contains("CPF inválido"));
    }

    @Test
    public void testCreatePixKey_InvalidCnpj() throws Exception {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("cnpj");
        pixKey.setKeyValue("123"); // CNPJ inválido
        pixKey.setAccountType("corrente");
        pixKey.setAgencyNumber("1234");
        pixKey.setAccountNumber("12345678");
        pixKey.setFirstName("John");
        pixKey.setLastName("Doe");

        doThrow(new Exception("CNPJ inválido")).when(pixKeyValidate).validateCnpj(anyString());

        Exception exception = assertThrows(Exception.class, () -> {
            pixKeyService.createPixKey(pixKey);
        });

        assertTrue(exception.getMessage().contains("CNPJ inválido"));
    }

    @Test
    public void testCreatePixKey_InvalidRandomKey() throws Exception {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType("aleatoria");
        pixKey.setKeyValue(UUID.randomUUID().toString().replaceAll("-", "") + "extra"); // Chave aleatória inválida
        pixKey.setAccountType("corrente");
        pixKey.setAgencyNumber("1234");
        pixKey.setAccountNumber("12345678");
        pixKey.setFirstName("John");
        pixKey.setLastName("Doe");

        doThrow(new Exception("Chave aleatória inválida")).when(pixKeyValidate).validateRandomKey(anyString());

        Exception exception = assertThrows(Exception.class, () -> {
            pixKeyService.createPixKey(pixKey);
        });

        assertTrue(exception.getMessage().contains("Chave aleatória inválida"));
    }
}
