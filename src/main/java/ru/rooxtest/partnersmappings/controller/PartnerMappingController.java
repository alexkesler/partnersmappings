package ru.rooxtest.partnersmappings.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rooxtest.partnersmappings.controller.support.CustomerNotFoundException;
import ru.rooxtest.partnersmappings.controller.support.Transformer;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;
import ru.rooxtest.partnersmappings.dto.PartnerMappingDto;
import ru.rooxtest.partnersmappings.security.AuthorizationHolder;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Контроллер для привязок
 */
@RestController
@RequestMapping(path = "/customers")
public class PartnerMappingController {
    private static final Logger log = LoggerFactory.getLogger(PartnerMappingController.class);


    @Autowired
    private PartnersMappingsService partnersMappingsService;
    @Autowired
    private Transformer transformer;
    @Autowired
    private AuthorizationHolder authorizationHolder;

    @RequestMapping(path = "/{custid}/partnermappings", method = RequestMethod.GET)
    ResponseEntity getPartnerMappingsForCustomer(@PathVariable String custid) {
        log.info("Receive GET for all PartnerMappings for Customer: " + custid);
        Customer customer = getCustomer(custid);
        List<PartnerMapping> partnerMappings = partnersMappingsService.findPartnerMappingsByCustomerId(customer.getId());
        List<PartnerMappingDto> partnerMappingDtos = transformer.transformPartnerMappngs(partnerMappings);
        log.info("Send " + partnerMappingDtos.size() + " PartnerMappings");
        return ResponseEntity.ok(partnerMappingDtos);
    }

    @RequestMapping(path = "/{custid}/partnermappings/{pmid}", method = RequestMethod.GET)
    ResponseEntity getPartnerMappingsForCustomer(@PathVariable String custid, @PathVariable UUID pmid) {
        log.info("Receive GET for PartnerMapping for Customer: " + custid + " with id: " + pmid);
        // проверяем что пользователь правильный если указан
        getCustomer(custid);
        PartnerMapping partnerMapping = partnersMappingsService.findPartnerMapping(pmid);
        PartnerMappingDto partnerMappingDto = transformer.transform(partnerMapping);
        return ResponseEntity.ok(partnerMappingDto);
    }

    @RequestMapping(path = "/{custid}/partnermappings", method = RequestMethod.POST)
    ResponseEntity createPartnerMapping(@PathVariable String custid, @RequestBody PartnerMappingDto partnerMappingDto) {
        log.info("Receive POST with PartnerMapping: " + partnerMappingDto);
        Customer customer = getCustomer(custid);
        PartnerMapping partnerMapping = transformer.transform(partnerMappingDto);
        // Устанавливаем код абонента из URL а не тот, что был (или не был) указан
        partnerMapping.setCustomerId(customer.getId());
        PartnerMapping savedPartnerMapping = partnersMappingsService.savePartnerMapping(partnerMapping);
        PartnerMappingDto savedPartnerMappingDto = transformer.transform(savedPartnerMapping);
        URI location = URI.create("/" + custid + "/partnermappings/" + savedPartnerMappingDto.getId());
        return ResponseEntity.created(location).body(savedPartnerMappingDto);
    }


    @RequestMapping(path = "/{custid}/partnermappings/{pmid}", method = RequestMethod.PUT)
    ResponseEntity updatePartnerMapping(@PathVariable String custid,
                                        @PathVariable UUID pmid,
                                        @RequestBody PartnerMappingDto partnerMappingDto) {
        log.info("Receive PUT with PartnerMapping: " + partnerMappingDto);
        Customer customer = getCustomer(custid);

        PartnerMapping partnerMapping = transformer.transform(partnerMappingDto);
        // устанавливаем Id из URL
        partnerMapping.setId(pmid);
        // Устанавливаем код абонента из URL а не тот, что был (или не был) указан
        partnerMapping.setCustomerId(customer.getId());

        PartnerMapping existPartnerMapping = partnersMappingsService.findPartnerMapping(pmid);

        PartnerMapping savedPartnerMapping = partnersMappingsService.savePartnerMapping(partnerMapping);
        PartnerMappingDto savedPartnerMappingDto = transformer.transform(savedPartnerMapping);

        if (existPartnerMapping != null) {
            return ResponseEntity.ok(savedPartnerMappingDto);
        } else {
            URI location = URI.create("/" + custid + "/partnermappings/" + savedPartnerMappingDto.getId());
            return ResponseEntity.created(location).body(savedPartnerMappingDto);
        }
    }


    @RequestMapping(path = "/{custid}/partnermappings/{pmid}", method = RequestMethod.DELETE)
    ResponseEntity removePartnerMapping(@PathVariable String custid, @PathVariable UUID pmid) {
        log.info("Receive DELETE for PartnerMapping: " + pmid);
        // проверяем что пользователь правильный если указан
        getCustomer(custid);
        PartnerMapping partnerMapping = partnersMappingsService.removePartnerMapping(pmid);
        if (partnerMapping==null)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(transformer.transform(partnerMapping));
    }


    private Customer getCustomer(String idString) {
        if (idString==null || idString.isEmpty() || !idString.matches("@me|[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12}"))
            throw new CustomerNotFoundException("Wrong id: " + idString);
        Customer customer = null;
        if (idString.equals("@me")) {
            customer = authorizationHolder.getCustomer();
        } else {
            UUID customerId = UUID.fromString(idString);
            customer = partnersMappingsService.findCustomer(customerId);
        }
        if (customer == null) throw new CustomerNotFoundException("Not found Customer with id: " + idString);
        return customer;
    }

}
