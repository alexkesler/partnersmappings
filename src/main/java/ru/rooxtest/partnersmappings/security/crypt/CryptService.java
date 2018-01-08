package ru.rooxtest.partnersmappings.security.crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rooxtest.partnersmappings.controller.support.CustomerNotFoundException;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.security.Token;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;

/**
 * Простая служба для расшифровки токена
 */
public class CryptService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
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
        if (!encryptedToken.matches("Bearer\\d+"))
            throw new DecryptException("Auth string is wrong");
        String idString = encryptedToken.substring(6);
        long id = Integer.parseInt(idString);
        Customer customer = partnersMappingsService.findCustomerById(id);
        if (customer==null) throw new CustomerNotFoundException();
        Token token = new Token(customer.getLogin(),customer.getPassword());
        return token;
    }
}
