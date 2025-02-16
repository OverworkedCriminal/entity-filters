package com.example.filter;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * It's necessary to introduce spring boot context for tests
 * environment since there's no application within this module.
 * Without it using @DataJpaTest does not work.
 */
@SpringBootApplication
public class TestConfig {}
