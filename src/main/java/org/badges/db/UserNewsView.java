package org.badges.db;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@EqualsAndHashCode(of = {""})
@ToString
@Entity
public class UserNewsView {

    private Long id;
    private Long userId;
    private Long entityId;
    private UserViewEventType eventType;

}
