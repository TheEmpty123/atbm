package com.enctool.app;

import com.enctool.app.asymmetric.KeyUtils;
import com.enctool.app.asymmetric.RSAUtils;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.util.Base64;

public class AsymmetricEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JComboBox<Integer> keySizeComboBox;
    private JTextArea publicKeyTextArea;
    private JTextArea privateKeyTextArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton generateKeyPairButton;
    private JButton encryptPrivateButton;
    private JButton decryptPublicButton;
    private JButton savePublicKeyButton;
    private JButton savePrivateKeyButton;
    private JButton loadPublicKeyButton;
    private JButton loadPrivateKeyButton;
    
    // File mode components
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
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    // Constants for CardLayout
    private static final String TEXT_MODE = "TEXT_MODE";
    private static final String FILE_MODE = "FILE_MODE";

    public AsymmetricEncryptionPanel() {
        setLayout(new BorderLayout());

        // Initialize file choosers
        inputFileChooser = new JFileChooser();
        outputFileChooser = new JFileChooser();

        // Create a control panel
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
        gbc.gridwidth = 4;
        controlPanel.add(modePanel, gbc);

        JLabel algorithmLabel = new JLabel("Algorithm:");
        algorithmComboBox = new JComboBox<>(new String[]{"RSA"});

        JLabel keySizeLabel = new JLabel("Key Size:");
        keySizeComboBox = new JComboBox<>(new Integer[]{1024, 2048, 3072, 4096});
        keySizeComboBox.setSelectedItem(2048); // Default to 2048

        generateKeyPairButton = new JButton("Generate Key Pair");
        encryptPrivateButton = new JButton("Encrypt with Private Key");
        decryptPublicButton = new JButton("Decrypt with Public Key");

        // Add components to control panel
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        controlPanel.add(algorithmLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(algorithmComboBox, gbc);

        gbc.gridx = 2;
        controlPanel.add(keySizeLabel, gbc);
        gbc.gridx = 3;
        controlPanel.add(keySizeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        controlPanel.add(generateKeyPairButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        controlPanel.add(encryptPrivateButton, gbc);
        gbc.gridx = 2;
        controlPanel.add(decryptPublicButton, gbc);

        // Create key panel
        JPanel keyPanel = new JPanel(new GridLayout(2, 1, 0, 10));

        JPanel publicKeyPanel = new JPanel(new BorderLayout());
        publicKeyPanel.add(new JLabel("Public Key:"), BorderLayout.NORTH);
        publicKeyTextArea = new JTextArea(4, 50);
        publicKeyTextArea.setLineWrap(true);
        publicKeyTextArea.setWrapStyleWord(true);
        publicKeyPanel.add(new JScrollPane(publicKeyTextArea), BorderLayout.CENTER);

        // Create public key buttons panel
        JPanel publicKeyButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePublicKeyButton = new JButton("Save Public Key");
        loadPublicKeyButton = new JButton("Load Public Key");
        publicKeyButtonsPanel.add(savePublicKeyButton);
        publicKeyButtonsPanel.add(loadPublicKeyButton);
        publicKeyPanel.add(publicKeyButtonsPanel, BorderLayout.SOUTH);

        JPanel privateKeyPanel = new JPanel(new BorderLayout());
        privateKeyPanel.add(new JLabel("Private Key:"), BorderLayout.NORTH);
        privateKeyTextArea = new JTextArea(4, 50);
        privateKeyTextArea.setLineWrap(true);
        privateKeyTextArea.setWrapStyleWord(true);
        privateKeyPanel.add(new JScrollPane(privateKeyTextArea), BorderLayout.CENTER);

        // Create private key buttons panel
        JPanel privateKeyButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        savePrivateKeyButton = new JButton("Save Private Key");
        loadPrivateKeyButton = new JButton("Load Private Key");
        privateKeyButtonsPanel.add(savePrivateKeyButton);
        privateKeyButtonsPanel.add(loadPrivateKeyButton);
        privateKeyPanel.add(privateKeyButtonsPanel, BorderLayout.SOUTH);

        keyPanel.add(publicKeyPanel);
        keyPanel.add(privateKeyPanel);

        // Create text panel
        textPanel = new JPanel(new GridLayout(2, 1, 0, 10));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input Text:"), BorderLayout.NORTH);
        inputTextArea = new JTextArea(5, 50);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Output Text:"), BorderLayout.NORTH);
        outputTextArea = new JTextArea(5, 50);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
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

        // Combined panel for main content
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(keyPanel, BorderLayout.NORTH);
        mainContentPanel.add(cardPanel, BorderLayout.CENTER);

        // Add all panels to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(mainContentPanel), BorderLayout.CENTER);
        
        // Add event listeners for mode selection
        textModeRadio.addActionListener(e -> cardLayout.show(cardPanel, TEXT_MODE));
        fileModeRadio.addActionListener(e -> cardLayout.show(cardPanel, FILE_MODE));
        
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

        // Add action listeners
        generateKeyPairButton.addActionListener(e -> {
            try {
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                int keySize = (Integer) keySizeComboBox.getSelectedItem();
                KeyPair keyPair = generateKeyPair(algorithm, keySize);

                // Display keys in Base64 format
                publicKeyTextArea.setText(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
                privateKeyTextArea.setText(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error generating key pair: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        encryptPrivateButton.addActionListener(e -> {
            try {
                String privateKeyString = privateKeyTextArea.getText();
                String algorithm = (String) algorithmComboBox.getSelectedItem();

                if (privateKeyString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please generate or enter a private key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (textModeRadio.isSelected()) {
                    // Text mode encryption
                    String input = inputTextArea.getText();
                    String encrypted = encryptWithPrivateKey(input, privateKeyString, algorithm);
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
                    
                    encryptFileWithPrivateKey(inputFilePath, outputFilePath, privateKeyString, algorithm);
                    JOptionPane.showMessageDialog(this,
                        "File encrypted successfully with private key",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                String errorMsg = "Encryption error: " + ex.getMessage();
                if (textModeRadio.isSelected()) {
                    outputTextArea.setText(errorMsg);
                } else {
                    JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        decryptPublicButton.addActionListener(e -> {
            try {
                String publicKeyString = publicKeyTextArea.getText();
                String algorithm = (String) algorithmComboBox.getSelectedItem();

                if (publicKeyString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please generate or enter a public key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (textModeRadio.isSelected()) {
                    // Text mode decryption
                    String input = inputTextArea.getText();
                    String decrypted = decryptWithPublicKey(input, publicKeyString, algorithm);
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
                    
                    decryptFileWithPublicKey(inputFilePath, outputFilePath, publicKeyString, algorithm);
                    JOptionPane.showMessageDialog(this,
                        "File decrypted successfully with public key",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                String errorMsg = "Decryption error: " + ex.getMessage();
                if (textModeRadio.isSelected()) {
                    outputTextArea.setText(errorMsg);
                } else {
                    JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listeners for the save and load buttons
        savePublicKeyButton.addActionListener(e -> {
            String publicKeyString = publicKeyTextArea.getText();
            if (publicKeyString.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No public key to save",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            saveKeyToFile(publicKeyString, "public_key.pem");
        });

        savePrivateKeyButton.addActionListener(e -> {
            String privateKeyString = privateKeyTextArea.getText();
            if (privateKeyString.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No private key to save",
                    "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            saveKeyToFile(privateKeyString, "private_key.pem");
        });

        loadPublicKeyButton.addActionListener(e -> {
            String key = loadKeyFromFile();
            if (key != null && !key.isEmpty()) {
                publicKeyTextArea.setText(key);
            }
        });

        loadPrivateKeyButton.addActionListener(e -> {
            String key = loadKeyFromFile();
            if (key != null && !key.isEmpty()) {
                privateKeyTextArea.setText(key);
            }
        });
    }

    private KeyPair generateKeyPair(String algorithm, int keySize) throws Exception {
        // Only RSA is supported in the current implementation
        return RSAUtils.generateKeyPair(keySize);
    }

    private String encryptWithPrivateKey(String plainText, String privateKeyString, String algorithm) {
        // Only RSA is supported in the current implementation
        return RSAUtils.encryptWithPrivateKey(plainText, privateKeyString);
    }

    private String decryptWithPublicKey(String encryptedText, String publicKeyString, String algorithm) {
        // Only RSA is supported in the current implementation
        return RSAUtils.decryptWithPublicKey(encryptedText, publicKeyString);
    }

    private void saveKeyToFile(String keyContent, String defaultFileName) {
        KeyUtils.saveKeyToFile(keyContent, defaultFileName, this);
    }

    private String loadKeyFromFile() {
        return KeyUtils.loadKeyFromFile(this);
    }
    
    /**
     * Encrypts a file using the private key
     */
    private void encryptFileWithPrivateKey(String inputFilePath, String outputFilePath,
                                         String privateKeyString, String algorithm)
                                         throws IOException {
        try {
            // Read the file content
            byte[] fileContent = Files.readAllBytes(new File(inputFilePath).toPath());
            
            // Convert to Base64 to handle binary data
            String base64Content = Base64.getEncoder().encodeToString(fileContent);
            
            // Encrypt the content
            String encryptedContent = encryptWithPrivateKey(base64Content, privateKeyString, algorithm);
            
            // Write the encrypted content to the output file
            try (FileWriter writer = new FileWriter(outputFilePath)) {
                writer.write(encryptedContent);
            }
        } catch (Exception e) {
            throw new IOException("Error encrypting file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Decrypts a file using the public key
     */
    private void decryptFileWithPublicKey(String inputFilePath, String outputFilePath,
                                        String publicKeyString, String algorithm)
                                        throws IOException {
        try {
            // Read the encrypted file content
            String encryptedContent = new String(Files.readAllBytes(new File(inputFilePath).toPath()));
            
            // Decrypt the content
            String decryptedBase64 = decryptWithPublicKey(encryptedContent, publicKeyString, algorithm);
            
            // Decode Base64 back to binary
            byte[] decryptedBytes = Base64.getDecoder().decode(decryptedBase64);
            
            // Write to output file
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(decryptedBytes);
            }
        } catch (Exception e) {
            throw new IOException("Error decrypting file: " + e.getMessage(), e);
        }
    }
}
