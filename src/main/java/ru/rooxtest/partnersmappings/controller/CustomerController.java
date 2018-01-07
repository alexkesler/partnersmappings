package ru.rooxtest.partnersmappings.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.rooxtest.partnersmappings.controller.support.WrongCustomerException;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;
import ru.rooxtest.partnersmappings.controller.support.CustomerNotFoundException;

import java.security.Principal;
import java.util.List;

/**
 * REST контроллер
 */
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PartnersMappingsService partnersMappingsService;

    @RequestMapping()
    ResponseEntity getCustomers() {
        log.info("Receive GET for all customers");
        List<Customer> customers = partnersMappingsService.findAllCustomers();
        customers.forEach((customer)->customer.setPassword(""));
        log.info("Send " + customers.size() + " customers");
        return ResponseEntity.ok(customers);
    }

    @RequestMapping(path = "/{id}")
    ResponseEntity getCustomerById(@PathVariable String id) {
        log.info("Receive GET for customer with id: " + id);
        Customer customer = getCustomer(id);
        return ResponseEntity.ok(customer);
    }


    @RequestMapping(path = "/{id}/partnermappings")
    ResponseEntity getPartnerMappingsForCustomer(@PathVariable String id) {
        log.info("Receive GET for all PartnerMappings for Customer: " + id);
        Customer customer = getCustomer(id);
        List<PartnerMapping> partnerMappings = partnersMappingsService.findPartnerMappingsByCustomerId(customer.getId());
        log.info("Send " + partnerMappings.size() + " PartnerMappings");
        return ResponseEntity.ok(partnerMappings);
    }

    @RequestMapping(path = "/{id}/partnermappings", method = RequestMethod.POST)
    ResponseEntity savePartnerMapping(@PathVariable String id, @RequestBody PartnerMapping partnerMapping) {
        log.info("Receive POST with PartnerMapping: " + partnerMapping);
        Customer customer = getCustomer(id);
        partnerMapping.setCustomerId(customer.getId());
        partnersMappingsService.savePartnerMapping(partnerMapping);
        return ResponseEntity.accepted().body(partnerMapping);
    }

    @RequestMapping(path = "/{id}/partnermappings/{pmid}", method = RequestMethod.DELETE)
    ResponseEntity removePartnerMapping(@PathVariable String id, @PathVariable long pmid) {
        log.info("Receive DELETE for PartnerMapping: " + pmid);
        getCustomer(id);
        partnersMappingsService.removePartnerMapping(pmid);
        return ResponseEntity.ok().build();
    }






    private Customer getCustomer(String id) {
        if (id==null || id.isEmpty() || !id.matches("@me|\\d+")) throw new RuntimeException("Wrong id: " + id);
        Customer customer = null;
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals("@me")) {
            customer = partnersMappingsService.findCustomerById(Long.parseLong(id));
        } else {
            customer = partnersMappingsService.findCustomerByLogin(user.getUsername());
        }
        if (customer == null) throw new CustomerNotFoundException("Not found Customer with id: " + id);
        if (!customer.getLogin().equals(user.getUsername())) throw new WrongCustomerException("PartnerMappings of customer: " + customer.getLogin() + " not permitted for current customer: " + user.getUsername());
        return customer;
    }
}
