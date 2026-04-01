package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SearchAgeId implements Serializable {

    private String search;
    private Integer ageOrder;
    private Integer age;

}
