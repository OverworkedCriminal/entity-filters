package com.example.filter.string;

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
@ValidateStringValueFilter
public class StringValueFilter {

    /**
     * Specifies what comparison should be used
     */
    private StringValueFilterType type;

    /**
     * Value used in comparisons.
     * 
     * Must be null when type is IS_NULL or IS_NOT_NULL
     */
    private String v;
}
