package ru.rooxtest.partnersmappings.domain;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;
import java.util.UUID;

/**
 * Суперкласс
 */
@MappedSuperclass
public abstract class AbstractEntity {

    // Используем UUID, основанное на времени вместо рандомного
    // для лучшей производительности БД
    private static final transient TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    @Id
    @Column(length = 16)
    protected UUID id = uuidGenerator.generate();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }


    // сравнение и hashCode только по id так как id уникальны
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
