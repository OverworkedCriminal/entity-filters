package com.example.filter.shared;

import com.example.filter.GenerateFilters;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@GenerateFilters
@NoArgsConstructor
@AllArgsConstructor
public class NumericValueFilterTestEntity {
    @Id
    private Long id;
    private Integer integerValue;
}
