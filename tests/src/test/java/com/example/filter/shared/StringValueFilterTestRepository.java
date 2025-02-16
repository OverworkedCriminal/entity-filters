package com.example.filter.shared;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StringValueFilterTestRepository
extends
    JpaRepository<StringValueFilterTestEntity, Long>,
    JpaSpecificationExecutor<StringValueFilterTestEntity> 
{}
