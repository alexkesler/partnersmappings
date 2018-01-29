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
    Customer findCustomer(UUID id);

    List<PartnerMapping> findPartnerMappingsByCustomerId(UUID customerId);
    PartnerMapping findPartnerMapping(UUID id);
    PartnerMapping savePartnerMapping(PartnerMapping partnerMapping);
    PartnerMapping removePartnerMapping(UUID id);

}
