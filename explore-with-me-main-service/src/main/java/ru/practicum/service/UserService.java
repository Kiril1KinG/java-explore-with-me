package ru.practicum.service;

import ru.practicum.model.User;

import java.util.Collection;

public interface UserService {

    User addUser(User user);

    Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
