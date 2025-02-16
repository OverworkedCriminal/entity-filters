package com.example.filter.shared;

import com.example.filter.GenerateFilters;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@GenerateFilters(
    fieldsToIgnore = {
        "ignoredField"
    }
)
public class FieldsToIgnoreTestEntity {
    @Id
    private Long id;

    /**
     * Field is used in test through reflections.
     */
    @SuppressWarnings("unused")
    private Integer ignoredField;
}
