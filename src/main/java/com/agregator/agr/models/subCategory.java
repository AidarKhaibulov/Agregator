package com.agregator.agr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sub_categories")
public class subCategory {
    @Id
    private short id;
    private String name;
    @OneToMany(mappedBy = "subCategory",cascade = CascadeType.REFRESH)
    private Set<Category> categories=new HashSet<>();
}
