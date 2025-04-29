package com.enctool.app.algorithm;

public class TripleDESAlgorithm extends SymmetricAlgorithm {
    
    @Override
    protected String getAlgorithmName() {
        return "DESede"; // This is the JCE name for Triple DES
    }
    
    @Override
    public String[] getSupportedModes() {
        return new String[] {"CBC", "ECB", "CFB", "OFB"};
    }
    
    @Override
    public String[] getSupportedPaddings() {
        return new String[] {"PKCS5Padding", "NoPadding", "ISO10126Padding"};
    }
    
    @Override
    public int[] getSupportedKeySizes() {
        // 3DES supports two key sizes:
        // - 112 bits (2-key Triple DES)
        // - 168 bits (3-key Triple DES)
        return new int[] {112, 168};
    }
    
    @Override
    public int getIVLength() {
        // 3DES uses 8-byte IV (64-bit block size)
        return 8;
    }
}
