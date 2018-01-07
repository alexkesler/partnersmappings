package ru.rooxtest.partnersmappings.service;

import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;

import java.util.List;

/**
 * Сервис для взаимодействия с хранилищем
 */
public interface PartnersMappingsService {

    List<Customer> findAllCustomers();
    Customer findCustomerById(long id);
    Customer findCustomerByLogin(String login);

    List<PartnerMapping> findPartnerMappingsByCustomerId(long customerId);
    void savePartnerMapping(PartnerMapping partnerMapping);
    void removePartnerMapping(long id);

}
