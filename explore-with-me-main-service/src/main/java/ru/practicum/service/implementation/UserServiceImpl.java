package ru.practicum.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.model.entity.User;
import ru.practicum.repository.jpaRepository.UserRepository;
import ru.practicum.repository.specificaton.UserSpec;
import ru.practicum.service.sample.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public User addUser(User user) {
        return repo.save(user);
    }

    @Override
    public Collection<User> getUsers(Collection<Long> ids, Integer from, Integer size) {
        Specification<User> spec = Specification.where(UserSpec.idIn(ids));
        return repo.findAll(spec, PageRequest.of(from / size, size, Sort.by("id"))).getContent();
    }

    @Override
    public void deleteUser(Long userId) {
        repo.deleteById(userId);
    }
}
