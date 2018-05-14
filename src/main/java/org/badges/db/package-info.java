@AnyMetaDef(
        name = "BaseEntity",
        metaType = "string",
        idType = "long",
        metaValues = {
                @MetaValue(value = "BADGE_ASSIGNMENT", targetEntity = BadgeAssignment.class),
        }
)
package org.badges.db;

import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;