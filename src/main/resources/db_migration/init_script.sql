CREATE SEQUENCE IF NOT EXISTS stock_data_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE TABLE stock_data
(
    id           BIGINT DEFAULT nextval('stock_data_id_seq')PRIMARY KEY,
    company_code VARCHAR(255) UNIQUE NOT NULL,
    company_name VARCHAR(255),
    at_date      DATE                NOT NULL,
    start_price  NUMERIC(15, 2)      NOT NULL,
    max_price    NUMERIC(15, 2)      NOT NULL,
    min_price    NUMERIC(15, 2)      NOT NULL,
    end_price    NUMERIC(15, 2)      NOT NULL,
    volume       BIGINT              NOT NULL
);
