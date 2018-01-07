package ru.rooxtest.partnersmappings.repository;

import org.springframework.stereotype.Repository;
import ru.rooxtest.partnersmappings.domain.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Репозиторий для абонентов
 */
@Repository
public class CustomerRepository {

    @PersistenceContext
    private EntityManager em;


    public List<Customer> findAll() {
        TypedQuery<Customer> query = em.createQuery("select c from Customer c", Customer.class);
        List<Customer> customers = query.getResultList();
        return customers;
    }

    public Customer findById(long id) {
        Customer customer = em.find(Customer.class, id);
        return customer;
    }

    public Customer findByLogin(String login) {
        TypedQuery<Customer> query = em.createQuery("select c from Customer c where c.login=:login", Customer.class);
        query.setParameter("login", login);
        Customer customer = null;
        List<Customer> customers = query.getResultList();
        if (customers.size() > 0)
            customer = customers.get(0);
        return customer;
    }

}
