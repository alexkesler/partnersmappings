package ru.rooxtest.partnersmappings.repository;

import org.springframework.stereotype.Repository;
import ru.rooxtest.partnersmappings.domain.PartnerMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * Репозиторий для CRUD операций с привязками аккаунта
 */
@Repository
public class PartnerMappingRepository {

    @PersistenceContext
    private EntityManager em;

    public List<PartnerMapping> findByCustomerId(long customerId) {
        TypedQuery<PartnerMapping> query = em.createQuery("select pm from PartnerMapping pm where pm.customerId=:customerId", PartnerMapping.class);
        query.setParameter("customerId", customerId);
        List<PartnerMapping> partnerMappings = query.getResultList();
        return partnerMappings;
    }

    public void save(PartnerMapping partnerMapping) {
        em.persist(partnerMapping);
    }

    public void remove(long id) {
        PartnerMapping partnerMapping = em.find(PartnerMapping.class, id);
        if (partnerMapping!=null)
            em.remove(partnerMapping);
    }


}
