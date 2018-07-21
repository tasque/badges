package org.badges.db;

import lombok.Data;
import org.badges.db.converter.UserPermissionsAttributeConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;

@Data
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = UserPermissionsAttributeConverter.class)
    private Set<UserPermission> userPermissions;

}
