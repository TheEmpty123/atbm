package com.enctool.app.algorithm;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

public class TwofishAlgorithm extends SymmetricAlgorithm {
    
    static {
        // Register Bouncy Castle provider for Twofish support
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    protected String getAlgorithmName() {
        return "Twofish";
    }

    @Override
    public String[] getSupportedModes() {
        return new String[]{"ECB", "CBC", "CTR", "GCM"};
    }

    @Override
    public String[] getSupportedPaddings() {
        return new String[]{"NoPadding", "PKCS5Padding"};
    }

    @Override
    public int[] getSupportedKeySizes() {
        return new int[]{128, 192, 256};
    }
    
    @Override
    public int getIVLength() {
        // Twofish uses 16-byte IV (128-bit block size)
        return 16;
    }
    
    @Override
    public String generateKey(int keySize) throws Exception {
        // Use Bouncy Castle's KeyGenerator for Twofish
        KeyGenerator keyGen = KeyGenerator.getInstance("Twofish", "BC");
        keyGen.init(keySize);
        SecretKey key = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
    
    @Override
    public String encrypt(String plainText, String keyText, String mode, String padding, String ivText) {
        try {
            // Decode the Base64 encoded key
            byte[] keyBytes = Base64.getDecoder().decode(keyText);
            SecretKey secretKey = new SecretKeySpec(keyBytes, getAlgorithmName());
            
            // Create and initialize cipher with specified mode and padding
            String transformation = getAlgorithmName() + "/" + mode + "/" + padding;
            Cipher cipher = Cipher.getInstance(transformation, "BC");
            
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
            throw new RuntimeException("Twofish encryption failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String decrypt(String encryptedText, String keyText, String mode, String padding, String ivText) {
        try {
            // Decode the Base64 encoded key and encrypted text
            byte[] keyBytes = Base64.getDecoder().decode(keyText);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            
            SecretKey secretKey = new SecretKeySpec(keyBytes, getAlgorithmName());
            
            // Create and initialize cipher with specified mode and padding
            String transformation = getAlgorithmName() + "/" + mode + "/" + padding;
            Cipher cipher = Cipher.getInstance(transformation, "BC");
            
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
            throw new RuntimeException("Twofish decryption failed: " + e.getMessage(), e);
        }
    }
}
