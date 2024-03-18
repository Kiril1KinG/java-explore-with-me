package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.repository.UserSpec;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    @Override
    public User addUser(User user) {
        return repo.save(user);
    }

    @Override
    public Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size) {
        Specification<User> spec = Specification.where(UserSpec.idIn(ids));
        return repo.findAll(spec, PageRequest.of(from / size, size, Sort.by("id").descending())).getContent();
    }

    @Override
    public void deleteUser(Long userId) {
        repo.deleteById(userId);
    }
}
