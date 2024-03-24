package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.mapper.UserMapper;
import ru.practicum.service.implementation.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminUserController {

    private final UserServiceImpl service;
    private final UserMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("POST admin/users");
        return mapper.toUserDto(service.addUser(mapper.toUser(newUserRequest)));
    }

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                        @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        return service.getUsers(ids, from, size).stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        service.deleteUser(userId);
    }
}
