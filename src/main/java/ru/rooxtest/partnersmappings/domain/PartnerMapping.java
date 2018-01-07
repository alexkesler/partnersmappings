package ru.rooxtest.partnersmappings.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Сущность привязка аккаунта
 */
@Entity
public class PartnerMapping {
    @Id
    @GeneratedValue() // strategy по умолчанию GenerationType.AUTO обеспечивает создание сквозных идентификаторов
    private long id;

    @Column(nullable = false)
    private long customerId;

    @Column(length = 63)
    private String partnerId;

    @Column(length = 127)
    private String accountId;

    @Column(length = 127)
    private String fio;

    @Column(length = 100000)
    private byte[] avatar;


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }

    public byte[] getAvatar() { return avatar; }
    public void setAvatar(byte[] avatar) { this.avatar = avatar; }

    // сравнение и hashCode только по id так как id уникальны
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartnerMapping that = (PartnerMapping) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
