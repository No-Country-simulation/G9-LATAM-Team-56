package com.finance_ia.api.repository;

import com.finance_ia.api.model.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    // Hereda métodos CRUD automáticos como save(), saveAll(), findAll(), etc.
}