package com.enctool.app.traditional;

import java.security.SecureRandom;

/**
 * Implementation of the Vigenere cipher algorithm.
 * The Vigenere cipher is a method of encrypting alphabetic text by using a simple form of
 * polyalphabetic substitution. It uses a keyword to determine the shift for each letter in the plaintext.
 */
public class VigenereCipher implements CipherAlgorithm {
    
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
        
        SecureRandom secureRandom = new SecureRandom();
        
        // Generate a random keyword of length between 5 and 10
        int keyLength = secureRandom.nextInt(6) + 5; // 5 to 10
        StringBuilder keyword = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            int index = secureRandom.nextInt(alphabetSize);
            keyword.append(alphabet.charAt(index));
        }
        return keyword.toString();
    }
    
    @Override
    public String getKeyTooltip(String language) {
        return "Enter a keyword using letters from the selected alphabet";
    }
    
    private String process(String input, String key, String language, boolean encrypt) {
        if (key.isEmpty()) {
            return "Error: Key cannot be empty for Vigenere Cipher";
        }
        
        String alphabet = getAlphabet(language);
        StringBuilder validKey = new StringBuilder();
        
        for (char c : key.toCharArray()) {
            String upperC = String.valueOf(c).toUpperCase();
            if (alphabet.contains(upperC)) {
                validKey.append(upperC);
            }
        }
        
        if (validKey.length() == 0) {
            return "Error: Key must contain at least one valid character for the selected alphabet";
        }
        
        String processedKey = validKey.toString();
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        int alphabetSize = alphabet.length();
        
        for (char c : input.toCharArray()) {
            String upperC = String.valueOf(c).toUpperCase();
            int charIndex = alphabet.indexOf(upperC);
            
            if (charIndex != -1) {
                char keyChar = processedKey.charAt(keyIndex % processedKey.length());
                int keyCharIndex = alphabet.indexOf(String.valueOf(keyChar).toUpperCase());
                
                int resultIndex;
                if (encrypt) {
                    resultIndex = (charIndex + keyCharIndex) % alphabetSize;
                } else {
                    resultIndex = (charIndex - keyCharIndex + alphabetSize) % alphabetSize;
                }
                
                char resultChar = alphabet.charAt(resultIndex);
                
                if (Character.isLowerCase(c)) {
                    resultChar = Character.toLowerCase(resultChar);
                }
                
                result.append(resultChar);
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    private String getAlphabet(String language) {
        return language.equals("English") ? ENGLISH_ALPHABET : VIETNAMESE_ALPHABET;
    }
}