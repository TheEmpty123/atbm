package com.enctool.app.asymmetric;

import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * Utility class for RSA asymmetric encryption operations.
 */
public class RSAUtils {
    
    /**
     * Generates a new RSA key pair with the specified key size.
     *
     * @param keySize the size of the key in bits
     * @return a new KeyPair
     * @throws Exception if key generation fails
     */
    public static KeyPair generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        return keyGen.generateKeyPair();
    }
    
    /**
     * Encrypts text using a public key.
     *
     * @param plainText the text to encrypt
     * @param publicKeyString the Base64 encoded public key
     * @return Base64 encoded encrypted text
     * @throws RuntimeException if encryption fails
     */
    public static String encryptWithPublicKey(String plainText, String publicKeyString) {
        try {
            // Convert Base64 encoded public key to PublicKey object
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));
            
            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            
            // Encrypt the text
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    /**
     * Decrypts text using a private key.
     *
     * @param encryptedText the Base64 encoded encrypted text
     * @param privateKeyString the Base64 encoded private key
     * @return decrypted text
     * @throws RuntimeException if decryption fails
     */
    public static String decryptWithPrivateKey(String encryptedText, String privateKeyString) {
        try {
            // Convert Base64 encoded private key to PrivateKey object
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));
            
            // Convert Base64 encoded encrypted text to byte array
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            
            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            
            // Decrypt the text
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * Encrypts text using a private key (for digital signatures).
     *
     * @param plainText the text to encrypt
     * @param privateKeyString the Base64 encoded private key
     * @return Base64 encoded encrypted text
     * @throws RuntimeException if encryption fails
     */
    public static String encryptWithPrivateKey(String plainText, String privateKeyString) {
        try {
            // Convert Base64 encoded private key to PrivateKey object
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(privateKeyBytes));
            
            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            
            // Encrypt the text
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption with private key failed", e);
        }
    }
    
    /**
     * Decrypts text using a public key (for verifying digital signatures).
     *
     * @param encryptedText the Base64 encoded encrypted text
     * @param publicKeyString the Base64 encoded public key
     * @return decrypted text
     * @throws RuntimeException if decryption fails
     */
    public static String decryptWithPublicKey(String encryptedText, String publicKeyString) {
        try {
            // Convert Base64 encoded public key to PublicKey object
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyBytes));
            
            // Convert Base64 encoded encrypted text to byte array
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            
            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            
            // Decrypt the text
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption with public key failed", e);
        }
    }
}