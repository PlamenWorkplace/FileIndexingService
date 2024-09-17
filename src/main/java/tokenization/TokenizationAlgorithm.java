package main.java.tokenization;

import java.io.File;
import java.util.List;

public interface TokenizationAlgorithm {

    List<String> tokenize(File File);

}
