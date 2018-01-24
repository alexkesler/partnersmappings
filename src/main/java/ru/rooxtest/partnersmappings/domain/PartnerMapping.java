package ru.rooxtest.partnersmappings.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

/**
 * Сущность привязка аккаунта
 */
@Entity
public class PartnerMapping  extends AbstractEntity {

    @Column(nullable = false)
    private UUID customerId;

    @Column(length = 63)
    private String partnerId;

    @Column(length = 127)
    private String accountId;

    @Column(length = 127)
    private String fio;

    @Column(length = 100000)
    private byte[] avatar;


    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }

    public byte[] getAvatar() { return avatar; }
    public void setAvatar(byte[] avatar) { this.avatar = avatar; }



}
