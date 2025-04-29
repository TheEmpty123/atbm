package com.enctool.app.traditional;

import java.security.SecureRandom;

/**
 * Implementation of the Substitution cipher algorithm.
 * The Substitution cipher is a method of encrypting in which units of plaintext are replaced
 * with other units according to a fixed system; the "units" may be single letters, pairs of
 * letters, triplets of letters, mixtures of the above, and so forth.
 */
public class SubstitutionCipher implements CipherAlgorithm {
    
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
        
        // Generate a random permutation of the alphabet
        char[] shuffledAlphabet = alphabet.toCharArray();
        SecureRandom secureRandom = new SecureRandom();
        
        // Fisher-Yates shuffle algorithm
        for (int i = shuffledAlphabet.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            // Swap characters at i and j
            char temp = shuffledAlphabet[i];
            shuffledAlphabet[i] = shuffledAlphabet[j];
            shuffledAlphabet[j] = temp;
        }
        return new String(shuffledAlphabet);
    }
    
    @Override
    public String getKeyTooltip(String language) {
        int size = language.equals("English") ? ENGLISH_ALPHABET.length() : VIETNAMESE_ALPHABET.length();
        return "Enter a " + size + "-letter substitution key";
    }
    
    private String process(String input, String key, String language, boolean encrypt) {
        String alphabet = getAlphabet(language);
        int alphabetSize = alphabet.length();
        
        if (key.length() != alphabetSize) {
            return "Error: Substitution Cipher key must be exactly " + alphabetSize +
                    " characters long for the " + language + " alphabet";
        }
        
        boolean[] used = new boolean[alphabetSize];
        for (char c : key.toCharArray()) {
            String upperC = String.valueOf(c).toUpperCase();
            int index = alphabet.indexOf(upperC);
            if (index == -1) {
                return "Error: Key contains characters not in the " + language + " alphabet";
            }
            if (used[index]) {
                return "Error: Key must contain each letter exactly once";
            }
            used[index] = true;
        }
        
        StringBuilder result = new StringBuilder();
        String keyUpperCase = key.toUpperCase();
        
        if (encrypt) {
            for (char c : input.toCharArray()) {
                String upperC = String.valueOf(c).toUpperCase();
                int index = alphabet.indexOf(upperC);
                
                if (index != -1) {
                    char mappedChar = keyUpperCase.charAt(index);
                    
                    if (Character.isLowerCase(c)) {
                        mappedChar = Character.toLowerCase(mappedChar);
                    }
                    
                    result.append(mappedChar);
                } else {
                    result.append(c);
                }
            }
        } else {
            for (char c : input.toCharArray()) {
                String upperC = String.valueOf(c).toUpperCase();
                int index = keyUpperCase.indexOf(upperC);
                
                if (index != -1) {
                    char originalChar = alphabet.charAt(index);
                    
                    if (Character.isLowerCase(c)) {
                        originalChar = Character.toLowerCase(originalChar);
                    }
                    
                    result.append(originalChar);
                } else {
                    result.append(c);
                }
            }
        }
        
        return result.toString();
    }
    
    private String getAlphabet(String language) {
        return language.equals("English") ? ENGLISH_ALPHABET : VIETNAMESE_ALPHABET;
    }
}