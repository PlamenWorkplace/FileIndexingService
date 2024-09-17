package main.java.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileWrapper {

    private final File file;
    private final List<String> tokens = new ArrayList<>();

    public FileWrapper(File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void addTokens(List<String> tokens) {
        this.tokens.addAll(tokens);
    }

    public boolean containsToken(String token) {
        return this.tokens.contains(token);
    }

}
