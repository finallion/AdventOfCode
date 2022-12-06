package com.finallion;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaySix implements Day {

    @Override
    public void partOne() {
        System.out.println(readFile(4));
    }

    @Override
    public void partTwo() {
        System.out.println(readFile(14));
    }

    public int readFile(int conseqChars) {
        try {
            List<String> lines = Files.readAllLines(Path.of(buildPath("Six")));
            Set<Character> chars = new HashSet<>();

            for (int i = 0; i < lines.get(0).length(); i++) {
                String part = lines.get(0).substring(i, i + conseqChars);

                for (Character character : part.toCharArray()) {
                    chars.add(character);
                }

                if (chars.size() == conseqChars) {
                    return i + conseqChars;
                } else {
                    chars.clear();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }
}
