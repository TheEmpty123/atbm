package com.enctool.app.algorithm;

public class AESAlgorithm extends SymmetricAlgorithm {
    
    @Override
    protected String getAlgorithmName() {
        return "AES";
    }
    
    @Override
    public String[] getSupportedModes() {
        return new String[] {"CBC", "ECB", "CFB", "OFB", "CTR", "GCM"};
    }
    
    @Override
    public String[] getSupportedPaddings() {
        return new String[] {"PKCS5Padding", "NoPadding", "ISO10126Padding"};
    }
    
    @Override
    public int[] getSupportedKeySizes() {
        return new int[] {128, 192, 256};
    }
}
