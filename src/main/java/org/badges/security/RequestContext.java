package org.badges.security;

import org.badges.db.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequestContext {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();


    public User getCurrentUser() {
        return Optional.ofNullable(currentUser.get())
                .orElse(new User().setId(1L));
    }

    void setCurrentEmplyee(User user) {
        currentUser.set(user);
    }

    void clear() {
        currentUser.remove();
    }

}
