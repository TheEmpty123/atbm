package com.enctool.app.traditional;

/**
 * Interface for traditional cipher algorithms.
 * All traditional cipher implementations should implement this interface.
 */
public interface CipherAlgorithm {
    
    /**
     * Encrypts the input text using the provided key.
     * 
     * @param input The text to encrypt
     * @param key The encryption key
     * @param language The language of the input text (e.g., "English", "Vietnamese")
     * @return The encrypted text or an error message if encryption fails
     */
    String encrypt(String input, String key, String language);
    
    /**
     * Decrypts the input text using the provided key.
     * 
     * @param input The text to decrypt
     * @param key The decryption key
     * @param language The language of the input text (e.g., "English", "Vietnamese")
     * @return The decrypted text or an error message if decryption fails
     */
    String decrypt(String input, String key, String language);
    
    /**
     * Generates a random key suitable for this cipher algorithm.
     * 
     * @param language The language for which to generate the key
     * @return A randomly generated key
     */
    String generateKey(String language);
    
    /**
     * Gets a tooltip text describing the expected key format.
     * 
     * @param language The language for which to get the tooltip
     * @return A string describing the expected key format
     */
    String getKeyTooltip(String language);
}