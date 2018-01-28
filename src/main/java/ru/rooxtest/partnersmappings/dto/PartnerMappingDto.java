package ru.rooxtest.partnersmappings.dto;

import java.util.UUID;

public class PartnerMappingDto {
    private UUID id;
    private UUID customerId;
    private String partnerId;
    private String accountId;
    private String fio;
    private byte[] avatar;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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
