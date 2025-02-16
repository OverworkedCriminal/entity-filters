package com.example.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.example.filter.shared.CodeGenerationTestEntity;
import com.example.filter.shared.CodeGenerationTestEntityFilters;

public class CodeGenerationTest {

    private void test_generatedField(
        String fieldName,
        Class<?> expectedClass,
        Class<?> expectedFilterClass
    ) throws NoSuchFieldException, SecurityException {
        final var field = CodeGenerationTestEntity.class.getDeclaredField(fieldName);
        assertEquals(expectedClass, field.getType());

        final var filterField = CodeGenerationTestEntityFilters.class.getDeclaredField(fieldName);
        assertEquals(expectedFilterClass, filterField.getType());
    }

    @Test
    public void fieldsGenerated_id_Long_NumericValueFilter() throws NoSuchFieldException, SecurityException {
        test_generatedField("id", Long.class, NumericValueFilter.class);
    }

    @Test
    public void fieldsGenerated_name_String_StringValueFilter() throws NoSuchFieldException, SecurityException {
        test_generatedField("name", String.class, StringValueFilter.class);
    }

    @Test
    public void fieldsGenerated_price_BigDecimal_NumericValueFilter() throws NoSuchFieldException, SecurityException {
        test_generatedField("price", BigDecimal.class, NumericValueFilter.class);
    }

    @Test
    public void fieldsGenerated_createdAt_LocalDate_NumericValueFilter() throws NoSuchFieldException, SecurityException {
        test_generatedField("createdAt", LocalDate.class, NumericValueFilter.class);
    }

    @Test
    public void fieldsGenerated_updatedAt_LocalDateTime_NumericValueFilter() throws NoSuchFieldException, SecurityException {
        test_generatedField("updatedAt", LocalDateTime.class, NumericValueFilter.class);
    }

    @Test
    public void fieldsGenerated_version_Integer_NumericValueFilter() throws NoSuchFieldException, SecurityException {
        test_generatedField("version", Integer.class, NumericValueFilter.class);
    }
}
