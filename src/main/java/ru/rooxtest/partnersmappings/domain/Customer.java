package ru.rooxtest.partnersmappings.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Сущность Абонент
 */
@Entity
public class Customer extends AbstractEntity {

    @Column
    private String fio;
    @Column
    private Double balance;

    @Column
    private Boolean active = true;

    @Column(length = 127, unique = true, nullable = false)
    private String login;

    @Column(length = 127)
    private String password;



    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


}
