package main.java.tokenization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCustomTokenizationAlgorithm implements TokenizationAlgorithm {

    @Override
    public List<String> tokenize(File file) {
        List<String> tokens = new ArrayList<>();
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File does not exist");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern wordPattern = Pattern.compile("\\b[a-zA-Z]+\\b");

            while ((line = reader.readLine()) != null) {
                Matcher matcher = wordPattern.matcher(line);
                while (matcher.find()) {
                    tokens.add(matcher.group());
                }
            }
        } catch (IOException e) {
            // TODO: log exception
        }

        return tokens;
    }

}
