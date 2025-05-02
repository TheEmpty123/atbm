package com.enctool.app.algorithm;

public class BlowfishAlgorithm extends SymmetricAlgorithm {
    
    @Override
    protected String getAlgorithmName() {
        return "Blowfish";
    }
    
    @Override
    public String[] getSupportedModes() {
        return new String[] {"CBC", "ECB", "CFB", "OFB"};
    }
    
    @Override
    public String[] getSupportedPaddings() {
        return new String[] {"PKCS5Padding", "NoPadding"};
    }
    
    @Override
    public int[] getSupportedKeySizes() {
        // Blowfish supports key sizes from 32 to 448 bits
        // Let's provide common options in 8-bit increments
        return new int[] {128, 192, 256, 320, 384, 448};
    }
}
