package com.finallion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayThree implements Day {

    @Override
    public void partOne() {
        System.out.println("The result is: " + readFile());
    }

    @Override
    public void partTwo() {
        //System.out.println("The result is: " + readFilePartTwo());
        System.out.println("The result is: " + partTwoSecondAttempt());
    }

    public int readFile() {
        File file = new File(this.buildPath("Three"));
        int priority = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String firstCompartment = line.substring(0, line.length() / 2);
                String secondCompartment = line.substring(line.length() / 2);

                for (int i = 0; i < firstCompartment.length(); i++) {
                    for (int ii = 0; ii < secondCompartment.length(); ii++) {
                        if (firstCompartment.charAt(i) == secondCompartment.charAt(ii)) {
                            char match = firstCompartment.charAt(i);
                            if (Character.isUpperCase(match)) {
                                priority += match - 38;
                            } else {
                                priority += match - 96;
                            }

                            ii = secondCompartment.length() - 1;
                            i = firstCompartment.length() - 1;
                        }
                    }
                }
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return priority;
    }

    public int partTwoSecondAttempt() {
        File file = new File(this.buildPath("Three"));
        Set<String> uniqueChars = new HashSet<>();

        int result = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < 3; i++) {
                    String line = scanner.nextLine();
                    // collects unique chars from each string
                    for (int ii = 0; ii < line.length(); ii++) {
                        uniqueChars.add(line.charAt(ii) + "");
                    }

                    stringBuilder.append(String.join("", uniqueChars));
                    uniqueChars.clear();
                }
                result += stringBuilder.toString().chars().mapToObj(c -> (char) c) // char stream
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // count duplicates
                        .entrySet().stream().filter(s -> s.getValue() == 3) // find three-duplicates
                        .map(Map.Entry::getKey).map(i -> i >= 'a' && i <= 'z' ? i - 'a' + 1 : i - 'A' + 1 + 26) // convert char to corresponding int value
                        .mapToInt(c -> (int) c).sum(); // sum them up
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int readFilePartTwo() {
        File file = new File(this.buildPath("Three"));
        int priority = 0;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Pattern pattern = Pattern.compile("([a-zA-Z])\\1\\1+");

            StringBuilder group = new StringBuilder();
            String line;
            int threeCounter = 0;
            Set<String> uniqueChars = new HashSet<>();
            while ((line = bufferedReader.readLine()) != null) {
                // collects unique chars from each string
                for (int i = 0; i < line.length(); i++) {
                    uniqueChars.add(line.charAt(i) + "");
                }

                // create string
                String uniqueString = String.join("", uniqueChars);
                uniqueChars.clear();

                // collect three strings into one group
                group.append(uniqueString);
                threeCounter++;

                if (threeCounter % 3 == 0) {
                    // sort characters in string and match three consecutive chars with regex
                    char[] chars = group.toString().toCharArray();
                    Arrays.sort(chars);

                    String groupString = new String(chars);
                    Matcher matcher = pattern.matcher(groupString);
                    if (matcher.find()) {
                        char match = matcher.group().charAt(0);
                        if (Character.isUpperCase(match)) {
                            priority += match - 38;
                        } else {
                            priority += match - 96;
                        }
                    }

                    group.delete(0, group.length());
                }

            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return priority;
    }
}
