package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.enumiration.AdminEventUpdateStateAction;
import ru.practicum.model.enumiration.EventState;
import ru.practicum.model.enumiration.UserUpdateEventStateAction;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id")
    private Category category;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Embedded
    private Location location;

    @Column(name = "req_moderation")
    private Boolean requestModeration;

    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;

    @Transient
    private Long views = 0L;

    @Transient
    private Integer confirmedRequests = 0;

    @Transient
    private List<Comment> comments;

    @Transient
    private UserUpdateEventStateAction stateAction;

    @Transient
    private AdminEventUpdateStateAction adminStateAction;
}
