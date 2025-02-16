package com.example.filter.shared;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("")
public class ValidationTestController {

    @PostMapping("/numeric-value-filter")
    public void postNumericValueFilter(@Valid @RequestBody NumericValueFilterTestEntityFilters filters) {}

    @PostMapping("/string-value-filter")
    public void postStringValueFilter(@Valid @RequestBody StringValueFilterTestEntityFilters filters) {}

}
