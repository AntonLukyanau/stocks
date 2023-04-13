package com.alukyanau.nysestocks;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "XM test task",
                version = "v1",
                description = """
                        Recommendation service for the New York Stock
                        Exchange (NYSE) that reads all the prices from the CSV files, calculates
                        the oldest/newest/min/max for each stock for the whole month. Also allow
                        users to compare normalized ranges and get information about specific stocks.
                        _________________________________________________________________________________
                        NYSE -              New-York Stock Exchange.
                        Normalized data -   stock data which calculated as (maxPrice - minPrice)/minPrice
                        """,
                contact = @Contact(
                        name = "Anton Lukyanau",
                        email = "anton_lukyanau1@epam.com"))
)
public class StocksApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocksApplication.class, args);
    }

}
