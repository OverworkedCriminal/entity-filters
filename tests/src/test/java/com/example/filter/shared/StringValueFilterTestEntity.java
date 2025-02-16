package com.example.filter.shared;

import com.example.filter.GenerateFilters;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@GenerateFilters
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StringValueFilterTestEntity {
    @Id
    private Long id;
    private String stringValue;
}
