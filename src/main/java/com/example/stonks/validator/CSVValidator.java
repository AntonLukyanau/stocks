package com.example.stonks.validator;

import com.example.stonks.util.NYSEConstants;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service("csvValidator")
public class CSVValidator implements Validator<String> {

    @Override
    public boolean validate(String data) {
        if (data == null || data.isBlank()) {
            return false;
        }
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
        return NYSEConstants.CSV_HEADER.equals(header);
    }

}
