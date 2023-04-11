CREATE TABLE IF NOT EXISTS request_to_nyse
(
    id               BIGSERIAL PRIMARY KEY,
    at_date          DATE NOT NULL,
    company_param    VARCHAR(255),
    frequency_param  VARCHAR(255),
    start_date_param DATE,
    end_date_param   DATE
);

CREATE TABLE IF NOT EXISTS stock_data
(
    id               BIGSERIAL PRIMARY KEY,
    request_id       BIGINT         NOT NULL,
    company_code     VARCHAR(255)   NOT NULL,
    at_date          DATE           NOT NULL,
    start_price      NUMERIC(15, 2) NOT NULL,
    max_price        NUMERIC(15, 2) NOT NULL,
    min_price        NUMERIC(15, 2) NOT NULL,
    end_price        NUMERIC(15, 2) NOT NULL,
    volume           BIGINT         NOT NULL,
    result_frequency VARCHAR(255)   NOT NULL,
    CONSTRAINT stock_data_request_to_nyse_fk
        FOREIGN KEY (request_id) REFERENCES request_to_nyse (id)
);

