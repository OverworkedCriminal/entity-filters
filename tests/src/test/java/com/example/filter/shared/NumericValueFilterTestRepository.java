package com.example.filter.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NumericValueFilterTestRepository
extends
    JpaRepository<NumericValueFilterTestEntity, Long>,
    JpaSpecificationExecutor<NumericValueFilterTestEntity>
{}
