package org.badges.it;


import com.google.common.collect.Sets;
import org.badges.db.User;
import org.badges.db.UserPermission;
import org.badges.db.UserRole;
import org.badges.db.repository.UserRepository;
import org.badges.db.repository.UserRoleRepository;
import org.badges.service.UserRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRoleTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Test
    public void test() {
        Set<UserRole> userRoles = Sets.newHashSet(
                userRoleRepository.save(new UserRole().setUserPermissions(Sets.newHashSet(UserPermission.READ_CAMPAIGN,
                        UserPermission.DELETE_BADGE))),
                userRoleRepository.save(new UserRole().setUserPermissions(Sets.newHashSet(UserPermission.UPDATE_USER_ROLE))));
        User user = userRepository.save(userRepository.findAll().get(0).setUserRoles(userRoles));

        assertThat(userRoleService.hasPermission(user, UserPermission.READ_CAMPAIGN), is(true));
    }
}
