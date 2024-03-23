package ru.practicum.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Location {

    @Column(name = "latitude")
    private Float lat;

    @Column(name = "lontitude")
    private Float lon;
}
