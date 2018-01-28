package ru.rooxtest.partnersmappings.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rooxtest.partnersmappings.controller.support.CustomerNotFoundException;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.security.AuthorizationHolder;
import ru.rooxtest.partnersmappings.service.PartnersMappingsService;

import java.util.List;
import java.util.UUID;

/**
 * контроллер для абонентов
 */
@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private PartnersMappingsService partnersMappingsService;
    @Autowired
    private AuthorizationHolder authorizationHolder;

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
