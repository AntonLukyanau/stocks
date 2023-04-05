CREATE SEQUENCE IF NOT EXISTS request_to_nyse_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS request_to_nyse
(
    id               BIGINT DEFAULT nextval('request_to_nyse_id_seq') PRIMARY KEY,
    at_date          DATE NOT NULL,
    company_param    VARCHAR(255),
    frequency_param  VARCHAR(255),
    start_date_param DATE,
    end_date_param   DATE
);

CREATE SEQUENCE IF NOT EXISTS stock_data_id_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS stock_data
(
    id           BIGINT DEFAULT nextval('stock_data_id_seq') PRIMARY KEY,
    company_code VARCHAR(255) UNIQUE NOT NULL,
    at_date      DATE                NOT NULL,
    start_price  NUMERIC(15, 2)      NOT NULL,
    max_price    NUMERIC(15, 2)      NOT NULL,
    min_price    NUMERIC(15, 2)      NOT NULL,
    end_price    NUMERIC(15, 2)      NOT NULL,
    volume       BIGINT              NOT NULL
);

