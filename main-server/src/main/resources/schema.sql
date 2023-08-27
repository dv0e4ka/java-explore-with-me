DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;


CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY,
    email VARCHAR(255),
    name  VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS location
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY,
    lat FLOAT,
    lon FLOAT,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY,
    pinned BOOLEAN,
    title  VARCHAR(55),
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY,
    confirmed_requests BIGINT,
    views              BIGINT,
    participant_limit   INT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(5000),
    published_on        TIMESTAMP WITHOUT TIME ZONE,
    state              VARCHAR(9),
    request_moderation  BOOLEAN,
    annotation         VARCHAR(2000),
    category_id        BIGINT,
    event_date          TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT,
    location_id        BIGINT,
    title              VARCHAR(120),
    paid               BOOLEAN,
    compilation        BIGINT,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT fk_event_user FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_event_location FOREIGN KEY (location_id) REFERENCES location (id),
    CONSTRAINT fk_event_compilation FOREIGN KEY (compilation) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY,
    created   TIMESTAMP WITHOUT TIME ZONE,
    event_id  BIGINT,
    requester BIGINT,
    status    VARCHAR(15)
);


