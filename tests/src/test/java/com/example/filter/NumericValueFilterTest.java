package com.example.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.filter.numeric.NumericValueFilter;
import com.example.filter.numeric.NumericValueFilterType;
import com.example.filter.shared.NumericValueFilterTestEntity;
import com.example.filter.shared.NumericValueFilterTestEntityFilters;
import com.example.filter.shared.NumericValueFilterTestRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class NumericValueFilterTest {

    @Autowired
    private NumericValueFilterTestRepository repository;


    @BeforeAll
    public void createFixture() {
        repository.saveAll(
            List.of(
                new NumericValueFilterTestEntity(1L, 1),
                new NumericValueFilterTestEntity(2L, 2),
                new NumericValueFilterTestEntity(3L, 3),
                new NumericValueFilterTestEntity(4L, 4),
                new NumericValueFilterTestEntity(5L, 5),
                new NumericValueFilterTestEntity(6L, 6),
                new NumericValueFilterTestEntity(7L, 7),
                new NumericValueFilterTestEntity(8L, 8),
                new NumericValueFilterTestEntity(9L, 9),
                new NumericValueFilterTestEntity(10L, 10)
            )
        );
    }

    private void test_value(
        NumericValueFilter<Integer> filter,
        Integer... expectedValues
    ) {
        final var filters = NumericValueFilterTestEntityFilters.builder()
            .integerValue(filter)
            .build();
        final var specification = filters.intoSpecification();

        final var foundRecords = repository.findAll(specification);

        assertEquals(expectedValues.length, foundRecords.size());
        final boolean allExpectedValuesFound = Stream.of(expectedValues)
            .allMatch(expectedValue -> {
                return foundRecords
                    .stream()
                    .filter(record -> record.getIntegerValue().equals(expectedValue))
                    .findAny()
                    .isPresent();
            });
        assertTrue(allExpectedValuesFound);
    }

    @Test
    public void valueLess() {
        test_value(
            new NumericValueFilter<Integer>(NumericValueFilterType.LESS, 4, null),
            1, 2, 3
        );
    }

    @Test
    public void valueLessEqual() {
        test_value(
            new NumericValueFilter<Integer>(NumericValueFilterType.LESS_EQUAL, 5, null),
            1, 2, 3, 4, 5
        );
    }

    @Test
    public void valueEqual() {
        test_value(
            new NumericValueFilter<Integer>(NumericValueFilterType.EQUAL, 10, null),
            10
        );
    }

    @Test
    public void valueGreater() {
        test_value(
            new NumericValueFilter<Integer>(NumericValueFilterType.GREATER, 1, null),
            2, 3, 4, 5, 6, 7, 8, 9, 10
        );
    }

    @Test
    public void valueGreaterEqual() {
        test_value(
            new NumericValueFilter<Integer>(NumericValueFilterType.GREATER_EQUAL, 5, null),
            5, 6, 7, 8, 9, 10
        );
    }

    @Test
    public void valueBetween() {
        test_value(
            new NumericValueFilter<Integer>(NumericValueFilterType.BETWEEN, 2, 4),
            2, 3, 4
        );
    }

}
