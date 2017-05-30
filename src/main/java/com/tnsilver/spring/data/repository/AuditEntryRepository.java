package com.tnsilver.spring.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tnsilver.spring.domain.AuditEntry;

@Repository("auditEntryRepository")
public interface AuditEntryRepository extends CrudRepository<AuditEntry, Long> {
}
