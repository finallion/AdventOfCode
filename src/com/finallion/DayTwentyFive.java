package com.finallion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DayTwentyFive implements Day {

    @Override
    public void partOne() {
        readFile();
    }

    @Override
    public void partTwo() {

    }

    private String snafuAddition(String result, String number) {
        StringBuilder sumString = new StringBuilder();
        int remainder = 0;
        for (int i = 0; i < Math.max(result.length(), number.length()); i++) {
            int summandOne = 0;
            int summandTwo = 0;

            if (result.length() - 1 - i >= 0) {
                switch (result.charAt(result.length() - 1 - i)) {
                    case '0' -> summandOne = 0;
                    case '1' -> summandOne = 1;
                    case '2' -> summandOne = 2;
                    case '-' -> summandOne = -1;
                    case '=' -> summandOne = -2;
                }
            }

            if (number.length() - 1 - i >= 0) {
                switch (number.charAt(number.length() -1 - i)) {
                    case '0' -> summandTwo = 0;
                    case '1' -> summandTwo = 1;
                    case '2' -> summandTwo = 2;
                    case '-' -> summandTwo = -1;
                    case '=' -> summandTwo = -2;
                }
            }

            int sum = summandOne + summandTwo + remainder;
            // edge case where sum exceeds snafu bounds -2 to 2 -> sum becomes two numbers, one is appended
            // second is saved as remainder for the next round
            switch (sum) {
                case 3 -> {
                    sum = -2;
                    remainder = 1;
                }
                case 4 -> {
                    sum = -1;
                    remainder = 1;
                }
                case 5 -> {
                    sum = 0;
                    remainder = 1;
                }
                case -3 -> {
                    sum = 2;
                    remainder = -1;
                }
                case -4 -> {
                    sum = 1;
                    remainder = -1;
                }
                case -5 -> {
                    sum = 0;
                    remainder = -1;
                }
                default -> remainder = 0;
            }

            switch (sum) {
                case -2 -> sumString.append("=");
                case -1 -> sumString.append("-");
                case 0 -> sumString.append("0");
                case 1 -> sumString.append("1");
                case 2 -> sumString.append("2");
            }

            // append last remainder
            if (i == Math.max(result.length(), number.length()) - 1 && remainder != 0) {
                sumString.append(remainder);
            }


        }
        return sumString.reverse().toString();
    }

    // test method to calculate decimal from snafu string
    private long calculateSNAFU(String snafuString) {
        long result = 0;
        long power = 0;
        for (int i = snafuString.length() - 1; i >= 0; i--) {
            long snafuNumber = 0;
            switch (snafuString.charAt(i)) {
                case '1' -> snafuNumber = 1;
                case '2' -> snafuNumber = 2;
                case '0' -> snafuNumber = 0;
                case '-' -> snafuNumber = -1;
                case '=' -> snafuNumber = -2;
            }
            result += snafuNumber * Math.pow(5, power);

            power++;
        }

        return result;
    }

    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("TwentyFive")));

            String result = lines.get(0);
            for (int i = 1; i < lines.size(); i++) {
                result = snafuAddition(result, lines.get(i));
            }

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
