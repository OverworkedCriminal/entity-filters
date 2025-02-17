package com.example.filter.numeric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ValidateNumericValueFilter
public class NumericValueFilter<T> {

    /**
     * Specifies what comparison should be used
     */
    private NumericValueFilterType type;

    /**
     * Value used in all comparisons.
     * In BETWEEN comparison used as lower value.
     * 
     * Must be null when type is IS_NULL or IS_NOT_NULL
     */
    private T v1;

    /**
     * Value used only in BETWEEN comparison as greater value.
     * 
     * Must be null unless type is "BETWEEN"
     */
    private T v2;
}
