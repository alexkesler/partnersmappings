package ru.rooxtest.partnersmappings.service;

import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для взаимодействия с хранилищем
 */
public interface PartnersMappingsService {

    List<Customer> findAllCustomers();
    Customer findCustomerById(UUID id);
    Customer findCustomerByLogin(String login);

    List<PartnerMapping> findPartnerMappingsByCustomerId(UUID customerId);
    void savePartnerMapping(PartnerMapping partnerMapping);
    void removePartnerMapping(UUID id);

}
