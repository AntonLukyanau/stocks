package com.example.stonks.validator;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service("csvValidator")
public class CSVValidator implements Validator<String> {

    @Override
    public boolean validate(String data) {
        Scanner scanner = new Scanner(data);
        String header = scanner.nextLine();
        if (!scanner.hasNextLine() || !headerIsValid(header)) {
            return false;
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",\"");
            if (values.length != 6) {
                return false;
            }
        }
        return true;
    }

    private boolean headerIsValid(String header) {
        String[] fieldNames = header.split(",");
        return fieldNames.length == 6;
    }

}