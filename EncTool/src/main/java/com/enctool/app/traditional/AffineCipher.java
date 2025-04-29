package com.enctool.app.traditional;

import java.security.SecureRandom;

/**
 * Implementation of the Affine cipher algorithm.
 * The Affine cipher is a type of monoalphabetic substitution cipher where each letter
 * in the alphabet is mapped to its numeric equivalent, encrypted using a simple mathematical
 * function, and converted back to a letter.
 */
public class AffineCipher implements CipherAlgorithm {
    
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
        
        // For Affine cipher, we need an 'a' that is coprime with the alphabet size
        // and a random 'b'
        int a;
        do {
            a = secureRandom.nextInt(alphabetSize - 1) + 1;
        } while (gcd(a, alphabetSize) != 1);
        
        int b = secureRandom.nextInt(alphabetSize);
        return a + "," + b;
    }
    
    @Override
    public String getKeyTooltip(String language) {
        String alphabetSize = language.equals("English") ? "26" : String.valueOf(VIETNAMESE_ALPHABET.length());
        return "Enter two numbers separated by a comma (a,b). 'a' must be coprime with " + alphabetSize;
    }
    
    private String process(String input, String key, String language, boolean encrypt) {
        String[] params = key.split(",");
        if (params.length != 2) {
            return "Error: Affine Cipher key must be in the format 'a,b' (e.g., '5,8')";
        }
        
        int a, b;
        try {
            a = Integer.parseInt(params[0]);
            b = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            return "Error: Key parameters must be numbers for Affine Cipher";
        }
        
        String alphabet = getAlphabet(language);
        int m = alphabet.length();
        
        if (gcd(a, m) != 1) {
            return "Error: First parameter 'a' must be coprime with the alphabet size (" + m + ")";
        }
        
        StringBuilder result = new StringBuilder();
        
        if (encrypt) {
            for (char c : input.toCharArray()) {
                String upperC = String.valueOf(c).toUpperCase();
                int index = alphabet.indexOf(upperC);
                
                if (index != -1) {
                    int encryptedIndex = (a * index + b) % m;
                    char encryptedChar = alphabet.charAt(encryptedIndex);
                    
                    if (Character.isLowerCase(c)) {
                        encryptedChar = Character.toLowerCase(encryptedChar);
                    }
                    
                    result.append(encryptedChar);
                } else {
                    result.append(c);
                }
            }
        } else {
            int aInverse = findModInverse(a, m);
            
            for (char c : input.toCharArray()) {
                String upperC = String.valueOf(c).toUpperCase();
                int index = alphabet.indexOf(upperC);
                
                if (index != -1) {
                    int decryptedIndex = (aInverse * (index - b + m)) % m;
                    if (decryptedIndex < 0) decryptedIndex += m;
                    
                    char decryptedChar = alphabet.charAt(decryptedIndex);
                    
                    if (Character.isLowerCase(c)) {
                        decryptedChar = Character.toLowerCase(decryptedChar);
                    }
                    
                    result.append(decryptedChar);
                } else {
                    result.append(c);
                }
            }
        }
        
        return result.toString();
    }
    
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
    
    private int findModInverse(int a, int m) {
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1) {
                return i;
            }
        }
        return 1;
    }
    
    private String getAlphabet(String language) {
        return language.equals("English") ? ENGLISH_ALPHABET : VIETNAMESE_ALPHABET;
    }
}