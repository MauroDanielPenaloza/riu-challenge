package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import jakarta.persistence.*;

@Entity
@Table(name = "search_ages")
@IdClass(SearchAgeId.class)
public class SearchAge {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_id")
    private SearchEntity search;

    @Id
    @Column(name = "age_order")
    private Integer ageOrder;

    @Id
    @Column(name = "age")
    private Integer age;

    public SearchAge() {
    }

    public SearchAge(SearchEntity search, Integer ageOrder, Integer age) {
        this.search = search;
        this.ageOrder = ageOrder;
        this.age = age;
    }

    public SearchEntity getSearch() {
        return search;
    }

    public void setSearch(SearchEntity search) {
        this.search = search;
    }

    public Integer getAgeOrder() {
        return ageOrder;
    }

    public void setAgeOrder(Integer ageOrder) {
        this.ageOrder = ageOrder;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
