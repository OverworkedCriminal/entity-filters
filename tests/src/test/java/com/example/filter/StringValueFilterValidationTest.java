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

import com.example.filter.string.StringValueFilterType;
import com.example.filter.shared.ValidationTestController;

@WebMvcTest(ValidationTestController.class)
public class StringValueFilterValidationTest {

    @Autowired
    MockMvc mvc;

    private void testValidation(HttpStatus expectedStatus, String body) throws Exception {
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post("/string-value-filter")
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
        value = StringValueFilterType.class,
        mode = Mode.EXCLUDE,
        names = {
            "IS_NULL", "IS_NOT_NULL"
        }        
    )
    public void valid_singleValueComparisons(StringValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "stringValue": {
                    "type": "%s",
                    "v": "text"
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
                "stringValue": {
                    "v": "text"
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
                "stringValue": {
                    "type": null,
                    "v": "text"
                }
            }
            """
        );
    }

    //#endregion

    //#region IS_NULL and IS_NOT_NULL

    @ParameterizedTest
    @EnumSource(
        value = StringValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void valid_nulls(StringValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "stringValue": {
                    "type": "%s"
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = StringValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void valid_nulls_nullV(StringValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.OK,
            """
            {
                "stringValue": {
                    "type": "%s",
                    "v": null
                }
            }
            """.formatted(type.toString())
        );
    }

    @ParameterizedTest
    @EnumSource(
        value = StringValueFilterType.class,
        names = { "IS_NULL", "IS_NOT_NULL" }
    )
    public void nulls_notNullV(StringValueFilterType type) throws Exception {
        testValidation(
            HttpStatus.BAD_REQUEST,
            """
            {
                "stringValue": {
                    "type": "%s",
                    "v": "text"
                }
            }
            """.formatted(type.toString())
        );
    }
    
    //#endregion
}
