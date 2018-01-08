package ru.rooxtest.partnersmappings.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * Сущность Абонент
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue // strategy по умолчанию GenerationType.AUTO обеспечивает создание сквозных идентификаторов
    private long id;

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


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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

    // сравнение и hashCode только по id так как id уникальны
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
