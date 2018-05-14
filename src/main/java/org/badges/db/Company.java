package org.badges.db;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@Getter
@Setter
@Entity
public class Company extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private boolean autoJoin;

    @OneToOne(fetch = FetchType.LAZY)
    private Employee creator;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany
    private Set<Employee> employees;

    private String imageUrl;

    @Override
    public Long getId() {
        return id;
    }
}
