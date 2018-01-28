package ru.rooxtest.partnersmappings.security;

import org.springframework.stereotype.Component;
import ru.rooxtest.partnersmappings.domain.Customer;

/**
 * Вспомогательный класс для хранения
 * текущего авторизованного абонента
 */
@Component
public class AuthorizationHolder {
    private Customer customer;

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}
