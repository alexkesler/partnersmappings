package ru.rooxtest.partnersmappings.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import ru.rooxtest.partnersmappings.controller.support.CustomerNotFoundException;
import ru.rooxtest.partnersmappings.controller.support.WrongCustomerException;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * REST контроллер
 */
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private PartnersMappingsService partnersMappingsService;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity getCustomers() {
        log.info("Receive GET for all customers");
        List<Customer> customers = partnersMappingsService.findAllCustomers();
        customers.forEach((customer)->customer.setPassword(""));
        log.info("Send " + customers.size() + " customers");
        return ResponseEntity.ok(customers);
    }

    @RequestMapping(path = "/{custid}", method = RequestMethod.GET)
    ResponseEntity getCustomerById(@PathVariable String custid) {
        log.info("Receive GET for customer with id: " + custid);
        Customer customer = getCustomer(custid);
        return ResponseEntity.ok(customer);
    }


    @RequestMapping(path = "/{custid}/partnermappings", method = RequestMethod.GET)
    ResponseEntity getPartnerMappingsForCustomer(@PathVariable String custid) {
        log.info("Receive GET for all PartnerMappings for Customer: " + custid);
        Customer customer = getCustomer(custid);
        List<PartnerMapping> partnerMappings = partnersMappingsService.findPartnerMappingsByCustomerId(customer.getId());
        log.info("Send " + partnerMappings.size() + " PartnerMappings");
        return ResponseEntity.ok(partnerMappings);
    }

    @RequestMapping(path = "/{custid}/partnermappings/{pmid}", method = RequestMethod.GET)
    ResponseEntity getPartnerMappingsForCustomer(@PathVariable String custid, @PathVariable UUID pmid) {
        log.info("Receive GET for PartnerMapping for Customer: " + custid + " with id: " + pmid);
        // проверяем что пользователь правильный если указан
        getCustomer(custid);
        PartnerMapping partnerMapping = partnersMappingsService.findPartnerMapping(pmid);
        return ResponseEntity.ok(partnerMapping);
    }

    @RequestMapping(path = "/{custid}/partnermappings", method = RequestMethod.POST)
    ResponseEntity createPartnerMapping(@PathVariable String custid, @RequestBody PartnerMapping partnerMapping) {
        log.info("Receive POST with PartnerMapping: " + partnerMapping);
        Customer customer = getCustomer(custid);
        partnerMapping.setCustomerId(customer.getId());
        partnersMappingsService.savePartnerMapping(partnerMapping);
        URI location = URI.create("/" + custid + "/partnermappings/" + partnerMapping.getId());
        return ResponseEntity.created(location).body(partnerMapping);
    }


    @RequestMapping(path = "/{custid}/partnermappings/{pmid}", method = RequestMethod.PUT)
    ResponseEntity updatePartnerMapping(@PathVariable String custid,
                                        @PathVariable UUID pmid,
                                        @RequestBody PartnerMapping partnerMapping) {
        log.info("Receive POST with PartnerMapping: " + partnerMapping);
        Customer customer = getCustomer(custid);

        partnerMapping.setId(pmid); /// устанавливаем Id из URL
        partnerMapping.setCustomerId(customer.getId());

        PartnerMapping existPartnerMapping = partnersMappingsService.findPartnerMapping(pmid);

        partnersMappingsService.savePartnerMapping(partnerMapping);

        if (existPartnerMapping != null) {
            return ResponseEntity.ok(partnerMapping);
        } else {
            URI location = URI.create("/" + custid + "/partnermappings/" + partnerMapping.getId());
            return ResponseEntity.created(location).body(partnerMapping);
        }
    }


    @RequestMapping(path = "/{custid}/partnermappings/{pmid}", method = RequestMethod.DELETE)
    ResponseEntity removePartnerMapping(@PathVariable String custid, @PathVariable UUID pmid) {
        log.info("Receive DELETE for PartnerMapping: " + pmid);
        // проверяем что пользователь правильный если указан
        getCustomer(custid);
        PartnerMapping partnerMapping = partnersMappingsService.removePartnerMapping(pmid);
        return ResponseEntity.ok(partnerMapping);
    }






    private Customer getCustomer(String idString) {
        if (idString==null || idString.isEmpty() || !idString.matches("@me|[0-9a-f]{8}\\-(?:[0-9a-f]{4}\\-){3}[0-9a-f]{12}"))
            throw new RuntimeException("Wrong id: " + idString);
        Customer customer = null;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!idString.equals("@me")) {
            customer = partnersMappingsService.findCustomerById(UUID.fromString(idString));
        } else {
            customer = partnersMappingsService.findCustomerByLogin(user.getUsername());
        }
        if (customer == null) throw new CustomerNotFoundException("Not found Customer with id: " + idString);
        if (!customer.getLogin().equals(user.getUsername())) throw new WrongCustomerException("PartnerMappings of customer: " + customer.getLogin() + " not permitted for current customer: " + user.getUsername());
        return customer;
    }
}
