package com.enctool.app.traditional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the Transposition cipher algorithm.
 * The Transposition cipher rearranges the positions of characters in the plaintext
 * without changing the actual characters themselves.
 */
public class TranspositionCipher implements CipherAlgorithm {
    
    @Override
    public String encrypt(String input, String key, String language) {
        return process(input, key, true);
    }
    
    @Override
    public String decrypt(String input, String key, String language) {
        return process(input, key, false);
    }
    
    @Override
    public String generateKey(String language) {
        SecureRandom secureRandom = new SecureRandom();
        // Generate a random number of columns between 2 and 10
        return String.valueOf(secureRandom.nextInt(9) + 2); // 2 to 10
    }
    
    @Override
    public String getKeyTooltip(String language) {
        return "Enter a number for the number of columns";
    }
    
    private String process(String input, String key, boolean encrypt) {
        if (key.isEmpty()) {
            return "Error: Key cannot be empty for Transposition Cipher";
        }
        
        try {
            int numColumns = Integer.parseInt(key);
            if (numColumns <= 0) {
                return "Error: Number of columns must be positive";
            }
            
            // Track positions of non-alphabetic characters
            List<Integer> nonAlphaPositions = new ArrayList<>();
            List<Character> nonAlphaChars = new ArrayList<>();
            
            // Extract only alphabetic characters for encryption/decryption
            StringBuilder cleanInput = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    cleanInput.append(c);
                } else {
                    nonAlphaPositions.add(i);
                    nonAlphaChars.add(c);
                }
            }
            
            String transformedText;
            int inputLength = cleanInput.length();

            int numRows = (int) Math.ceil((double) inputLength / numColumns);
            if (encrypt) {
                char[][] grid = new char[numRows][numColumns];
                
                // Fill grid row by row
                for (int i = 0; i < numRows; i++) {
                    Arrays.fill(grid[i], ' '); // Fill with spaces
                }
                
                for (int i = 0; i < inputLength; i++) {
                    int row = i / numColumns;
                    int col = i % numColumns;
                    grid[row][col] = cleanInput.charAt(i);
                }
                
                // Read out column by column
                StringBuilder result = new StringBuilder();
                for (int j = 0; j < numColumns; j++) {
                    for (int i = 0; i < numRows; i++) {
                        if (i * numColumns + j < inputLength) {
                            result.append(grid[i][j]);
                        }
                    }
                }
                
                transformedText = result.toString();
            } else {
                // Calculate number of rows

                // Calculate chars in last row (could be fewer than numColumns)
                int lastRowChars = inputLength % numColumns;
                if (lastRowChars == 0) lastRowChars = numColumns;
                
                // Initialize grid with spaces
                char[][] grid = new char[numRows][numColumns];
                for (int i = 0; i < numRows; i++) {
                    Arrays.fill(grid[i], ' ');
                }
                
                // Calculate number of chars in each column
                int[] charsPerColumn = new int[numColumns];
                for (int j = 0; j < numColumns; j++) {
                    charsPerColumn[j] = numRows;
                    // If this is a column that would extend beyond the last row's end
                    if (j >= lastRowChars) {
                        charsPerColumn[j] = numRows - 1;
                    }
                }
                
                // Fill the grid column by column
                int index = 0;
                for (int col = 0; col < numColumns; col++) {
                    for (int row = 0; row < charsPerColumn[col]; row++) {
                        if (index < inputLength) {
                            grid[row][col] = cleanInput.charAt(index++);
                        }
                    }
                }
                
                // Read out row by row
                StringBuilder result = new StringBuilder();
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numColumns; col++) {
                        // Only append if it's not a padding space in the last row
                        if (!(row == numRows - 1 && col >= lastRowChars) && grid[row][col] != ' ') {
                            result.append(grid[row][col]);
                        }
                    }
                }
                
                transformedText = result.toString();
            }
            
            // Reinsert non-alphabetic characters at their original positions
            if (!nonAlphaPositions.isEmpty()) {
                StringBuilder finalResult = getStringBuilder(transformedText, nonAlphaPositions, nonAlphaChars);

                return finalResult.toString();
            }
            
            return transformedText;
            
        } catch (NumberFormatException e) {
            return "Error: Key must be a number for this Transposition Cipher";
        }
    }

    private static StringBuilder getStringBuilder(String transformedText, List<Integer> nonAlphaPositions, List<Character> nonAlphaChars) {
        StringBuilder finalResult = new StringBuilder(transformedText);

        for (int i = 0; i < nonAlphaPositions.size(); i++) {
            int pos = nonAlphaPositions.get(i);
            if (pos <= finalResult.length()) {
                finalResult.insert(pos, nonAlphaChars.get(i));
            } else {
                // If position is beyond current length, append at the end
                finalResult.append(nonAlphaChars.get(i));
            }
        }
        return finalResult;
    }
}
