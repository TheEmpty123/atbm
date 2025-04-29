package com.enctool.app;

import com.enctool.app.algorithm.AESAlgorithm;
import com.enctool.app.algorithm.BlowfishAlgorithm;
import com.enctool.app.algorithm.DESAlgorithm;
import com.enctool.app.algorithm.RC4Algorithm;
import com.enctool.app.algorithm.TripleDESAlgorithm;
import com.enctool.app.algorithm.SymmetricAlgorithm;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class SymmetricEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JComboBox<String> modeComboBox;
    private JComboBox<String> paddingComboBox;
    private JComboBox<Integer> keySizeComboBox;
    private JPasswordField keyPasswordField;
    private JTextField ivTextField;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton generateKeyButton;
    private JButton generateIVButton;
    private JButton loadKeyButton;
    private JButton saveKeyButton;
    private JFileChooser fileChooser;
    
    // Input/output text labels
    private JLabel inputTextLabel;
    private JLabel outputTextLabel;
    
    // File operation components
    private JRadioButton textModeRadio;
    private JRadioButton fileModeRadio;
    private JTextField inputFileTextField;
    private JTextField outputFileTextField;
    private JButton selectInputFileButton;
    private JButton selectOutputFileButton;
    private JFileChooser inputFileChooser;
    private JFileChooser outputFileChooser;
    private JPanel textPanel;
    private JPanel filePanel;
    
    // Algorithm handler objects
    private SymmetricAlgorithm currentAlgorithm;
    private final SymmetricAlgorithm aesAlgorithm;
    private final SymmetricAlgorithm desAlgorithm;
    private final SymmetricAlgorithm blowfishAlgorithm;
    private final SymmetricAlgorithm tripleDESAlgorithm;
    private final SymmetricAlgorithm rc4Algorithm;

    // Constants for CardLayout
    private static final String TEXT_MODE = "TEXT_MODE";
    private static final String FILE_MODE = "FILE_MODE";
    
    // CardLayout container
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public SymmetricEncryptionPanel() {
        setLayout(new BorderLayout());
        
        // Initialize algorithm handlers
        aesAlgorithm = new AESAlgorithm();
        desAlgorithm = new DESAlgorithm();
        blowfishAlgorithm = new BlowfishAlgorithm();
        tripleDESAlgorithm = new TripleDESAlgorithm();
        rc4Algorithm = new RC4Algorithm();
        currentAlgorithm = aesAlgorithm; // Default to AES
        
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Key files (*.key)", "key"));
        
        inputFileChooser = new JFileChooser();
        outputFileChooser = new JFileChooser();
        
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
        
        JLabel algorithmLabel = new JLabel("Algorithm:");
        algorithmComboBox = new JComboBox<>(new String[]{"AES", "DES", "3DES", "Blowfish", "RC4"});
        
        JLabel keySizeLabel = new JLabel("Key Size (bits):");
        keySizeComboBox = new JComboBox<>();
        for (int keySize : currentAlgorithm.getSupportedKeySizes()) {
            keySizeComboBox.addItem(keySize);
        }
        
        JLabel modeLabel = new JLabel("Mode:");
        modeComboBox = new JComboBox<>(currentAlgorithm.getSupportedModes());
        
        JLabel paddingLabel = new JLabel("Padding:");
        paddingComboBox = new JComboBox<>(currentAlgorithm.getSupportedPaddings());
        
        JLabel keyLabel = new JLabel("Key:");
        keyPasswordField = new JPasswordField(20);
        
        JLabel ivLabel = new JLabel("IV:");
        ivTextField = new JTextField(20);
        
        generateKeyButton = new JButton("Generate Key");
        generateIVButton = new JButton("Generate IV");
        loadKeyButton = new JButton("Load Key");
        saveKeyButton = new JButton("Save Key");
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        
        // Initialize input/output text labels
        inputTextLabel = new JLabel("Input Text:");
        outputTextLabel = new JLabel("Output Text:");

        // Add components to control panel
        gbc.gridx = 0; 
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(algorithmLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(algorithmComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(keySizeLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(keySizeComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        controlPanel.add(modeLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(modeComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        controlPanel.add(paddingLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(paddingComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        controlPanel.add(keyLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(keyPasswordField, gbc);
        
        // Key operation buttons in a panel
        JPanel keyButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        keyButtonPanel.add(generateKeyButton);
        keyButtonPanel.add(loadKeyButton);
        keyButtonPanel.add(saveKeyButton);
        
        gbc.gridx = 2; gbc.gridy = 5;
        controlPanel.add(keyButtonPanel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        controlPanel.add(ivLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(ivTextField, gbc);
        gbc.gridx = 2;
        controlPanel.add(generateIVButton, gbc);
        
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionButtonPanel.add(encryptButton);
        actionButtonPanel.add(decryptButton);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 3;
        controlPanel.add(actionButtonPanel, gbc);

        // Create text areas
        textPanel = new JPanel(new GridLayout(2, 1, 0, 10)); // 2 rows
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputTextLabel, BorderLayout.NORTH);
        inputTextArea = new JTextArea(10, 40);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);
        
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputTextLabel, BorderLayout.NORTH);
        outputTextArea = new JTextArea(10, 40);
        outputTextArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);
        
        textPanel.add(inputPanel);
        textPanel.add(outputPanel);
        
        // Create file panel
        filePanel = new JPanel(new GridLayout(2, 1, 0, 10));
        
        JPanel inputFilePanel = new JPanel(new BorderLayout());
        inputFilePanel.add(new JLabel("Input File:"), BorderLayout.NORTH);
        JPanel inputFileSelectionPanel = new JPanel(new BorderLayout());
        inputFileTextField = new JTextField();
        inputFileTextField.setEditable(false);
        selectInputFileButton = new JButton("Browse...");
        inputFileSelectionPanel.add(inputFileTextField, BorderLayout.CENTER);
        inputFileSelectionPanel.add(selectInputFileButton, BorderLayout.EAST);
        inputFilePanel.add(inputFileSelectionPanel, BorderLayout.CENTER);
        
        JPanel outputFilePanel = new JPanel(new BorderLayout());
        outputFilePanel.add(new JLabel("Output File:"), BorderLayout.NORTH);
        JPanel outputFileSelectionPanel = new JPanel(new BorderLayout());
        outputFileTextField = new JTextField();
        outputFileTextField.setEditable(false);
        selectOutputFileButton = new JButton("Browse...");
        outputFileSelectionPanel.add(outputFileTextField, BorderLayout.CENTER);
        outputFileSelectionPanel.add(selectOutputFileButton, BorderLayout.EAST);
        outputFilePanel.add(outputFileSelectionPanel, BorderLayout.CENTER);
        
        filePanel.add(inputFilePanel);
        filePanel.add(outputFilePanel);
        
        // Create card layout for switching between text and file panels
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(textPanel, TEXT_MODE);
        cardPanel.add(filePanel, FILE_MODE);
        
        // Initially show text panel
        cardLayout.show(cardPanel, TEXT_MODE);

        // Add panels to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        
        // Add event listeners for mode selection
        textModeRadio.addActionListener(e -> {
            cardLayout.show(cardPanel, TEXT_MODE);
        });
        
        fileModeRadio.addActionListener(e -> {
            cardLayout.show(cardPanel, FILE_MODE);
        });
        
        // File selection listeners
        selectInputFileButton.addActionListener(e -> {
            int returnVal = inputFileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = inputFileChooser.getSelectedFile();
                inputFileTextField.setText(selectedFile.getAbsolutePath());
                
                // Suggest output file name
                String inputPath = selectedFile.getAbsolutePath();
                String suggestedOutputPath;
                if (inputPath.toLowerCase().endsWith(".enc")) {
                    // For encrypted files, remove .enc extension for decryption
                    suggestedOutputPath = inputPath.substring(0, inputPath.length() - 4);
                } else {
                    // For unencrypted files, add .enc extension for encryption
                    suggestedOutputPath = inputPath + ".enc";
                }
                outputFileTextField.setText(suggestedOutputPath);
            }
        });
        
        selectOutputFileButton.addActionListener(e -> {
            int returnVal = outputFileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                outputFileTextField.setText(outputFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        // Add event listeners
        algorithmComboBox.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            switch (selectedAlgorithm) {
                case "AES":
                    currentAlgorithm = aesAlgorithm;
                    break;
                case "DES":
                    currentAlgorithm = desAlgorithm;
                    break;
                case "3DES":
                    currentAlgorithm = tripleDESAlgorithm;
                    break;
                case "Blowfish":
                    currentAlgorithm = blowfishAlgorithm;
                    break;
                case "RC4":
                    currentAlgorithm = rc4Algorithm;
                    break;
            }
            
            // Update mode, padding, and key size options based on selected algorithm
            updateModeAndPaddingOptions();
            updateKeySizeOptions();
        });
        
        modeComboBox.addActionListener(e -> {
            // Enable/disable IV field based on mode
            String selectedMode = (String) modeComboBox.getSelectedItem();
            boolean requiresIV = currentAlgorithm.requiresIV(selectedMode);
            ivTextField.setEnabled(requiresIV);
            generateIVButton.setEnabled(requiresIV);
            
            if (!requiresIV) {
                ivTextField.setText("");
            }
        });
        
        generateKeyButton.addActionListener(e -> {
            try {
                // Get selected key size
                int keySize = (Integer) keySizeComboBox.getSelectedItem();
                String encodedKey = currentAlgorithm.generateKey(keySize);
                keyPasswordField.setText(encodedKey);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error generating key: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        generateIVButton.addActionListener(e -> {
            try {
                String encodedIV = currentAlgorithm.generateIV();
                ivTextField.setText(encodedIV);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error generating IV: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        loadKeyButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    String keyContent = new String(Files.readAllBytes(
                        Paths.get(fileChooser.getSelectedFile().getPath())));
                    keyPasswordField.setText(keyContent.trim());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error loading key: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        saveKeyButton.addActionListener(e -> {
            if (keyPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this,
                    "Please generate or enter a key first",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().toLowerCase().endsWith(".key")) {
                        selectedFile = new File(selectedFile.getPath() + ".key");
                    }
                    
                    try (FileWriter writer = new FileWriter(selectedFile)) {
                        writer.write(new String(keyPasswordField.getPassword()));
                    }
                    
                    JOptionPane.showMessageDialog(this,
                        "Key saved successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error saving key: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        encryptButton.addActionListener(e -> {
            try {
                String key = new String(keyPasswordField.getPassword());
                String mode = (String) modeComboBox.getSelectedItem();
                String padding = (String) paddingComboBox.getSelectedItem();
                String iv = ivTextField.getText();
                
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter or generate a key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (currentAlgorithm.requiresIV(mode) && iv.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "This mode requires an IV. Please enter or generate one.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (textModeRadio.isSelected()) {
                    // Text mode encryption
                    String input = inputTextArea.getText();
                    String encrypted = currentAlgorithm.encrypt(input, key, mode, padding, iv);
                    outputTextArea.setText(encrypted);
                } else {
                    // File mode encryption
                    String inputFilePath = inputFileTextField.getText();
                    String outputFilePath = outputFileTextField.getText();
                    
                    if (inputFilePath.isEmpty() || outputFilePath.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Please select both input and output files",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    encryptFile(inputFilePath, outputFilePath, key, mode, padding, iv);
                    JOptionPane.showMessageDialog(this,
                        "File encrypted successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                String errorMsg = "Encryption error: " + ex.getMessage();
                if (textModeRadio.isSelected()) {
                    outputTextArea.setText(errorMsg);
                } else {
                    JOptionPane.showMessageDialog(this,
                        errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        decryptButton.addActionListener(e -> {
            try {
                String key = new String(keyPasswordField.getPassword());
                String mode = (String) modeComboBox.getSelectedItem();
                String padding = (String) paddingComboBox.getSelectedItem();
                String iv = ivTextField.getText();
                
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a key", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (currentAlgorithm.requiresIV(mode) && iv.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "This mode requires an IV. Please enter the same IV used for encryption.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (textModeRadio.isSelected()) {
                    // Text mode decryption
                    String input = inputTextArea.getText();
                    String decrypted = currentAlgorithm.decrypt(input, key, mode, padding, iv);
                    outputTextArea.setText(decrypted);
                } else {
                    // File mode decryption
                    String inputFilePath = inputFileTextField.getText();
                    String outputFilePath = outputFileTextField.getText();
                    
                    if (inputFilePath.isEmpty() || outputFilePath.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Please select both input and output files",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    decryptFile(inputFilePath, outputFilePath, key, mode, padding, iv);
                    JOptionPane.showMessageDialog(this,
                        "File decrypted successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                String errorMsg = "Decryption error: " + ex.getMessage();
                if (textModeRadio.isSelected()) {
                    outputTextArea.setText(errorMsg);
                } else {
                    JOptionPane.showMessageDialog(this,
                        errorMsg,
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Initialize IV field state based on default mode
        String defaultMode = (String) modeComboBox.getSelectedItem();
        ivTextField.setEnabled(currentAlgorithm.requiresIV(defaultMode));
        generateIVButton.setEnabled(currentAlgorithm.requiresIV(defaultMode));
    }
    
    /**
     * Encrypts a file using the specified parameters
     */
    private void encryptFile(String inputFilePath, String outputFilePath, 
                             String key, String mode, String padding, String iv) 
                             throws IOException {
        File inputFile = new File(inputFilePath);
        byte[] fileContent = Files.readAllBytes(inputFile.toPath());
        
        // Convert the file content to Base64 to use existing encryption method
        String base64Content = Base64.getEncoder().encodeToString(fileContent);
        String encryptedContent = currentAlgorithm.encrypt(base64Content, key, mode, padding, iv);
        
        // Write encrypted content to output file
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(encryptedContent);
        }
    }
    
    /**
     * Decrypts a file using the specified parameters
     */
    private void decryptFile(String inputFilePath, String outputFilePath, 
                             String key, String mode, String padding, String iv) 
                             throws IOException {
        // Read encrypted content
        String encryptedContent = new String(Files.readAllBytes(Paths.get(inputFilePath)));
        
        // Decrypt content
        String decryptedBase64 = currentAlgorithm.decrypt(encryptedContent, key, mode, padding, iv);
        byte[] decryptedBytes = Base64.getDecoder().decode(decryptedBase64);
        
        // Write decrypted content to output file
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(decryptedBytes);
        }
    }
    
    /**
     * Updates the mode and padding combo boxes based on the current algorithm
     */
    private void updateModeAndPaddingOptions() {
        modeComboBox.removeAllItems();
        paddingComboBox.removeAllItems();
        
        for (String mode : currentAlgorithm.getSupportedModes()) {
            modeComboBox.addItem(mode);
        }
        
        for (String padding : currentAlgorithm.getSupportedPaddings()) {
            paddingComboBox.addItem(padding);
        }
        
        // Update IV field state based on selected mode
        String selectedMode = (String) modeComboBox.getSelectedItem();
        if (selectedMode != null) {
            ivTextField.setEnabled(currentAlgorithm.requiresIV(selectedMode));
            generateIVButton.setEnabled(currentAlgorithm.requiresIV(selectedMode));
        }
    }
    
    /**
     * Updates the key size combo box based on the current algorithm
     */
    private void updateKeySizeOptions() {
        keySizeComboBox.removeAllItems();
        
        for (int keySize : currentAlgorithm.getSupportedKeySizes()) {
            keySizeComboBox.addItem(keySize);
        }
    }
}
