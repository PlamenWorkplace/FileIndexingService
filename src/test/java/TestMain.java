package test.java;

import main.java.component.MainFrame;

import java.io.File;
import java.net.URL;

public class TestMain {

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        URL url = TestMain.class.getClassLoader().getResource("test/resources");
        if (url == null) {
            System.out.println("File not found");
            return;
        }
        frame.placeFilesInPane(new File(url.getFile()));
        frame.getWordField().setText("Lorem");
    }

}
