package com.example.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.filter.numeric.NumericValueFilterType;
import com.example.filter.shared.ValidationTestController;

@WebMvcTest(ValidationTestController.class)
public class NumericValueFilterValidationTest {

    @Autowired
    MockMvc mvc;

    private void testValidation(HttpStatus expectedStatus, String body) throws Exception {
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/numeric-value-filter")
                    .contentType("application/json")
                    .content(body)
            )
            .andExpect(
                MockMvcResultMatchers
                    .status()
                    .is(expectedStatus.value())
            );
    }

    //#region single value comparisons

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        mode = Mode.EXCLUDE,
        names = {
            "BETWEEN",
            "IS_NULL",
            "IS_NOT_NULL"
        }
    )
    public void valid_emptyV2(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": 5
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        mode = Mode.EXCLUDE,
        names = {
            "BETWEEN",
            "IS_NULL",
            "IS_NOT_NULL"
        }
    )
    public void valid_nullV2(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": 5,
                    "v2": null
                }
            }
            """.formatted(type.toString())
        );
    }

    @Test
    public void emptyType() throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "v1": 5,
                    "v2": null
                }
            }
            """
        );
    }

    @Test
    public void nullType() throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": null,
                    "v1": 5,
                    "v2": null
                }
            }
            """
        );
    }

    //#endregion

    //#region multi value comparisons

    @Test
    public void valid_between() throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": 1,
                    "v2": 3
                }
            }
            """.formatted(NumericValueFilterType.BETWEEN)
        );
    }

    @Test
    public void between_emptyV1() throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v2": 3
                }
            }
            """.formatted(NumericValueFilterType.BETWEEN)
        );
    }

    @Test
    public void between_nullV1() throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": null,
                    "v2": 3
                }
            }
            """.formatted(NumericValueFilterType.BETWEEN)
        );
    }

    @Test
    public void between_emptyV2() throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": 1
                }
            }
            """.formatted(NumericValueFilterType.BETWEEN)
        );
    }

    @Test
    public void between_nullV2() throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": 1,
                    "v2": null
                }
            }
            """.formatted(NumericValueFilterType.BETWEEN)
        );
    }
    
    //#endregion

    //#region IS_NULL and IS_NOT_NULL

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void valid_nulls(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "%s"
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void valid_nulls_nullV1(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": null
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void valid_nulls_nullV2(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v2": null
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void nulls_notNullV1(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v1": 5
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = NumericValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void nulls_notNullV2(NumericValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "%s",
                    "v2": 5
                }
            }
            """.formatted(type.toString())
        );
    }
    
    //#endregion
}
