package com.example.filter.shared;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.filter.GenerateFilters;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GenerateFilters
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenerationTestEntity {
    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private Integer version;
}
