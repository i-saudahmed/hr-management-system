CREATE TABLE leave_balance (
                               id              BIGSERIAL PRIMARY KEY,
                               employee_id      BIGINT NOT NULL REFERENCES employee(id),
                               leave_type       VARCHAR(20) NOT NULL,
                               year             INT NOT NULL,
                               total_allotted   INT NOT NULL,
                               used             INT NOT NULL DEFAULT 0,
                               UNIQUE (employee_id, leave_type, year)
);