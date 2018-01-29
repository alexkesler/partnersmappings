package ru.rooxtest.partnersmappings.controller.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;
import ru.rooxtest.partnersmappings.dto.CustomerDto;
import ru.rooxtest.partnersmappings.dto.PartnerMappingDto;
import ru.rooxtest.partnersmappings.storage.AvatarStorage;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;

@Component
public class Transformer {
    private static final Logger log = LoggerFactory.getLogger(Transformer.class);

    @Autowired
    private AvatarStorage avatarStorage;

    public CustomerDto transform(Customer customer) {
        if (customer==null) return null;
        CustomerDto customerDto = new CustomerDto();

        customerDto.setId(customer.getId());
        customerDto.setBalance(customer.getBalance());
        customerDto.setLogin(customer.getLogin());
        customerDto.setPassword(customer.getPassword());
        customerDto.setActive(customer.getActive());

        return customerDto;
    }

    public List<CustomerDto> transformCustomers(Collection<Customer> customers) {
        return customers.stream().map(customer -> transform(customer)).collect(Collectors.toList());
    }

    public PartnerMappingDto transform(PartnerMapping partnerMapping) {
        if (partnerMapping==null) return null;
        PartnerMappingDto partnerMappingDto = new PartnerMappingDto();

        partnerMappingDto.setId(partnerMapping.getId());
        partnerMappingDto.setCustomerId(partnerMapping.getCustomerId());
        partnerMappingDto.setPartnerId(partnerMapping.getPartnerId());
        partnerMappingDto.setAccountId(partnerMapping.getAccountId());
        partnerMappingDto.setFio(partnerMapping.getFio());
        partnerMappingDto.setAvatar(avatarStorage.readAvatar(partnerMapping.getId()));

        return partnerMappingDto;
    }

    public List<PartnerMappingDto> transformPartnerMappngs(Collection<PartnerMapping> partnerMappings) {
        return partnerMappings.stream().map(partnerMapping -> transform(partnerMapping)).collect(Collectors.toList());
    }


    public PartnerMapping transform(PartnerMappingDto partnerMappingDto) {
        if (partnerMappingDto==null) return null;
        PartnerMapping partnerMapping = new PartnerMapping();

        partnerMapping.setId(partnerMappingDto.getId());
        partnerMapping.setCustomerId(partnerMappingDto.getCustomerId());
        partnerMapping.setPartnerId(partnerMappingDto.getPartnerId());
        partnerMapping.setAccountId(partnerMappingDto.getAccountId());
        partnerMapping.setFio(partnerMappingDto.getFio());
        avatarStorage.writeAvatar(partnerMapping.getId(), partnerMappingDto.getAvatar());

        return partnerMapping;
    }

    public List<PartnerMapping> transformPartnerMappingDtos(Collection<PartnerMappingDto> partnerMappingDtos) {
        return partnerMappingDtos.stream().map(partnerMappingDto -> transform(partnerMappingDto)).collect(Collectors.toList());
    }




}
