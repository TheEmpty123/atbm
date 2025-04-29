package com.enctool.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;

public class AsymmetricEncryptionPanel extends JPanel {
    private JComboBox<String> algorithmComboBox;
    private JComboBox<Integer> keySizeComboBox;
    private JTextArea publicKeyTextArea;
    private JTextArea privateKeyTextArea;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton generateKeyPairButton;
    private JButton encryptPublicButton;
    private JButton decryptPrivateButton;
    private JButton encryptPrivateButton;
    private JButton decryptPublicButton;
    private JButton savePublicKeyButton;
    private JButton savePrivateKeyButton;
    private JButton loadPublicKeyButton;
    private JButton loadPrivateKeyButton;

    public AsymmetricEncryptionPanel() {
        setLayout(new BorderLayout());
        
        // Create a control panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel algorithmLabel = new JLabel("Algorithm:");
        algorithmComboBox = new JComboBox<>(new String[]{"RSA"});
        
        JLabel keySizeLabel = new JLabel("Key Size:");
        keySizeComboBox = new JComboBox<>(new Integer[]{1024, 2048, 3072, 4096});
        keySizeComboBox.setSelectedItem(2048); // Default to 2048
        
        generateKeyPairButton = new JButton("Generate Key Pair");
        encryptPublicButton = new JButton("Encrypt with Public Key");
        decryptPrivateButton = new JButton("Decrypt with Private Key");
        encryptPrivateButton = new JButton("Encrypt with Private Key");
        decryptPublicButton = new JButton("Decrypt with Public Key");

        // Add components to control panel
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(algorithmLabel, gbc);
        gbc.gridx = 1;
        controlPanel.add(algorithmComboBox, gbc);
        
        gbc.gridx = 2;
        controlPanel.add(keySizeLabel, gbc);
        gbc.gridx = 3;
        controlPanel.add(keySizeComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        controlPanel.add(generateKeyPairButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        controlPanel.add(encryptPublicButton, gbc);
        gbc.gridx = 2;
        controlPanel.add(decryptPrivateButton, gbc);
        
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
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        
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

        // Combined panel for main content
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(keyPanel, BorderLayout.NORTH);
        mainContentPanel.add(textPanel, BorderLayout.CENTER);

        // Add all panels to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(mainContentPanel), BorderLayout.CENTER);
        
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
        
        encryptPublicButton.addActionListener(e -> {
            try {
                String input = inputTextArea.getText();
                String publicKeyString = publicKeyTextArea.getText();
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                
                if (publicKeyString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please generate or enter a public key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String encrypted = encryptWithPublicKey(input, publicKeyString, algorithm);
                outputTextArea.setText(encrypted);
            } catch (Exception ex) {
                outputTextArea.setText("Encryption error: " + ex.getMessage());
            }
        });
        
        decryptPrivateButton.addActionListener(e -> {
            try {
                String input = inputTextArea.getText();
                String privateKeyString = privateKeyTextArea.getText();
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                
                if (privateKeyString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please generate or enter a private key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String decrypted = decryptWithPrivateKey(input, privateKeyString, algorithm);
                outputTextArea.setText(decrypted);
            } catch (Exception ex) {
                outputTextArea.setText("Decryption error: " + ex.getMessage());
            }
        });
        
        encryptPrivateButton.addActionListener(e -> {
            try {
                String input = inputTextArea.getText();
                String privateKeyString = privateKeyTextArea.getText();
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                
                if (privateKeyString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please generate or enter a private key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String encrypted = encryptWithPrivateKey(input, privateKeyString, algorithm);
                outputTextArea.setText(encrypted);
            } catch (Exception ex) {
                outputTextArea.setText("Encryption error: " + ex.getMessage());
            }
        });
        
        decryptPublicButton.addActionListener(e -> {
            try {
                String input = inputTextArea.getText();
                String publicKeyString = publicKeyTextArea.getText();
                String algorithm = (String) algorithmComboBox.getSelectedItem();
                
                if (publicKeyString.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please generate or enter a public key first", 
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String decrypted = decryptWithPublicKey(input, publicKeyString, algorithm);
                outputTextArea.setText(decrypted);
            } catch (Exception ex) {
                outputTextArea.setText("Decryption error: " + ex.getMessage());
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
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
        return keyGen.generateKeyPair();
    }
    
    private String encryptWithPublicKey(String plainText, String publicKeyString, String algorithm) {
        try {
            // Convert Base64 encoded public key to PublicKey object
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PublicKey publicKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));
            
            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            
            // Encrypt the text
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    private String decryptWithPrivateKey(String encryptedText, String privateKeyString, String algorithm) {
        try {
            // Convert Base64 encoded private key to PrivateKey object
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));
            
            // Convert Base64 encoded encrypted text to byte array
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            
            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            
            // Decrypt the text
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    private String encryptWithPrivateKey(String plainText, String privateKeyString, String algorithm) {
        try {
            // Convert Base64 encoded private key to PrivateKey object
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));
            
            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            
            // Encrypt the text
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption with private key failed", e);
        }
    }
    
    private String decryptWithPublicKey(String encryptedText, String publicKeyString, String algorithm) {
        try {
            // Convert Base64 encoded public key to PublicKey object
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PublicKey publicKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));
            
            // Convert Base64 encoded encrypted text to byte array
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            
            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            
            // Decrypt the text
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption with public key failed", e);
        }
    }
    
    private void saveKeyToFile(String keyContent, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Key");
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                writer.write(keyContent);
                JOptionPane.showMessageDialog(this,
                    "Key saved successfully to " + fileToSave.getName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error saving key: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String loadKeyFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Key");
        
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                JOptionPane.showMessageDialog(this,
                    "Key loaded successfully from " + fileToLoad.getName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                return content.toString();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error loading key: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }
}
