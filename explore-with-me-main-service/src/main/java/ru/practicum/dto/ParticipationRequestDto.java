package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.ParticipationStatus;

@Getter
@Setter
public class ParticipationRequestDto {

    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private ParticipationStatus status;
}
