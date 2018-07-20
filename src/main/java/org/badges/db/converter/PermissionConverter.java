package org.badges.db.converter;

import org.badges.db.UserPermission;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class PermissionConverter implements AttributeConverter<Set<UserPermission>, String> {
    @Override
    public String convertToDatabaseColumn(Set<UserPermission> attribute) {
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<UserPermission> convertToEntityAttribute(String dbData) {
        return Stream.of(dbData.split(","))
                .map(UserPermission::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(UserPermission.class)));
    }
}
