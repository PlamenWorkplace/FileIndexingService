package main.java.component;

import main.java.model.FileWrapper;
import main.java.tokenization.MyCustomTokenizationAlgorithm;
import main.java.tokenization.TokenizationAlgorithm;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private final TokenizationAlgorithm algorithm;
    private final java.util.List<FileWrapper> fileList;
    private final JTextField wordField;
    private final DefaultListModel<String> listModel;

    public MainFrame() {
        this.algorithm = new MyCustomTokenizationAlgorithm();
        this.fileList = new ArrayList<>();

        setEnvironment();

        // Create MenuItem
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem selectDirItem = new JMenuItem("Open...");
        selectDirItem.addActionListener(this::openActionPerformed);
        fileMenu.add(selectDirItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.PAGE_AXIS));
        verticalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        verticalPanel.setOpaque(false);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.setOpaque(false);
        wordField = new JTextField();
        wordField.setMaximumSize(new Dimension(200, wordField.getPreferredSize().height));
        wordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilePanel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilePanel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilePanel();
            }
        });
        searchPanel.add(new JLabel("Enter word to search:  "));
        searchPanel.add(wordField);
        searchPanel.add(Box.createVerticalGlue());

        // Add vertical glue to push the searchPanel to the center
        verticalPanel.add(Box.createVerticalGlue());
        verticalPanel.add(searchPanel);
        verticalPanel.add(Box.createVerticalGlue());
        add(verticalPanel);

        // Create the list for displaying files
        listModel = new DefaultListModel<>();
        JList<String> fileList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(fileList);
        add(scrollPane);

        setVisible(true);
    }

    private void updateFilePanel() {
        listModel.clear();
        String currentWord = wordField.getText();

        // Show all files if word is empty
        if (currentWord.isEmpty()) {
            for (FileWrapper fileWrapper : this.fileList) {
                listModel.addElement(fileWrapper.getFile().getAbsolutePath());
            }
            return;
        }

        // Otherwise, show the files that contain the word
        for (FileWrapper fileWrapper : this.fileList) {
            if (fileWrapper.containsToken(currentWord)) {
                listModel.addElement(fileWrapper.getFile().getAbsolutePath());
            }
        }
    }

    private void openActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            placeFilesInPane(fileChooser.getSelectedFile());
        }
    }

    private void setEnvironment() {
        setLayout(new GridLayout(2, 1));
        setTitle("Indexing App");
        setLook();
        setLocation(200, 100);
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void placeFilesInPane(File directory) {
        listModel.clear();
        placeFiles(directory);
    }

    private void placeFiles(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        listModel.addElement(file.getAbsolutePath());

                        FileWrapper fileWrapper = new FileWrapper(file);
                        fileWrapper.addTokens(algorithm.tokenize(file));
                        fileList.add(fileWrapper);
                    } else if (file.isDirectory()) {
                        placeFiles(file);
                    }
                }
            }
        }
    }

    private void setLook() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JTextField getWordField() {
        return this.wordField;
    }

    public DefaultListModel<String> getListModel() {
        return this.listModel;
    }

}
