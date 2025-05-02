package com.enctool.app.algorithm;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public abstract class SymmetricAlgorithm {
    
    protected abstract String getAlgorithmName();
    
    public abstract String[] getSupportedModes();
    
    public abstract String[] getSupportedPaddings();
    
    public abstract int[] getSupportedKeySizes();
    
    public String generateKey() throws Exception {
        // Use default key size
        return generateKey(getSupportedKeySizes()[0]);
    }
    
    public String generateKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(getAlgorithmName());
        keyGen.init(keySize);
        SecretKey key = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    
    public String generateIV() throws Exception {
        // Create a secure random number generator
        SecureRandom secureRandom = new SecureRandom();
        // Generate IV with the appropriate length for the algorithm
        byte[] iv = new byte[getIVLength()];
        secureRandom.nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }
    
    public boolean requiresIV(String mode) {
        // These modes require an IV
        return !"ECB".equals(mode);
    }
    
    public int getIVLength() {
        // DES and TripleDES use 8-byte IV (64-bit block size)
        if (getAlgorithmName().contains("DES")) {
            return 8;
        }
        // Blowfish uses 8-byte IV (64-bit block size)
        else if (getAlgorithmName().equals("Blowfish")) {
            return 8;
        }
        // AES uses 16-byte IV (128-bit block size)
        return 16;
    }
    
    public String encrypt(String plainText, String keyText, String mode, String padding, String ivText) {
        try {
            // Decode the Base64 encoded key
            byte[] keyBytes = Base64.getDecoder().decode(keyText);
            SecretKey secretKey = new SecretKeySpec(keyBytes, getAlgorithmName());
            
            // Create and initialize cipher with specified mode and padding
            String transformation = getAlgorithmName() + "/" + mode + "/" + padding;
            Cipher cipher = Cipher.getInstance(transformation);
            
            if (requiresIV(mode) && ivText != null && !ivText.isEmpty()) {
                // Initialize with IV if mode requires it
                byte[] ivBytes = Base64.getDecoder().decode(ivText);
                // Ensure IV is the correct length
                byte[] properIVBytes = new byte[getIVLength()];
                System.arraycopy(ivBytes, 0, properIVBytes, 0, Math.min(ivBytes.length, getIVLength()));
                
                IvParameterSpec iv = new IvParameterSpec(properIVBytes);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            } else {
                // Otherwise initialize without IV
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }
            
            // Encrypt
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed: " + e.getMessage(), e);
        }
    }
    
    public String decrypt(String encryptedText, String keyText, String mode, String padding, String ivText) {
        try {
            // Decode the Base64 encoded key and encrypted text
            byte[] keyBytes = Base64.getDecoder().decode(keyText);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            
            SecretKey secretKey = new SecretKeySpec(keyBytes, getAlgorithmName());
            
            // Create and initialize cipher with specified mode and padding
            String transformation = getAlgorithmName() + "/" + mode + "/" + padding;
            Cipher cipher = Cipher.getInstance(transformation);
            
            if (requiresIV(mode) && ivText != null && !ivText.isEmpty()) {
                // Initialize with IV if mode requires it
                byte[] ivBytes = Base64.getDecoder().decode(ivText);
                // Ensure IV is the correct length
                byte[] properIVBytes = new byte[getIVLength()];
                System.arraycopy(ivBytes, 0, properIVBytes, 0, Math.min(ivBytes.length, getIVLength()));
                
                IvParameterSpec iv = new IvParameterSpec(properIVBytes);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            } else {
                // Otherwise initialize without IV
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }
            
            // Decrypt
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
    }
    
    // For backward compatibility
    public String encrypt(String plainText, String keyText, String mode, String padding) {
        return encrypt(plainText, keyText, mode, padding, null);
    }
    
    // For backward compatibility
    public String decrypt(String encryptedText, String keyText, String mode, String padding) {
        return decrypt(encryptedText, keyText, mode, padding, null);
    }
    
    // For backward compatibility
    public String encrypt(String plainText, String keyText) {
        return encrypt(plainText, keyText, getSupportedModes()[0], getSupportedPaddings()[0], null);
    }
    
    // For backward compatibility
    public String decrypt(String encryptedText, String keyText) {
        return decrypt(encryptedText, keyText, getSupportedModes()[0], getSupportedPaddings()[0], null);
    }
}
