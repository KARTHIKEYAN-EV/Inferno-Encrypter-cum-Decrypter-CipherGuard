# 🔐 Inferno-Encrypter-cum-Decrypter: CipherGuard

A robust, user-friendly, and cross-platform Java application for securing your digital world.  
**CipherGuard** allows you to effortlessly encrypt and decrypt both text and files using multiple classic ciphers, all through an intuitive console interface.

---

## ✨ Features

| Feature | Description |
|----------|-------------|
| **Multiple Ciphers** | Choose from Caesar, XOR, and Substitution cipher algorithms. |
| **Multi-Format Support** | Encrypt and decrypt both plain text and entire files. |
| **OOP Principles** | Built with clean Java, utilizing inheritance, polymorphism, and encapsulation. |
| **Cross-Platform** | Runs anywhere Java is installed (Windows, macOS, Linux). |
| **Operation Logging** | Automatically logs all encryption and decryption activities to `log.txt`. |

---

## 📸 Demo
| CipherGuard v1.0 |
|-----------------------|
| 1. 🔒 Encrypt Data |
| 2. 🔓 Decrypt Data |
| 3. ❌ Exit |

---

## 🚀 Getting Started

### 🧩 Prerequisites

- Java Development Kit (JDK) 8 or higher  
- A terminal or command prompt  
- Git (to clone the repository)

---

### ⚙️ Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/Inferno-Encrypter-cum-Decrypter-CipherGuard.git
   cd Inferno-Encrypter-cum-Decrypter-CipherGuard

2. **Compile the Java files:**
   ```bash
   javac -d bin crypto/main/*.java crypto/algorithms/*.java crypto/io/*.java crypto/exceptions/*.java

3. **Run the application:**
   ```bash
   java -cp bin crypto.main.MainApp

---

### 🛠️ Usage

1. **Launch the application from your terminal.**

2. **Select either Encrypt or Decrypt.**

3. **Choose your input method: enter text directly or select a file.**

4. **Pick an encryption algorithm from the list.**

5. **Provide a key when prompted (e.g., shift number for Caesar Cipher).**

6. **View results:**

```bash

Encrypted text → saved in encrypted.txt

Decrypted text → saved in decrypted.txt

Operation log → appended to log.txt

```

### 💡 Example Usage (Caesar Cipher)
```bash
 Select 'Encrypt'
 Choose 'File'
 Enter file path: 'sample.txt'
 Choose algorithm: 'Caesar Cipher'
 Enter key: 3
 Check 'encrypted.txt' for the result!
```

---

### 🧰 Tech Stack

```bash
Language: Java

Paradigm: Object-Oriented Programming (OOP)

Key Concepts: Inheritance, Polymorphism, Encapsulation, Exception Handling, File I/O
```

---

### 👥 Contributing

   **We welcome contributions! Please feel free to submit issues and pull requests.**

1. **Fork the project**

2. **Create your Feature Branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```

3. **Commit your Changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```

4. **Push to the Branch**
   ```bash
   git push origin feature/AmazingFeature
   ```

5. **Open a Pull Request**

---

### 📁 Project Structure

```text
Inferno-Encrypter-cum-Decrypter-CipherGuard/
├── 📂 crypto
│   ├── 📂 algorithms          # Cipher implementations
│   │   ├── Cipher.java        # Abstract base class
│   │   ├── CaesarCipher.java
│   │   ├── XORCipher.java
│   │   └── SubstitutionCipher.java
│   ├── 📂 io                  # File operations
│   │   └── FileHandler.java
│   ├── 📂 exceptions          # Custom error handling
│   │   ├── InvalidKeyException.java
│   │   └── CryptoException.java
│   └── 📂 main                # Application entry point
│       └── MainApp.java
├── 📜 encrypted.txt           # Generated output
├── 📜 decrypted.txt           # Generated output
├── 📜 log.txt                 # Operation history
└── 📜 README.md

