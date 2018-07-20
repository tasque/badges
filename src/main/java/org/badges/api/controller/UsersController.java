package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.db.User;
import org.badges.db.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> search(@RequestParam(defaultValue = "") String name) {
        return userRepository.findByNameContainingIgnoreCaseAndEnabledIsTrue(name);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") long id){
        return userRepository.getOne(id);
    }
}
