package org.badges.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "newsType"})
@Where(clause = "deleted=false")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "news_type")
    private NewsType newsType;

    @Column(name = "entity_id")
    private Long entityId;

    private String comment;

    @ManyToOne
    private User author;

    private String tags;

    @BatchSize(size = 20)
    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_news",
            joinColumns = {@JoinColumn(name = "news_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> toUsers = Collections.emptySet();

    private boolean deleted;

    @Column(name = "create_date")
    private Date createDate;

//    @Any(metaDef = "BaseEntity", metaColumn = @Column(name = "news_type"), fetch = FetchType.LAZY)
//    @JoinColumn(name = "entity_id")
//    private BaseEntity entity;

    private String arg0;

    private String arg1;

    private String arg2;

}
