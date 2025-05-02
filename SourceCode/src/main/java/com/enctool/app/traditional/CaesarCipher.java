package com.enctool.app.traditional;

import java.security.SecureRandom;

/**
 * Implementation of the Caesar cipher algorithm.
 * The Caesar cipher is a substitution cipher where each letter in the plaintext
 * is shifted a certain number of places down the alphabet.
 */
public class CaesarCipher implements CipherAlgorithm {
    
    // Define alphabets
    private static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY";
    
    @Override
    public String encrypt(String input, String key, String language) {
        return process(input, key, language, true);
    }
    
    @Override
    public String decrypt(String input, String key, String language) {
        return process(input, key, language, false);
    }
    
    @Override
    public String generateKey(String language) {
        String alphabet = getAlphabet(language);
        int alphabetSize = alphabet.length();
        
        // Generate a random shift value between 1 and alphabetSize-1
        SecureRandom secureRandom = new SecureRandom();
        return String.valueOf(secureRandom.nextInt(alphabetSize - 1) + 1);
    }
    
    @Override
    public String getKeyTooltip(String language) {
        return "Enter a number for shift value (e.g., 3)";
    }
    
    private String process(String input, String key, String language, boolean encrypt) {
        int shift;
        try {
            shift = Integer.parseInt(key);
        } catch (NumberFormatException e) {
            return "Error: Key must be a number for Caesar Cipher";
        }
        
        if (!encrypt) {
            shift = -shift; // For decryption, reverse the shift
        }
        
        String alphabet = getAlphabet(language);
        int alphabetSize = alphabet.length();
        
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            String upperC = String.valueOf(c).toUpperCase();
            int index = alphabet.indexOf(upperC);
            
            if (index != -1) {
                // Character is in the alphabet
                int newIndex = (index + shift) % alphabetSize;
                if (newIndex < 0) newIndex += alphabetSize; // Handle negative shifts
                
                char shiftedChar = alphabet.charAt(newIndex);
                
                // Preserve case
                if (Character.isLowerCase(c)) {
                    shiftedChar = Character.toLowerCase(shiftedChar);
                }
                
                result.append(shiftedChar);
            } else {
                // Character not in alphabet, leave unchanged
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private String getAlphabet(String language) {
        return language.equals("English") ? ENGLISH_ALPHABET : VIETNAMESE_ALPHABET;
    }
}