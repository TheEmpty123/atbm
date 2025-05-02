package com.enctool.app;

import com.enctool.app.traditional.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

public class TraditionalEncryptionPanel extends JPanel {
    private JComboBox<String> cipherComboBox;
    private JComboBox<String> languageComboBox;
    private JTextField keyTextField;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton generateKeyButton;
    
    // Hill Cipher specific components
    private JPanel hillKeyPanel;
    private JPanel standardKeyPanel;
    private JSpinner matrixSizeSpinner;
    private JPanel matrixPanel;
    private JTextField[][] matrixFields;
    private static final int MAX_MATRIX_SIZE = 5;

    // Define alphabets
    private static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY";

    // Map to store cipher algorithm instances
    private final HashMap<String, CipherAlgorithm> cipherAlgorithms;

    public TraditionalEncryptionPanel() {
        // Initialize cipher algorithms
        cipherAlgorithms = new HashMap<>();
        cipherAlgorithms.put("Caesar Cipher", new CaesarCipher());
        cipherAlgorithms.put("Affine Cipher", new AffineCipher());
        cipherAlgorithms.put("Vigenere Cipher", new VigenereCipher());
        cipherAlgorithms.put("Substitution Cipher", new SubstitutionCipher());
        cipherAlgorithms.put("Hill Cipher", new HillCipher());
        cipherAlgorithms.put("Transposition Cipher", new TranspositionCipher());

        setLayout(new BorderLayout());

        // Create components
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel cipherLabel = new JLabel("Cipher Method:");
        cipherComboBox = new JComboBox<>(new String[]{"Caesar Cipher", "Affine Cipher", "Vigenere Cipher", "Substitution Cipher", "Hill Cipher", "Transposition Cipher"});

        JLabel languageLabel = new JLabel("Alphabet:");
        languageComboBox = new JComboBox<>(new String[]{"English", "Vietnamese"});

        // Create standard key panel
        standardKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel keyLabel = new JLabel("Key:");
        keyTextField = new JTextField(20);
        generateKeyButton = new JButton("Generate Key");
        standardKeyPanel.add(keyLabel);
        standardKeyPanel.add(keyTextField);
        standardKeyPanel.add(generateKeyButton);

        // Create Hill cipher key panel
        hillKeyPanel = createHillKeyPanel();
        
        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");

        // Add components to control panel
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(cipherLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        controlPanel.add(cipherComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(languageLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        controlPanel.add(languageComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        controlPanel.add(standardKeyPanel, gbc);
        controlPanel.add(hillKeyPanel, gbc);
        // Initially hide the Hill key panel
        hillKeyPanel.setVisible(false);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        controlPanel.add(encryptButton, gbc);
        gbc.gridx = 1;
        controlPanel.add(decryptButton, gbc);

        // Create text areas
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Input Text:"), BorderLayout.NORTH);
        inputTextArea = new JTextArea(10, 40);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Output Text:"), BorderLayout.NORTH);
        outputTextArea = new JTextArea(10, 40);
        outputTextArea.setEditable(false);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        textPanel.add(inputPanel);
        textPanel.add(outputPanel);

        // Add panels to main panel
        add(controlPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        // Add action listeners
        encryptButton.addActionListener(e -> {
            String input = inputTextArea.getText();
            String key = getKeyFromUI();
            String cipher = (String) cipherComboBox.getSelectedItem();
            String language = (String) languageComboBox.getSelectedItem();

            String output = performTraditionalEncryption(input, key, cipher, language, true);
            outputTextArea.setText(output);
        });

        decryptButton.addActionListener(e -> {
            String input = inputTextArea.getText();
            String key = getKeyFromUI();
            String cipher = (String) cipherComboBox.getSelectedItem();
            String language = (String) languageComboBox.getSelectedItem();

            String output = performTraditionalEncryption(input, key, cipher, language, false);
            outputTextArea.setText(output);
        });

        // Add listener for language change to update the UI
        languageComboBox.addActionListener(e -> {
            String selectedLanguage = (String) languageComboBox.getSelectedItem();
            String selectedCipher = (String) cipherComboBox.getSelectedItem();

            // Update placeholder or example for the key field based on language and cipher
            if ("Substitution Cipher".equals(selectedCipher)) {
                String placeholder = selectedLanguage.equals("English") ?
                        "Enter a 26-letter substitution key" :
                        "Enter a " + VIETNAMESE_ALPHABET.length() + "-letter substitution key";
                keyTextField.setToolTipText(placeholder);
            }
        });

        // Add listener for the Generate Key button
        generateKeyButton.addActionListener(e -> {
            String selectedCipher = (String) cipherComboBox.getSelectedItem();
            String selectedLanguage = (String) languageComboBox.getSelectedItem();

            try {
                String generatedKey = generateKeyForCipher(selectedCipher, selectedLanguage);
                
                if ("Hill Cipher".equals(selectedCipher)) {
                    updateHillMatrixFromKey(generatedKey);
                } else {
                    keyTextField.setText(generatedKey);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error generating key: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add listener for cipher selection to update the key field tooltip and visibility
        cipherComboBox.addActionListener(e -> {
            String selectedCipher = (String) cipherComboBox.getSelectedItem();
            String selectedLanguage = (String) languageComboBox.getSelectedItem();

            updateKeyFieldTooltip(selectedCipher, selectedLanguage);
            
            // Show/hide the appropriate key input based on selected cipher
            boolean isHillCipher = "Hill Cipher".equals(selectedCipher);
            hillKeyPanel.setVisible(isHillCipher);
            standardKeyPanel.setVisible(!isHillCipher);
        });

        // Initialize tooltip
        updateKeyFieldTooltip((String) cipherComboBox.getSelectedItem(),
                (String) languageComboBox.getSelectedItem());
    }
    
    private JPanel createHillKeyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Hill Cipher Matrix",
                TitledBorder.LEFT,
                TitledBorder.TOP));
        
        JPanel sizePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sizePanel.add(new JLabel("Matrix Size:"));
        
        SpinnerNumberModel model = new SpinnerNumberModel(2, 2, MAX_MATRIX_SIZE, 1);
        matrixSizeSpinner = new JSpinner(model);
        sizePanel.add(matrixSizeSpinner);
        
        JButton resizeButton = new JButton("Resize");
        sizePanel.add(resizeButton);
        
        JButton generateMatrixButton = new JButton("Generate Matrix");
        sizePanel.add(generateMatrixButton);
        
        panel.add(sizePanel);
        
        matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(2, 2, 5, 5));
        
        // Initialize with 2x2 matrix
        matrixFields = new JTextField[MAX_MATRIX_SIZE][MAX_MATRIX_SIZE];
        for (int i = 0; i < MAX_MATRIX_SIZE; i++) {
            for (int j = 0; j < MAX_MATRIX_SIZE; j++) {
                matrixFields[i][j] = new JTextField(3);
                if (i < 2 && j < 2) {
                    matrixPanel.add(matrixFields[i][j]);
                }
            }
        }
        
        panel.add(matrixPanel);
        
        // Add action listener for resize button
        resizeButton.addActionListener(e -> {
            int size = (Integer) matrixSizeSpinner.getValue();
            resizeMatrix(size);
        });
        
        // Add action listener for generate matrix button
        generateMatrixButton.addActionListener(e -> {
            String selectedLanguage = (String) languageComboBox.getSelectedItem();
            String generatedKey = generateKeyForCipher("Hill Cipher", selectedLanguage);
            updateHillMatrixFromKey(generatedKey);
        });
        
        return panel;
    }
    
    private void resizeMatrix(int size) {
        matrixPanel.removeAll();
        matrixPanel.setLayout(new GridLayout(size, size, 5, 5));
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixPanel.add(matrixFields[i][j]);
            }
        }
        
        matrixPanel.revalidate();
        matrixPanel.repaint();
        hillKeyPanel.revalidate();
    }
    
    private void updateHillMatrixFromKey(String key) {
        try {
            String[] rows = key.split(";");
            int size = rows.length;
            
            // Update spinner
            matrixSizeSpinner.setValue(size);
            resizeMatrix(size);
            
            // Fill the matrix fields
            for (int i = 0; i < size; i++) {
                String[] elements = rows[i].split(",");
                for (int j = 0; j < elements.length; j++) {
                    matrixFields[i][j].setText(elements[j].trim());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error parsing Hill matrix key: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getKeyFromUI() {
        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        
        if ("Hill Cipher".equals(selectedCipher)) {
            int size = (Integer) matrixSizeSpinner.getValue();
            StringBuilder keyBuilder = new StringBuilder();
            
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    keyBuilder.append(matrixFields[i][j].getText().trim());
                    if (j < size - 1) {
                        keyBuilder.append(",");
                    }
                }
                if (i < size - 1) {
                    keyBuilder.append(";");
                }
            }
            
            return keyBuilder.toString();
        } else {
            return keyTextField.getText();
        }
    }

    private void updateKeyFieldTooltip(String cipher, String language) {
        CipherAlgorithm algorithm = cipherAlgorithms.get(cipher);
        if (algorithm != null) {
            String tooltip = algorithm.getKeyTooltip(language);
            keyTextField.setToolTipText(tooltip);
        } else {
            keyTextField.setToolTipText("Enter a key appropriate for the selected cipher");
        }
    }

    private String generateKeyForCipher(String cipher, String language) {
        CipherAlgorithm algorithm = cipherAlgorithms.get(cipher);
        if (algorithm != null) {
            return algorithm.generateKey(language);
        } else {
            throw new IllegalArgumentException("Unsupported cipher method");
        }
    }

    private String performTraditionalEncryption(String input, String key, String cipher, String language, boolean encrypt) {
        CipherAlgorithm algorithm = cipherAlgorithms.get(cipher);
        if (algorithm != null) {
            return encrypt ? algorithm.encrypt(input, key, language) : algorithm.decrypt(input, key, language);
        } else {
            return "Unsupported cipher method";
        }
    }

    private String getAlphabet(String language) {
        return language.equals("English") ? ENGLISH_ALPHABET : VIETNAMESE_ALPHABET;
    }
}
