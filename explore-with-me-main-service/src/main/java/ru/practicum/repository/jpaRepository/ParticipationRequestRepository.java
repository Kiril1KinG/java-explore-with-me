package ru.practicum.repository.jpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.model.entity.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long>, JpaSpecificationExecutor<ParticipationRequest> {
}
