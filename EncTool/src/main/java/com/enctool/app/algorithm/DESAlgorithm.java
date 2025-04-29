package com.enctool.app.algorithm;

public class DESAlgorithm extends SymmetricAlgorithm {
    
    @Override
    protected String getAlgorithmName() {
        return "DES";
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
        // DES only supports 56-bit keys (technically 64 bits, but 8 are parity bits)
        return new int[] {56};
    }
}
