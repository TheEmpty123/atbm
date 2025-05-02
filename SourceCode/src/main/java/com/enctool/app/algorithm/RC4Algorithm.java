package com.enctool.app.algorithm;

public class RC4Algorithm extends SymmetricAlgorithm {
    
    @Override
    protected String getAlgorithmName() {
        // "ARCFOUR" is Java's standard name for RC4
        return "ARCFOUR";
    }
    
    @Override
    public String[] getSupportedModes() {
        // ARCFOUR in Java doesn't use modes but requires ECB for the transformation
        return new String[] {"ECB"};
    }
    
    @Override
    public String[] getSupportedPaddings() {
        // ARCFOUR in Java only supports NoPadding
        return new String[] {"NoPadding"};
    }
    
    @Override
    public int[] getSupportedKeySizes() {
        // RC4 supports key sizes from 40 to 2048 bits
        // Common key sizes used are 40, 56, 128, 256
        return new int[] {40, 56, 128, 256};
    }
    
    @Override
    public boolean requiresIV(String mode) {
        // RC4 does not use an IV
        return false;
    }
    
    @Override
    public int getIVLength() {
        // RC4 does not use an IV
        return 0;
    }
    
    @Override
    public String encrypt(String plainText, String keyText, String mode, String padding, String ivText) {
        // Force the correct transformation for RC4 regardless of selected mode/padding
        return super.encrypt(plainText, keyText, "ECB", "NoPadding", ivText);
    }
    
    @Override
    public String decrypt(String encryptedText, String keyText, String mode, String padding, String ivText) {
        // Force the correct transformation for RC4 regardless of selected mode/padding
        return super.decrypt(encryptedText, keyText, "ECB", "NoPadding", ivText);
    }
}
