package com.enctool.app.traditional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Hill cipher algorithm.
 * The Hill cipher is a polygraphic substitution cipher based on linear algebra.
 * Each letter is represented by a number, and encryption is performed using matrix multiplication.
 */
public class HillCipher implements CipherAlgorithm {

    // Define alphabets
    private static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String VIETNAMESE_ALPHABET = "AĂÂBCDĐEÊGHIKLMNOÔƠPQRSTUƯVXY";

    @Override
    public String encrypt(String input, String key, String language) {
        return process(input, key, language, true);
    }

    @Override
    public String decrypt(String input, String key, String language) {
        return process(input, key, language, false);
    }

    @Override
    public String generateKey(String language) {
        String alphabet = getAlphabet(language);
        int alphabetSize = alphabet.length();

        SecureRandom secureRandom = new SecureRandom();

        // For simplicity, generate a 2x2 matrix with determinant coprime to alphabet size
        int a11, a12, a21, a22;
        int determinant;
        do {
            a11 = secureRandom.nextInt(alphabetSize);
            a12 = secureRandom.nextInt(alphabetSize);
            a21 = secureRandom.nextInt(alphabetSize);
            a22 = secureRandom.nextInt(alphabetSize);
            determinant = (a11 * a22 - a12 * a21) % alphabetSize;
            if (determinant < 0) determinant += alphabetSize;
        } while (gcd(determinant, alphabetSize) != 1 || determinant == 0);

        return a11 + "," + a12 + ";" + a21 + "," + a22;
    }

    @Override
    public String getKeyTooltip(String language) {
        return "Enter matrix elements separated by commas";
    }

    private String process(String input, String key, String language, boolean encrypt) {
        String alphabet = getAlphabet(language);
        int m = alphabet.length();

        // Parse the matrix from the key
        String[] rows = key.split(";");
        if (rows.length < 2) {
            return "Error: Hill Cipher key must be a matrix (e.g., '2,3;1,4' for a 2x2 matrix)";
        }

        String[] firstRowElements = rows[0].split(",");
        int n = firstRowElements.length; // Matrix dimension

        if (rows.length != n) {
            return "Error: Hill Cipher requires a square matrix";
        }

        int[][] matrix = new int[n][n];

        try {
            // Parse matrix elements
            for (int i = 0; i < n; i++) {
                String[] elements = rows[i].split(",");
                if (elements.length != n) {
                    return "Error: All rows must have the same number of elements";
                }

                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Integer.parseInt(elements[j].trim()) % m;
                    if (matrix[i][j] < 0) matrix[i][j] += m;
                }
            }

            // Check if matrix is invertible
            int det = determinant(matrix, n) % m;
            if (det < 0) det += m;
            if (det == 0 || gcd(det, m) != 1) {
                return "Error: Matrix must be invertible (determinant must be coprime with alphabet size)";
            }

            // Track positions of non-alphabet characters for preservation
            List<Integer> nonAlphabetPositions = new ArrayList<>();
            List<Character> nonAlphabetChars = new ArrayList<>();

            // Extract only alphabet characters while keeping track of other characters
            StringBuilder cleanedInput = new StringBuilder();
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                String upperC = String.valueOf(c).toUpperCase();
                if (alphabet.contains(upperC)) {
                    cleanedInput.append(upperC);
                } else {
                    nonAlphabetPositions.add(i);
                    nonAlphabetChars.add(c);
                }
            }

            // Pad the input if needed
            while (cleanedInput.length() % n != 0) {
                cleanedInput.append(alphabet.charAt(0)); // Pad with first letter of alphabet
            }

            StringBuilder processedText = new StringBuilder();

