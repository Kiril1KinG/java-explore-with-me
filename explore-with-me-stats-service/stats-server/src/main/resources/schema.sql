create table hits (
    id int generated by default as identity,
    app varchar() not null,
    uri varchar() not null,
    ip varchar() not null,
    timestamp timestamp not null
);