package com.example.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates class that allows filtering @Entity using
 * Spring Boot Specification API.
 * 
 * For any T class TFilters will be generated.
 * 
 * Relies on hibernate-jpamodelgen to generate accessors to @Entity fields.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GenerateFilters {}
