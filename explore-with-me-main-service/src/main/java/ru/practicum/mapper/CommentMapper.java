package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.model.entity.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    LocalDateTime ldt = LocalDateTime.now();

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "event.id", source = "eventId")
    Comment toComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    @Mapping(target = "createdOn", expression = "java(comment.getCreatedOn().format(FORMATTER))")
    CommentDto toCommentDto(Comment comment);
}
