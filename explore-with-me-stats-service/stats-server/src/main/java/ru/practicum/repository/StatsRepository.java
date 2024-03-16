package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.entity.HitEntity;

public interface StatsRepository extends JpaRepository<HitEntity, Long>, JpaSpecificationExecutor<HitEntity> {
}
