package com.example.filter;

import jakarta.validation.constraints.NotNull;
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
public class NumericValueFilter<T> {

    /**
     * Specifies what comparison should be used
     */
    @NotNull
    private NumericValueFilterType type;

    /**
     * Value used in all comparisons.
     * In "between" comparison used as lower value.
     */
    @NotNull
    private T v1;

    /**
     * Value used only in "between" comparison as greater value.
     */
    private T v2;
}
