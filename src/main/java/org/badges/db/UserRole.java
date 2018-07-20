package org.badges.db;

import lombok.Data;
import org.badges.db.converter.PermissionConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

@Data
@Entity
public class UserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = PermissionConverter.class)
    private Set<UserPermission> userPermissions;

    @Override
    public Long getId() {
        return id;
    }

}
