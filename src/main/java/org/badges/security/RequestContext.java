package org.badges.security;

import lombok.RequiredArgsConstructor;
import org.badges.db.User;
import org.badges.db.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestContext {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((UserPrincipal) authentication.getPrincipal()).getId();
        return userRepository.getOne(id);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserPrincipal) authentication.getPrincipal()).getId();

    }


}
