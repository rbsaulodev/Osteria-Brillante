CREATE TABLE reservations (
    id UUID PRIMARY KEY,

    customer_id UUID NOT NULL,
    table_id UUID NOT NULL,

    reservation_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    party_size INTEGER NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_reservation_customer
        FOREIGN KEY (customer_id)
        REFERENCES users (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_reservation_table
        FOREIGN KEY (table_id)
        REFERENCES restaurant_tables (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_reservation_time ON reservations (reservation_time);
CREATE INDEX idx_reservation_table ON reservations (table_id);
CREATE INDEX idx_reservation_customer ON reservations (customer_id);