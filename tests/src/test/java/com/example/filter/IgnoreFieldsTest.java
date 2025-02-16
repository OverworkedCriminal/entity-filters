package com.example.filter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.filter.shared.FieldsToIgnoreTestEntity;
import com.example.filter.shared.FieldsToIgnoreTestEntityFilters;

public class IgnoreFieldsTest {

    @Test
    public void ignoredFieldWasIgnored() throws NoSuchFieldException, SecurityException {
        final var FIELD_NAME = "ignoredField";

        // Make sure field exist on Entity
        FieldsToIgnoreTestEntity.class.getDeclaredField(FIELD_NAME);

        assertThrows(NoSuchFieldException.class, () -> {
            // Make sure field does not exist on EntityFilters
            FieldsToIgnoreTestEntityFilters.class.getDeclaredField(FIELD_NAME);
        });
    }
}
