# Encryption Tool

This is a simple Java Swing application that provides various encryption and decryption capabilities.

## Features

The application has three main tabs:

1. **Traditional Ciphers**
   - Caesar Cipher
   - Affine Cipher
   - Vigenère Cipher
   - Substitution Cipher
   - Hill Cipher
   - Transposition Cipher

2. **Symmetric Encryption**
   - AES
   - DES
   - Blowfish

3. **Asymmetric Encryption**
   - RSA

## How to Build

This is a Maven project. To build it, run:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## How to Run

You can run the application using:

```bash
java -jar target/encryption-tool-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Or, if you're in an IDE like IntelliJ IDEA or Eclipse, you can run the `EncToolApp` class directly.

## System Requirements

- Java 11 or higher
- Maven (for building)

## Usage Instructions

### Traditional Ciphers
- **Caesar Cipher**: Enter a numeric shift value as the key
- **Affine Cipher**: Enter the key in the format "a,b" (e.g., "5,8") where both values are integers and 'a' is coprime with 26
- **Vigenère Cipher**: Enter any text key consisting of letters
- **Substitution Cipher**: Enter a 26-letter key representing the entire alphabet substitution
- **Hill Cipher**: Currently a placeholder implementation
- **Transposition Cipher**: Enter a number representing the number of columns
