package com.example.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.filter.shared.StringValueFilterTestEntity;
import com.example.filter.shared.StringValueFilterTestEntityFilters;
import com.example.filter.shared.StringValueFilterTestRepository;
import com.example.filter.string.StringValueFilter;
import com.example.filter.string.StringValueFilterType;

@DataJpaTest
public class StringValueFilterTest {

    @Autowired
    private StringValueFilterTestRepository repository;

    //#region comparisons

    @Test
    public void valueEqual() {
        final var value = "I will be looking for this value";
        repository.saveAll(
            List.of(
                new StringValueFilterTestEntity(1L, "This should not be found"),
                new StringValueFilterTestEntity(2L, value),
                new StringValueFilterTestEntity(3L, "This also should not be found")
            )
        );
        final var filters = StringValueFilterTestEntityFilters.builder()
            .stringValue(new StringValueFilter(StringValueFilterType.EQUAL, value))
            .build();
        final var specification = filters.intoSpecification();

        final var foundRecords = repository.findAll(specification);

        assertEquals(1, foundRecords.size());
        final var foundRecord = foundRecords.getFirst();
        assertEquals(value, foundRecord.getStringValue());
    }

    @Test
    public void valueEqualIgnoreCase() {
        final var value = "I will be looking for this value";
        final var searchedValue = "I WiLl bE LoOkInG FoR ThIs vAlUe";
        assertTrue(value.equalsIgnoreCase(searchedValue));

        repository.saveAll(
            List.of(
                new StringValueFilterTestEntity(1L, "This should not be found"),
                new StringValueFilterTestEntity(2L, value),
                new StringValueFilterTestEntity(3L, "This also should not be found")
            )
        );
        final var filters = StringValueFilterTestEntityFilters.builder()
            .stringValue(new StringValueFilter(StringValueFilterType.EQUAL_IGNORE_CASE, searchedValue))
            .build();
        final var specification = filters.intoSpecification();

        final var foundRecords = repository.findAll(specification);

        assertEquals(1, foundRecords.size());
        final var foundRecord = foundRecords.getFirst();
        assertEquals(value, foundRecord.getStringValue());
    }

    //#endregion

    //#region

    @Test
    public void valueNull() {
        repository.saveAll(
            List.of(
                new StringValueFilterTestEntity(1L, "This should not be found"),
                new StringValueFilterTestEntity(2L, null),
                new StringValueFilterTestEntity(3L, "This also should not be found")
            )
        );
        
        final var filters = StringValueFilterTestEntityFilters.builder()
            .stringValue(new StringValueFilter(StringValueFilterType.IS_NULL, null))
            .build();
        final var specification = filters.intoSpecification();

        final var foundRecords = repository.findAll(specification);
        assertEquals(1, foundRecords.size());

        final var allContainNullValue = foundRecords
            .stream()
            .allMatch(record -> record.getStringValue() == null);

        assertTrue(allContainNullValue);
    }

    @Test
    public void valueNotNull() {
        repository.saveAll(
            List.of(
                new StringValueFilterTestEntity(1L, "This should be found"),
                new StringValueFilterTestEntity(2L, null),
                new StringValueFilterTestEntity(3L, "This also should be found")
            )
        );
        
        final var filters = StringValueFilterTestEntityFilters.builder()
            .stringValue(new StringValueFilter(StringValueFilterType.IS_NOT_NULL, null))
            .build();
        final var specification = filters.intoSpecification();

        final var foundRecords = repository.findAll(specification);
        assertEquals(2, foundRecords.size());

        final var allContainNotNullValue = foundRecords
            .stream()
            .allMatch(record -> record.getStringValue() != null);

        assertTrue(allContainNotNullValue);
    }

    //#endregion
}
