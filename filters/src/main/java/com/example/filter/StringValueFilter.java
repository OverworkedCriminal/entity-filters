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
public class StringValueFilter {

    /**
     * Specifies what comparison should be used
     */
    @NotNull
    private StringValueFilterType type;

    /**
     * Value used in comparisons
     */
    @NotNull
    private String v;
}
