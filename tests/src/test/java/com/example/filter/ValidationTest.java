package com.example.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.filter.shared.ValidationTestController;

@WebMvcTest(ValidationTestController.class)
public class ValidationTest {

    @Autowired
    MockMvc mvc;

    private void test_valueFilter(String path, HttpStatus expectedStatus, String body) throws Exception {
        mvc
            .perform(
                MockMvcRequestBuilders
                    .post(path)
                    .contentType("application/json")
                    .content(body)
            )
            .andExpect(
                MockMvcResultMatchers
                    .status()
                    .is(expectedStatus.value())
            );
    }

    //#region NumericValueFilter

    private void test_numericValueFilter(HttpStatus expectedStatus, String body) throws Exception {
        test_valueFilter("/numeric-value-filter", expectedStatus, body);
    }

    @Test
    public void numericValueFilter() throws Exception {
        test_numericValueFilter(
            HttpStatus.OK,
            """
            {
                "integerValue": {
                    "type": "EQUAL",
                    "v1": 5
                }
            }
            """
        );
    }

    @Test
    public void numericValueFilter_typeEmpty() throws Exception {
        test_numericValueFilter(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "v1": 5
                }
            }
            """
        );
    }

    @Test
    public void numericValueFilter_typeNull() throws Exception {
        test_numericValueFilter(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": null,
                    "v1": 5
                }
            }
            """
        );
    }

    @Test
    public void numericValueFilter_v1Empty() throws Exception {
        test_numericValueFilter(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "EQUAL"
                }
            }
            """
        );
    }

    @Test
    public void numericValueFilter_v1Null() throws Exception {
        test_numericValueFilter(
            HttpStatus.BAD_REQUEST,
            """
            {
                "integerValue": {
                    "type": "EQUAL",
                    "v1": null
                }
            }
            """
        );
    }

    //#endregion

    //#region StringValueFilter

    private void test_stringValueFilter(HttpStatus expectedStatus, String body) throws Exception {
        test_valueFilter("/string-value-filter", expectedStatus, body);
    }

    @Test
    public void stringValueFilter() throws Exception {
        test_stringValueFilter(
            HttpStatus.OK,
            """
            {
                "stringValue": {
                    "type": "EQUAL",
                    "v": "text"
                }
            }
            """
        );
    }

    @Test
    public void stringValueFilter_typeEmpty() throws Exception {
        test_stringValueFilter(
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
    public void stringValueFilter_typeNull() throws Exception {
        test_stringValueFilter(
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

    @Test
    public void stringValueFilter_vEmpty() throws Exception {
        test_stringValueFilter(
            HttpStatus.BAD_REQUEST,
            """
            {
                "stringValue": {
                    "type": "EQUAL"
                }
            }
            """
        );
    }

    @Test
    public void stringValueFilter_vNull() throws Exception {
        test_stringValueFilter(
            HttpStatus.BAD_REQUEST,
            """
            {
                "stringValue": {
                    "type": "EQUAL",
                    "v": null
                }
            }
            """
        );
    }
    
    //#endregion
}
