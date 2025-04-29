package com.enctool.app.asymmetric;

import javax.swing.*;
import java.io.*;

/**
 * Utility class for key file operations.
 */
public class KeyUtils {
    
    private static final int KEY_LINE_LENGTH = 64;
    private static final String PUBLIC_KEY_HEADER = "----Start public key----";
    private static final String PUBLIC_KEY_FOOTER = "----End public key----";
    private static final String PRIVATE_KEY_HEADER = "----Start private key----";
    private static final String PRIVATE_KEY_FOOTER = "----End private key----";
    
    /**
     * Saves a key to a file.
     *
     * @param keyContent the key content to save
     * @param defaultFileName the default file name
     * @param parent the parent component for dialog display
     * @return true if the key was saved successfully, false otherwise
     */
    public static boolean saveKeyToFile(String keyContent, String defaultFileName, JComponent parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Key");
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                // Format the key with header, content, and footer
                boolean isPublicKey = defaultFileName.contains("public");
                String formattedKey = formatKeyForSaving(keyContent, isPublicKey);
                
                writer.write(formattedKey);
                JOptionPane.showMessageDialog(parent,
                    "Key saved successfully to " + fileToSave.getName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent,
                    "Error saving key: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Formats a key for saving with the required header, formatted content, and footer.
     * 
     * @param keyContent the raw key content
     * @param isPublicKey true if this is a public key, false for private key
     * @return the formatted key
     */
    private static String formatKeyForSaving(String keyContent, boolean isPublicKey) {
        StringBuilder formatted = new StringBuilder();
        
        // Add the appropriate header
        formatted.append(isPublicKey ? PUBLIC_KEY_HEADER : PRIVATE_KEY_HEADER).append("\n");
        
        // Format the key content with 64 characters per line
        for (int i = 0; i < keyContent.length(); i += KEY_LINE_LENGTH) {
            int end = Math.min(i + KEY_LINE_LENGTH, keyContent.length());
            formatted.append(keyContent.substring(i, end)).append("\n");
        }
        
        // Add the appropriate footer
        formatted.append(isPublicKey ? PUBLIC_KEY_FOOTER : PRIVATE_KEY_FOOTER);
        
        return formatted.toString();
    }
    
    /**
     * Loads a key from a file.
     *
     * @param parent the parent component for dialog display
     * @return the key content, or null if loading failed or was cancelled
     */
    public static String loadKeyFromFile(JComponent parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Key");
        
        int userSelection = fileChooser.showOpenDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                StringBuilder content = new StringBuilder();
                String line;
                boolean isKeyContent = false;
                
                while ((line = reader.readLine()) != null) {
                    if (line.equals(PUBLIC_KEY_HEADER) || line.equals(PRIVATE_KEY_HEADER)) {
                        isKeyContent = true;
                        continue;
                    }
                    
                    if (line.equals(PUBLIC_KEY_FOOTER) || line.equals(PRIVATE_KEY_FOOTER)) {
                        isKeyContent = false;
                        continue;
                    }
                    
                    if (isKeyContent) {
                        content.append(line);
                    }
                }
                
                JOptionPane.showMessageDialog(parent,
                    "Key loaded successfully from " + fileToLoad.getName(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                return content.toString();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(parent,
                    "Error loading key: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }
}
