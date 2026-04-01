package com.sling.techtest.infrastructure.adapter.out.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "search_ages")
@IdClass(SearchAgeId.class)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Data
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

}
