package ru.rooxtest.partnersmappings.security.crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rooxtest.partnersmappings.controller.support.CustomerNotFoundException;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.security.Token;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;

import java.util.UUID;

/**
 * Простая служба для расшифровки токена
 */
public class CryptService {
    private static final Logger log = LoggerFactory.getLogger(CryptService.class);
    @Autowired
    private PartnersMappingsService partnersMappingsService;

    /**
     * Расшифровывает информацию о пользователе, содержащуюся в токене аутентификации
     * и загружает другие данные
     *
     * @param encryptedToken зашифрованный токен аутентификации
     * @return
     * @throws Exception
     */
    public Token decryptToken(String encryptedToken) throws Exception {
        log.info("Processing token: " + encryptedToken);
        if (!encryptedToken.matches("Bearer[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12}"))
            throw new DecryptException("Auth string is wrong");
        String idString = encryptedToken.substring(6);
        UUID id = UUID.fromString(idString);
        Customer customer = partnersMappingsService.findCustomerById(id);
        if (customer==null) throw new CustomerNotFoundException();
        Token token = new Token(customer.getLogin(),customer.getPassword());
        return token;
    }

}
