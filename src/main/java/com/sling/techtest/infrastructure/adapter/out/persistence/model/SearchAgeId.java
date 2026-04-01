package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import java.io.Serializable;
import java.util.Objects;

public class SearchAgeId implements Serializable {

    private String search;
    private Integer ageOrder;
    private Integer age;

    public SearchAgeId() {}

    public SearchAgeId(String search, Integer ageOrder, Integer age) {
        this.search = search;
        this.ageOrder = ageOrder;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchAgeId that = (SearchAgeId) o;
        return Objects.equals(search, that.search) &&
               Objects.equals(ageOrder, that.ageOrder) &&
               Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(search, ageOrder, age);
    }
}
