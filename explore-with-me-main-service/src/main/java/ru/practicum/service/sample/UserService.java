package ru.practicum.service.sample;

import ru.practicum.model.entity.User;

import java.util.Collection;

public interface UserService {

    User addUser(User user);

    Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
