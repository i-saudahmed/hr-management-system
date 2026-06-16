create TABLE department
(
    id        BIGSERIAL PRIMARY KEY,
    name      varchar(100) NOT NULL UNIQUE,
    status    BOOLEAN      NOT NULL DEFAULT true,
    created_at TIMESTAMP    NOT NULL ,
    updated_at TIMESTAMP
)