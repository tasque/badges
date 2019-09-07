package org.badges.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badges.db.User;
import org.badges.db.repository.UserRepository;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class LdapUserDetailsContextMapper implements UserDetailsContextMapper {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        Object mail = ctx.getObjectAttribute("mail");
        log.debug("User mail is {}", mail);
        User currentUser = userRepository.findByEmail(mail.toString());

        if (currentUser == null && "iFuture".equalsIgnoreCase(ctx.getObjectAttribute("company").toString())) {
            log.warn("going to create new user " + ctx);
            currentUser = userRepository.save(new User()
                    .setTitle(ctx.getObjectAttribute("title").toString())
                    .setEmail(mail.toString())
                    .setName(ctx.getObjectAttribute("displayname").toString())
                    .setEnabled(true)
                    .setImageUrl("https://png.pngtree.com/svg/20170914/d78a96cd9e.png"));
        }
        if (currentUser == null || currentUser.isDisabled()) {
            log.warn("Username {}, Context {}, User {}", username, ctx, currentUser);
            throw new RuntimeException("User disabled");
        }
        List<GrantedAuthority> permissions = Collections.emptyList();
        if (!CollectionUtils.isEmpty(currentUser.getUserRoles())) {
            permissions = currentUser.getUserRoles().stream().flatMap(userRole -> userRole.getUserPermissions().stream())
                    .map(Enum::name)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return new UserPrincipal(currentUser, username, "", permissions);
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        // do nothing
    }
}
