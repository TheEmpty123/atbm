package com.enctool.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class HashPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton hashButton;
    private JButton clearButton;
    private JRadioButton textModeRadio;
    private JRadioButton fileModeRadio;
    private JTextField filePathTextField;
    private JButton browseButton;
    private JFileChooser fileChooser;
    
    // List of supported hash algorithms (Java standard algorithms)
    private static final List<String> SUPPORTED_ALGORITHMS = Arrays.asList(
        "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"
    );
    
    public HashPanel() {
        setLayout(new BorderLayout());
        
        // Create components
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add mode selection (text vs file)
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textModeRadio = new JRadioButton("Text Mode", true);
        fileModeRadio = new JRadioButton("File Mode");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(textModeRadio);
        modeGroup.add(fileModeRadio);
        modePanel.add(textModeRadio);
        modePanel.add(fileModeRadio);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        controlPanel.add(modePanel, gbc);
        
        // Add algorithm selection
        JLabel algorithmLabel = new JLabel("Hash Algorithm:");
        algorithmComboBox = new JComboBox<>(SUPPORTED_ALGORITHMS.toArray(new String[0]));
        algorithmComboBox.setSelectedItem("SHA-256"); // Default to SHA-256
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(algorithmLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        controlPanel.add(algorithmComboBox, gbc);
        
        // Add file selection components
        JPanel filePanel = new JPanel(new BorderLayout());
        filePathTextField = new JTextField(25);
        filePathTextField.setEditable(false);
        browseButton = new JButton("Browse...");
        filePanel.add(new JLabel("File:"), BorderLayout.WEST);
        filePanel.add(filePathTextField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        controlPanel.add(filePanel, gbc);
        filePanel.setVisible(false); // Initially hide file panel
        
        // Add action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hashButton = new JButton("Compute Hash");
        clearButton = new JButton("Clear");
        actionPanel.add(hashButton);
        actionPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        controlPanel.add(actionPanel, gbc);
        
        // Create text areas
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input Text:"), BorderLayout.NORTH);
        inputTextArea = new JTextArea(10, 40);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);
        
        // Output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Hash Result:"), BorderLayout.NORTH);
        outputTextArea = new JTextArea(5, 40);
        outputTextArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);
        
        textPanel.add(inputPanel);
        textPanel.add(outputPanel);
        
        // Add all panels to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);
        
        // Initialize file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        
        // Add event listeners
        textModeRadio.addActionListener(e -> {
            filePanel.setVisible(false);
            inputTextArea.setEnabled(true);
        });
        
        fileModeRadio.addActionListener(e -> {
            filePanel.setVisible(true);
            inputTextArea.setEnabled(false);
        });
        
        browseButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathTextField.setText(selectedFile.getAbsolutePath());
            }
        });
        
        hashButton.addActionListener(e -> {
            try {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                String result;
                
                if (textModeRadio.isSelected()) {
                    String input = inputTextArea.getText();
                    result = computeHash(input, algorithm);
                } else {
                    String filePath = filePathTextField.getText();
                    if (filePath.isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Please select a file first", 
                            "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    result = computeFileHash(filePath, algorithm);
                }
                
                outputTextArea.setText(result);
            } catch (Exception ex) {
                outputTextArea.setText("Error computing hash: " + ex.getMessage());
            }
        });
        
        clearButton.addActionListener(e -> {
            inputTextArea.setText("");
            outputTextArea.setText("");
            filePathTextField.setText("");
        });
    }
    
    /**
     * Computes the hash of a text string using the specified algorithm
     */
    private String computeHash(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }
    
    /**
     * Computes the hash of a file using the specified algorithm
     */
    private String computeFileHash(String filePath, String algorithm) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        
        return bytesToHex(digest.digest());
    }
    
    /**
     * Convert a byte array to a hexadecimal string
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