            if (encrypt) {
                // Encrypt
                for (int i = 0; i < cleanedInput.length(); i += n) {
                    // Extract the block
                    int[] block = new int[n];
                    for (int j = 0; j < n; j++) {
                        block[j] = alphabet.indexOf(cleanedInput.charAt(i + j));
                    }

                    // Multiply block by matrix
                    int[] encryptedBlock = new int[n];
                    for (int j = 0; j < n; j++) {
                        int sum = 0;
                        for (int k = 0; k < n; k++) {
                            sum += matrix[j][k] * block[k];
                        }
                        encryptedBlock[j] = sum % m;
                    }

                    // Convert to characters
                    for (int value : encryptedBlock) {
                        processedText.append(alphabet.charAt(value));
                    }
                }
            } else {
                // Decrypt
                // Find inverse matrix
                int[][] inverseMatrix = new int[n][n];
                int detInverse = findModInverse(det, m);

                // Calculate adjugate matrix
                int[][] adjMatrix = adjugate(matrix, n);

                // Calculate inverse matrix
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        inverseMatrix[i][j] = (adjMatrix[i][j] * detInverse) % m;
                        if (inverseMatrix[i][j] < 0) inverseMatrix[i][j] += m;
                    }
                }

                // Process blocks
                for (int i = 0; i < cleanedInput.length(); i += n) {
                    // Extract the block
                    int[] block = new int[n];
                    for (int j = 0; j < n; j++) {
                        block[j] = alphabet.indexOf(cleanedInput.charAt(i + j));
                    }

                    // Multiply block by inverse matrix
                    int[] decryptedBlock = new int[n];
                    for (int j = 0; j < n; j++) {
                        int sum = 0;
                        for (int k = 0; k < n; k++) {
                            sum += inverseMatrix[j][k] * block[k];
                        }
                        decryptedBlock[j] = sum % m;
                    }

                    // Convert to characters
                    for (int value : decryptedBlock) {
                        processedText.append(alphabet.charAt(value));
                    }
                }
            }

            // Remove padding if decrypting
            String result = processedText.toString();

            // Reinsert non-alphabet characters at their original positions
            if (!nonAlphabetPositions.isEmpty()) {
                StringBuilder finalResult = new StringBuilder(result);

                // Insert non-alphabet characters at their positions
                for (int i = 0; i < nonAlphabetPositions.size(); i++) {
                    int pos = nonAlphabetPositions.get(i);
                    // Adjust position for already inserted characters and truncate if exceeding
                    if (pos <= finalResult.length()) {
                        finalResult.insert(pos, nonAlphabetChars.get(i));
                    }
                }

                return finalResult.toString();
            }

            return result;

        } catch (NumberFormatException e) {
            return "Error: Invalid matrix elements in Hill Cipher key";
        } catch (Exception e) {
            return "Error processing Hill Cipher: " + e.getMessage();
        }
    }

    private int determinant(int[][] matrix, int n) {
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        int det = 0;
        for (int i = 0; i < n; i++) {
            int[][] subMatrix = new int[n-1][n-1];
            for (int j = 1; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (k < i) subMatrix[j-1][k] = matrix[j][k];
                    else if (k > i) subMatrix[j-1][k-1] = matrix[j][k];
                }
            }
            det += Math.pow(-1, i) * matrix[0][i] * determinant(subMatrix, n-1);
        }
        return det;
    }

    private int[][] adjugate(int[][] matrix, int n) {
        int[][] adj = new int[n][n];

        if (n == 1) {
            adj[0][0] = 1;
            return adj;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Calculate cofactor
                int[][] minor = new int[n-1][n-1];

                // Fill the minor matrix
                for (int k = 0, r = 0; k < n; k++) {
                    if (k == i) continue;
                    for (int l = 0, c = 0; l < n; l++) {
                        if (l == j) continue;
                        minor[r][c++] = matrix[k][l];
                    }
                    r++;
                }

                int sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = sign * determinant(minor, n-1); // Note: j, i instead of i, j for transpose
            }
        }

        return adj;
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    private int findModInverse(int a, int m) {
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1) {
                return i;
            }
        }
        return 1;
    }

    private String getAlphabet(String language) {
        return language.equals("English") ? ENGLISH_ALPHABET : VIETNAMESE_ALPHABET;
    }
}
