package ru.rooxtest.partnersmappings.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rooxtest.partnersmappings.domain.Customer;
import ru.rooxtest.partnersmappings.repository.CustomerRepository;

import java.util.ArrayList;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByLogin(login);
        if (customer==null) throw new UsernameNotFoundException("User with login '" + login + "' not found");
        User user = new User(login,
                customer.getPassword(),
                customer.getActive(),
                true,
                true,
                true,
                new ArrayList<SimpleGrantedAuthority>());
        return user;
    }
}
