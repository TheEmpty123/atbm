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

This is a Maven project that can build both a JAR file and a Windows executable (.exe) file. Before building, make sure you have Java 11 or higher installed (see [Installing Java](#installing-java) section).

### Using Maven directly

If you have Maven installed, run:

```bash
mvn clean package
```

### Using the Maven wrapper

If you don't have Maven installed, you can use the included Maven wrapper:

1. First, make sure JAVA_HOME is set to your JDK installation directory:
   ```
   # For Command Prompt:
   set JAVA_HOME=C:\path\to\your\jdk

   # For PowerShell:
   $env:JAVA_HOME = "C:\path\to\your\jdk"
   ```

   For example, if you installed Eclipse Adoptium JDK 21:
   ```
   # For Command Prompt:
   set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot

   # For PowerShell:
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
   ```

2. Add Java to your PATH:
   ```
   # For Command Prompt:
   set PATH=%JAVA_HOME%\bin;%PATH%

   # For PowerShell:
   $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
   ```

3. Then run the Maven wrapper:
   ```
   .\mvnw.cmd clean package
   ```

Both methods will create:
1. A JAR file (`enctool.jar`) in the `target` directory
2. A Windows executable (`EncryptionTool.exe`) in the `target` directory

### About the Executable (.exe)

The Windows executable file is created automatically during the build process using the Launch4j Maven plugin. The executable:
- Uses the application icon from the project root (`app_icon.ico`)
- Bundles a JRE with the application (no separate Java installation required)
- Can be run by double-clicking like any other Windows application
- Contains all necessary dependencies including the Bouncy Castle provider
- Includes a security policy file to avoid JNI errors

### Troubleshooting the Build

If you encounter build issues:

1. Make sure Java is installed and JAVA_HOME is set correctly
2. Verify Java is in your PATH by running `java -version`
3. If using the Maven wrapper, make sure you're in the project root directory
4. Check that the `app_icon.ico` file exists in the project root

#### Launch4j JRE Path Issue

If you encounter an error like "Failed to build the executable; please verify your configuration.: Enter: JRE path", you need to update the Launch4j configuration in the pom.xml file:

1. Open the pom.xml file
2. Find the Launch4j plugin configuration (around line 55)
3. In the `<jre>` section, add a `<path>` element with the path to your JDK:
   ```xml
   <jre>
       <path>C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot</path>
       <minVersion>11.0.0</minVersion>
       <requiresJdk>false</requiresJdk>
       <opts>
           <opt>-Dfile.encoding=UTF-8</opt>
       </opts>
   </jre>
   ```
4. Save the file and run the build again

### Troubleshooting Runtime Issues

#### JNI Errors

The latest version of this application has been configured to avoid JNI errors by:
1. Bundling a JRE with the executable
2. Including a security policy file
3. Properly configuring the classpath for all dependencies
4. Setting appropriate Java options

If you still encounter a JNI error when running the application (e.g., "A JNI error has occurred, please check your installation and try again"), try the following solutions:

1. **Use the Latest Build**: Make sure you're using the latest build of the application, which includes all the fixes for JNI errors.

2. **Run Using JAR Instead of EXE**: Try running the application using the JAR file instead of the executable:
   ```
   java -jar target/enctool.jar -Djava.security.policy=java.policy
   ```

3. **Check the java.policy File**: Ensure the java.policy file is present in the same directory as the executable. If not, create it with the following content:
   ```
   // Grant all permissions to the application
   grant {
       permission java.security.AllPermission;
   };
   ```

4. **Verify System Architecture**: Ensure your system is 64-bit, as the bundled JRE is configured for 64-bit systems.

5. **Manual JRE Configuration**: If you're still having issues, you can modify the pom.xml file to use a specific JRE path:
   ```xml
   <jre>
       <path>C:\Path\To\Your\JRE</path>
       <minVersion>21.0.0</minVersion>
       <opts>
           <opt>-Dfile.encoding=UTF-8</opt>
           <opt>-Djava.library.path=.</opt>
           <opt>-Djava.security.policy=java.policy</opt>
       </opts>
   </jre>
   ```

## How to Run

There are several ways to run the application:

### Using the JAR file

After building, you can run the application using:

```bash
java -jar target/enctool.jar
```

### Using the Windows executable

After building, you can run the Windows executable by double-clicking the `EncryptionTool.exe` file in the `target` directory.

You can also create a shortcut to the executable:
1. Right-click on `EncryptionTool.exe` in the `target` directory
2. Select "Create shortcut"
3. Move the shortcut to your desktop or another convenient location

The executable contains all necessary dependencies and a bundled JRE, so it will run on any 64-bit Windows system without requiring a separate Java installation. Make sure the java.policy file is in the same directory as the executable.

### Using an IDE

If you're using an IDE like IntelliJ IDEA or Eclipse, you can run the `EncToolApp` class directly.

## Pre-built Executables

If you don't want to build the project yourself, you can:

1. Download a pre-built executable from the project's releases page (if available)
2. Ask someone with Java and Maven installed to build it for you and share the executable
3. Use a build service that can compile Java applications and create executables

## System Requirements

### For Running the Application
- 64-bit Windows operating system
- No separate Java installation required (JRE is bundled with the executable)

### For Building the Application
- Java 21 or higher
- Maven (for building, optional if using the Maven wrapper)
- 64-bit Windows operating system

### Installing Java for Building

If you want to build the application yourself, you'll need to install Java 21 or higher:

1. Download and install Java 21 or higher from one of these sources:
   - [Eclipse Temurin](https://adoptium.net/) (recommended)
   - [Oracle Java](https://www.oracle.com/java/technologies/downloads/)

2. After installation, set the JAVA_HOME environment variable:
   ```
   # For Command Prompt:
   set JAVA_HOME=C:\path\to\your\jdk

   # For PowerShell:
   $env:JAVA_HOME = "C:\path\to\your\jdk"
   ```

3. Add Java to your PATH:
   ```
   # For Command Prompt:
   set PATH=%JAVA_HOME%\bin;%PATH%

   # For PowerShell:
   $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
   ```

4. Verify the installation:
   ```
   java -version
   ```

Note: For just running the application, no Java installation is required as the executable bundles its own JRE.

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
