package ru.rooxtest.partnersmappings.dto;

import java.util.UUID;

public class CustomerDto {
    private UUID id;
    private Double balance;
    private Boolean active = true;
    private String login;
    private String password;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
