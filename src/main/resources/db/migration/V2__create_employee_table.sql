create TABLE employee
(
    id              BIGSERIAL PRIMARY KEY,
    first_name      varchar(100) NOT NULL,
    last_name       varchar(100) NOT NULL,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password        VARCHAR(255) not null,
    role            varchar(30)  not null,
    status          varchar(30)  not null DEFAULT 'ACTIVE',
    employment_type varchar(30),
    join_date       DATE,
    department_id   BIGINT references department (id),
    manager_id      BIGINT REFERENCES employee (id),
    created_at      TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP
)