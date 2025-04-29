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
   - 3DES
   - Blowfish
   - RC4
   - Twofish (requires the Bouncy Castle provider)

3. **Asymmetric Encryption**
   - RSA

## How to Build

This is a Maven project. To build it, you can use either Maven directly or the included Maven wrapper.

### Using Maven directly

If you have Maven installed, run:

```bash
mvn clean package
```

### Using the Maven wrapper

If you don't have Maven installed, you can use the included Maven wrapper:

1. First, make sure JAVA_HOME is set to your JDK installation directory:
   ```
   set JAVA_HOME=C:\path\to\your\jdk
   ```

2. Then run the Maven wrapper:
   ```
   .\mvnw.cmd clean package
   ```

Both methods will create a JAR file (`enctool.jar`) and a Windows executable (`EncryptionTool.exe`) in the `target` directory.

### About the Executable (.exe)

The Windows executable file is created automatically during the build process. It:
- Uses the application icon from the project root
- Requires Java 11 or higher to be installed on the system
- Can be run by double-clicking like any other Windows application

## How to Run

You can run the application using:

```bash
java -jar target/enctool.jar
```

Or, if you're in an IDE like IntelliJ IDEA or Eclipse, you can run the `EncToolApp` class directly.

Alternatively, on Windows, you can double-click the `EncryptionTool.exe` file that was generated during the build process.

## System Requirements

- Java 11 or higher
- Maven (for building)

## Usage Instructions

### Traditional Ciphers
- **Caesar Cipher**: Enter a numeric shift value as the key
- **Affine Cipher**: Enter the key in the format "a,b" (e.g., "5,8") where both values are integers and 'a' is coprime with 26
- **Vigenère Cipher**: Enter any text key consisting of letters
- **Substitution Cipher**: Enter a 26-letter key representing the entire alphabet substitution
- **Hill Cipher**: Enter a matrix of integers with dimensions nxn, formatted as row values separated by commas, with rows separated by semicolons
- **Transposition Cipher**: Enter a number representing the number of columns

### Symmetric Encryption
- **AES**: Supports key sizes of 128, 192, and 256 bits
- **DES**: Supports a key size of 56 bits
- **3DES**: Supports key sizes of 112 and 168 bits
- **Blowfish**: Supports key sizes between 32 and 448 bits
- **RC4**: Supports various key sizes
- **Twofish**: Supports key sizes of 128, 192, and 256 bits (requires the Bouncy Castle provider)

### Asymmetric Encryption
- **RSA**: Supports key sizes of 1024, 2048, 3072, and 4096 bits
