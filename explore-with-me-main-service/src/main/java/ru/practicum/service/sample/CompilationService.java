package ru.practicum.service.sample;

import ru.practicum.model.entity.Compilation;

import java.util.Collection;

public interface CompilationService {

    Collection<Compilation> getCompilationsByFilters(Boolean pinned, Integer from, Integer size);

    Compilation getCompilationById(Long compId);

    Compilation addCompilation(Compilation compilation);

    Compilation updateCompilation(Compilation compilation);

    void removeCompilationById(Long compId);
}
