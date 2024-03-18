package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.EventRequestStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {

    @NotNull
    @NotEmpty
    private List<Integer> requestIds;

    @NotNull
    private EventRequestStatus status;
}
