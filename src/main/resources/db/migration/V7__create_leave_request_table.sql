CREATE TABLE leave_request (
                               id                  BIGSERIAL PRIMARY KEY,
                               employee_id         BIGINT NOT NULL REFERENCES employee(id),
                               leave_type          VARCHAR(20) NOT NULL,
                               start_date          DATE NOT NULL,
                               end_date            DATE NOT NULL,
                               total_days          INT NOT NULL,
                               reason              VARCHAR(500),
                               status              VARCHAR(40) NOT NULL DEFAULT 'PENDING_MANAGER_APPROVAL',
                               manager_approved_by BIGINT REFERENCES employee(id),
                               manager_approved_at TIMESTAMP,
                               hr_approved_by      BIGINT REFERENCES employee(id),
                               hr_approved_at      TIMESTAMP,
                               rejection_reason    VARCHAR(500),
                               created_at          TIMESTAMP NOT NULL DEFAULT now(),
                               updated_at          TIMESTAMP
);

